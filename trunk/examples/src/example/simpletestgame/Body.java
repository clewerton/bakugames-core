package example.simpletestgame;

import org.bakugames.core.Entity;
import org.bakugames.core.input.Instruction;
import org.bakugames.core.traits.BasicControllableComponent;
import org.bakugames.core.traits.Renderable;
import org.bakugames.core.traits.Updateable;
import org.bakugames.util.CompareUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Body extends BasicControllableComponent implements Renderable, Updateable {
  private float x;
  private float y;
  private float scale;
  private Image image;
  private int zOrder;
  
  public Body(Image image, float x, float y, int zOrder) {
    this(null, image, x, y, zOrder);
  }

  public Body(Entity owner, Image image, float x, float y, int zOrder) {
    super("body", owner);
    
    this.image = image;
    this.x = x;
    this.y = y;
    this.scale = 1;
    this.zOrder = zOrder;
    
    registerInstructions();
  }

  private void registerInstructions() {
    getImplementation().set("turn left", 
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            image.rotate(- 0.2f * delta);
          }
        });
    
    getImplementation().set("turn right",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            image.rotate(0.2f * delta);
          }
        });
    
    getImplementation().set("run",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            float hip = 0.4f * delta;
    
            x += hip * Math.sin(Math.toRadians(image.getRotation()));
            y -= hip * Math.cos(Math.toRadians(image.getRotation()));
          }
        });
    
    getImplementation().set("scale up",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
            image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);
          }
        });
    
    getImplementation().set("scale down",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            scale -= (scale <= 1.0f) ? 0 : 0.1f;
            image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);
          }
        });
  }

  // Slick methods
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    image.draw(x, y, scale);
  }
  
  // interface methods
  @Override
  public int compareTo(Renderable o) {
    return CompareUtils.compareTo(this, o);
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

  @Override
  public int getZOrder() {
    return zOrder;
  }

  public void setZOrder(int zOrder) {
    this.zOrder = zOrder;
  }
}
