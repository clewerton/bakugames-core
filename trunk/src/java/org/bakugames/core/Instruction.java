package org.bakugames.core;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface Instruction {
  String getName();
  void execute(Component c, GameContainer gc, StateBasedGame sb, int delta);
}
