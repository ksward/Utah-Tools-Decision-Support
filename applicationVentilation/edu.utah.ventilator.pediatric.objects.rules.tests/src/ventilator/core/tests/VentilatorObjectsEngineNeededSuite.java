package ventilator.core.tests;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import core.drools.utilities.RulesEngineException;
import drools.engine.KnowledgeEngine;


@RunWith(Suite.class)
@SuiteClasses({ 
		TestVentilatorPreferencePageConstruction.class })

public class VentilatorObjectsEngineNeededSuite {
	public static KnowledgeEngine knowledgeEngine;
	
	@BeforeClass
	public static void setup() throws RulesEngineException{
		knowledgeEngine = new KnowledgeEngine();
	}
	
	public static KnowledgeEngine getEngine(){
		return knowledgeEngine;
	}
}
