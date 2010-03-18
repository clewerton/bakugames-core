package org.bakugames.core;
import org.bakugames.core.ComponentTest;
import org.bakugames.core.EntityTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ComponentTest.class,
  EntityTest.class
})
public class FullTest { /* empty block */ }
