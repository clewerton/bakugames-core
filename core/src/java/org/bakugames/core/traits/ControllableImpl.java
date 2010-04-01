package org.bakugames.core.traits;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.bakugames.core.input.Instruction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ControllableImpl implements Controllable {
  private Queue<Instruction> instructionQueue;
  private Map<Object, Instruction> instructionMap;

  public ControllableImpl() {
    instructionQueue = new LinkedList<Instruction>();
    instructionMap = new HashMap<Object, Instruction>();
  }

  // Controllable operations
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
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
    while(hasInstructionsQueued())
      dequeue().execute(gc, sb, delta);
  }

  // map operations
  public Instruction get(Object id) {
    if(id == null)
      return null;
    
    return getInstructionMap().get(id);
  }
  
  public void set(Object id, Instruction instruction) {
    if(id == null || instruction == null)
      throw new IllegalArgumentException(id + ", " + instruction);
    
    getInstructionMap().put(id, instruction);
  }
  
  public Instruction remove(Object id) {
    if(id == null)
      return null;
    
    return getInstructionMap().remove(id);
  }
  
  // queue operations
  public void enqueue(Instruction e) {
    if(e == null)
      return;
    
    getInstructionQueue().add(e);
  }

  public Instruction dequeue() {
    return getInstructionQueue().poll();
  }

  public boolean hasInstructionsQueued() {
    return ! getInstructionQueue().isEmpty();
  }

  // properties
  public Map<Object, Instruction> getInstructionMap() {
    return instructionMap;
  }
  
  public Queue<Instruction> getInstructionQueue() {
    return instructionQueue;
  }
}
