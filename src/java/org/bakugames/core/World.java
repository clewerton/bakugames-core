package org.bakugames.core;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class World implements Renderable, Updateable {
  private static final class EntityList extends ArrayList<Entity> {
    private static final long serialVersionUID = 1L;

    private World world;
    
    public EntityList(World world) {
      this.world = world;
    }

    @Override
    public boolean add(Entity e) {
      if(e == null)
        return false;
      
      if(contains(e))
        return false;
      
      super.add(e);
      e.setWorld(world);
      
      return true;
    }

    @Override
    public boolean contains(Object e) {
      if(e == null)
        return false;
      
      return super.contains(e);
    }

    @Override
    public boolean remove(Object e) {
      if(e == null)
        return false;
      
      boolean result = super.remove(e);
      
      if(result)
        ((Entity) e).setWorld(null);
      
      return result;
    }
  }

  private List<Entity> entities;

  public World() {
    setEntities0(new EntityList(this));
  }

  // entity management
  public void add(Entity e) {
    getEntities().add(e);
  }
  
  public boolean contains(Entity e) {
    return getEntities().contains(e);
  }
  
  public void remove(Entity e) {
    getEntities().remove(e);
  }
  
  // Slick methods
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    renderEntities(gc, sb, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    updateEntities(gc, sb, delta);
  }

  protected void renderEntities(GameContainer gc, StateBasedGame sb, Graphics gr) {
    for(Entity e : getEntities())
      e.render(gc, sb, gr);
  }

  protected void updateEntities(GameContainer gc, StateBasedGame sb, int delta) {
    for(Entity e : getEntities())
      e.update(gc, sb, delta);
  }
  
  // properties
  protected List<Entity> getEntities() {
    return entities;
  }

  private void setEntities0(List<Entity> entities) {
    this.entities = entities;
  }
}
