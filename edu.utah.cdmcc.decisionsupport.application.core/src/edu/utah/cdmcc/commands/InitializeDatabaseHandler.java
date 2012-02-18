package edu.utah.cdmcc.commands;

import org.apache.log4j.Logger;
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
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class InitializeDatabaseHandler extends AbstractHandler {
	private static Logger log = Logger.getLogger(InitializeDatabaseHandler.class);
	private static final int YES = 0;
	private IWorkbenchWindow window;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		log.info("Starting the initialize database handler");
		window = HandlerUtil.getActiveWorkbenchWindow(event);
		log.debug("Have the window: " + window.toString());
		MessageDialog destroyDatabaseDialog = checkDialogToVerifyDestroyDatabase();
		if (destroyDatabaseDialog.open() == YES) {
			log.debug("About to destroy the database - user has confirmed");
			initializeDatabase();
			log.debug("Database destroyed - now calling the controllers");
			ApplicationControllers.getPatientController().firePatientsChanged(null);
			ApplicationControllers.getPatientController().updatePatientList();
			ApplicationControllers.getDatabaseController().fireDatabaseChanged();
			((ApplicationWindow) window).setStatus("Database initialized to empty ...");
		} else {
			((ApplicationWindow) window).setStatus("Database initialization canceled by user ...");
		}
		return null;
	}

	private MessageDialog checkDialogToVerifyDestroyDatabase() {
		MessageDialog dialog = new MessageDialog(null, "Attention - Permanent Database Deletion", null,
				"Are you certain you wish to initialize the database?" + " This is"
						+ " a permanent change and will erase ALL patients" + " and decisions from the database.",
				MessageDialog.QUESTION, new String[] { "Yes - Erase (destroy) Database", "No - Keep Existing Data" }, 1);
		return dialog;
	}

	private void initializeDatabase() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			log.info("About to try initializing the database in a hibernate transaction");
			session.beginTransaction();
			SchemaExport se = new SchemaExport(HibernateUtil.getConfiguration());
			se.create(false, true);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			log.debug("Hibernate exception: " + e);
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
}
