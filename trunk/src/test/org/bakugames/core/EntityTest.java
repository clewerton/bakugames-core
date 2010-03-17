package org.bakugames.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static util.TestUtils.assertComponentNotPluggedIn;
import static util.TestUtils.assertComponentPluggedIn;

import mock.RenderableAndUpdateableComponent;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;
import org.bakugames.core.exception.IdConflictException;
import org.junit.Before;
import org.junit.Test;

public class EntityTest {
  private Entity e;

  @Before
  public void setUp() {
    e = new Entity();
  }

  @Test
  public void plugAndUnplug() {
    Component c = new Component("id");

    assertComponentNotPluggedIn(e, c);

    e.plug(c);

    assertComponentPluggedIn(e, c);
    
    e.unplug(c.getId());
    
    assertComponentNotPluggedIn(e, c);
  }
  
  @Test
  public void plugNull() {
    e.plug(null);
  }
  
  @Test
  public void plugRedundant() {
    Component c = new Component("id");

    assertComponentNotPluggedIn(e, c);

    e.plug(c);

    assertComponentPluggedIn(e, c);
    
    e.plug(c);
    
    assertComponentPluggedIn(e, c);
  }
  
  @Test
  public void plugWithIdConflict() {
    Component id1 = new Component("id", e);
    Component id2 = new Component("id");
    
    assertComponentPluggedIn(e, id1);
    assertComponentNotPluggedIn(e, id2);
    
    try {
      e.plug(id2);
      fail();
    } catch(IdConflictException e1) {
      assertEquals("id", e1.getId());
    }
    
    assertComponentPluggedIn(e, id1);
    assertComponentNotPluggedIn(e, id2);
  }
  
  @Test
  public void unplugNull() {
    Component id = new Component("id", e);
    
    assertNull(e.unplug(null));
    
    assertComponentPluggedIn(e, id);
  }
  
  @Test
  public void unplugUnknown() {
    Component id = new Component("id", e);
    
    assertNull(e.unplug(""));
    
    assertComponentPluggedIn(e, id);
  }
  
  @Test
  public void swap() {
    Component id1 = new Component("id", e);
    Component id2 = new Component("id");
    
    assertComponentPluggedIn(e, id1);
    assertComponentNotPluggedIn(e, id2);
    
    assertSame(id1, e.swap(id2));
    
    assertComponentNotPluggedIn(e, id1);
    assertComponentPluggedIn(e, id2);
  }
  
  @Test
  public void swapNull() {
    Component id = new Component("id", e);
    
    assertNull(e.swap(null));
    
    assertComponentPluggedIn(e, id);
  }
  
  @Test
  public void swapUnknown() {
    Component id = new Component("id");
    
    assertComponentNotPluggedIn(e, id);
    
    assertNull(e.swap(id));
    
    assertComponentPluggedIn(e, id);
  }
  
  @Test
  public void get() {
    Component id = new Component("id", e);
    
    assertSame(id, e.get(id.getId()));
  }
  
  @Test
  public void getNull() {
    assertNull(e.get(null));
  }
  
  @Test
  public void getUnknown() {
    assertNull(e.get(null));
  }
  
  @Test
  public void has() {
    Component id = new Component("id", e);
    Component sbrubbles = new Component("sbrubbles");
    
    assertTrue(e.has(id.getId()));
    assertFalse(e.has(sbrubbles.getId()));
    assertFalse(e.has(""));
  }
  
  @Test
  public void hasNull() {
    assertFalse(e.has(null));
  }
  
  @Test
  public void render() {
    RenderableAndUpdateableComponent c = new RenderableAndUpdateableComponent("id", e);
    
    assertEquals(0, c.renderCount);
    assertEquals(0, c.updateCount);
    
    e.render(null, null, null);
    
    assertEquals(1, c.renderCount);
    assertEquals(0, c.updateCount);
    
    e.update(null, null, 0);
    
    assertEquals(1, c.renderCount);
    assertEquals(1, c.updateCount);
  }
}
