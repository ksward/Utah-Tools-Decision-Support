package edu.utah.cdmcc.decisionsupport.application;

import java.util.Map;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hsqldb.Server;
import org.osgi.framework.BundleContext;
import core.hibernate.HibernateUtil;
import edu.utah.cdmcc.decisionsupport.application.core.DecisionSupportActivatorTemplate;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.preferences.DatabaseMapFactory;
import edu.utah.cdmcc.preferences.DatabasePreferenceConstants;

public class DecisionSupportActivator extends DecisionSupportActivatorTemplate {

	public final static String ID = "edu.utah.cdmcc.decisionsupport.glucose";
	private static DecisionSupportActivator plugin;

	public DecisionSupportActivator() {
		plugin = this;
	}
	
	@Override
	public void startServer() {
		String[] args1 = { "-database", "glucoseData", "-port", String.valueOf(9001), "-no_system_exit", "true" };
		Server.main(args1);
		super.startServer();
	};

	public void start(final BundleContext context) {
		System.out.println("In the start method of the activator");
		ApplicationControllers.setApplication(getDefault());  // must be at top of this method
		try {
			super.start(context);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("Getting the preference store");
		databasePrefs = getDefault().getPreferenceStore();
		databasePreference = databasePrefs.getString(DatabasePreferenceConstants.DATABASE_CHOICE);
		System.out.println("The database preference is "+ databasePreference);
		
		if (databasePreference.equals("hsqldb")) {
			System.out.println("Starting HSQLDB because is preference");
			startServer();
			HibernateUtil.injectDatabasePreference(new DatabaseMapFactory().getDatabasePreferenceMap());
		}

		if (databasePreference.equals("mysql")) {
			if (testMySQLConnection(this)) {
				System.out.println("Injecting mysql");
				HibernateUtil.injectDatabasePreference(new DatabaseMapFactory().getDatabasePreferenceMap());
			} else {
				System.out.println("Defaulting to HSQLDB because of MySQL failure");
				startServer();
				Map<String, String> databasePreferenceMap = new DatabaseMapFactory().getDatabasePreferenceMap();
				databasePrefs.setValue(DatabasePreferenceConstants.DATABASE_CHOICE, "hsqldb");
				databasePreferenceMap.put("DATABASE_CHOICE","hsqldb");
				HibernateUtil.injectDatabasePreference(databasePreferenceMap);
			}
		}

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			SchemaUpdate su = new SchemaUpdate(HibernateUtil.getConfiguration());
			su.execute(false, true);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			throw new UtahToolboxException(ErrorCode.SCHEMA_UPDATE_ERROR, e);
		}
				
		ApplicationControllers.getDecisionController().addDecisionFiredListener(ApplicationControllers.getClockJob());
		ApplicationControllers.getPatientController().updatePatientList();
		ApplicationControllers.getPatientController().setActivePatient(null);
		System.out.println("Finished with start bundle routine");
	}

	public void stop(final BundleContext context) throws Exception {	
		if (ApplicationControllers.getHsqldbController().getHsqldbIsRunning() == true)
			stopHsqldb();
		plugin = null;	
		super.stop(context);
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
