package edu.utah.cdmcc.commands;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class AddFakePatientsHandler extends AbstractHandler {
	private static Logger log = Logger.getLogger(AddFakePatientsHandler.class);
	private IWorkbenchWindow window;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		log.info("Starting the add fake patients handler");
		window = HandlerUtil.getActiveWorkbenchWindow(event);
		log.debug("Have the window: " + window.toString());
		if (ApplicationControllers.getPatientController().getPatients()
				.isEmpty()) {
			((ApplicationWindow) window)
					.setStatus("Making fake patients and adding to database ...");
			IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
					.getPatientDAO();	
			try {
				log.debug("Starting to try the transaction");
				patientDAO.getSession().beginTransaction();
				patientDAO.createPatient(new Patient("Pediatric", "John",
						"12-34-56", "ST02CHOM0001", new GregorianCalendar(1999,
								Calendar.DECEMBER, 15),  30.34,
						72.86));
				patientDAO.createPatient(new Patient("Adult", "Susan",
						"23-34-56", "ST02CHOM0002", new GregorianCalendar(1980,
								Calendar.OCTOBER, 15),  72.34,
						72.86));
				patientDAO.createPatient(new Patient("Pediatric", "Steven",
						"34-34-56", "ST02CHOM0003", new GregorianCalendar(1999,
								Calendar.NOVEMBER, 15),  12.34,
						72.86));
				patientDAO.createPatient(new Patient("Adult", "Mary",
						"45-34-56", "ST02CHOM0004", new GregorianCalendar(1953,
								Calendar.DECEMBER, 25),  80.34,
						72.86));
				Patient newPatient = new Patient("Adult", "Esther", "56-34-56",
						"ST02CHOM0005", new GregorianCalendar(1969,
								Calendar.DECEMBER, 15),  67.8,
						72.86);
				patientDAO.createPatient(newPatient);
				patientDAO.getSession().getTransaction().commit();
				log.debug("Transaction committed");
				ApplicationControllers.getPatientController()
						.firePatientsChanged(null);
				// null in previous command so that viewers do not update based
				// on the last
				// fake patient being added here.

			} catch (HibernateException e) {
				log.debug("Hibernate exception when trying to insert fake patients");
				log.debug("Hibernate exception: " + e);
				e.printStackTrace();
			}
		} else {
			log.info("Database not empty so fake patients not permitted");
			MessageDialog
					.openInformation(
							null,
							"Fake Patient Creation Failed",
							"Fake patients can only be added "
									+ "to an empty database. You must first initialize the database if you really want to add a new set of fake patients.");
			((ApplicationWindow) window)
					.setStatus("Creating fake patients aborted because database not empty ...");
		}
		return null;
	}
}
