package org.tron.core.net.messagehandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.core.config.args.Args;
import org.tron.core.exception.P2pException;
import org.tron.core.exception.P2pException.TypeEnum;
import org.tron.core.net.AloneNetDelegate;
import org.tron.core.net.message.TransactionMessage;
import org.tron.core.net.message.TransactionsMessage;
import org.tron.core.net.message.AloneMessage;
import org.tron.core.net.peer.Item;
import org.tron.core.net.peer.PeerConnection;
import org.tron.core.net.service.AdvService;
import org.tron.protos.Protocol.Inventory.InventoryType;
import org.tron.protos.Protocol.ReasonCode;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.Transaction.Contract.ContractType;

@Slf4j(topic = "net")
@Component
public class TransactionsMsgHandler implements AloneMsgHandler {

  private static int MAX_ALN_SIZE = 50_000;
  private static int MAX_SMART_CONTRACT_SUBMIT_SIZE = 100;
  @Autowired
  private AloneNetDelegate aloneNetDelegate;
  @Autowired
  private AdvService advService;

  private BlockingQueue<AlnEvent> smartContractQueue = new LinkedBlockingQueue(MAX_ALN_SIZE);

  private BlockingQueue<Runnable> queue = new LinkedBlockingQueue();

  private int threadNum = Args.getInstance().getValidateSignThreadNum();
  private ExecutorService alnHandlePool = new ThreadPoolExecutor(threadNum, threadNum, 0L,
      TimeUnit.MILLISECONDS, queue);

  private ScheduledExecutorService smartContractExecutor = Executors
      .newSingleThreadScheduledExecutor();

  public void init() {
    handleSmartContract();
  }

  public void close() {
    smartContractExecutor.shutdown();
  }

  public boolean isBusy() {
    return queue.size() + smartContractQueue.size() > MAX_ALN_SIZE;
  }

  @Override
  public void processMessage(PeerConnection peer, AloneMessage msg) throws P2pException {
    TransactionsMessage transactionsMessage = (TransactionsMessage) msg;
    check(peer, transactionsMessage);
    for (Transaction aln : transactionsMessage.getTransactions().getTransactionsList()) {
      int type = aln.getRawData().getContract(0).getType().getNumber();
      if (type == ContractType.TriggerSmartContract_VALUE
          || type == ContractType.CreateSmartContract_VALUE) {
        if (!smartContractQueue.offer(new AlnEvent(peer, new TransactionMessage(aln)))) {
          logger.warn("Add smart contract failed, queueSize {}:{}", smartContractQueue.size(),
              queue.size());
        }
      } else {
        alnHandlePool.submit(() -> handleTransaction(peer, new TransactionMessage(aln)));
      }
    }
  }

  private void check(PeerConnection peer, TransactionsMessage msg) throws P2pException {
    for (Transaction aln : msg.getTransactions().getTransactionsList()) {
      Item item = new Item(new TransactionMessage(aln).getMessageId(), InventoryType.ALN);
      if (!peer.getAdvInvRequest().containsKey(item)) {
        throw new P2pException(TypeEnum.BAD_MESSAGE,
            "aln: " + msg.getMessageId() + " without request.");
      }
      peer.getAdvInvRequest().remove(item);
    }
  }

  private void handleSmartContract() {
    smartContractExecutor.scheduleWithFixedDelay(() -> {
      try {
        while (queue.size() < MAX_SMART_CONTRACT_SUBMIT_SIZE) {
          AlnEvent event = smartContractQueue.take();
          alnHandlePool.submit(() -> handleTransaction(event.getPeer(), event.getMsg()));
        }
      } catch (Exception e) {
        logger.error("Handle smart contract exception.", e);
      }
    }, 1000, 20, TimeUnit.MILLISECONDS);
  }

  private void handleTransaction(PeerConnection peer, TransactionMessage aln) {
    if (peer.isDisconnect()) {
      logger.warn("Drop aln {} from {}, peer is disconnect.", aln.getMessageId(),
          peer.getInetAddress());
      return;
    }

    if (advService.getMessage(new Item(aln.getMessageId(), InventoryType.ALN)) != null) {
      return;
    }

    try {
      aloneNetDelegate.pushTransaction(aln.getTransactionCapsule());
      advService.broadcast(aln);
    } catch (P2pException e) {
      logger.warn("Aln {} from peer {} process failed. type: {}, reason: {}",
          aln.getMessageId(), peer.getInetAddress(), e.getType(), e.getMessage());
      if (e.getType().equals(TypeEnum.BAD_ALN)) {
        peer.disconnect(ReasonCode.BAD_TX);
      }
    } catch (Exception e) {
      logger.error("Aln {} from peer {} process failed.", aln.getMessageId(), peer.getInetAddress(),
          e);
    }
  }

  class AlnEvent {

    @Getter
    private PeerConnection peer;
    @Getter
    private TransactionMessage msg;
    @Getter
    private long time;

    public AlnEvent(PeerConnection peer, TransactionMessage msg) {
      this.peer = peer;
      this.msg = msg;
      this.time = System.currentTimeMillis();
    }
  }
}