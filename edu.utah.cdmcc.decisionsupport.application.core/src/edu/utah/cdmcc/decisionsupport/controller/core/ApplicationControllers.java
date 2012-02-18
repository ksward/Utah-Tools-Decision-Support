package edu.utah.cdmcc.decisionsupport.controller.core;

import edu.utah.cdmcc.decisionsupport.application.core.DecisionSupportActivatorTemplate;
import edu.utah.cdmcc.decisionsupport.clock.core.ClockJob;

/**
 * This controller is  used to hold application controllers
 * as well as a pointer to the application itself.
 * 
 * @author J. Michael Dean, M.D.
 *
 */
public class ApplicationControllers {
	private static PatientController patientController;
	private static DecisionController decisionController;
	private static HSQLDBController hsqldbController;
	private static ClockJob clockJob;
	private static DatabaseController databaseController;
	private static LaboratoryController laboratoryController;
	private static UserController userController;
	private static DecisionSupportActivatorTemplate application;

	static {
		patientController = new PatientController();
		decisionController = new DecisionController();
		hsqldbController = new HSQLDBController();
		clockJob = new ClockJob("Decision Clock");
		databaseController = new DatabaseController();
		laboratoryController = new LaboratoryController();
		userController = new UserController();
	}

	public static PatientController getPatientController() {
		return patientController;
	}

	public static DecisionController getDecisionController() {
		return decisionController;
	}

	public static HSQLDBController getHsqldbController() {
		return hsqldbController;
	}

	public static ClockJob getClockJob() {
		return clockJob;
	}

	public static DatabaseController getDatabaseController() {
		return databaseController;
	}
	
	public static LaboratoryController getLaboratoryController() {
		return laboratoryController;
	}

	public static UserController getUserController() {
		return userController;
	}

	public static DecisionSupportActivatorTemplate getApplication() {
		return application;
	}

	public static void setApplication(DecisionSupportActivatorTemplate application) {
		ApplicationControllers.application = application;
	}
	
}
