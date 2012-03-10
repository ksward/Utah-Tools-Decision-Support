package core.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.utah.cdmcc.drools.engine.tests.KnowledgeEngineTests;

@RunWith(Suite.class)
@SuiteClasses({
	TestPatientFunctions.class,
	TestExperimentalSingleValueClasses.class,
	TestPatientValidator.class,
	TestExperimentalMultiValueClasses.class,
	KnowledgeEngineTests.class
//	TestNamedQueriesAccessible.class,
//	TestPatientAdditionValidationDeletionJUnit4.class,
//	TestUpdatePatient.class
})
public class CoreObjectsTestSuiteWithMocks {

	@BeforeClass
	public static void setUp() throws Exception {
		// put in any setup needed before classes are called
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		// put in any cleanup needed after classes are called
	}
}
