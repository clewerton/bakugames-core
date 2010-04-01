package org.bakugames.core.mock;

import org.bakugames.core.input.Control;
import org.newdawn.slick.Input;

public class PointerControl implements Control {
  public static class Pointer {
    public boolean set = false;
  }
  
  private Pointer pointer;
  
  public PointerControl(Pointer pointer) {
    this.pointer = pointer;
  }

  @Override
  public boolean happened(Input input) {
    return pointer.set;
  }
  
  @Override
  public boolean equals(Object o) {
    if(this == o)
      return true;
    
    if(o == null)
      return false;
    
    return (o instanceof PointerControl)
        && ((PointerControl) o).pointer == this.pointer;
  }
  
  @Override
  public int hashCode() {
    return pointer.hashCode();
  }
}
