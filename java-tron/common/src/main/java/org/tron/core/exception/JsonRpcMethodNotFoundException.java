package org.tron.core.exception;

public class JsonRpcMethodNotFoundException extends AloneException {

  public JsonRpcMethodNotFoundException() {
    super();
  }

  public JsonRpcMethodNotFoundException(String msg) {
    super(msg);
  }

  public JsonRpcMethodNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}