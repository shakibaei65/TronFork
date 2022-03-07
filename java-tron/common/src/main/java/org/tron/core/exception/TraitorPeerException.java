package org.tron.core.exception;

public class TraitorPeerException extends AloneException {

  public TraitorPeerException() {
    super();
  }

  public TraitorPeerException(String message) {
    super(message);
  }

  public TraitorPeerException(String message, Throwable cause) {
    super(message, cause);
  }
}
