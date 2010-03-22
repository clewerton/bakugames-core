package org.bakugames.core.mock;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.Renderable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class RenderableComponent extends Component implements Renderable {
  public int renderCount = 0;
  
  public RenderableComponent(String id) {
    super(id);
  }
  
  public RenderableComponent(String id, Entity owner) {
    super(id, owner);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    renderCount++;
  }
}
