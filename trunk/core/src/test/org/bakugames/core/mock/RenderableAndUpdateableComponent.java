package org.bakugames.core.mock;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.traits.Renderable;
import org.bakugames.core.traits.Updateable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class RenderableAndUpdateableComponent extends Component implements Renderable, Updateable {
  public int renderCount = 0;
  public int updateCount = 0;
  public int zOrder = 0;
  
  public RenderableAndUpdateableComponent(String id) {
    super(id);
  }

  public RenderableAndUpdateableComponent(String id, Entity owner) {
    super(id, owner);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    renderCount++;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    updateCount++;
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