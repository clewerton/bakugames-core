/*
 * Originally from http://slick.cokeandcode.com/wiki/doku.php?id=01_-_a_basic_slick_game,
 * refactored to use bakugames entities.  
 */
package example.simpletestgame;

import org.bakugames.core.Entity;
import org.bakugames.core.World;
import org.bakugames.core.input.BasicPlayer;
import org.bakugames.core.input.InputBasedPlayer;
import org.bakugames.core.input.Instruction;
import org.bakugames.core.traits.ControllableImpl;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SlickBasicGame extends BasicGameState {
  private Entity plane;
  private Entity land;
  
  private World world;
  private InputBasedPlayer player;
  private InputBasedPlayer controller;
  
  private int id;
  
  public SlickBasicGame(int id) {
    this.id = id;
  }

  @Override
  public int getID() {
    return id;
  }
  
  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    world = new World();
    
    plane = new Entity(world, 1);
    plane.plug(new Body(new Image("examples/resources/trollface.png"), 400, 300, 0));
    
    land = new Entity(world, 0);
    land.plug(new BackgroundRenderer(new Image("examples/resources/land.jpg")));
    
    player = new BasicPlayer(plane);
    player.bind(new KeyDownControl(Input.KEY_A), "turn left");
    player.bind(new KeyDownControl(Input.KEY_D), "turn right");
    player.bind(new KeyDownControl(Input.KEY_W), "run");
    player.bind(new KeyDownControl(Input.KEY_2), "scale up");
    player.bind(new KeyDownControl(Input.KEY_1), "scale down");
    
    controller = new BasicPlayer(
        new ControllableImpl() {{
          set("exit", 
              new Instruction() {
                @Override
                public void execute(GameContainer gc, StateBasedGame sb, int delta) {
                  System.exit(0);
                }
              });
        }});
    controller.bind(new KeyDownControl(Input.KEY_ESCAPE), "exit");
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    controller.update(gc, sbg, delta);
    world.update(gc, sbg, delta);
    player.update(gc, sbg, delta);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    world.render(gc, sbg, g);
  }
}