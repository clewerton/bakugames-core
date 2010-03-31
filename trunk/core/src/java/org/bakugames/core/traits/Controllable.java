package org.bakugames.core.traits;

import java.util.Set;

public interface Controllable extends Updateable {
  void execute(Object instruction);
  boolean understands(Object instruction);
  
  Set<Object> getInstructionSet();
}
