package edu.utah.cdmcc.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.jasypt.digest.StandardStringDigester;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.hibernate.AuditLogInterceptor;
import core.hibernate.HibernateUtil;
import core.patient.object.Patient;
import edu.utah.cdmcc.preferences.DatabaseMapFactory;
import edu.utah.cdmcc.preferences.StudyPreferenceConstants;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;

public class TransferPatientsToTargetHandler extends AbstractHandler {

	private Map<String, String> databasePreferenceMap;
	private StandardStringDigester encryptor = new StandardStringDigester();
	private AuditLogInterceptor interceptor = new AuditLogInterceptor();

	public Object execute(ExecutionEvent event) throws ExecutionException {
		databasePreferenceMap = new DatabaseMapFactory().getDatabasePreferenceMap();
		if (!encryptor.isInitialized()){
			encryptor.setSaltSizeBytes(0); // makes the digest reproducible
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (testMySQLTargetConnection()) {
			HibernateUtil.buildTargetSessionFactory(new DatabaseMapFactory().getDatabasePreferenceMap());

			IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			List<Patient> myPatients = patientDAO.getAllPatients();
			patientDAO.getSession().getTransaction().commit();
			patientDAO.getSession().close();

			for (Patient patient : myPatients) {

				patientDAO.getSession().beginTransaction();
				Patient newPatient = (Patient) patientDAO.getSession().get(Patient.class, patient.getId());
				patientDAO.getSession().getTransaction().commit();
				patientDAO.getSession().close();

				if (Activator.getDefault().getPreferenceStore().getBoolean(StudyPreferenceConstants.ENCRYPTION_ON)){
					encryptObject(newPatient);
				}

				Session targetSession = HibernateUtil.getTargetSessionFactory().openSession(interceptor);
				targetSession.beginTransaction();
				interceptor.setSession(targetSession);
				interceptor.setUserId("mdean");  // fix later to pull in the current user
				targetSession.replicate(newPatient, ReplicationMode.LATEST_VERSION);
				targetSession.getTransaction().commit();
				targetSession.close();
			}
			((ApplicationWindow) window).setStatus("Data has been replicated to the target ...");
		} else {
			((ApplicationWindow) window).setStatus("TARGET database was not reachable - check settings ...");
		}
		return null;
	}

	private void encryptObject(Patient patient) {
		patient.setFirstName(encryptor.digest(patient.getFirstName()).substring(1,10));
		patient.setLastName(encryptor.digest(patient.getLastName()).substring(1,10));
		patient.setMedRecNum(encryptor.digest(patient.getMedRecNum()).substring(1,10));	
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
