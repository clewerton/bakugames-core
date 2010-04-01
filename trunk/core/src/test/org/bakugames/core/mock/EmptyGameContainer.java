package org.bakugames.core.mock;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;

public class EmptyGameContainer extends GameContainer {

  public EmptyGameContainer(Game game) {
    super(game);
    
  }

  @Override
  public int getScreenHeight() {
    
    return 0;
  }

  @Override
  public int getScreenWidth() {
    
    return 0;
  }

  @Override
  public boolean hasFocus() {
    
    return false;
  }

  @Override
  public boolean isMouseGrabbed() {
    
    return false;
  }

  @Override
  public void setDefaultMouseCursor() {
    

  }

  @Override
  public void setIcon(String ref) throws SlickException {
    

  }

  @Override
  public void setIcons(String[] refs) throws SlickException {
    

  }

  @Override
  public void setMouseCursor(String ref, int hotSpotX, int hotSpotY) throws SlickException {
    

  }

  @Override
  public void setMouseCursor(ImageData data, int hotSpotX, int hotSpotY) throws SlickException {
    

  }

  @Override
  public void setMouseCursor(Image image, int hotSpotX, int hotSpotY) throws SlickException {
    

  }

  @Override
  public void setMouseCursor(Cursor cursor, int hotSpotX, int hotSpotY) throws SlickException {
    

  }

  @Override
  public void setMouseGrabbed(boolean grabbed) {
    

  }

}
