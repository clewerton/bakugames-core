package mock.simpletestgame;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.Renderable;
import org.bakugames.core.Updateable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class Body extends Component implements Renderable, Updateable {
  private float x;
  private float y;
  private float scale;
  private Image image;
  
  public Body(Image image, float x, float y) {
    this(null, image, x, y);
  }

  public Body(Entity owner, Image image, float x, float y) {
    super("body", owner);
    
    this.image = image;
    this.x = x;
    this.y = y;
    this.scale = 1;
  }

  // Slick methods
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    image.draw(x, y, scale);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    Input input = gc.getInput();

    if(input.isKeyDown(Input.KEY_A))
      image.rotate(- 0.2f * delta);
    
    if(input.isKeyDown(Input.KEY_D))
      image.rotate(0.2f * delta);
    
    if(input.isKeyDown(Input.KEY_W)) {
      float hip = 0.4f * delta;

      x += hip * Math.sin(Math.toRadians(image.getRotation()));
      y -= hip * Math.cos(Math.toRadians(image.getRotation()));
    }

    if(input.isKeyDown(Input.KEY_2)) {
      scale += (scale >= 5.0f) ? 0 : 0.1f;
      image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);
    }
    
    if(input.isKeyDown(Input.KEY_1)) {
      scale -= (scale <= 1.0f) ? 0 : 0.1f;
      image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);
    }
  }

  // properties
  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public float getRotation() {
    return image.getRotation();
  }

  public void setRotation(float rotation) {
    image.setRotation(rotation);
  }
}
