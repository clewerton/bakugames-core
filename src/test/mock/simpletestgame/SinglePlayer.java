package mock.simpletestgame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bakugames.core.Entity;
import org.bakugames.core.Instruction;
import org.bakugames.core.Player;
import org.bakugames.core.traits.Controllable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class SinglePlayer implements Player {
  private Entity entity;
  private Map<String, Instruction> commandMap;
  
  public SinglePlayer(Entity entity) {
    this.entity = entity;
   
    Set<Instruction> instructionSet = entity.getInstructionSet();
    
    commandMap = new HashMap<String, Instruction>();
    for(Instruction i : instructionSet)
      commandMap.put(i.getName(), i);
  }

  // input methods
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    Input input = gc.getInput();

    if(input.isKeyDown(Input.KEY_A))
      controlPressed("turn left");
    
    if(input.isKeyDown(Input.KEY_D))
      controlPressed("turn right");
    
    if(input.isKeyDown(Input.KEY_W))
      controlPressed("run");

    if(input.isKeyDown(Input.KEY_2))
      controlPressed("scale up");
    
    if(input.isKeyDown(Input.KEY_1))
      controlPressed("scale down");
  }
  
  protected void controlPressed(String name) {
    Instruction i = commandMap.get(name);
    if(i == null)
      return;
    
    entity.execute(i);
  }

  @Override
  public List<? extends Controllable> getControlled() {
    return Arrays.asList(entity);
  }
}
