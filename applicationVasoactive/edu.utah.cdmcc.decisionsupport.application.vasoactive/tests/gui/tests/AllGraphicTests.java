package gui.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({DemographicCompositeTests.class, DecisionAddedCompositeTest.class,
	TestGlucoseFieldsAddedComposite.class})
public class AllGraphicTests {
}
