package org.bakugames.core;
import org.bakugames.core.entity.ComponentTest;
import org.bakugames.core.entity.EntityTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ComponentTest.class,
  EntityTest.class
})
public class FullTest { /* empty block */ }
