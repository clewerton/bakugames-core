package mock.simpletestgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bakugames.core.Entity;
import org.bakugames.core.Instruction;
import org.bakugames.input.Control;
import org.bakugames.input.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class SinglePlayer implements Player {
  private Entity entity;
  private Map<Control, Instruction> inputMap;
  
  public SinglePlayer(Entity entity) {
    this.entity = entity;
   
    inputMap = new HashMap<Control, Instruction>();
    
    init();
  }

  private void init() {
    Set<Instruction> instructionSet = entity.getInstructionSet();
    
    Map<String, Instruction> instructionMap = new HashMap<String, Instruction>();
    for(Instruction i : instructionSet)
      instructionMap.put(i.getName(), i);
    
    bind(new KeyDownControl(Input.KEY_A), instructionMap.get("turn left"));
    bind(new KeyDownControl(Input.KEY_D), instructionMap.get("turn right"));
    bind(new KeyDownControl(Input.KEY_W), instructionMap.get("run"));
    bind(new KeyDownControl(Input.KEY_2), instructionMap.get("scale up"));
    bind(new KeyDownControl(Input.KEY_1), instructionMap.get("scale down"));
  }
  
  // operations
  @Override
  public void bind(Control control, Instruction instruction) {
    if(control == null || instruction == null)
      throw new IllegalArgumentException(control + ", " + instruction);
    
    inputMap.put(control, instruction);
  }
  
  @Override
  public void unbind(Control control) {
    inputMap.remove(control);
  }
  
  @Override
  public void clear(Instruction instruction) {
    if(instruction == null)
      return;
    
    for(Control c : getControlsFor(instruction))
      inputMap.remove(c);
  }
  
  protected List<Control> getControlsFor(Instruction instruction) {
    if(! inputMap.containsValue(instruction))
      return Collections.EMPTY_LIST;
    
    List<Control> controls = new ArrayList<Control>();
    for(Entry<Control, Instruction> entry : inputMap.entrySet())
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
