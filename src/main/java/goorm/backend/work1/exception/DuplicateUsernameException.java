package goorm.backend.work1.exception;

public class DuplicateUsernameException extends RuntimeException {
  public DuplicateUsernameException(String message) {
    super(message);
  }
}
