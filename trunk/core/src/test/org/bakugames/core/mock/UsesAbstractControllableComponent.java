package org.bakugames.core.mock;

import org.bakugames.core.Entity;
import org.bakugames.core.traits.AbstractControllableComponent;

public class UsesAbstractControllableComponent extends AbstractControllableComponent {
  public UsesAbstractControllableComponent(String id) {
    super(id);
  }

  public UsesAbstractControllableComponent(String id, Entity entity) {
    super(id, entity);
  }
}
