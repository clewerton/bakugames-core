package org.bakugames.core.traits;

import java.util.Set;

public interface Controllable extends Updateable {
  void execute(Object instructionId);
  boolean understands(Object instructionId);
  
  Set<Object> getInstructionSet();
}
