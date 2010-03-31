package org.bakugames.core.mock;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.traits.Renderable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class RenderableComponent extends Component implements Renderable {
  public int renderCount = 0;
  public int zOrder = 0;
  public long nanoTime = 0;
  
  public RenderableComponent(String id, int zOrder) {
    super(id);
    
    this.zOrder = zOrder;
  }
  
  public RenderableComponent(String id, Entity owner) {
    super(id, owner);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    renderCount++;
    nanoTime = System.nanoTime();
  }

  @Override
  public int getZOrder() {
    return zOrder;
  }
  
  // interface methods
  @Override
  public int compareTo(Renderable o) {
    return getZOrder() - (o != null ? o.getZOrder() : 0);
  }
}
