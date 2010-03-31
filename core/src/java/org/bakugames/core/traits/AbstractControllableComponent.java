package org.bakugames.core.traits;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.input.Instruction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public abstract class AbstractControllableComponent extends Component implements Controllable {
  private Queue<Instruction> instructionQueue;
  private Map<Object, Instruction> instructionMap;

  public AbstractControllableComponent(String id) {
    this(id, null);
  }
  
  public AbstractControllableComponent(String id, Entity entity) {
    super(id, entity);
    
    instructionQueue = new LinkedList<Instruction>();
    instructionMap = new HashMap<Object, Instruction>();
  }

  // Controllable operations
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    executeInstructions(gc, sb, delta);
  }

  @Override
  public Set<Object> getInstructionSet() {
    return Collections.unmodifiableSet(getInstructionMap().keySet());
  }

  @Override
  public void execute(Object instruction) {
    if(! understands(instruction))
      return;
    
    enqueue(get(instruction));
  }

  @Override
  public boolean understands(Object instruction) {
    if(instruction == null)
      return false;
    
    return getInstructionMap().containsKey(instruction);
  }

  // helper methods
  protected void executeInstructions(GameContainer gc, StateBasedGame sb, int delta) {
    while(hasUnprocessedInstructions())
      dequeue().execute(gc, sb, delta);
  }

  // map operations
  protected Instruction get(Object id) {
    return getInstructionMap().get(id);
  }
  
  protected void set(Object id, Instruction instruction) {
    if(id == null || instruction == null)
      throw new IllegalArgumentException("id = " + id + ", instruction = " + instruction);
    
    getInstructionMap().put(id, instruction);
  }
  
  protected Instruction remove(Object id) {
    if(id == null)
      return null;
    
    return getInstructionMap().remove(id);
  }
  
  // queue operations
  protected void enqueue(Instruction instruction) {
    getInstructionQueue().add(instruction);
  }
  
  protected Instruction dequeue() {
    return getInstructionQueue().poll();
  }
  
  protected boolean hasUnprocessedInstructions() {
    return ! getInstructionQueue().isEmpty();
  }
  
  // properties
  protected Map<Object, Instruction> getInstructionMap() {
    return instructionMap;
  }
  
  protected Queue<Instruction> getInstructionQueue() {
    return instructionQueue;
  }
}
