package example.simpletestgame;

import org.bakugames.core.Entity;
import org.bakugames.core.input.AbstractInputBasedPlayer;
import org.newdawn.slick.Input;

public class SinglePlayer extends AbstractInputBasedPlayer {
  private Entity entity;
  
  public SinglePlayer(Entity entity) {
    this.entity = entity;
   
    init();
  }

  private void init() {
    bind(new KeyDownControl(Input.KEY_A), "turn left");
    bind(new KeyDownControl(Input.KEY_D), "turn right");
    bind(new KeyDownControl(Input.KEY_W), "run");
    bind(new KeyDownControl(Input.KEY_2), "scale up");
    bind(new KeyDownControl(Input.KEY_1), "scale down");
  }
  
  @Override
  protected void emitInstruction(Object instructionId) {
    entity.execute(instructionId);
  }
}
