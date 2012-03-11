package glucose.core.tests;
/**
 * 	This is a JUnit suite that calls other suites that may require specific
 *  setup, as well as individual saline.tests that require no expensive setup.  The
 *  goal is to organize test calls to avoid redundancy.
 *  
 *  For example, some saline.tests require a running instance of HSQLDB so that DAO
 *  and retrieval methods can be tested.  Other saline.tests do not require this, and
 *  we avoid the overhead of starting and stopping the server by separating them out.
 *  
 *  For another example, some saline.tests might require an expensive set up such as a
 *  knowledge inference engine instantiation.
 *  
 *  It is permissible to add individual JUnit saline.tests (not just suites) but it is
 *  recommended to put saline.tests into one of the included suites so that those suites
 *  can be called separately when desired.
 *  
 *  Run this class as a JUnit test to exercise all saline.tests that exist in this package.
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
				GlucoseObjectsEngineNeededSuite.class//,
				//GlucoseObjectsNeedDatabaseSuite.class
	
				})
/**
 * The following is permissible (individual saline.tests instead of suites):
@SuiteClasses({ApplicationCoreNeedsDatabaseSuite.class,
				TestPatientFunctions.class,
				TestGlucoseLaboratoryObject.class})
*/
public class AllGlucoseObjectsTests {

}
