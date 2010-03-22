package org.bakugames.util;

import org.bakugames.core.Renderable;

public class CompareUtils {
  public static int compareTo(Renderable r1, Renderable r2) {
    int r1order = (r1 != null ? r1.getZOrder() : 0);
    int r2order = (r2 != null ? r2.getZOrder() : 0);
    
    return (r1order < r2order 
         ? -1 
         : (r1order == r2order 
             ? 0 
             : 1));
  }
}
