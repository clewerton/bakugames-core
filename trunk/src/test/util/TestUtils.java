package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.bakugames.core.entity.Component;
import org.bakugames.core.entity.Entity;

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
  
  public static void assertCompatible(Class<?> expected, Object actual) {
    assertNotNull(expected);
    assertNotNull(actual);
    
    assertTrue(expected.isAssignableFrom(actual.getClass()));
  }
}
