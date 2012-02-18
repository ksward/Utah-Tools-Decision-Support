package edu.utah.cdmcc.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import core.hibernate.HibernateUtil;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.preferences.DatabaseMapFactory;

/**
 * This handler erases the TARGET (not the local) database.  It is
 * necessary to call this routine to construct the target, so we have to
 * have this functionality.  However, it is extremely dangerous.
 * 
 * @author mdean
 *
 */
 public class InitializeTargetDatabaseHandler extends AbstractHandler {
	private static final int YES = 0;
	private Map<String, String> databasePreferenceMap;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		databasePreferenceMap = new DatabaseMapFactory().getDatabasePreferenceMap();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (testMySQLTargetConnection()) {
			HibernateUtil.buildTargetSessionFactory(databasePreferenceMap);
			MessageDialog destroyDatabaseDialog = checkDialogToVerifyDestroyDatabase();
			if (destroyDatabaseDialog.open() == YES) {
				initializeDatabase();
				((ApplicationWindow) window).setStatus("TARGET database initialized to empty ...");
			} else {
				((ApplicationWindow) window).setStatus("TARGET database initialization canceled by user ...");
			}
		} else {
			((ApplicationWindow) window).setStatus("TARGET database was not reachable - check settings ...");
		}
		return null;
	}
	
	private void initializeDatabase() {
		Session targetSession = HibernateUtil.getTargetSessionFactory().openSession();
		try {
			targetSession.beginTransaction();
			SchemaExport se = new SchemaExport(HibernateUtil.getTargetConfiguration());
			se.create(false, true);
			targetSession.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
	
	private MessageDialog checkDialogToVerifyDestroyDatabase() {
		MessageDialog dialog = new MessageDialog(null, "Attention - Permanent TARGET Database Deletion", null,
				"Are you certain you wish to initialize the target database?" + " This is"
						+ " a permanent change and will erase ALL patients" + " and decisions from the TARGET database."+
						"NOTE:  This will erase your TARGET, not just the local database!",
				MessageDialog.QUESTION, new String[] { "Yes - Erase (destroy) TARGET Database", "No - Keep Existing Data" }, 1);
		return dialog;
	}
	
	private boolean testMySQLTargetConnection() {
		Connection con = null;
		String myDatabase = databasePreferenceMap.get("TARGET_DATABASE_CONNECTION_URL");
		String myUser = databasePreferenceMap.get("TARGET_DATABASE_USERNAME");
		String myPassword = databasePreferenceMap.get("TARGET_DATABASE_PASSWORD");

		try {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			con = DriverManager.getConnection(myDatabase, myUser, myPassword);
			if (!con.isClosed()) {
				con.close();
				return true;
			}
		} catch (SQLException e) {
			
			MessageDialog dialog = new MessageDialog(null,
					"Attention - Cannot connect to mySQL Server",
					null,
					"Impossible to connect to the mySQL TARGET.  Check that the server is running and check that " +
					"you have entered the correct settings in the preferences.",
					MessageDialog.ERROR,
					new String[] {"Continue"},0);
			int returnVal = dialog.open();
			if (returnVal == 1){
				return false;
			}
		}
		return false;
	}
}
