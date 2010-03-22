package org.bakugames.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.bakugames.core.Renderable;
import org.bakugames.core.mock.SimpleRenderable;
import org.junit.Before;
import org.junit.Test;

public class SortedListTest {
  private SortedList<Renderable> list;
  
  @Before
  public void setUp() {
    list = new SortedList(new ArrayList<Renderable>());
    assertEquals(0, list.size());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void buildWithNull() {
    new SortedList(null);
  }
  
  @Test
  public void buildWithUnorderedList() {
    SimpleRenderable[] renderables = new SimpleRenderable[] { 
        new SimpleRenderable(2), 
        new SimpleRenderable(1), 
        new SimpleRenderable(0) };
    list = new SortedList(Arrays.asList(renderables));
    
    for(int i = 0, n = list.size(); i < n; i++)
      assertEquals(i, list.get(i).getZOrder());
  }
  
  @Test
  public void add() {
    SimpleRenderable[] renderables = new SimpleRenderable[] { 
        new SimpleRenderable(2), 
        new SimpleRenderable(1), 
        new SimpleRenderable(0) };
    
    assertTrue(list.add(renderables[0]));
    assertTrue(list.add(renderables[1]));
    assertTrue(list.add(renderables[2]));
    
    for(int i = 0, n = list.size(); i < n; i++)
      assertEquals(i, list.get(i).getZOrder());
    
    renderables = new SimpleRenderable[] { 
        new SimpleRenderable(3), 
        new SimpleRenderable(4), 
        new SimpleRenderable(5) };
    
    assertTrue(list.add(renderables[0]));
    assertTrue(list.add(renderables[1]));
    assertTrue(list.add(renderables[2]));
    
    for(int i = 0, n = list.size(); i < n; i++)
      assertEquals(i, list.get(i).getZOrder());
  }
  
  @Test
  public void addWithSameZOrder() {
    Renderable r1 = new SimpleRenderable(1);
    
    list.add(r1);
    
    Renderable r01 = new SimpleRenderable(0);
    Renderable r02 = new SimpleRenderable(0);
    
    list.add(r01);
    list.add(r02);
    
    Renderable r2 = new SimpleRenderable(2);
    
    list.add(r2);
    
    assertTrue(list.indexOf(r01) < list.indexOf(r1));
    assertTrue(list.indexOf(r02) < list.indexOf(r1));
    assertTrue(list.indexOf(r1) < list.indexOf(r2));
    assertTrue(list.indexOf(r01) < list.indexOf(r2));
    assertTrue(list.indexOf(r02) < list.indexOf(r2));
  }
  
  @Test
  public void addNull() {    
    assertFalse(list.add(null));
    
    assertEquals(0, list.size());
  }
  
  @Test
  public void addRedundant() {
    SimpleRenderable redundant = new SimpleRenderable(0);
    
    assertTrue(list.add(redundant));
    
    assertEquals(1, list.size());
    
    assertFalse(list.add(redundant));
    
    assertEquals(1, list.size());
  }
  
  @Test
  public void addAll() {
    SimpleRenderable[] renderables = new SimpleRenderable[] { 
        new SimpleRenderable(2), 
        new SimpleRenderable(1), 
        new SimpleRenderable(0) };
    assertTrue(list.addAll(Arrays.asList(renderables)));
    
    assertEquals(3, list.size());
    for(int i = 0, n = list.size(); i < n; i++)
      assertEquals(i, list.get(i).getZOrder());
  }
  
  @Test
  public void addAllWithNulls() {
    SimpleRenderable[] renderables = new SimpleRenderable[] { 
        new SimpleRenderable(2), 
        null, 
        new SimpleRenderable(0) };
    assertTrue(list.addAll(Arrays.asList(renderables)));
    
    assertEquals(2, list.size());
    assertSame(renderables[2], list.get(0));
    assertSame(renderables[0], list.get(1));
  }
  
  @Test
  public void addAllNull() {
    assertFalse(list.addAll(null));
    
    assertEquals(0, list.size());
  }
  
  @Test
  public void addAllRedundant() {
    SimpleRenderable r0 = new SimpleRenderable(0);
    SimpleRenderable r1 = new SimpleRenderable(1);
    SimpleRenderable r2 = new SimpleRenderable(2);
    
    assertTrue(list.addAll(Arrays.asList(r0, r1, r2)));
    assertFalse(list.addAll(Arrays.asList(r2, r0, r1, null)));
  }
  
  @Test
  public void set() {
    SimpleRenderable r0 = new SimpleRenderable(0);
    SimpleRenderable r1 = new SimpleRenderable(1);
    SimpleRenderable r2 = new SimpleRenderable(2);
    
    assertSame(r2, list.set(-35, r2));
    assertSame(r1, list.set(0, r1));
    assertSame(r0, list.set(100, r0));
    assertNull(list.set(0, null));
    
    // the index parameter should be ignored
    assertSame(0, list.indexOf(r0));
    assertSame(1, list.indexOf(r1));
    assertSame(2, list.indexOf(r2));
  }
}
