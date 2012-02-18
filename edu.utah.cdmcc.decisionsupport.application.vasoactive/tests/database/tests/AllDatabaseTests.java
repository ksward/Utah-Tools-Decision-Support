package database.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.hsqldb.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is the place to add test classes that need to have the database
 * server open.  Simply add the classes to the SuiteClasses annotation.
 * 
 * @author mdean
 *
 */
@RunWith(Suite.class)
@SuiteClasses( { TestSchemaCreateUpdateJUnit4.class,
//		TestPatientAdditionValidationDeletionJUnit4.class,
//		TestGlucoseDecisionAdditionValidationDeletionJUnit4.class,
//		TestGlucoseDecisionRetrievalMethodsJUnit4.class,
		TestPatientNamedQueries.class})
public class AllDatabaseTests {

	@BeforeClass
	public static void setUp() throws Exception {
		String[] args1 = { "-database", "glucoseData", "-port",
				String.valueOf(9001), "-no_system_exit", "true" };
		Server.main(args1);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:hsql://localhost:9001";
		Connection con = DriverManager.getConnection(url, "sa", "");
		String sql = "SHUTDOWN";
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
}
