package org.bakugames.util;

import static org.bakugames.util.CompareUtils.compareTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.bakugames.core.Entity;
import org.bakugames.core.World;
import org.bakugames.core.mock.SimpleRenderable;
import org.junit.Test;

public class CompareUtilsTest {
  @Test
  public void compareToSameZOrder() {
    Entity first = new Entity(0);
    World second = new World(); // by default it's zero
    
    assertEquals(0, compareTo(first, second));
    assertEquals(0, first.compareTo(second));
    assertEquals(0, compareTo(second, first));
    assertEquals(0, second.compareTo(first));
    assertEquals(0, compareTo(first, first));
    assertEquals(0, first.compareTo(first));
    assertEquals(0, compareTo(second, second));
    assertEquals(0, second.compareTo(second));
    
    SimpleRenderable k2 = new SimpleRenderable(Integer.MAX_VALUE);
    SimpleRenderable everest = new SimpleRenderable(Integer.MAX_VALUE);
    
    assertEquals(0, compareTo(k2, everest));
    assertEquals(0, compareTo(everest, k2));
    
    SimpleRenderable challengerDeep = new SimpleRenderable(Integer.MIN_VALUE);
    SimpleRenderable tartarus = new SimpleRenderable(Integer.MIN_VALUE);
    
    assertEquals(0, compareTo(challengerDeep, tartarus));
    assertEquals(0, compareTo(tartarus, challengerDeep));
  }
  
  @Test
  public void compareToDifferentZOrder() {
    SimpleRenderable low = new SimpleRenderable(1);
    SimpleRenderable high = new SimpleRenderable(2);
    
    assertTrue(compareTo(low, high) < 0);
    assertTrue(compareTo(high, low) > 0);
  }
  
  @Test
  public void compareToNull() {
    SimpleRenderable nonNull = new SimpleRenderable(0);
    SimpleRenderable reallyLow = new SimpleRenderable(-Integer.MIN_VALUE);
    
    assertTrue(compareTo(nonNull, null) > 0);
    assertTrue(compareTo(null, nonNull) < 0);
    assertTrue(compareTo(reallyLow, null) > 0);
    assertTrue(compareTo(null, reallyLow) < 0);
    assertEquals(0, compareTo(null, null));
  }
}
