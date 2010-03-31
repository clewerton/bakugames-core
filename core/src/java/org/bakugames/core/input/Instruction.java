package org.bakugames.core.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface Instruction {
  // TODO has the same argument list as Updateable.update. Merge the two?
  void execute(GameContainer gc, StateBasedGame sb, int delta);
}
