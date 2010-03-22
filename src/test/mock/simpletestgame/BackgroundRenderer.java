package mock.simpletestgame;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.Renderable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class BackgroundRenderer extends Component implements Renderable {
  private Image image;
  
  public BackgroundRenderer(Image image) {
    this(image, null);
  }

  public BackgroundRenderer(Image image, Entity owner) {
    super("renderer", owner);
    
    this.image = image;
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    image.draw(0, 0);
  }
}
