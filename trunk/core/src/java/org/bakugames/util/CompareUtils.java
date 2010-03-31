package org.bakugames.util;

import org.bakugames.core.traits.Renderable;

public final class CompareUtils {
  private CompareUtils() { /* empty block */ }
  
  public static int compareTo(Renderable r1, Renderable r2) {
    if(r1 == r2)
      return 0;
    
    if(r1 != null && r2 == null)
      return 1;
    
    if(r1 == null && r2 != null)
      return -1;
    
    int r1order = r1.getZOrder();
    int r2order = r2.getZOrder();
    
    return (r1order < r2order 
         ? -1 
         : (r1order == r2order 
             ? 0 
             : 1));
  }
}
