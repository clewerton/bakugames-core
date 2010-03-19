package org.bakugames.core.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static util.TestUtils.assertComponentNotPluggedIn;
import static util.TestUtils.assertComponentPluggedIn;

import mock.RenderableAndUpdateableComponent;

import org.bakugames.core.Renderable;
import org.bakugames.core.entity.Component;
import org.bakugames.core.entity.Entity;
import org.bakugames.core.entity.exception.IdConflictException;
import org.junit.Before;
import org.junit.Test;

public class ComponentTest {
  private Component c;
  
  @Before
  public void setUp() {
    c = new RenderableAndUpdateableComponent("id");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void buildWithNullId() {
    new Component(null);
  }
  
  @Test
  public void buildWithEntity() {
    Entity e = new Entity();
    
    c = new RenderableAndUpdateableComponent("id", e);
    
    assertComponentPluggedIn(e, c);
  } 
  
  @Test
  public void buildWithIdConflict() {
    Entity e = new Entity();
    
    Component id1 = new Component("id", e);
    Component id2 = null;
    
    assertComponentPluggedIn(e, id1);
    assertNull(id2);
    
    try {
      id2 = new Component("id", e);
      fail();
    } catch(IdConflictException e1) {
      assertEquals("id", e1.getId());
    }
    
    assertComponentPluggedIn(e, id1);
    assertNull(id2);
  }
  
  @Test
  public void as() {
    Renderable r = c.as(Renderable.class);
    
    assertSame(r, c);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void asNull() {
    c.as(null);
  }
  
  @Test(expected = ClassCastException.class)
  public void asInvalid() {
    c.as(String.class);
  }
  
  @Test
  public void id() {
    assertEquals("id", c.getId());
    assertEquals("sbrubbles", new Component("sbrubbles").getId());
    assertEquals("", new Component("").getId());
  }
  
  @Test
  public void owner() {
    Entity e = new Entity();
    
    assertComponentNotPluggedIn(e, c);
    
    c.setOwner(e);
    
    assertComponentPluggedIn(e, c);
    
    c.setOwner(null);
    
    assertComponentNotPluggedIn(e, c);
  }
  
  @Test
  public void ownerRedundant() {
    Entity e = new Entity();
    
    assertComponentNotPluggedIn(e, c);
    
    c.setOwner(e);
    
    assertComponentPluggedIn(e, c);
    
    c.setOwner(e);
    
    assertComponentPluggedIn(e, c);
  }
  
  @Test
  public void ownerWithConflict() {
    Entity e = new Entity();
    c.setOwner(e);
    
    Component id = new Component("id");
    
    assertComponentPluggedIn(e, c);
    assertComponentNotPluggedIn(e, id);
    
    try {
      id.setOwner(e);
      fail();
    } catch(IdConflictException e1) {
      assertEquals(id.getId(), e1.getId());
    }
    
    assertComponentPluggedIn(e, c);
    assertComponentNotPluggedIn(e, id);
  }
  
  @Test
  public void entitySwap() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    
    c.setOwner(e1);
    
    assertComponentPluggedIn(e1, c);
    assertComponentNotPluggedIn(e2, c);
    
    c.setOwner(e2);
    
    assertComponentNotPluggedIn(e1, c);
    assertComponentPluggedIn(e2, c);
  }
  
  @Test
  public void entitySwapWithConflict() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    
    c.setOwner(e1);
    Component id = new Component("id", e2);
    
    assertComponentPluggedIn(e1, c);
    assertComponentPluggedIn(e2, id);
    assertComponentNotPluggedIn(e1, id);
    assertComponentNotPluggedIn(e2, c);
    
    try {
      c.setOwner(e2);
      fail();
    } catch(IdConflictException e) {
      assertEquals(c.getId(), e.getId());
    }
    
    assertComponentPluggedIn(e1, c);
    assertComponentPluggedIn(e2, id);
    assertComponentNotPluggedIn(e1, id);
    assertComponentNotPluggedIn(e2, c);
  }
}
