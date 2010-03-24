package mock.simpletestgame;

import org.bakugames.core.Entity;
import org.bakugames.input.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class AIPlayer implements Player {
  private Entity entity;
  private int time;
  
  public AIPlayer(Entity entity) {
    this.entity = entity;
    time = 0;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    System.out.println("delta = " + delta);
    time += delta;
    
    if(time < 400) {
      entity.execute("run");
      return;
    }
    
    if(time < 625) {
      entity.execute("turn right");
      return;
    }
    
    if(time < 1025) {
      entity.execute("run");
      return;
    }
    
    if(time < 1250) {
      entity.execute("turn right");
      return;
    }
    
    if(time < 1650) {
      entity.execute("run");
      return;
    }
    
    if(time < 1875) {
      entity.execute("turn right");
      return;
    }
    
    if(time < 2275) {
      entity.execute("run");
      return;
    }
    
    time = 0;
  }
}
