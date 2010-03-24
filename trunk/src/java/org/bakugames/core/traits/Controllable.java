package org.bakugames.core.traits;

import java.util.Set;

public interface Controllable extends Updateable {
  void execute(String instruction);
  boolean understands(String instruction);
  
  Set<String> getInstructionSet();
}
