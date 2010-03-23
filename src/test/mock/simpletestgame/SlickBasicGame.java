/*
 * Originally from http://slick.cokeandcode.com/wiki/doku.php?id=01_-_a_basic_slick_game,
 * refactored to use bakugames entities.  
 */
package mock.simpletestgame;

import org.bakugames.core.Entity;
import org.bakugames.core.World;
import org.bakugames.input.Player;
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
  private Player player;
  
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
    plane.plug(new Body(new Image("src/test/mock/simpletestgame/trollface.png"), 400, 300, 0));
    
    land = new Entity(world, 0);
    land.plug(new BackgroundRenderer(new Image("src/test/mock/simpletestgame/land.jpg")));
    
    player = new SinglePlayer(plane);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    Input input = gc.getInput();
    
    if(input.isKeyDown(Input.KEY_ESCAPE))
      System.exit(0);
    
    world.update(gc, sbg, delta);
    player.update(gc, sbg, delta);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    world.render(gc, sbg, g);
  }
}