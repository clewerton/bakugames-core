package mock.simpletestgame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bakugames.core.Entity;
import org.bakugames.core.Instruction;
import org.bakugames.core.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class BasicPlayer extends Player {
  private Entity entity;
  private Map<String, Instruction> commandMap;
  
  public BasicPlayer(Entity entity) {
    super(entity);
    
    commandMap = new HashMap<String, Instruction>();
    populateCommandMap();
  }

  private void populateCommandMap() {
    Set<Instruction> instructionSet = entity.getInstructionSet();
    
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

  public Entity getEntity() {
    return entity;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }
}
