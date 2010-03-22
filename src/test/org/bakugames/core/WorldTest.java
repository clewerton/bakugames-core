package org.bakugames.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static util.TestUtils.assertEntityInWorld;
import static util.TestUtils.assertEntityNotInWorld;

import org.bakugames.core.mock.RenderableAndUpdateableComponent;
import org.bakugames.core.mock.RenderableComponent;
import org.bakugames.core.mock.UpdateableComponent;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class WorldTest {
  private World w;

  private static class TimedEntity extends Entity {
    public long nanoTime = 0;

    public TimedEntity(int zOrder) {
      super(null, zOrder);
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
      super.render(gc, sb, gr);
      nanoTime = System.nanoTime();
    }
  }
  
  @Before
  public void setUp() {
    w = new World();
  }
  
  @Test
  public void addAndRemove() {
    Entity e = new Entity();
    
    assertEntityNotInWorld(w, e);
    
    w.add(e);
    
    assertEntityInWorld(w, e);
    
    w.remove(e);
    
    assertEntityNotInWorld(w, e);
  }
  
  @Test
  public void addNull() {
    Object[] entities = w.getEntities().toArray();
    
    w.add(null);
    
    assertArrayEquals(entities, w.getEntities().toArray());
  }
  
  @Test
  public void addRedundant() {
    Entity e = new Entity(w);
    
    assertEntityInWorld(w, e);
    
    Object[] entities = w.getEntities().toArray();
    
    w.add(e);
    
    assertArrayEquals(entities, w.getEntities().toArray());
  }
  
  @Test
  public void removeNull() {
    Object[] entities = w.getEntities().toArray();
    
    w.remove(null);
    
    assertArrayEquals(entities, w.getEntities().toArray());
  }
  
  @Test
  public void removeAbsent() {
    Entity e = new Entity();
    
    Object[] entities = w.getEntities().toArray();
    
    w.remove(e);
    
    assertEntityNotInWorld(w, e);
    assertArrayEquals(entities, w.getEntities().toArray());
    
    World w2 = new World();
    w2.add(e);
    
    assertEntityNotInWorld(w, e);
    assertEntityInWorld(w2, e);
    
    w.remove(e);
    
    assertEntityNotInWorld(w, e);
    assertEntityInWorld(w2, e);
  }
  
  @Test
  public void contains() {
    Entity in = new Entity(w);
    Entity out = new Entity();
    
    assertTrue(w.contains(in));
    assertFalse(w.contains(out));
  }
  
  @Test
  public void containsNull() {
    assertFalse(w.contains(null));
  }
  
  @Test
  public void renderAndUpdate() {
    Entity e = new Entity(w);
    RenderableAndUpdateableComponent c = new RenderableAndUpdateableComponent("c", e);
    
    Entity e2 = new Entity(w);
    RenderableComponent r = new RenderableComponent("r", e2);
    UpdateableComponent u = new UpdateableComponent("u", e2);
    
    assertEquals(0, c.renderCount);
    assertEquals(0, r.renderCount);
    assertEquals(0, c.updateCount);
    assertEquals(0, u.updateCount);
    
    w.render(null, null, null);
    
    assertEquals(1, c.renderCount);
    assertEquals(1, r.renderCount);
    assertEquals(0, c.updateCount);
    assertEquals(0, u.updateCount);
    
    w.update(null, null, 0);
    
    assertEquals(1, c.renderCount);
    assertEquals(1, r.renderCount);
    assertEquals(1, c.updateCount);
    assertEquals(1, u.updateCount);
  }
  
  @Test
  public void zOrder() {
    TimedEntity e0 = new TimedEntity(0);
    TimedEntity e1 = new TimedEntity(1);
    
    w.add(e1);
    w.add(e0);
    
    assertEquals(0, e0.nanoTime);
    assertEquals(0, e1.nanoTime);
    
    w.render(null, null, null);
    
    assertTrue(e0.nanoTime + ", " + e1.nanoTime, e0.nanoTime < e1.nanoTime);
  } 
}
