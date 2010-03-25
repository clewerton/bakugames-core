package example.simpletestgame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainGame extends StateBasedGame {
  private static final int SLICK_BASED_GAME = 0;

  public MainGame() {
    super("whatever.com");
    
    addState(new SlickBasicGame(SLICK_BASED_GAME));
    enterState(SLICK_BASED_GAME);
  }

  @Override
  public void initStatesList(GameContainer gc) throws SlickException {
    getState(SLICK_BASED_GAME).init(gc, this);
  }

  public static void main(String[] args) throws SlickException {
    AppGameContainer app = new AppGameContainer(new MainGame());

    app.setDisplayMode(800, 600, false);
    app.start();
  }
}
