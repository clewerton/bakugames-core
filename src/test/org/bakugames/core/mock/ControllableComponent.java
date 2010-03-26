package org.bakugames.core.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.traits.Controllable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class ControllableComponent extends Component implements Controllable {
  public Set<Object> instructionSet;
  public List<Object> instructionsExecuted;
  public int updateCount;
  
  public ControllableComponent(String id, String... instructions) {
    this(id, null, instructions);
  }

  public ControllableComponent(String id, Entity owner, String... instructions) {
    super(id, owner);
    
    instructionSet = new HashSet<Object>();
    instructionsExecuted = new ArrayList<Object>();
    updateCount = 0;
    
    for(String instruction : instructions)
      instructionSet.add(instruction);
  }

  @Override
  public void execute(Object instruction) {
    if(! understands(instruction))
      return;
    
    instructionsExecuted.add(instruction);
  }

  @Override
  public Set<Object> getInstructionSet() {
    return instructionSet;
  }

  @Override
  public boolean understands(Object instruction) {
    return instructionSet.contains(instruction);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    updateCount++;
  }
}
