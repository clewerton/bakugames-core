package org.bakugames.core.traits;

import java.util.Set;

import org.bakugames.core.Instruction;

public interface Controllable extends Updateable {
  void execute(Instruction instruction);
  boolean understands(Instruction instruction);
  
  Set<Instruction> getInstructionSet();
}
