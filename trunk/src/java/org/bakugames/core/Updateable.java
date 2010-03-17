package org.bakugames.core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface Updateable {
  void update(GameContainer gc, StateBasedGame sb, int delta);
}
