package core.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestPatientFunctions.class,
	DemographicCompositeTests.class//,
	//DecisionAddedCompositeTest.class
	
})

public class CoreObjectsNoDatabaseSuite {
}
