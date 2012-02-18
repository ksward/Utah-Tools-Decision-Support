package core.hibernate;

import java.net.URL;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class HibernateUtil {

	private static Logger log = Logger.getLogger(HibernateUtil.class);
	private static Configuration configuration;
	private static Configuration targetConfiguration;
	private static SessionFactory sessionFactory;
	private static SessionFactory targetSessionFactory;

	static {

		configuration = new AnnotationConfiguration();
		targetConfiguration = new AnnotationConfiguration();
		//configuration.configure("/hibernate.cfg.xml");
		//sessionFactory = configuration.buildSessionFactory();
	}
	
	/**
	 * Returns the Hibernate configuration used for storing data
	 * 
	 * @return Configuration
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the Hibernate configuration used for transferring to target
	 * 
	 * @return Configuration
	 */
	public static Configuration getTargetConfiguration(){
		log.debug("Getting target configuration");
		return targetConfiguration;
	}
	
	public static SessionFactory getSessionFactory() {
		log.debug("Getting normal session factory");
		SessionFactory sf = null;
		String sfName = configuration
				.getProperty(Environment.SESSION_FACTORY_NAME);
		if (sfName != null) {
			try {
				sf = (SessionFactory) new InitialContext().lookup(sfName);
			} catch (NamingException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			sf = sessionFactory;
		}
		if (sf == null)
			throw new IllegalStateException("SessionFactory not available.");
		return sf;
	}

	public static SessionFactory getTargetSessionFactory() {
		log.debug("Getting target session factory");
		SessionFactory sf = null;
		String sfName = targetConfiguration
				.getProperty(Environment.SESSION_FACTORY_NAME);
		if (sfName != null) {
			try {
				sf = (SessionFactory) new InitialContext().lookup(sfName);
			} catch (NamingException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			sf = targetSessionFactory;
		}
		if (sf == null)
			throw new IllegalStateException("TargetSessionFactory not available.");
		return sf;
	}
	
	/**
	 * Create a new session factory with the configuration that is relevant to
	 * the injected database preference. Currently this only handles HSQLDB and
	 * MySQL, but this method can be enhanced later to permit specific
	 * configurations for other databases.
	 * 
	 * @param databasePreference
	 */
	public static void injectDatabasePreference(
			Map<String, String> databasePreferenceMap) {
		log.debug("Injecting database preferences");
		String databasePreference = databasePreferenceMap
				.get("DATABASE_CHOICE");
		if (databasePreference.equals("hsqldb")) {
			log.debug("Preference is hsqldb");
			rebuildSessionFactory(createHsqldbConfiguration(databasePreferenceMap));
		}
		if (databasePreference.equals("mysql")) {
			log.debug("Preference is mysql");
			rebuildSessionFactory(createMySQLConfiguration(databasePreferenceMap));
		}
	}
	
	public static void injectDatabasePreferenceAndConfigurationResource(Map<String, String> databasePreferenceMap,
			URL resource){
		log.debug("Injecting database AND configuration file");
		rebuildSessionFactory(CreateHsqldbConfigurationForUnitTesting(databasePreferenceMap,resource));
		
		
	}
	


	public static void buildTargetSessionFactory(Map<String, String> databasePreferenceMap){
		log.debug("In buildTargetSessionFactory in HibernateUtil");
		rebuildTargetSessionFactory(createTargetConfiguration(databasePreferenceMap));
	}
	
	
	private static Configuration createMySQLConfiguration(Map<String, String> databasePreferenceMap) {
		Configuration mysqlConfiguration = null;
		try {
			log.debug("Trying to configure mySQL");
			mysqlConfiguration = new AnnotationConfiguration();
			mysqlConfiguration.configure("/hibernate.mysql.xml");
			mysqlConfiguration.setProperty("hibernate.connection.url", databasePreferenceMap.get("MYSQL_DATABASE_CONNECTION_URL"));
			mysqlConfiguration.setProperty("hibernate.connection.username", databasePreferenceMap.get("MYSQL_DATABASE_USERNAME"));
			mysqlConfiguration.setProperty("hibernate.connection.password", databasePreferenceMap.get("MYSQL_DATABASE_PASSWORD"));
			//mysqlConfiguration.setProperty("hibernate.connection.dialect", databasePreferenceMap.get("MYSQL_DATABASE_DIALECT"));
			//mysqlConfiguration.setProperty("hibernate.connection.driver_class", databasePreferenceMap.get("MYSQL_DATABASE_DRIVER"));


		} catch (Throwable ex) {
			log.error("Building the mysql configuration failed");
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		return mysqlConfiguration;
	}

	private static Configuration createHsqldbConfiguration(Map<String, String> databasePreferenceMap) {
		Configuration hsqldbConfiguration = null;
		try {
			log.debug("Trying to configure HSQLDB");
			hsqldbConfiguration = new AnnotationConfiguration();
			hsqldbConfiguration.configure("/hibernate.hsqldb.xml");
			hsqldbConfiguration.setProperty("hibernate.connection.url", databasePreferenceMap.get("HSQLDB_DATABASE_CONNECTION_URL"));
			hsqldbConfiguration.setProperty("hibernate.connection.username", databasePreferenceMap.get("HSQLDB_DATABASE_USERNAME"));
			hsqldbConfiguration.setProperty("hibernate.connection.password", databasePreferenceMap.get("HSQLDB_DATABASE_PASSWORD"));
			//hsqldbConfiguration.setProperty("hibernate.connection.dialect", databasePreferenceMap.get("HSQLDB_DATABASE_DIALECT"));
			//hsqldbConfiguration.setProperty("hibernate.connection.driver_class", databasePreferenceMap.get("HSQLDB_DATABASE_DRIVER"));


		} catch (Throwable ex) {
			log.error("Building the HSQLDB configuration failed");
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		return hsqldbConfiguration;
	}
	
	private static Configuration CreateHsqldbConfigurationForUnitTesting(
			Map<String, String> databasePreferenceMap, URL resource) {
		Configuration hsqldbConfiguration = null;
		try {
			log.debug("Trying to configure HSQLDB");
			hsqldbConfiguration = new AnnotationConfiguration();
			hsqldbConfiguration.configure(resource);
			hsqldbConfiguration.setProperty("hibernate.connection.url", databasePreferenceMap.get("HSQLDB_DATABASE_CONNECTION_URL"));
			hsqldbConfiguration.setProperty("hibernate.connection.username", databasePreferenceMap.get("HSQLDB_DATABASE_USERNAME"));
			hsqldbConfiguration.setProperty("hibernate.connection.password", databasePreferenceMap.get("HSQLDB_DATABASE_PASSWORD"));
			//hsqldbConfiguration.setProperty("hibernate.connection.dialect", databasePreferenceMap.get("HSQLDB_DATABASE_DIALECT"));
			//hsqldbConfiguration.setProperty("hibernate.connection.driver_class", databasePreferenceMap.get("HSQLDB_DATABASE_DRIVER"));


		} catch (Throwable ex) {
			log.error("Building the HSQLDB configuration failed");
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		return hsqldbConfiguration;
	}
	

	private static Configuration createTargetConfiguration(Map<String, String> databasePreferenceMap) {
		Configuration mysqlConfiguration = null;
		try {
			log.debug("Creating target configuration");
			mysqlConfiguration = new AnnotationConfiguration();
			mysqlConfiguration.configure("/hibernate.mysql.xml");
			mysqlConfiguration.setProperty("hibernate.connection.url", databasePreferenceMap.get("TARGET_DATABASE_CONNECTION_URL"));
			mysqlConfiguration.setProperty("hibernate.connection.username", databasePreferenceMap.get("TARGET_DATABASE_USERNAME"));
			mysqlConfiguration.setProperty("hibernate.connection.password", databasePreferenceMap.get("TARGET_DATABASE_PASSWORD"));
			//mysqlConfiguration.setProperty("hibernate.connection.dialect", databasePreferenceMap.get("MYSQL_DATABASE_DIALECT"));
			//mysqlConfiguration.setProperty("hibernate.connection.driver_class", databasePreferenceMap.get("MYSQL_DATABASE_DRIVER"));


		} catch (Throwable ex) {
			log.error("Building the mysql configuration failed");
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		return mysqlConfiguration;
	}
	
	private static void rebuildSessionFactory(Configuration cfg) {
		log.debug("Rebuilding the SessionFactory from given Configuration.");

		if (sessionFactory == null) {
			sessionFactory = cfg.buildSessionFactory();
			configuration = cfg;
		} else {
			if (!sessionFactory.isClosed()) {
				sessionFactory.close();
			}
			sessionFactory = cfg.buildSessionFactory();
			configuration = cfg;
		}
	}
	
	private static void rebuildTargetSessionFactory(Configuration cfg) {
		log.debug("Rebuilding the TargetSessionFactory from given Configuration.");

		if (targetSessionFactory == null) {
			targetSessionFactory = cfg.buildSessionFactory();
			targetConfiguration = cfg;
		} else {
			if (!targetSessionFactory.isClosed()) {
				targetSessionFactory.close();
			}
			targetSessionFactory = cfg.buildSessionFactory();
			targetConfiguration = cfg;
		}
	}
}
