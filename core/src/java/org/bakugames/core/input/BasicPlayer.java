package org.bakugames.core.input;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bakugames.core.traits.Controllable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BasicPlayer implements InputBasedPlayer {
  private Map<Control, Object> inputMap;
  private Controllable controlled;
  
  public BasicPlayer(Controllable controlled) {
    this.controlled = controlled;
    
    inputMap = new HashMap<Control, Object>();
  }

  @Override
  public void bind(Control control, Object instructionId) {
    if(control == null || instructionId == null)
      throw new IllegalArgumentException(control + ", " + instructionId);
    
    inputMap.put(control, instructionId);
  }

  @Override
  public void unbind(Control control) {
    if(control == null)
      return;
    
    inputMap.remove(control);
  }

  @Override
  public Object get(Control control) {
    return inputMap.get(control);
  }
  
  @Override
  public Control[] getControls() {
    return inputMap.keySet().toArray(new Control[inputMap.size()]);
  }
  
  @Override
  public void clear(Object instructionId) {
    if(instructionId == null)
      return;
    
    for(Control c : getControlsFor(instructionId))
      inputMap.remove(c);
  }

  protected Set<Control> getControlsFor(Object instructionId) {
    if(! inputMap.containsValue(instructionId))
      return Collections.EMPTY_SET;
    
    Set<Control> controls = new HashSet<Control>();
    for(Entry<Control, Object> entry : inputMap.entrySet())
      if(instructionId.equals(entry.getValue()))
        controls.add(entry.getKey());
    
    return controls;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    if(getControlled() == null)
      return;
    
    Input input = gc.getInput();
    
    for(Control control : getControls())
      if(control.happened(input))
        getControlled().execute(get(control));
    
    getControlled().update(gc, sb, delta);
  }
  
  @Override
  public Controllable getControlled() {
    return controlled;
  }

  protected void setControlled(Controllable controlled) {
    this.controlled = controlled;
  }
}