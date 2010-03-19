package mock;

import org.bakugames.core.Updateable;
import org.bakugames.core.entity.Component;
import org.bakugames.core.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class UpdateableComponent extends Component implements Updateable {
  public int updateCount = 0;
  
  public UpdateableComponent(String id) {
    super(id);
  }

  public UpdateableComponent(String id, Entity owner) {
    super(id, owner);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    updateCount++;
  }
}
