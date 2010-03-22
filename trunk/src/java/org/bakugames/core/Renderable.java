package org.bakugames.core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface Renderable extends Comparable<Renderable> {
  void render(GameContainer gc, StateBasedGame sb, Graphics gr);
  int getZOrder();
}
