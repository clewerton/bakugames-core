package example.simpletestgame;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.input.Instruction;
import org.bakugames.core.traits.Controllable;
import org.bakugames.core.traits.Renderable;
import org.bakugames.core.traits.Updateable;
import org.bakugames.util.CompareUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Body extends Component implements Renderable, Updateable, Controllable {
  private float x;
  private float y;
  private float scale;
  private Image image;
  private int zOrder;
  private Queue<Instruction> instructionQueue;
  private Map<Object, Instruction> instructionMap;
    
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
    
    instructionQueue = new LinkedList<Instruction>();
    instructionMap = new HashMap<Object, Instruction>();
    registerInstructions();
  }

  private void registerInstructions() {
    instructionMap.put(
        "turn left", 
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            image.rotate(- 0.2f * delta);
          }
        });
    
    instructionMap.put(
        "turn right",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            image.rotate(0.2f * delta);
          }
        });
    
    instructionMap.put(
        "run",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            float hip = 0.4f * delta;
    
            x += hip * Math.sin(Math.toRadians(image.getRotation()));
            y -= hip * Math.cos(Math.toRadians(image.getRotation()));
          }
        });
    
    instructionMap.put(
        "scale up",
        new Instruction() {
          @Override
          public void execute(GameContainer gc, StateBasedGame sb, int delta) {
            scale += (scale >= 5.0f) ? 0 : 0.1f;
            image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);
          }
        });
    
    instructionMap.put(
        "scale down",
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
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    while(! instructionQueue.isEmpty())
      instructionQueue.poll().execute(gc, sb, delta);
  }

  // interface methods
  @Override
  public int compareTo(Renderable o) {
    return CompareUtils.compareTo(this, o);
  }
  
  @Override
  public Set<Object> getInstructionSet() {
    return Collections.unmodifiableSet(instructionMap.keySet());
  }

  @Override
  public void execute(Object instruction) {
    if(! understands(instruction))
      return;
    
    instructionQueue.add(instructionMap.get(instruction));
  }

  @Override
  public boolean understands(Object instruction) {
    if(instruction == null)
      return false;
    
    return instructionMap.containsKey(instruction);
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
