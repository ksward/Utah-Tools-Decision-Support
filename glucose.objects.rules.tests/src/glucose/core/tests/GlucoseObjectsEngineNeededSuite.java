package glucose.core.tests;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import core.drools.utilities.RulesEngineException;

import drools.engine.KnowledgeEngine;

@RunWith(Suite.class)
@SuiteClasses({ TestGlucoseRangeDetection.class, 
		TestGlucoseFunctions.class,
		TestInsulinOnAndOff.class, 
		TestInsulinDosing.class,
		TestGiveGlucose.class, 
		TestDetectWeightClassification.class,
		TestGlucosePreferencePageConstruction.class })
		
public class GlucoseObjectsEngineNeededSuite {

	public static KnowledgeEngine knowledgeEngine;
	
	@BeforeClass
	public static void setup() throws RulesEngineException{
		knowledgeEngine = new KnowledgeEngine();
	}
	
	public static KnowledgeEngine getEngine(){
		return knowledgeEngine;
	}
}
