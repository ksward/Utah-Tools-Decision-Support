package edu.utah.cdmcc.decisionsupport.objects.core.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	KnowledgeEngineTests.class,
	TestPatientValidator.class,
	TestPatientFunctions.class,
	DemographicCompositeTests.class,
	DecisionAddedCompositeTest.class
	//TODO I can make this work if I make sure it runs on the right UI Thread like SWTbot tests
	
})

public class CoreObjectsNoDatabaseSuite {
}
