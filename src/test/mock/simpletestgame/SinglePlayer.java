package mock.simpletestgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bakugames.core.Entity;
import org.bakugames.input.Control;
import org.bakugames.input.InputBasedPlayer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class SinglePlayer implements InputBasedPlayer {
  private Entity entity;
  private Map<Control, String> inputMap;
  
  public SinglePlayer(Entity entity) {
    this.entity = entity;
   
    inputMap = new HashMap<Control, String>();
    
    init();
  }

  private void init() {
    bind(new KeyDownControl(Input.KEY_A), "turn left");
    bind(new KeyDownControl(Input.KEY_D), "turn right");
    bind(new KeyDownControl(Input.KEY_W), "run");
    bind(new KeyDownControl(Input.KEY_2), "scale up");
    bind(new KeyDownControl(Input.KEY_1), "scale down");
  }
  
  // operations
  @Override
  public void bind(Control control, String instruction) {
    if(control == null || instruction == null)
      throw new IllegalArgumentException(control + ", " + instruction);
    
    inputMap.put(control, instruction);
  }
  
  @Override
  public void unbind(Control control) {
    if(control == null)
      throw new IllegalArgumentException("null");
    
    inputMap.remove(control);
  }
  
  @Override
  public void clear(String instruction) {
    if(instruction == null)
      return;
    
    for(Control c : getControlsFor(instruction))
      inputMap.remove(c);
  }
  
  protected List<Control> getControlsFor(String instruction) {
    if(! inputMap.containsValue(instruction))
      return Collections.EMPTY_LIST;
    
    List<Control> controls = new ArrayList<Control>();
    for(Entry<Control, String> entry : inputMap.entrySet())
      if(instruction.equals(entry.getValue()))
        controls.add(entry.getKey());
    
    return controls;
  }
  
  // input methods
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    Input input = gc.getInput();

    checkForInput(input);
  }

  protected void checkForInput(Input input) {
    for(Control control : inputMap.keySet())
      if(control.happened(input))
        entity.execute(inputMap.get(control));
  }
}
