package org.tron.common.overlay.discover.node.statistics;

import lombok.extern.slf4j.Slf4j;
import org.tron.common.net.udp.message.UdpMessageTypeEnum;
import org.tron.common.overlay.message.Message;
import org.tron.core.net.message.FetchInvDataMessage;
import org.tron.core.net.message.InventoryMessage;
import org.tron.core.net.message.MessageTypes;
import org.tron.core.net.message.TransactionsMessage;

@Slf4j
public class MessageStatistics {

  //udp discovery
  public final MessageCount discoverInPing = new MessageCount();
  public final MessageCount discoverOutPing = new MessageCount();
  public final MessageCount discoverInPong = new MessageCount();
  public final MessageCount discoverOutPong = new MessageCount();
  public final MessageCount discoverInFindNode = new MessageCount();
  public final MessageCount discoverOutFindNode = new MessageCount();
  public final MessageCount discoverInNeighbours = new MessageCount();
  public final MessageCount discoverOutNeighbours = new MessageCount();

  //tcp p2p
  public final MessageCount p2pInHello = new MessageCount();
  public final MessageCount p2pOutHello = new MessageCount();
  public final MessageCount p2pInPing = new MessageCount();
  public final MessageCount p2pOutPing = new MessageCount();
  public final MessageCount p2pInPong = new MessageCount();
  public final MessageCount p2pOutPong = new MessageCount();
  public final MessageCount p2pInDisconnect = new MessageCount();
  public final MessageCount p2pOutDisconnect = new MessageCount();

  //tcp alone
  public final MessageCount aloneInMessage = new MessageCount();
  public final MessageCount aloneOutMessage = new MessageCount();

  public final MessageCount aloneInSyncBlockChain = new MessageCount();
  public final MessageCount aloneOutSyncBlockChain = new MessageCount();
  public final MessageCount aloneInBlockChainInventory = new MessageCount();
  public final MessageCount aloneOutBlockChainInventory = new MessageCount();

  public final MessageCount aloneInAlnInventory = new MessageCount();
  public final MessageCount aloneOutAlnInventory = new MessageCount();
  public final MessageCount aloneInAlnInventoryElement = new MessageCount();
  public final MessageCount aloneOutAlnInventoryElement = new MessageCount();

  public final MessageCount aloneInBlockInventory = new MessageCount();
  public final MessageCount aloneOutBlockInventory = new MessageCount();
  public final MessageCount aloneInBlockInventoryElement = new MessageCount();
  public final MessageCount aloneOutBlockInventoryElement = new MessageCount();

  public final MessageCount aloneInAlnFetchInvData = new MessageCount();
  public final MessageCount aloneOutAlnFetchInvData = new MessageCount();
  public final MessageCount aloneInAlnFetchInvDataElement = new MessageCount();
  public final MessageCount aloneOutAlnFetchInvDataElement = new MessageCount();

  public final MessageCount aloneInBlockFetchInvData = new MessageCount();
  public final MessageCount aloneOutBlockFetchInvData = new MessageCount();
  public final MessageCount aloneInBlockFetchInvDataElement = new MessageCount();
  public final MessageCount aloneOutBlockFetchInvDataElement = new MessageCount();


  public final MessageCount aloneInAln = new MessageCount();
  public final MessageCount aloneOutAln = new MessageCount();
  public final MessageCount aloneInAlns = new MessageCount();
  public final MessageCount aloneOutAlns = new MessageCount();
  public final MessageCount aloneInBlock = new MessageCount();
  public final MessageCount aloneOutBlock = new MessageCount();
  public final MessageCount aloneOutAdvBlock = new MessageCount();

  public void addUdpInMessage(UdpMessageTypeEnum type) {
    addUdpMessage(type, true);
  }

  public void addUdpOutMessage(UdpMessageTypeEnum type) {
    addUdpMessage(type, false);
  }

  public void addTcpInMessage(Message msg) {
    addTcpMessage(msg, true);
  }

  public void addTcpOutMessage(Message msg) {
    addTcpMessage(msg, false);
  }

  private void addUdpMessage(UdpMessageTypeEnum type, boolean flag) {
    switch (type) {
      case DISCOVER_PING:
        if (flag) {
          discoverInPing.add();
        } else {
          discoverOutPing.add();
        }
        break;
      case DISCOVER_PONG:
        if (flag) {
          discoverInPong.add();
        } else {
          discoverOutPong.add();
        }
        break;
      case DISCOVER_FIND_NODE:
        if (flag) {
          discoverInFindNode.add();
        } else {
          discoverOutFindNode.add();
        }
        break;
      case DISCOVER_NEIGHBORS:
        if (flag) {
          discoverInNeighbours.add();
        } else {
          discoverOutNeighbours.add();
        }
        break;
      default:
        break;
    }
  }

  private void addTcpMessage(Message msg, boolean flag) {

    if (flag) {
      aloneInMessage.add();
    } else {
      aloneOutMessage.add();
    }

    switch (msg.getType()) {
      case P2P_HELLO:
        if (flag) {
          p2pInHello.add();
        } else {
          p2pOutHello.add();
        }
        break;
      case P2P_PING:
        if (flag) {
          p2pInPing.add();
        } else {
          p2pOutPing.add();
        }
        break;
      case P2P_PONG:
        if (flag) {
          p2pInPong.add();
        } else {
          p2pOutPong.add();
        }
        break;
      case P2P_DISCONNECT:
        if (flag) {
          p2pInDisconnect.add();
        } else {
          p2pOutDisconnect.add();
        }
        break;
      case SYNC_BLOCK_CHAIN:
        if (flag) {
          aloneInSyncBlockChain.add();
        } else {
          aloneOutSyncBlockChain.add();
        }
        break;
      case BLOCK_CHAIN_INVENTORY:
        if (flag) {
          aloneInBlockChainInventory.add();
        } else {
          aloneOutBlockChainInventory.add();
        }
        break;
      case INVENTORY:
        InventoryMessage inventoryMessage = (InventoryMessage) msg;
        int inventorySize = inventoryMessage.getInventory().getIdsCount();
        messageProcess(inventoryMessage.getInvMessageType(),
                aloneInAlnInventory,aloneInAlnInventoryElement,aloneInBlockInventory,
                aloneInBlockInventoryElement,aloneOutAlnInventory,aloneOutAlnInventoryElement,
                aloneOutBlockInventory,aloneOutBlockInventoryElement,
                flag, inventorySize);
        break;
      case FETCH_INV_DATA:
        FetchInvDataMessage fetchInvDataMessage = (FetchInvDataMessage) msg;
        int fetchSize = fetchInvDataMessage.getInventory().getIdsCount();
        messageProcess(fetchInvDataMessage.getInvMessageType(),
                aloneInAlnFetchInvData,aloneInAlnFetchInvDataElement,aloneInBlockFetchInvData,
                aloneInBlockFetchInvDataElement,aloneOutAlnFetchInvData,aloneOutAlnFetchInvDataElement,
                aloneOutBlockFetchInvData,aloneOutBlockFetchInvDataElement,
                flag, fetchSize);
        break;
      case ALNS:
        TransactionsMessage transactionsMessage = (TransactionsMessage) msg;
        if (flag) {
          aloneInAlns.add();
          aloneInAln.add(transactionsMessage.getTransactions().getTransactionsCount());
        } else {
          aloneOutAlns.add();
          aloneOutAln.add(transactionsMessage.getTransactions().getTransactionsCount());
        }
        break;
      case ALN:
        if (flag) {
          aloneInMessage.add();
        } else {
          aloneOutMessage.add();
        }
        break;
      case BLOCK:
        if (flag) {
          aloneInBlock.add();
        }
        aloneOutBlock.add();
        break;
      default:
        break;
    }
  }
  
  
  private void messageProcess(MessageTypes messageType,
                              MessageCount inAln,
                              MessageCount inAlnEle,
                              MessageCount inBlock,
                              MessageCount inBlockEle,
                              MessageCount outAln,
                              MessageCount outAlnEle,
                              MessageCount outBlock,
                              MessageCount outBlockEle,
                              boolean flag, int size) {
    if (flag) {
      if (messageType == MessageTypes.ALN) {
        inAln.add();
        inAlnEle.add(size);
      } else {
        inBlock.add();
        inBlockEle.add(size);
      }
    } else {
      if (messageType == MessageTypes.ALN) {
        outAln.add();
        outAlnEle.add(size);
      } else {
        outBlock.add();
        outBlockEle.add(size);
      }
    }
  }

}
