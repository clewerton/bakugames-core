package example.simpletestgame;

import org.bakugames.core.input.Control;
import org.newdawn.slick.Input;

public class KeyDownControl implements Control {
  private int key;
  
  public KeyDownControl(int key) {
    this.key = key;
  }

  // operations
  @Override
  public boolean happened(Input input) {
    return input.isKeyDown(getKey());
  }
  
  // utility methods
  @Override
  public boolean equals(Object o) {
    if(o == this)
      return true;
    
    if(! (o instanceof KeyDownControl))
      return false;
    
    return getKey() == ((KeyDownControl) o).getKey();
  }
  
  @Override
  public int hashCode() {
    return getKey();
  }
  
  // properties
  public int getKey() {
    return key;
  }
}
