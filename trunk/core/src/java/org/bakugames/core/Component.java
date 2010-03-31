package org.bakugames.core;

import org.bakugames.core.exception.IdConflictException;

public class Component {
  private Entity owner;
  private String id;
  
  public Component(String id) {
    this(id, null);
  }
  
  public Component(String id, Entity owner) {
    setId0(id);
    setOwner0(owner);
  }

  public <T> T as(Class<T> type) throws IllegalArgumentException, ClassCastException {
    if(type == null)
      throw new IllegalArgumentException("null");
    
    return type.cast(this);
  }
  
  // properties
  public Entity getOwner() {
    return owner;
  }
  
  public void setOwner(Entity owner) {
    setOwner0(owner);
  }

  private void setOwner0(Entity owner) {
    if(this.owner == owner) // new boss, same as the old boss 
      return;
    
    if(owner != null && owner.has(getId())) // there's a spy around here!
      throw new IdConflictException(getId());
    
    if(this.owner != null && this.owner.has(getId())) // pull the plug
      this.owner.unplug(getId());
    
    this.owner = owner;
    
    if(owner != null) // hook me up
      owner.plug(this);
  }
  
  public final String getId() {
    return id;
  }
  
  private void setId0(String id) throws IllegalArgumentException {
    if(id == null)
      throw new IllegalArgumentException("null");
    
    this.id = id;
  }
}
