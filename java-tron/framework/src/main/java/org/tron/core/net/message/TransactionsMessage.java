package org.tron.core.net.message;

import java.util.List;
import org.tron.core.capsule.TransactionCapsule;
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Transaction;

public class TransactionsMessage extends AloneMessage {

  private Protocol.Transactions transactions;

  public TransactionsMessage(List<Transaction> alns) {
    Protocol.Transactions.Builder builder = Protocol.Transactions.newBuilder();
    alns.forEach(aln -> builder.addTransactions(aln));
    this.transactions = builder.build();
    this.type = MessageTypes.ALNS.asByte();
    this.data = this.transactions.toByteArray();
  }

  public TransactionsMessage(byte[] data) throws Exception {
    super(data);
    this.type = MessageTypes.ALNS.asByte();
    this.transactions = Protocol.Transactions.parseFrom(getCodedInputStream(data));
    if (isFilter()) {
      compareBytes(data, transactions.toByteArray());
      TransactionCapsule.validContractProto(transactions.getTransactionsList());
    }
  }

  public Protocol.Transactions getTransactions() {
    return transactions;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(super.toString()).append("aln size: ")
        .append(this.transactions.getTransactionsList().size()).toString();
  }

  @Override
  public Class<?> getAnswerMessage() {
    return null;
  }

}
