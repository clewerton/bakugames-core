package org.bakugames.core;

import java.util.Set;

public interface Controllable {
  void execute(Instruction instruction);
  boolean understands(Instruction instruction);
  
  Set<Instruction> getInstructionSet();
}
