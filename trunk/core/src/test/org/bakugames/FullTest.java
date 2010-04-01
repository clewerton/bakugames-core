package org.bakugames;
import org.bakugames.core.ComponentTest;
import org.bakugames.core.EntityTest;
import org.bakugames.core.WorldTest;
import org.bakugames.core.input.BasicPlayerTest;
import org.bakugames.core.traits.BasicControllableComponentTest;
import org.bakugames.core.traits.ControllableImplTest;
import org.bakugames.util.CompareUtilsTest;
import org.bakugames.util.SortedListTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  SortedListTest.class,
  CompareUtilsTest.class,
  BasicPlayerTest.class,
  ControllableImplTest.class,
  ComponentTest.class,
  BasicControllableComponentTest.class,
  EntityTest.class,
  WorldTest.class
})
public class FullTest { /* empty block */ }
