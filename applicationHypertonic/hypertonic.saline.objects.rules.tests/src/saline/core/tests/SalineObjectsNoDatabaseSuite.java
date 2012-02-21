package saline.core.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestSalinePreferencePageConstruction.class,
	TestHypertonicConstructors.class,
	TestHypertonicCalculationFunctions.class,
	TestHypertonicDecisionAddToPatient.class,
	KnowledgeEngineTest.class,
	TestHypertonicDecisionValidator.class
})

public class SalineObjectsNoDatabaseSuite {

}
