package org.bakugames.input;

import org.bakugames.core.Instruction;
import org.bakugames.core.traits.Updateable;

public interface Player extends Updateable {
  void bind(Control control, Instruction instruction);
  void unbind(Control control);
  void clear(Instruction instruction);
}
