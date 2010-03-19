package org.bakugames.core;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ComponentTest.class,
  EntityTest.class,
  WorldTest.class
})
public class FullTest { /* empty block */ }
