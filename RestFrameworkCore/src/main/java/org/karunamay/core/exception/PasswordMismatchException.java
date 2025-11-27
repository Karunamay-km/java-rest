package org.karunamay.core.exception;

public class PasswordMismatchException extends RuntimeException {
  public PasswordMismatchException(String message) {
    super(message);
  }
}
