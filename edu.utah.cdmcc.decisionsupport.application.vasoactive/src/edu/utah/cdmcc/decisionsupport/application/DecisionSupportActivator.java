package edu.utah.cdmcc.decisionsupport.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hsqldb.Server;
import org.osgi.framework.BundleContext;
import core.hibernate.HibernateUtil;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.decisionsupport.database.preferences.DatabasePreferenceConstants;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class DecisionSupportActivator extends AbstractUIPlugin {

	public final static String ID = "edu.utah.cdmcc.decisionsupport.glucose";
	private static DecisionSupportActivator plugin;
	private String databasePreference;
	private IPreferenceStore databasePrefs;
	private String databaseUrl;
	private String databaseUser;
	private String databaseDriver;

	/**
	 * Starts the HSQLDB server.
	 * 
	 */
	private void startServer() {

		String[] args1 = { "-database", "glucoseData", "-port", String.valueOf(9001), "-no_system_exit", "true" };
		Server.main(args1);
		ApplicationControllers.getHsqldbController().setHsqldbIsRunning(true);
	}

	/**
	 * Stops the HSQLDB server.
	 * <P>
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void stopHsqldb() throws ClassNotFoundException, SQLException {

		if (ApplicationControllers.getHsqldbController().getHsqldbIsRunning() == true) {
			databaseUrl = databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_CONNECTION_URL);
			databaseUser = databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_USERNAME);
			databaseDriver = databasePrefs.getString(DatabasePreferenceConstants.HSQLDB_DATABASE_DRIVER);
			Class.forName(databaseDriver);
			Connection con = DriverManager.getConnection(databaseUrl, databaseUser, "");
			String sql = "SHUTDOWN";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
		ApplicationControllers.getHsqldbController().setHsqldbIsRunning(false);
	}

	public DecisionSupportActivator() {
		plugin = this;
	}

	public void start(final BundleContext context) {

		try {
			super.start(context);
		} catch (Exception e1) {
			System.out.println("Failed in start of super of DecisionSupportActivator");
			e1.printStackTrace();
		}
		databasePrefs = getDefault().getPreferenceStore();
		databasePreference = databasePrefs.getString(DatabasePreferenceConstants.DATABASE_CHOICE);
		
		if (databasePreference == "hsqldb") {
			startServer();
		}
		
		ApplicationControllers.getDecisionController().addDecisionFiredListener(ApplicationControllers.getClockJob());

		try {
			Class.forName("core.hibernate.HibernateUtil");
		} catch (ClassNotFoundException e1) {
			System.out.println("Failed in Class.forName looking for HibernateUtil");
			e1.printStackTrace();
		}

		// Session session =
		// HibernateUtil.getSessionFactory().getCurrentSession();
		// HibernateUtil.injectDatabasePreference(getPreferenceStore().getString(PreferenceConstants.DATABASE_CHOICE));
		HibernateUtil.injectDatabasePreference("hsqldb");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
			su.execute(false, true);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			throw new UtahToolboxException(ErrorCode.SCHEMA_UPDATE_ERROR, e);
		}

		ApplicationControllers.getPatientController().updatePatientList();
		ApplicationControllers.getPatientController().setActivePatient(null);
	}

	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		stopHsqldb();
		plugin = null;
	}

	public static DecisionSupportActivator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(final String path) {
		ImageDescriptor returnImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
				"edu.utah.cdmcc.decisionsupport.glucose", path);
		return returnImageDescriptor;
	}
}
