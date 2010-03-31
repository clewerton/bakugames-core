package org.bakugames.core.input;

public interface InputBasedPlayer extends Player {
  void bind(Control control, Object instructionId);
  void unbind(Control control);
  
  Object get(Control control);
  Control[] getControls();
  
  void clear(Object instructionId);
}
