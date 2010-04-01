package org.bakugames.core.input;

import org.bakugames.core.traits.Controllable;
import org.bakugames.core.traits.Updateable;

public interface Player extends Updateable {
  Controllable getControlled();
}