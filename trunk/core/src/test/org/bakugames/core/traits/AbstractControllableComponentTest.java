package org.bakugames.core.traits;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.bakugames.core.Entity;
import org.bakugames.core.input.Instruction;
import org.bakugames.core.mock.UsesAbstractControllableComponent;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class AbstractControllableComponentTest {
  private AbstractControllableComponent c;
  private Entity e;
  private Instruction iA;
  private Instruction iB;
  private Instruction iC;
  
  private boolean iAWasExecuted;
  private boolean iBWasExecuted;
  private boolean iCWasExecuted;
  
  @Before
  public void setUp() {
    c = new UsesAbstractControllableComponent("c");
    e = new Entity();
    
    iA = new Instruction() {
      @Override
      public void execute(GameContainer gc, StateBasedGame sb, int delta) {
        iAWasExecuted = ! iAWasExecuted;
      }
    };
    
    iB = new Instruction() {
      @Override
      public void execute(GameContainer gc, StateBasedGame sb, int delta) {
        iBWasExecuted = ! iBWasExecuted;
      }
    };
    
    iC = new Instruction() {
      @Override
      public void execute(GameContainer gc, StateBasedGame sb, int delta) {
        iCWasExecuted = ! iCWasExecuted;
      }
    };
    
    iAWasExecuted = false;
    iBWasExecuted = false;
    iCWasExecuted = false;
  }
  
  @Test
  public void getSetRemove() {
    e.plug(c);
    
    assertTrue(e.getInstructionSet().isEmpty());
    assertNull(c.get("a"));
    assertNull(c.get("b"));
    assertNull(c.get("c"));
    
    c.set("a", iA);
    c.set("b", iB);
    c.set("c", iC);
    
    assertTrue(e.getInstructionSet().containsAll(Arrays.asList("a", "b", "c")));
    assertEquals(3, e.getInstructionSet().size());
    assertSame(iA, c.get("a"));
    assertSame(iB, c.get("b"));
    assertSame(iC, c.get("c"));
    
    assertSame(iB, c.remove("b"));
    
    assertTrue(e.getInstructionSet().containsAll(Arrays.asList("a", "c")));
    assertEquals(2, e.getInstructionSet().size());
    assertSame(iA, c.get("a"));
    assertNull(c.get("b"));
    assertSame(iC, c.get("c"));
  }
  
  @Test
  public void getNull() {
    assertNull(c.get(null));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void setWithIdNull() {
    c.set(null, iA);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void setWithInstructionNull() {
    c.set("id", null);
  }
  
  @Test
  public void removeNullOrUnknown() {
    c.set("a", iA);
    assertArrayEquals(new Object[] { "a" }, c.getInstructionSet().toArray());
    
    assertNull(c.remove(null));
    assertArrayEquals(new Object[] { "a" }, c.getInstructionSet().toArray());
    
    assertNull(c.remove(new Object()));
    assertArrayEquals(new Object[] { "a" }, c.getInstructionSet().toArray());
  }
  
  @Test
  public void executeAndUpdate() {
    e.plug(c);
    
    c.set("a", iA);
    c.set("b", iB);
    c.set("c", iC);
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertTrue(c.getInstructionQueue().isEmpty());
    
    e.execute("a");
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iA }, c.getInstructionQueue().toArray());
    
    e.execute("b");
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iA, iB }, c.getInstructionQueue().toArray());
    
    e.update(null, null, 0);
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertTrue(c.getInstructionQueue().isEmpty());
    
    e.execute("c");
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iC }, c.getInstructionQueue().toArray());
    
    e.update(null, null, 0);
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertTrue(iCWasExecuted);
    assertTrue(c.getInstructionQueue().isEmpty());
  }
  
  @Test
  public void executeNull() {
    e.plug(c);
    
    c.set("a", iA);
    c.set("b", iB);
    c.set("c", iC);
    
    assertTrue(c.getInstructionQueue().isEmpty());
    
    e.execute("ijosajiosa");
    
    assertTrue(c.getInstructionQueue().isEmpty());
  }
  
  @Test
  public void understands() {
    assertFalse(c.understands("a"));
    
    c.set("a", iA);
    
    assertTrue(c.understands("a"));
    
    assertFalse(c.understands(null));
    
  }
}
