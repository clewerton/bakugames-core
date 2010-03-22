package org.bakugames.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedList<E extends Comparable<E>> implements List<E> {
  private List<E> backingList;

  public SortedList(List<E> backingList) {
    if(backingList == null)
      throw new IllegalArgumentException("null");

    this.backingList = backingList;
  }

  public boolean add(E e) {
    int index = binarySearch(backingList, e);

    if(index >= 0) // e is already in the list, don't put it again
      return false;

    backingList.add(- index - 1, e);
    return true;
  }

  public void add(int index, E element) {
    // ignore the index, put element in its proper place
    add(element);
  }

  public boolean addAll(Collection<? extends E> c) {
    boolean result = false;
    for(E e : c)
      result = result || add(e);
    
    return result;
  }

  public boolean addAll(int index, Collection<? extends E> c) {
    // ignore the index, put element in its proper place
    return addAll(c);
  }

  public void clear() {
    backingList.clear();
  }

  public boolean contains(Object o) {
    return backingList.contains(o);
  }

  public boolean containsAll(Collection<?> c) {
    return backingList.containsAll(c);
  }

  public boolean equals(Object o) {
    return backingList.equals(o);
  }

  public E get(int index) {
    return backingList.get(index);
  }

  public int hashCode() {
    return backingList.hashCode();
  }

  public int indexOf(Object o) {
    return backingList.indexOf(o);
  }

  public boolean isEmpty() {
    return backingList.isEmpty();
  }

  public Iterator<E> iterator() {
    return backingList.iterator();
  }

  public int lastIndexOf(Object o) {
    return backingList.lastIndexOf(o);
  }

  public ListIterator<E> listIterator() {
    return backingList.listIterator();
  }

  public ListIterator<E> listIterator(int index) {
    return backingList.listIterator(index);
  }

  public E remove(int index) {
    return backingList.remove(index);
  }

  public boolean remove(Object o) {
    return backingList.remove(o);
  }

  public boolean removeAll(Collection<?> c) {
    return backingList.removeAll(c);
  }

  public boolean retainAll(Collection<?> c) {
    return backingList.retainAll(c);
  }

  public E set(int index, E element) {
    // ignore the index, put element in its proper place
    add(element);
    return element;
  }

  public int size() {
    return backingList.size();
  }

  public List<E> subList(int fromIndex, int toIndex) {
    return backingList.subList(fromIndex, toIndex);
  }

  public Object[] toArray() {
    return backingList.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return backingList.toArray(a);
  }

  private static <E extends Comparable<E>> int binarySearch(List<E> list, E key) {
    int low = 0;
    int high = list.size() - 1;

    while (low <= high) {
        int mid = (low + high) >>> 1;
        E midVal = list.get(mid);
        int cmp = midVal.compareTo(key);

        if (cmp < 0)
          low = mid + 1;
        else if (cmp > 0)
          high = mid - 1;
        else if(midVal.equals(key))
          return mid; // key found
        else
          break;
    }
    
    return -(low + 1);  // key not found
  }
}
