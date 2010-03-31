package org.bakugames;
import org.bakugames.core.ComponentTest;
import org.bakugames.core.EntityTest;
import org.bakugames.core.WorldTest;
import org.bakugames.core.traits.AbstractControllableComponentTest;
import org.bakugames.util.CompareUtilsTest;
import org.bakugames.util.SortedListTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  SortedListTest.class,
  CompareUtilsTest.class,
  ComponentTest.class,
  AbstractControllableComponentTest.class,
  EntityTest.class,
  WorldTest.class
})
public class FullTest { /* empty block */ }