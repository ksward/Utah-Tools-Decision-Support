package listener.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { PatientListeners.class, DecisionListeners.class })
public class AllListenerTests {
}
