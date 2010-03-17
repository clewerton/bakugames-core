package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.bakugames.core.Component;
import org.bakugames.core.Entity;

public class TestUtils {
  public static void assertComponentNotPluggedIn(Entity e, Component c) {
    assertNotSame(e, c.getOwner());
    assertNotSame(c, e.get(c.getId()));
  }

  public static void assertComponentPluggedIn(Entity e, Component c) {
    assertSame(e, c.getOwner());
    assertSame(c, e.get(c.getId()));
  }
  
  public static void assertNotSame(Object expected, Object actual) {
    assertFalse("expected two different instances, both are " + expected, expected == actual);
  }
}
