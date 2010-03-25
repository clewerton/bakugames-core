package org.bakugames.core;

import static org.junit.Assert.assertArrayEquals;
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

import java.util.Arrays;
import java.util.Map;

import org.bakugames.core.exception.ComponentIdMismatchException;
import org.bakugames.core.exception.IdConflictException;
import org.bakugames.core.mock.ControllableComponent;
import org.bakugames.core.mock.RenderableAndUpdateableComponent;
import org.bakugames.core.mock.RenderableComponent;
import org.bakugames.core.mock.UpdateableComponent;
import org.bakugames.core.traits.Renderable;
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
    Entity e = new Entity(null, 0);
    
    assertNull(e.getWorld());
    assertFalse(w.contains(e));
  }
  
  @Test
  public void buildWithWorld() {
    World w = new World();
    Entity e = new Entity(w, 0);
    
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
    RenderableAndUpdateableComponent ru = new RenderableAndUpdateableComponent("ru", e);
    RenderableComponent r = new RenderableComponent("r", e);
    UpdateableComponent u = new UpdateableComponent("u", e);
    ControllableComponent c = new ControllableComponent("c", e, "a");
    
    assertEquals(0, ru.renderCount);
    assertEquals(0, r.renderCount);
    
    assertEquals(0, ru.updateCount);
    assertEquals(0, u.updateCount);
    assertEquals(0, c.updateCount);
    
    e.render(null, null, null);
    
    assertEquals(1, ru.renderCount);
    assertEquals(1, r.renderCount);
    
    assertEquals(0, ru.updateCount);
    assertEquals(0, u.updateCount);
    assertEquals(0, c.updateCount);
    
    e.update(null, null, 0);
    
    assertEquals(1, ru.renderCount);
    assertEquals(1, r.renderCount);
    
    assertEquals(1, ru.updateCount);
    assertEquals(1, u.updateCount);
    assertEquals(1, c.updateCount);
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
  
  @Test
  public void zOrder() {
    RenderableComponent r0 = new RenderableComponent("r0", 0);
    RenderableComponent r1 = new RenderableComponent("r1", 1);
    
    e.plug(r1);
    e.plug(r0);
    
    assertEquals(0, r0.nanoTime);
    assertEquals(0, r1.nanoTime);
    
    e.render(null, null, null);
    
    assertTrue(r0.nanoTime + ", " + r1.nanoTime, r0.nanoTime < r1.nanoTime);
  }
  
  @Test
  public void execute() {
    ControllableComponent c = new ControllableComponent("c", e, "a", "b", "c");
    
    assertEquals(0, c.instructionsExecuted.size());
    
    // checking via the entity
    assertEquals(3, e.getInstructionSet().size());
    assertTrue(e.getInstructionSet().containsAll(Arrays.asList("a", "b", "c")));
    
    e.execute("a");
    
    assertEquals(1, c.instructionsExecuted.size());
    assertEquals("a", c.instructionsExecuted.get(0));
    
    e.execute("c");
    e.execute("b");
    
    assertArrayEquals(new Object[] { "a", "c", "b" }, c.instructionsExecuted.toArray());
  }
  
  @Test
  public void understands() {
    e.plug(new ControllableComponent("c", "a", "b", "c"));
    
    assertTrue(e.understands("a"));
    assertTrue(e.understands("b"));
    assertTrue(e.understands("c"));
    assertFalse(e.understands("d"));
    assertFalse(e.understands(""));
    assertFalse(e.understands(null));
  }
  
  @Test
  public void multipleControllables() {
    ControllableComponent c1 = new ControllableComponent("c1", e, "a", "b", "c");
    ControllableComponent c2 = new ControllableComponent("c2", e, "d", "e", "f");
    
    assertEquals(6, e.getInstructionSet().size());
    assertTrue(e.getInstructionSet().containsAll(Arrays.asList("a", "b", "c", "d", "e", "f")));
    
    assertTrue(c1.instructionsExecuted.isEmpty());
    assertTrue(c2.instructionsExecuted.isEmpty());
    
    e.execute("a");
    
    assertArrayEquals(new Object[] { "a" }, c1.instructionsExecuted.toArray());
    assertTrue(c2.instructionsExecuted.isEmpty());
    
    e.execute("f");
    e.execute("b");
    
    assertArrayEquals(new Object[] { "a", "b" }, c1.instructionsExecuted.toArray());
    assertArrayEquals(new Object[] { "f" }, c2.instructionsExecuted.toArray());
  }
  
  @Test
  public void overlappingInstructionSets() {
    ControllableComponent c1 = new ControllableComponent("c1", e, "a", "b", "c");
    ControllableComponent c2 = new ControllableComponent("c2", e, "b", "c", "d");
    
    assertEquals(4, e.getInstructionSet().size());
    assertTrue(e.getInstructionSet().containsAll(Arrays.asList("a", "b", "c", "d")));
    
    assertTrue(c1.instructionsExecuted.isEmpty());
    assertTrue(c2.instructionsExecuted.isEmpty());
    
    e.execute("a");
    
    assertArrayEquals(new Object[] { "a" }, c1.instructionsExecuted.toArray());
    assertTrue(c2.instructionsExecuted.isEmpty());
    
    e.execute("d");
    
    assertArrayEquals(new Object[] { "a" }, c1.instructionsExecuted.toArray());
    assertArrayEquals(new Object[] { "d" }, c2.instructionsExecuted.toArray());
    
    e.execute("c");
    e.execute("b");
    
    assertArrayEquals(new Object[] { "a", "c", "b" }, c1.instructionsExecuted.toArray());
    assertArrayEquals(new Object[] { "d", "c", "b" }, c2.instructionsExecuted.toArray());
  }
}
