package edu.utah.cdmcc.decisionsupport.application.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.preferences.DatabasePreferenceConstants;

public class DecisionSupportActivatorTemplate extends AbstractUIPlugin {

	protected String databaseUrl;
	protected String databaseUser;
	protected String databaseDriver;
	protected String databasePreference;
	protected IPreferenceStore databasePrefs;
	
	public void startServer() {

		ApplicationControllers.getHsqldbController().setHsqldbIsRunning(true);
	}
	
	public void stopHsqldb() throws ClassNotFoundException, SQLException {

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
	
	public boolean testMySQLConnection(Object sender) {
		Shell shell;
		Connection con = null;
		String myDatabase = databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_CONNECTION_URL);
		String myUser = databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_USERNAME);
		String myPassword = databasePrefs.getString(DatabasePreferenceConstants.MYSQL_DATABASE_PASSWORD);

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
			if(sender.getClass().isInstance(this)){ // If I am already in running application I cannot
			Display display = new Display();        // create a duplicate Display/Shell or it will crash
			shell = new Shell(display);
			} else {
				shell = null;
			}

			MessageDialog dialog = new MessageDialog(shell,
					"Attention - Cannot connect to mySQL Server",
					null,
					"Impossible to connect to mySQL.  Check that the server is running and check that " +
					"you have entered the correct settings in the preferences.\n\n" +
					"The program will continue with a local HSQLDB database so you can correct the preferences.",
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