package edu.utah.cdmcc.decisionsupport.objects.core.tests;
/**
 * 	This is a JUnit suite that calls other suites that may require specific
 *  setup, as well as individual core.tests that require no expensive setup.  The
 *  goal is to organize test calls to avoid redundancy.
 *  
 *  For example, some core.tests require a running instance of HSQLDB so that DAO
 *  and retrieval methods can be tested.  Other core.tests do not require this, and
 *  we avoid the overhead of starting and stopping the server by separating them out.
 *  
 *  For another example, some core.tests might require an expensive set up such as a
 *  knowledge inference engine instantiation.
 *  
 *  It is permissible to add individual JUnit core.tests (not just suites) but it is
 *  recommended to put core.tests into one of the included suites so that those suites
 *  can be called separately when desired.
 *  
 *  Run this class as a JUnit test to exercise all core.tests that exist in this package.
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CoreObjectsNeedsDatabaseSuite.class,
				CoreObjectsNoDatabaseSuite.class})				
public class AllCoreObjectTests {
}
