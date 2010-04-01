package org.bakugames.core.traits;

import java.util.Set;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BasicControllableComponent extends Component implements Controllable {
  private ControllableImpl implementation;

  public BasicControllableComponent(String id) {
    this(id, null);
  }
  
  public BasicControllableComponent(String id, Entity entity) {
    super(id, entity);
    
    setImplementation0(new ControllableImpl());
  }
  
  // Slick operations
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    implementation.update(gc, sb, delta);
  }

  // Controllable operations
  @Override
  public void execute(Object instruction) {
    implementation.execute(instruction);
  }

  @Override
  public Set<Object> getInstructionSet() {
    return implementation.getInstructionSet();
  }

  @Override
  public boolean understands(Object instruction) {
    return implementation.understands(instruction);
  }

  // properties
  protected ControllableImpl getImplementation() {
    return implementation;
  }

  protected void setImplementation(ControllableImpl implementation) {
    setImplementation0(implementation);
  }

  private void setImplementation0(ControllableImpl implementation) {
    if(implementation == null)
      throw new IllegalArgumentException("null");
    
    this.implementation = implementation;
  }
}