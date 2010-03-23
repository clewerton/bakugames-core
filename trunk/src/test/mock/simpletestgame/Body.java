package mock.simpletestgame;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.bakugames.core.Component;
import org.bakugames.core.Controllable;
import org.bakugames.core.Entity;
import org.bakugames.core.Instruction;
import org.bakugames.core.Renderable;
import org.bakugames.core.Updateable;
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
  private Set<Instruction> instructionSet;
  private Queue<Instruction> instructionQueue;
  
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
    instructionSet = new HashSet<Instruction>();
    registerInstructions();
  }

  private void registerInstructions() {
    registerInstruction(new Instruction() {
      @Override
      public String getName() {
        return "turn left";
      }
      
      @Override
      public void execute(Component c, GameContainer gc, StateBasedGame sb, int delta) {
        image.rotate(- 0.2f * delta);
      }
    });
    
    registerInstruction(new Instruction() {
      @Override
      public String getName() {
        return "turn right";
      }
      
      @Override
      public void execute(Component c, GameContainer gc, StateBasedGame sb, int delta) {
        image.rotate(0.2f * delta);
      }
    });
    
    registerInstruction(new Instruction() {
      @Override
      public String getName() {
        return "run";
      }
      
      @Override
      public void execute(Component c, GameContainer gc, StateBasedGame sb, int delta) {
        float hip = 0.4f * delta;

        x += hip * Math.sin(Math.toRadians(image.getRotation()));
        y -= hip * Math.cos(Math.toRadians(image.getRotation()));
      }
    });
    
    registerInstruction(new Instruction() {
      @Override
      public String getName() {
        return "scale up";
      }
      
      @Override
      public void execute(Component c, GameContainer gc, StateBasedGame sb, int delta) {
        scale += (scale >= 5.0f) ? 0 : 0.1f;
        image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);
      }
    });
    
    registerInstruction(new Instruction() {
      @Override
      public String getName() {
        return "scale down";
      }
      
      @Override
      public void execute(Component c, GameContainer gc, StateBasedGame sb, int delta) {
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
      instructionQueue.poll().execute(this, gc, sb, delta);
  }

  // interface methods
  @Override
  public int compareTo(Renderable o) {
    return CompareUtils.compareTo(this, o);
  }
  
  @Override
  public Set<Instruction> getInstructionSet() {
    return Collections.unmodifiableSet(instructionSet);
  }

  @Override
  public void execute(Instruction instruction) {
    if(! understands(instruction))
      return;
    
    instructionQueue.add(instruction);
  }

  @Override
  public boolean understands(Instruction instruction) {
    if(instruction == null)
      return false;
    
    return instructionSet.contains(instruction);
  }
  
  protected void registerInstruction(Instruction instruction) {
    if(instruction == null)
      return;
    
    instructionSet.add(instruction);
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
