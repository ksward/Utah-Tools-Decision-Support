package core.tests;

import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import core.hibernate.HibernateUtil;


public class ResetTestingDatabaseSchema {
	public ResetTestingDatabaseSchema() {
		super();
	}

	public void resetMYSQLDatabase() {
		
		Map<String, String> databasePreferenceMap = new HashMap<String, String>();
		databasePreferenceMap.put("DATABASE_CHOICE", "mysql");
		databasePreferenceMap.put("MYSQL_DATABASE_CONNECTION_URL", "jdbc:mysql://localhost:3306/testLaboratory");
		databasePreferenceMap.put("MYSQL_DATABASE_USERNAME", "root");
		databasePreferenceMap.put("MYSQL_DATABASE_PASSWORD", "root");
		databasePreferenceMap.put("MYSQL_DATABASE_DIALECT", "org.hibernate.dialect.MySQLDialect");
		
		HibernateUtil.injectDatabasePreference(databasePreferenceMap);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SchemaExport se = new SchemaExport(HibernateUtil.getConfiguration());
		se.create(false, true);
		session.getTransaction().commit();
	}
	
	public void resetHSQLDBDatabase() {
		
		Map<String, String> databasePreferenceMap = new HashMap<String, String>();
		databasePreferenceMap.put("DATABASE_CHOICE", "hsqldb");
		databasePreferenceMap.put("HSQLDB_DATABASE_CONNECTION_URL", "jdbc:hsqldb:hsql://localhost");
		databasePreferenceMap.put("HSQLDB_DATABASE_USERNAME", "sa");
		databasePreferenceMap.put("HSQLDB_DATABASE_PASSWORD", "");
		
		HibernateUtil.injectDatabasePreference(databasePreferenceMap);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SchemaExport se = new SchemaExport(HibernateUtil.getConfiguration());
		se.create(false, true);
		session.getTransaction().commit();
	}
}

