package org.bakugames;
import org.bakugames.core.ComponentTest;
import org.bakugames.core.EntityTest;
import org.bakugames.core.WorldTest;
import org.bakugames.util.SortedListTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  SortedListTest.class,
  ComponentTest.class,
  EntityTest.class,
  WorldTest.class
})
public class FullTest { /* empty block */ }
