package org.bakugames.core.exception;

public class ComponentIdMismatchException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private String id;
  private String componentId;
  
  public ComponentIdMismatchException(String id, String componentId) {
    this.id = id;
    this.componentId = componentId;
  }

  public String getComponentId() {
    return componentId;
  }

  public String getId() {
    return id;
  }
}
