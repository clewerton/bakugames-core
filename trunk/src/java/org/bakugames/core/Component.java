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

  public <T> T as(Class<T> type) {
    if(type == null)
      throw new IllegalArgumentException(String.valueOf(type));
    
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
    if(this.owner == owner)
      return;
    
    if(owner != null && owner.has(getId()))
      throw new IdConflictException(getId());
    
    if(this.owner != null && this.owner.has(getId()))
      this.owner.unplug(getId());
    
    this.owner = owner;
    
    if(owner != null)
      owner.plug(this);
  }
  
  public final String getId() {
    return id;
  }
  
  private void setId0(String id) throws IllegalArgumentException {
    if(id == null)
      throw new IllegalArgumentException(id);
    
    this.id = id;
  }
}
