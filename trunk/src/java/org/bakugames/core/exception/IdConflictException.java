package org.bakugames.core.exception;

public class IdConflictException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private String id;
  
  public IdConflictException(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
