package org.tron.core.exception;

public class DupTransactionException extends AloneException {

  public DupTransactionException() {
    super();
  }

  public DupTransactionException(String message) {
    super(message);
  }
}
