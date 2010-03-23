package org.bakugames.core;

import java.util.List;

import org.bakugames.core.traits.Controllable;
import org.bakugames.core.traits.Updateable;

public interface Player extends Updateable {
  List<? extends Controllable> getControlled();
}
