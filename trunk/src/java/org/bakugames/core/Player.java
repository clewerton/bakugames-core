package org.bakugames.core;

public abstract class Player implements Updateable {
  private Entity entity;

  public Player(Entity entity) {
    setEntity0(entity);
  }

  public Entity getEntity() {
    return entity;
  }

  public void setEntity(Entity entity) {
    setEntity0(entity);
  }
  
  private void setEntity0(Entity entity) {
    if(entity == null)
      throw new IllegalArgumentException("null");
    
    this.entity = entity;
  }  
}
