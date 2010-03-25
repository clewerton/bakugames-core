package example.simpletestgame;

import java.util.Arrays;
import java.util.List;

import org.bakugames.core.Entity;
import org.bakugames.core.input.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class AIPlayer implements Player {
  private enum Step {
    RUN(300, "run"), TURN_RIGHT(226, "turn right");
    
    private int time;
    private int delay;
    private String instruction;

    private Step(int delay, String instruction) {
      this.delay = delay;
      this.instruction = instruction;
      
      time = 0;
    }
    
    public boolean execute(Entity entity, int delta) {
      time += delta;
      
      if(time < delay) {
        entity.execute(instruction);
        return false;
      }
      
      time = 0;
      return true;
    }
  }
  
  private Entity entity;
  private List<Step> script;
  private int cursor;
  
  public AIPlayer(Entity entity) {
    this.entity = entity;
    
    script = Arrays.asList(Step.RUN, Step.TURN_RIGHT);
    cursor = 0;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    Step step = script.get(cursor);
    
    if(step.execute(entity, delta))
      cursor = (cursor + 1) % script.size();
  }
}
