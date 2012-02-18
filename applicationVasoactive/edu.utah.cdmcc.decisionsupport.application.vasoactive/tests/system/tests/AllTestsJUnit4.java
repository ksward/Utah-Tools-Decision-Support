package system.tests;

import listener.tests.AllListenerTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import core.tests.AllPatientTests;
import database.tests.AllDatabaseTests;
import entity.tests.AllEntityTests;
//import glucose.tests.AllGlucoseDroolsRulesTests;
import gui.tests.AllGraphicTests;

@RunWith(Suite.class)
@SuiteClasses({ 
	AllDatabaseTests.class,  
	AllEntityTests.class,
//	AllGlucoseDroolsRulesTests.class, 
	AllListenerTests.class, 
	AllGraphicTests.class,
	AllPatientTests.class
	})
public class AllTestsJUnit4 {

}


