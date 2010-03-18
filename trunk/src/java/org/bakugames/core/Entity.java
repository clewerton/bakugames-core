package org.bakugames.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bakugames.core.exception.IdConflictException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Entity implements Renderable, Updateable {
  private Map<String, Component> componentMap;
  
  // these two lists are backed by componentMap, so they won't be
  // accessed anywhere else but here 
  private List<Renderable> renderableComponents;
  private List<Updateable> updateableComponents;
  
  public Entity() {
    componentMap = new HashMap<String, Component>();
    
    renderableComponents = new ArrayList<Renderable>();
    updateableComponents = new ArrayList<Updateable>();
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
    if(id == null)
      return null;
    
    return componentMap.get(id);
  }
  
  public boolean has(String id) {
    if(id == null)
      return false;
    
    return componentMap.containsKey(id);
  }
  
  protected void plugComponent(Component c) {
    if(c == null)
      return;
    
    Component cPrime = get(c.getId());
    if(cPrime == c)
      return;
    
    if(cPrime != null)
      throw new IdConflictException(c.getId());
    
    c.setOwner(this);
    
    componentMap.put(c.getId(), c);
    
    if(c instanceof Renderable)
      renderableComponents.add((Renderable) c);
    
    if(c instanceof Updateable)
      updateableComponents.add((Updateable) c);
  }
  
  protected Component unplugComponent(String id) {
    if(id == null)
      return null;
    
    Component c = componentMap.remove(id);
    
    if(c == null)
      return null;
    
    if(c instanceof Renderable)
      renderableComponents.remove((Renderable) c);
    
    if(c instanceof Updateable)
      updateableComponents.remove((Updateable) c);
   
    c.setOwner(null);
    
    return c;
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
}
