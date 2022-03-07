package org.tron.core.exception;

public class UnReachBlockException extends AloneException {

  public UnReachBlockException() {
    super();
  }

  public UnReachBlockException(String message) {
    super(message);
  }

  public UnReachBlockException(String message, Throwable cause) {
    super(message, cause);
  }
}
