package org.bakugames.core.traits;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.bakugames.core.input.Instruction;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ControllableImplTest {
  private ControllableImpl c;
  private Instruction iA;
  private Instruction iB;
  private Instruction iC;
  
  private boolean iAWasExecuted;
  private boolean iBWasExecuted;
  private boolean iCWasExecuted;
  
  @Before
  public void setUp() {
    c = new ControllableImpl();
    
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
    assertTrue(c.getInstructionSet().isEmpty());
    assertNull(c.get("a"));
    assertNull(c.get("b"));
    assertNull(c.get("c"));
    
    c.set("a", iA);
    c.set("b", iB);
    c.set("c", iC);
    
    assertTrue(c.getInstructionSet().containsAll(Arrays.asList("a", "b", "c")));
    assertEquals(3, c.getInstructionSet().size());
    assertSame(iA, c.get("a"));
    assertSame(iB, c.get("b"));
    assertSame(iC, c.get("c"));
    
    assertSame(iB, c.remove("b"));
    
    assertTrue(c.getInstructionSet().containsAll(Arrays.asList("a", "c")));
    assertEquals(2, c.getInstructionSet().size());
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
  public void executeAndUpdate() throws SlickException {
    c.set("a", iA);
    c.set("b", iB);
    c.set("c", iC);
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertTrue(c.getInstructionQueue().isEmpty());
    
    c.execute("a");
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iA }, c.getInstructionQueue().toArray());
    
    c.execute("b");
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iA, iB }, c.getInstructionQueue().toArray());
    
    c.update(null, null, 0);
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertTrue(c.getInstructionQueue().isEmpty());
    
    c.execute("c");
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iC }, c.getInstructionQueue().toArray());
    
    c.update(null, null, 0);
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertTrue(iCWasExecuted);
    assertTrue(c.getInstructionQueue().isEmpty());
  }
  
  @Test
  public void enqueueAndDequeue() {
    assertFalse(c.hasInstructionsQueued());
    
    c.enqueue(iA);
    c.enqueue(iB);
    c.enqueue(iC);
    c.enqueue(iA);
    
    assertTrue(c.hasInstructionsQueued());
    assertArrayEquals(new Object[] { iA, iB, iC, iA }, c.getInstructionQueue().toArray());
    
    assertSame(iA, c.dequeue());
    assertSame(iB, c.dequeue());
    assertSame(iC, c.dequeue());
    assertSame(iA, c.dequeue());
    
    assertFalse(c.hasInstructionsQueued());
  }
  
  @Test
  public void enqueueNull() {
    assertFalse(c.hasInstructionsQueued());
    
    c.enqueue(null);
    
    assertFalse(c.hasInstructionsQueued());
  }
  
  @Test
  public void dequeueEmptyQueue() {
    assertFalse(c.hasInstructionsQueued());
    assertNull(c.dequeue());
    assertNull(c.dequeue());
    assertFalse(c.hasInstructionsQueued());
  }
  
  @Test
  public void executeNull() {
    c.set("a", iA);
    c.set("b", iB);
    c.set("c", iC);
    
    assertFalse(c.hasInstructionsQueued());
    
    c.execute("ijosajiosa");
    
    assertFalse(c.hasInstructionsQueued());
  }
  
  @Test
  public void understands() {
    assertFalse(c.understands("a"));
    
    c.set("a", iA);
    
    assertTrue(c.understands("a"));
    assertFalse(c.understands(null));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void changingInstructionSet() {
    c.getInstructionSet().add("aknoksa");
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void changingInstructionSet2() {
    c.getInstructionSet().addAll(Arrays.asList("aknoksa", new Object()));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void changingInstructionSet3() {
    c.getInstructionSet().remove("aknoksa");
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void changingInstructionSet4() {
    c.getInstructionSet().removeAll(Arrays.asList("aknoksa", new Object()));
  }
}
