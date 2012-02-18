package entity.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import core.tests.TestLaboratoryResults;
import core.tests.TestPatientFunctions;

@RunWith(Suite.class)
@SuiteClasses( { TestPatientFunctions.class, TestLaboratoryResults.class})
public class AllEntityTests {
}
