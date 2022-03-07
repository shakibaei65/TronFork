package org.tron.core.exception;

public class AloneRuntimeException extends RuntimeException {

  public AloneRuntimeException() {
    super();
  }

  public AloneRuntimeException(String message) {
    super(message);
  }

  public AloneRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public AloneRuntimeException(Throwable cause) {
    super(cause);
  }

  protected AloneRuntimeException(String message, Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }


}
