package org.bakugames.core.mock;

import org.bakugames.core.traits.Renderable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class SimpleRenderable implements Renderable {
  private int zOrder;
  
  public SimpleRenderable(int zOrder) {
    this.zOrder = zOrder;
  }

  @Override
  public int getZOrder() {
    return zOrder;
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // empty block
  }

  @Override
  public int compareTo(Renderable o) {
    int thatZOrder = (o != null ? o.getZOrder() : 0);
    int thisZOrder = getZOrder();
    
    return (thisZOrder < thatZOrder 
         ? -1 
         : (thisZOrder == thatZOrder 
             ? 0 
             : 1));
  }
}
