package org.bakugames.input;


public interface InputBasedPlayer extends Player {
  void bind(Control control, String instruction);
  void unbind(Control control);
  void clear(String instruction);
}
