package org.bakugames.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bakugames.core.exception.ComponentIdMismatchException;
import org.bakugames.core.exception.IdConflictException;
import org.bakugames.core.traits.Controllable;
import org.bakugames.core.traits.Renderable;
import org.bakugames.core.traits.Updateable;
import org.bakugames.util.CompareUtils;
import org.bakugames.util.SortedList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Entity implements Renderable, Updateable, Controllable {
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
      if(c.equals(cPrime))
        return c;
      
      if(cPrime != null)
        throw new IdConflictException(key);
      
      c.setOwner(entity);
      
      super.put(key, c);
      
      if(c instanceof Renderable && ! entity.renderableComponents.contains(c))
        entity.renderableComponents.add((Renderable) c);
      
      if(c instanceof Updateable && ! entity.updateableComponents.contains(c))
        entity.updateableComponents.add((Updateable) c);
      
      if(c instanceof Controllable && ! entity.controllableComponents.contains(c))
        entity.controllableComponents.add((Controllable) c);
      
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
     
      if(c instanceof Controllable)
        entity.controllableComponents.remove((Controllable) c);
      
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
  private int zOrder;
  
  // these lists are backed by componentMap, so they won't be
  // accessed anywhere else but here 
  // TODO use TreeSet instead?
  private List<Renderable> renderableComponents; // sorted by z-order
  private List<Updateable> updateableComponents;
  private List<Controllable> controllableComponents;
  
  public Entity() {
    this(null, 0);
  }
  
  public Entity(int zOrder) {
    this(null, zOrder);
  }
  
  public Entity(World world) {
    this(world, 0);
  }
  
  public Entity(World world, int zOrder) {
    this.zOrder = zOrder;
    
    setWorld0(world);
    
    renderableComponents = new SortedList(new ArrayList<Renderable>());
    updateableComponents = new ArrayList<Updateable>();
    controllableComponents = new ArrayList<Controllable>();
    
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
      throw new IllegalArgumentException("null");
    
    Component c = get(id);
    return (c != null) ? c.as(type) : null;
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
  
  // interface methods
  @Override
  public int compareTo(Renderable o) {
    return CompareUtils.compareTo(this, o);
  }
  
  @Override
  public boolean understands(Object instruction) {
    if(instruction == null)
      return false;
    
    return getInstructionSet().contains(instruction);
  }
  
  @Override
  public Set<Object> getInstructionSet() {
    Set<Object> instructionSet = new HashSet<Object>();
    
    for(Controllable c : controllableComponents)
      instructionSet.addAll(c.getInstructionSet());
      
    return instructionSet;
  }
  
  // Slick operations
  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    renderComponents(gc, sb, gr);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    updateComponents(gc, sb, delta);
  }

  @Override
  public void execute(Object instructionId) {
    orderComponents(instructionId);
  }

  protected void renderComponents(GameContainer gc, StateBasedGame sb, Graphics gr) {
    for(Renderable r : renderableComponents)
      r.render(gc, sb, gr);
  }
  
  protected void updateComponents(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    for(Updateable u : updateableComponents)
      u.update(gc, sb, delta);
  }

  protected void orderComponents(Object instruction) {
    for(Controllable c : controllableComponents)
      c.execute(instruction);
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
    if(this.world == world) // nothing changes
      return;
    
    if(this.world != null && this.world.contains(this)) // 3, 2, 1, BLASTOFF!
      this.world.remove(this);
    
    this.world = world;
    
    if(world != null) // Toto, I don't think we're in Kansas anymore
      world.add(this);
  }

  @Override
  public int getZOrder() {
    return zOrder;
  }
}
