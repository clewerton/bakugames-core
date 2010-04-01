package org.bakugames.core.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.bakugames.core.mock.EmptyGameContainer;
import org.bakugames.core.mock.PointerControl;
import org.bakugames.core.mock.PointerControl.Pointer;
import org.bakugames.core.traits.ControllableImpl;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BasicPlayerTest {
  private BasicPlayer player;
  private ControllableImpl controlled;
  
  private int aCounter;
  private int bCounter;
  private int cCounter;
  
  private Control aControl;
  private Control bControl;
  private Control cControl;
  
  private Pointer aPointer;
  private Pointer bPointer;
  private Pointer cPointer;
  private EmptyGameContainer gc;
  
  @Before
  public void setUp() {
    controlled = new ControllableImpl();
    
    controlled.set("a", new Instruction() {
      @Override
      public void execute(GameContainer gc, StateBasedGame sb, int delta) {
        aCounter++;
      }
    });
    
    controlled.set("b", new Instruction() {
      @Override
      public void execute(GameContainer gc, StateBasedGame sb, int delta) {
        bCounter++;
      }
    });
    
    controlled.set("c", new Instruction() {
      @Override
      public void execute(GameContainer gc, StateBasedGame sb, int delta) {
        cCounter++;
      }
    });
    
    aCounter = 0;
    bCounter = 0;
    cCounter = 0;
    
    aPointer = new Pointer();
    bPointer = new Pointer();
    cPointer = new Pointer();
    
    aControl = new PointerControl(aPointer);
    bControl = new PointerControl(bPointer);
    cControl = new PointerControl(cPointer);
    
    player = new BasicPlayer(controlled);
    player.bind(aControl, "a");
    player.bind(bControl, "b");
    
    gc = new EmptyGameContainer(null);
  }
  
  @Test
  public void getNullAndUnknown() {
    assertNull(player.get(null));
    assertNull(player.get(new Control() {
      @Override
      public boolean happened(Input input) {
        return false;
      }
    }));
  }
  
  @Test
  public void constructorAndSetControlled() {
    assertSame(controlled, player.getControlled());
    
    BasicPlayer player2 = new BasicPlayer(null);
    assertNull(player2.getControlled());
    
    player2.setControlled(controlled);
    assertSame(controlled, player2.getControlled());
    
    player.setControlled(null);
    assertNull(player.getControlled());
  }
  
  @Test
  public void bindAndUnbind() {
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
    assertNull(player.get(cControl));
    
    player.bind(cControl, "c");
    
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
    assertSame("c", player.get(cControl));
    
    player.unbind(aControl);
    
    assertNull(player.get(aControl));
    assertSame("b", player.get(bControl));
    assertSame("c", player.get(cControl));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void bindWithNullControl() {
    player.bind(null, "sdfomnmovps");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void bindWithNullInstructionId() {
    player.bind(aControl, null);
  }
  
  @Test
  public void bindPreviouslyBound() {
    assertSame("a", player.get(aControl));
    
    player.bind(aControl, "c");
    
    assertSame("c", player.get(aControl));
  }
  
  @Test
  public void unbindNull() {
    player.unbind(null);
  }
  
  @Test
  public void getControlsForAndClear() {
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
    assertNull(player.get(cControl));
    
    player.bind(cControl, "a");
    
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
    assertSame("a", player.get(cControl));
    
    Set<Control> controlsForA = player.getControlsFor("a");
    assertEquals(2, controlsForA.size());
    assertTrue(controlsForA.containsAll(Arrays.asList(aControl, cControl)));
    
    player.clear("a");
    
    assertTrue(player.getControlsFor("a").isEmpty());
    
    Set<Control> controlsForB = player.getControlsFor("b");
    assertEquals(1, controlsForB.size());
    assertTrue(controlsForB.contains(bControl));
    
    player.clear("b");
    
    assertTrue(player.getControlsFor("b").isEmpty());
  }
  
  @Test
  public void getControlsForNullAndUnknown() {
    assertTrue(player.getControlsFor(null).isEmpty());
    assertTrue(player.getControlsFor("opsnivniodsv").isEmpty());
  }
  
  @Test
  public void clearNullAndUnknown() {
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
    
    player.clear(null);
    
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
    
    player.clear("bmpforomi");
    
    assertSame("a", player.get(aControl));
    assertSame("b", player.get(bControl));
  }
  
  @Test
  public void update() throws SlickException {
    assertEquals(0, aCounter);
    assertEquals(0, bCounter);
    assertEquals(0, cCounter);
    
    player.update(gc, null, 0);
    
    assertEquals(0, aCounter);
    assertEquals(0, bCounter);
    assertEquals(0, cCounter);
    
    aPointer.set = true;
    
    player.update(gc, null, 0);
    
    assertEquals(1, aCounter);
    assertEquals(0, bCounter);
    assertEquals(0, cCounter);
    
    bPointer.set = true;
    
    player.update(gc, null, 0);
    
    assertEquals(2, aCounter);
    assertEquals(1, bCounter);
    assertEquals(0, cCounter);
    
    cPointer.set = true;
    
    player.update(gc, null, 0);
    
    assertEquals(3, aCounter);
    assertEquals(2, bCounter);
    assertEquals(0, cCounter); // cControl hasn't been bound yet!
    
    player.bind(cControl, "c");
    
    player.update(gc, null, 0);
    
    assertEquals(4, aCounter);
    assertEquals(3, bCounter);
    assertEquals(1, cCounter);
    
    aPointer.set = false;
    
    player.update(gc, null, 0);
    
    assertEquals(4, aCounter);
    assertEquals(4, bCounter);
    assertEquals(2, cCounter);
  }
  
  @Test
  public void updateWithNullControlled() throws SlickException {
    aPointer.set = true;
    bPointer.set = true;
    cPointer.set = true;
    
    player.bind(cControl, "c");
    
    assertEquals(0, aCounter);
    assertEquals(0, bCounter);
    assertEquals(0, cCounter);
    
    player.update(gc, null, 0);
    
    assertEquals(1, aCounter);
    assertEquals(1, bCounter);
    assertEquals(1, cCounter);
    
    player.setControlled(null);
    
    player.update(gc, null, 0);
    
    assertEquals(1, aCounter);
    assertEquals(1, bCounter);
    assertEquals(1, cCounter);
    
    player.setControlled(controlled);
    
    player.update(gc, null, 0);
    
    assertEquals(2, aCounter);
    assertEquals(2, bCounter);
    assertEquals(2, cCounter);
  }
}
