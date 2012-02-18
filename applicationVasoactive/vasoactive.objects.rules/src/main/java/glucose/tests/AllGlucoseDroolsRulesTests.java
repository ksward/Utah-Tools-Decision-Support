package glucose.tests;

import org.junit.AfterClass;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import drools.engine.KnowledgeEngine;
import drools.engine.KnowledgeEngineTest;
import drools.engine.RulesEngineException;


/**
 * This suite is the place to add Drools tests. I have set this up to use the
 * software from the IBM article by Olivieri which encapsulates Drools well.
 * That article was updated March 2008 for Drools 4.04.
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah) Copyright 2005 -
 *         2008. All Rights Reserved.
 * 
 */

@RunWith(Suite.class)
@SuiteClasses( { 
//	TestGlucoseRangeDetection.class, 
//	TestGlucoseFunctions.class,
//	TestGlucoseLaboratoryObject.class,
//	TestInsulinOnAndOff.class, 
//	TestInsulinDosing.class,
//	TestGiveGlucose.class, 
//	TestDetectWeightClassification.class,
	TestHypotensionRules.class,
	TestDopamineAdjustments.class,
//	TestGlucosePreferencePage.class,
	KnowledgeEngineTest.class 
	})
		
public class AllGlucoseDroolsRulesTests {
	//public static DecisionEngine decisionEngine;
	public static KnowledgeEngine knowledgeEngine;

	@BeforeClass
	public static void setUp() throws RulesEngineException {
		if (Thread.currentThread().getContextClassLoader() == null) {
			Thread.currentThread().setContextClassLoader(AllGlucoseDroolsRulesTests.class.getClassLoader());
		}
		//decisionEngine = new DecisionEngine();
		//knowledgeEngine = new KnowledgeEngine();
		knowledgeEngine = new KnowledgeEngine();
	}

	public static KnowledgeEngine getEngine() {
		return knowledgeEngine;
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

//	public static DecisionEngine getEngine() {
//		return decisionEngine;
//	}

}
