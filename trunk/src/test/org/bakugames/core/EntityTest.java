package org.bakugames.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static util.TestUtils.assertCompatible;
import static util.TestUtils.assertComponentNotPluggedIn;
import static util.TestUtils.assertComponentPluggedIn;
import static util.TestUtils.assertEntityInWorld;
import static util.TestUtils.assertEntityNotInWorld;

import java.util.Map;


import org.bakugames.core.exception.ComponentIdMismatchException;
import org.bakugames.core.exception.IdConflictException;
import org.bakugames.core.mock.RenderableAndUpdateableComponent;
import org.bakugames.core.mock.RenderableComponent;
import org.bakugames.core.mock.UpdateableComponent;
import org.junit.Before;
import org.junit.Test;

public class EntityTest {
  private Entity e;
  private Map<String, Component> componentMap;

  @Before
  public void setUp() {
    e = new Entity();
    componentMap = e.getComponentMap();
  }

  @Test
  public void buildWithNull() {
    World w = new World();
    Entity e = new Entity(null);
    
    assertNull(e.getWorld());
    assertFalse(w.contains(e));
  }
  
  @Test
  public void buildWithWorld() {
    World w = new World();
    Entity e = new Entity(w);
    
    assertEntityInWorld(w, e);
  }
  
  @Test
  public void setWorld() {
    World earth = new World();
    World mars = new World();
    
    assertEntityNotInWorld(earth, e);
    assertEntityNotInWorld(mars, e);
    
    e.setWorld(earth);
    
    assertEntityInWorld(earth, e);
    assertEntityNotInWorld(mars, e);
    
    e.setWorld(mars);
    
    assertEntityNotInWorld(earth, e);
    assertEntityInWorld(mars, e);
    
    e.setWorld(null);
    
    assertEntityNotInWorld(earth, e);
    assertEntityNotInWorld(mars, e);
  }

  @Test
  public void plugAndUnplug() {
    Component c = new Component("id");

    assertComponentNotPluggedIn(e, c);
    assertFalse(componentMap.containsKey(c.getId()));
    assertFalse(componentMap.containsValue(c));

    e.plug(c);

    assertComponentPluggedIn(e, c);
    assertSame(c, componentMap.get(c.getId()));

    e.unplug(c.getId());
    
    assertComponentNotPluggedIn(e, c);
    assertFalse(componentMap.containsKey(c.getId()));
    assertFalse(componentMap.containsValue(c));
  }
  
  @Test
  public void plugNull() {
    assertEquals(0, componentMap.size());
    
    e.plug(null);
    
    assertEquals(0, componentMap.size());
  }
  
  @Test
  public void plugRedundant() {
    Component c = new Component("id");

    assertComponentNotPluggedIn(e, c);
    assertFalse(componentMap.containsKey(c.getId()));
    assertFalse(componentMap.containsValue(c));
    assertEquals(0, componentMap.size());

    e.plug(c);

    assertComponentPluggedIn(e, c);
    assertSame(c, componentMap.get(c.getId()));
    assertEquals(1, componentMap.size());
    
    e.plug(c);
    
    assertComponentPluggedIn(e, c);
    assertSame(c, componentMap.get(c.getId()));
    assertEquals(1, componentMap.size());
  }
  
  @Test
  public void plugWithIdConflict() {
    Component in = new Component("id", e);
    Component out = new Component("id");
    
    assertComponentPluggedIn(e, in);
    assertComponentNotPluggedIn(e, out);
    assertSame(in, componentMap.get("id"));
    
    try {
      e.plug(out);
      fail();
    } catch(IdConflictException e1) {
      assertEquals("id", e1.getId());
    }
    
    assertComponentPluggedIn(e, in);
    assertComponentNotPluggedIn(e, out);
    assertSame(in, componentMap.get("id"));
  }
  
  @Test
  public void unplugNull() {
    Component id = new Component("id", e);
    
    assertNull(e.unplug(null));
    
    assertComponentPluggedIn(e, id);
    assertSame(id, componentMap.get("id"));
  }
  
  @Test
  public void unplugUnknown() {
    Component id = new Component("id", e);
    
    assertNull(e.unplug(""));
    
    assertComponentPluggedIn(e, id);
    assertSame(id, componentMap.get("id"));
  }
  
  @Test
  public void swap() {
    Component id1 = new Component("id", e);
    Component id2 = new Component("id");
    
    assertComponentPluggedIn(e, id1);
    assertSame(id1, componentMap.get("id"));
    assertComponentNotPluggedIn(e, id2);
    
    assertSame(id1, e.swap(id2));
    
    assertComponentNotPluggedIn(e, id1);
    assertComponentPluggedIn(e, id2);
    assertSame(id2, componentMap.get("id"));
  }
  
  @Test
  public void swapNull() {
    Component id = new Component("id", e);
    
    assertNull(e.swap(null));
    
    assertComponentPluggedIn(e, id);
    assertSame(id, componentMap.get("id"));
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
    assertSame(id, componentMap.get(id.getId()));
  }
  
  @Test
  public void getNull() {
    assertNull(e.get(null));
    assertNull(componentMap.get(null));
  }
  
  @Test
  public void getUnknown() {
    assertNull(e.get(""));
    assertNull(componentMap.get(""));
  }
  
  @Test
  public void getAs() {
    RenderableComponent r = new RenderableComponent("r", e);
    
    assertSame(r, e.getAs(r.getId(), Renderable.class));
    assertCompatible(Renderable.class, e.getAs(r.getId(), Renderable.class));
  }
  
  @Test
  public void getAsNullId() {
    assertNull(e.getAs(null, Component.class));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void getAsNullType() {
    e.getAs("id", null);
  }
  
  @Test
  public void getAsUnknownComponent() {
    assertNull(e.getAs("", Component.class));
  }
  
  @Test(expected = ClassCastException.class)
  public void getAsInvalidType() {
    Component c = new Component("id", e);
    
    assertComponentPluggedIn(e, c);
    
    e.getAs("id", String.class);
  }
  
  @Test
  public void has() {
    Component id = new Component("id", e);
    Component sbrubbles = new Component("sbrubbles");
    
    assertTrue(e.has(id.getId()));
    assertFalse(e.has(sbrubbles.getId()));
    assertFalse(e.has(""));
    
    assertTrue(componentMap.containsKey(id.getId()));
    assertFalse(componentMap.containsKey(sbrubbles.getId()));
    assertFalse(componentMap.containsKey(""));
  }
  
  @Test
  public void hasNull() {
    assertFalse(e.has(null));
    assertFalse(componentMap.containsKey(null));
  }
  
  @Test
  public void renderAndUpdate() {
    RenderableAndUpdateableComponent c = new RenderableAndUpdateableComponent("id", e);
    RenderableComponent r = new RenderableComponent("r", e);
    UpdateableComponent u = new UpdateableComponent("u", e);
    
    assertEquals(0, c.renderCount);
    assertEquals(0, r.renderCount);
    
    assertEquals(0, c.updateCount);
    assertEquals(0, u.updateCount);
    
    e.render(null, null, null);
    
    assertEquals(1, c.renderCount);
    assertEquals(1, r.renderCount);
    
    assertEquals(0, c.updateCount);
    assertEquals(0, u.updateCount);
    
    e.update(null, null, 0);
    
    assertEquals(1, c.renderCount);
    assertEquals(1, r.renderCount);
    
    assertEquals(1, c.updateCount);
    assertEquals(1, u.updateCount);
  }
  
  @Test
  public void componentMapPutIdDifferentFromComponentId() {
    try {
      componentMap.put("", new Component("id"));
      fail();
    } catch(ComponentIdMismatchException e1) {
      assertEquals("", e1.getId());
      assertEquals("id", e1.getComponentId());
    }
  }
  
  @Test
  public void putNull() {
    assertEquals(0, componentMap.size());
    assertNull(componentMap.put("id", null));
    assertEquals(0, componentMap.size());
  }
}
