package org.tron.core.exception;

public class TooBigTransactionException extends AloneException {

  public TooBigTransactionException() {
    super();
  }

  public TooBigTransactionException(String message) {
    super(message);
  }
}
