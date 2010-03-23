package org.bakugames.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.bakugames.core.mock.SimpleRenderable;
import org.bakugames.core.traits.Renderable;
import org.junit.Before;
import org.junit.Test;

public class SortedListTest {
  private SortedList<Renderable> list;
  private ArrayList<Renderable> backingList;
  
  @Before
  public void setUp() {
    backingList = new ArrayList<Renderable>();
    list = new SortedList(backingList);
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
  public void addIndex() {
    SimpleRenderable r0 = new SimpleRenderable(0);
    SimpleRenderable r1 = new SimpleRenderable(1);
    SimpleRenderable r2 = new SimpleRenderable(2);
    
    list.add(-35, r2);
    list.add(0, r1);
    list.add(100, r0);
    list.add(0, null);
    
    // the index parameter should be ignored
    assertSame(0, list.indexOf(r0));
    assertSame(1, list.indexOf(r1));
    assertSame(2, list.indexOf(r2));
  }
  
  @Test
  public void addAllIndex() {
    SimpleRenderable[] renderables = new SimpleRenderable[] { 
        new SimpleRenderable(5), 
        new SimpleRenderable(4), 
        new SimpleRenderable(3),
        new SimpleRenderable(2), 
        new SimpleRenderable(1), 
        new SimpleRenderable(0) };
    
    assertTrue(list.addAll(Arrays.asList(renderables[0], renderables[1], renderables[2])));
    assertEquals(3, list.size());
    
    // the index parameter should be ignored
    assertTrue(list.addAll(1, Arrays.asList(renderables[3], renderables[4], renderables[5])));
    assertEquals(6, list.size());
    
    assertSame(renderables[5], list.get(0));
    assertSame(renderables[4], list.get(1));
    assertSame(renderables[3], list.get(2));
    assertSame(renderables[2], list.get(3));
    assertSame(renderables[1], list.get(4));
    assertSame(renderables[0], list.get(5));
  }
  
  @Test
  public void clear() {
    SimpleRenderable r0 = new SimpleRenderable(0);
    SimpleRenderable r1 = new SimpleRenderable(1);
    SimpleRenderable r2 = new SimpleRenderable(2);
    
    assertTrue(list.addAll(Arrays.asList(r0, r1, r2)));
    assertEquals(3, list.size());
    
    list.clear();
    
    assertEquals(0, list.size());
  }
  
  @Test
  public void equals_hashCode_isEmpty() {
    assertEquals(list, backingList);
    assertEquals(list.isEmpty(), backingList.isEmpty());
    assertEquals(list.hashCode(), backingList.hashCode());
    
    list.addAll(Arrays.asList(
        new SimpleRenderable(3),
        new SimpleRenderable(2), 
        new SimpleRenderable(1), 
        new SimpleRenderable(0)));
    
    assertEquals(list, backingList);
    assertEquals(list.isEmpty(), backingList.isEmpty());
    assertEquals(list.hashCode(), backingList.hashCode());
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
