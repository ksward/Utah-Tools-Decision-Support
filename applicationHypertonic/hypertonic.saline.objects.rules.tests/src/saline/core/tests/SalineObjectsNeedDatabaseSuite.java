package saline.core.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.hsqldb.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import saline.core.tests.SalineResetTestingDatabaseSchema;

@RunWith(Suite.class)
@SuiteClasses({
	TestHypertonicDecisionRetrievalFunctions.class,
	TestHypertonicDecisionsToDatabase.class,
	TestSchemaCreateUpdate.class
	
})

public class SalineObjectsNeedDatabaseSuite {
	@BeforeClass
	public static void setUp() throws Exception {

		//Thread.currentThread().setContextClassLoader(HypertonicPatientDecisions.class.getClassLoader());
		
		startHSQLDB();
		resetSchema();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		resetSchema();
		stopHSQLDBServer();
	}
	
	private static void resetSchema() {
		SalineResetTestingDatabaseSchema reset = new SalineResetTestingDatabaseSchema();
		reset.resetHSQLDBDatabase();
	}
	
	private static void startHSQLDB() {
		String[] args1 = { "-database", "testData", "-port",
				String.valueOf(9001), "-no_system_exit", "true" };
		Server.main(args1);
	}

	private static void stopHSQLDBServer() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:hsql://localhost:9001";
		Connection con = DriverManager.getConnection(url, "sa", "");
		String sql = "SHUTDOWN";
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
}
