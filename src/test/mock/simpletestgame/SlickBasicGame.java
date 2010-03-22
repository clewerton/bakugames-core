/*
 * Originally from http://slick.cokeandcode.com/wiki/doku.php?id=01_-_a_basic_slick_game,
 * refactored to use bakugames entities.  
 */
package mock.simpletestgame;

import org.bakugames.core.Entity;
import org.bakugames.core.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SlickBasicGame extends BasicGameState {
  private Entity plane;
  private Entity land;
  
  private World world;
  
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
    
    // TODO deal with z-order!
    land = new Entity(world);
    land.plug(new BackgroundRenderer(new Image("src/test/mock/simpletestgame/land.jpg")));
    
    plane = new Entity(world);
    plane.plug(new Body(new Image("src/test/mock/simpletestgame/trollface.png"), 400, 300));
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    world.update(gc, null, delta);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    world.render(gc, null, g);
  }
}