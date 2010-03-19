package org.bakugames.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bakugames.core.exception.ComponentIdMismatchException;
import org.bakugames.core.exception.IdConflictException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Entity implements Renderable, Updateable {
  private static final class ComponentMap extends HashMap<String, Component> {
    private static final long serialVersionUID = 1L;

    private Entity entity;
    
    public ComponentMap(Entity entity) {
      this.entity = entity;
    }
    
    @Override
    public Component get(Object key) {
      if(key == null)
        return null;
      
      return super.get(key);
    }

    @Override
    public Component put(String key, Component c) {
      if(c == null)
        return null;
      
      if(! c.getId().equals(key))
        throw new ComponentIdMismatchException(key, c.getId());
      
      Component cPrime = get(key);
      if(cPrime == c)
        return c;
      
      if(cPrime != null)
        throw new IdConflictException(key);
      
      c.setOwner(entity);
      
      super.put(key, c);
      
      if(c instanceof Renderable)
        entity.renderableComponents.add((Renderable) c);
      
      if(c instanceof Updateable)
        entity.updateableComponents.add((Updateable) c);
      
      return c;
    }

    @Override
    public Component remove(Object key) {
      if(! (key instanceof String))
        return null;
      
      String id = (String) key;
      
      Component c = super.remove(id);
      
      if(c == null)
        return null;
      
      if(c instanceof Renderable)
        entity.renderableComponents.remove((Renderable) c);
      
      if(c instanceof Updateable)
        entity.updateableComponents.remove((Updateable) c);
     
      c.setOwner(null);
      
      return c;
    }

    @Override
    public boolean containsKey(Object key) {
      if(key == null)
        return false;
      
      return super.containsKey(key);
    }
  }

  private World world;
  private Map<String, Component> componentMap;
  
  // these two lists are backed by componentMap, so they won't be
  // accessed anywhere else but here 
  private List<Renderable> renderableComponents; // TODO sort by z-order
  private List<Updateable> updateableComponents;
  
  public Entity() {
    this(null);
  }
  
  public Entity(World world) {
    setWorld0(world);
    
    renderableComponents = new ArrayList<Renderable>();
    updateableComponents = new ArrayList<Updateable>();
    
    componentMap = new ComponentMap(this);
  }
  
  // component management
  public void plug(Component c) {
    plugComponent(c);
  }

  public Component unplug(String id) {
    return unplugComponent(id);
  }

  public Component swap(Component c) {
    return swapComponent(c);
  }

  public Component get(String id) {
    return getComponentMap().get(id);
  }
  
  public <T> T getAs(String id, Class<T> type) {
    if(type == null)
      throw new IllegalArgumentException(String.valueOf(type));
    
    Component c = get(id);
    if(c == null)
      return null;
    
    return c.as(type);
  }
  
  public boolean has(String id) {
    return getComponentMap().containsKey(id);
  }
  
  protected void plugComponent(Component c) {
    if(c == null)
      return;
    
    getComponentMap().put(c.getId(), c);
  }
  
  protected Component unplugComponent(String id) {
    return getComponentMap().remove(id);
  }

  protected Component swapComponent(Component c) {
    if(c == null)
      return null;
    
    Component old = unplugComponent(c.getId());
    plugComponent(c);
    
    return old;
  }
  
  // Slick operations
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    renderComponents(gc, sb, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    updateComponents(gc, sb, delta);
  }

  protected void renderComponents(GameContainer gc, StateBasedGame sb, Graphics gr) {
    for(Renderable r : renderableComponents)
      r.render(gc, sb, gr);
  }
  
  protected void updateComponents(GameContainer gc, StateBasedGame sb, int delta) {
    for(Updateable u : updateableComponents)
      u.update(gc, sb, delta);
  }

  // properties
  protected Map<String, Component> getComponentMap() {
    return componentMap;
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    setWorld0(world);
  }

  private void setWorld0(World world) {
    if(this.world == world)
      return;
    
    if(this.world != null && this.world.contains(this))
      this.world.remove(this);
    
    this.world = world;
    
    if(world != null)
      world.add(this);
  }
}
