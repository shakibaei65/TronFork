package org.tron.core.exception;

public class BalanceInsufficientException extends AloneException {

  public BalanceInsufficientException() {
    super();
  }

  public BalanceInsufficientException(String message) {
    super(message);
  }
}
