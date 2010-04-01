package org.bakugames.core.traits;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.bakugames.core.Entity;
import org.bakugames.core.input.Instruction;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BasicControllableComponentTest {
  private BasicControllableComponent c;
  private Entity e;
  
  private Instruction iA;
  private Instruction iB;
  private Instruction iC;
  
  private boolean iAWasExecuted;
  private boolean iBWasExecuted;
  private boolean iCWasExecuted;
  
  @Before
  public void setUp() {
    c = new BasicControllableComponent("c");
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
  public void plugAndUnplug() {
    assertTrue(e.getInstructionSet().isEmpty());
    assertTrue(c.getInstructionSet().isEmpty());
    
    e.plug(c);
    
    assertTrue(e.getInstructionSet().isEmpty());
    assertTrue(c.getInstructionSet().isEmpty());
    
    c.getImplementation().set("a", iA);
    c.getImplementation().set("b", iB);
    c.getImplementation().set("c", iC);
    
    assertEquals(3, e.getInstructionSet().size());
    assertEquals(3, c.getInstructionSet().size());
    assertTrue(c.getInstructionSet().containsAll(Arrays.asList("a", "b", "c")));
    assertTrue(c.getInstructionSet().containsAll(e.getInstructionSet()));
    assertTrue(e.getInstructionSet().containsAll(c.getInstructionSet()));
    
    assertSame(iB, c.getImplementation().remove("b"));
    
    assertEquals(2, e.getInstructionSet().size());
    assertEquals(2, c.getInstructionSet().size());
    assertTrue(c.getInstructionSet().containsAll(Arrays.asList("a", "c")));
    assertTrue(c.getInstructionSet().containsAll(e.getInstructionSet()));
    assertTrue(e.getInstructionSet().containsAll(c.getInstructionSet()));
    
    assertSame(c, e.unplug(c.getId()));
    
    assertTrue(e.getInstructionSet().isEmpty());
    assertEquals(2, c.getInstructionSet().size());
    assertTrue(c.getInstructionSet().containsAll(Arrays.asList("a", "c")));
  }
  
  @Test
  public void understandAndExecute() {
    e.plug(c);
    assertFalse(e.understands("a"));
    assertFalse(c.understands("a"));
    
    c.getImplementation().set("a", iA);
    
    assertTrue(e.understands("a"));
    assertTrue(c.understands("a"));
    assertFalse(c.getImplementation().hasInstructionsQueued());
    
    e.execute("a");
    
    assertArrayEquals(new Object[] { iA }, c.getImplementation().getInstructionQueue().toArray());
    
    assertFalse(e.understands("b"));
    assertFalse(c.understands("b"));
    
    e.execute("b");
    
    assertArrayEquals(new Object[] { iA }, c.getImplementation().getInstructionQueue().toArray());
    
    assertFalse(e.understands(null));
    assertFalse(c.understands(null));
    
    e.execute(null);
    
    assertArrayEquals(new Object[] { iA }, c.getImplementation().getInstructionQueue().toArray());
    
    c.getImplementation().set("b", iB);
    
    assertTrue(e.understands("b"));
    assertTrue(c.understands("b"));
    
    e.execute("b");
    
    assertArrayEquals(new Object[] { iA, iB }, c.getImplementation().getInstructionQueue().toArray());
    
    e.unplug(c.getId());
    
    assertFalse(e.understands("a"));
    assertFalse(e.understands("b"));
    assertTrue(c.understands("a"));
    assertTrue(c.understands("b"));
  }
  
  @Test
  public void executeAndUpdate() throws SlickException {
    c.getImplementation().set("a", iA);
    c.getImplementation().set("b", iB);
    c.getImplementation().set("c", iC);
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertTrue(c.getImplementation().getInstructionQueue().isEmpty());
    
    c.execute("a");
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iA }, c.getImplementation().getInstructionQueue().toArray());
    
    c.execute("b");
    
    assertFalse(iAWasExecuted);
    assertFalse(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iA, iB }, c.getImplementation().getInstructionQueue().toArray());
    
    c.update(null, null, 0);
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertTrue(c.getImplementation().getInstructionQueue().isEmpty());
    
    c.execute("c");
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertFalse(iCWasExecuted);
    assertArrayEquals(new Object[] { iC }, c.getImplementation().getInstructionQueue().toArray());
    
    c.update(null, null, 0);
    
    assertTrue(iAWasExecuted);
    assertTrue(iBWasExecuted);
    assertTrue(iCWasExecuted);
    assertTrue(c.getImplementation().getInstructionQueue().isEmpty());
  }
  
  @Test
  public void executeNull() {
    c.getImplementation().set("a", iA);
    c.getImplementation().set("b", iB);
    c.getImplementation().set("c", iC);
    
    assertFalse(c.getImplementation().hasInstructionsQueued());
    
    c.execute("ijosajiosa");
    
    assertFalse(c.getImplementation().hasInstructionsQueued());
  }
  
  @Test
  public void setImplementation() {
    ControllableImpl original = c.getImplementation();
    original.set("a", iA);
    c.execute("a");
    
    assertTrue(c.understands("a"));
    assertFalse(c.understands("b"));
    assertFalse(c.understands("c"));
    assertArrayEquals(new Object[] { iA }, c.getImplementation().getInstructionQueue().toArray());
    
    ControllableImpl other = new ControllableImpl();
    other.set("b", iB);
    other.set("c", iC);
    other.execute("c");
    other.execute("c");
    other.execute("b");
    
    assertFalse(other.understands("a"));
    assertTrue(other.understands("b"));
    assertTrue(other.understands("c"));
    assertArrayEquals(new Object[] { iC, iC, iB }, other.getInstructionQueue().toArray());
    
    c.setImplementation(other);
    
    assertFalse(c.understands("a"));
    assertTrue(c.understands("b"));
    assertTrue(c.understands("c"));
    assertArrayEquals(new Object[] { iC, iC, iB }, c.getImplementation().getInstructionQueue().toArray());
    
    assertTrue(original.understands("a"));
    assertFalse(original.understands("b"));
    assertFalse(original.understands("c"));
    assertArrayEquals(new Object[] { iA }, original.getInstructionQueue().toArray());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void setImplementationNull() {
    c.setImplementation(null);
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
