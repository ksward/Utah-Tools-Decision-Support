package core.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import core.decision.object.ClinicalDecision;
import core.hibernate.HibernateValidationHandler;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.Patient;

/**
 * This handles all patient creation routines, utilizing the Hibernate
 * interface.  It also handles deletion of patients.  Patient objects
 * are validated by the Hibernate validator, and then the object is
 * made persistent to assure its storage in the database.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *
 */
public class PatientDAOHibernate extends GenericHibernateDAO<Patient, String> implements
		IPatientDAO {
	static Logger logger = Logger.getLogger(PatientDAOHibernate.class);
	private ClassValidator<Patient> personValidator;
	private InvalidValue[] validationMessages;
	private String errorMessage;
	
	/**
	 * Routine to add a new patient to the database; Checks for duplicate
	 * TrialDB number and invalid fields based on Hibernate validator object.
	 * <p>
	 * <p>
	 * @return Boolean to indicate whether patient was added (True) or not
	 *         (False).
	 */
	public boolean addPatient(final Patient newPatient) {
		logger.debug("Adding new patient");
		if (createPatient(newPatient)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Routine creates a new patient. The patient object (newPatient) is
	 * validated against a Hibernate ClassValidator, checked for whether it
	 * already exists in the database, and the return parameter determines
	 * success of the operation.
	 * 
	 * @param newPatient
	 * @return Boolean to indicate if patient was created (True) or failed to be
	 *         created (False).
	 */
	public boolean createPatient(final Patient newPatient) {
		logger.debug("Entering create patient in DAO");
		if (studyIDAlreadyExists(newPatient.getStudyID())) {
			logger.info("Patient already exists");
			notifyUserDuplicateRecordAndFailToAdd();
			return false;
		}
		logger.debug("Create patient method - ready to start validator");
		personValidator = new ClassValidator<Patient>(Patient.class);
		validationMessages = personValidator.getInvalidValues(newPatient);
		logger.debug("Returned from validator with error length: "
				+ validationMessages.length);
		if (validationMessages.length == 0) {
			logger.debug("Validation errors zero so about to make persistent");
			makePersistent(newPatient);
			return true;
		} else
			errorMessage = HibernateValidationHandler
					.handleValidationMessages(validationMessages);
		MessageDialog.openError(null, "Error in decision data entry",
				errorMessage);
		return false;
	}
	
	private  void notifyUserDuplicateRecordAndFailToAdd() {
		MessageDialog dialog = new MessageDialog(null,
				"Attention - Cannot connect to mySQL Server",
				null,
				"Impossible to connect to the mySQL TARGET.  Check that the server is running and check that " +
				"you have entered the correct settings in the preferences.",
				MessageDialog.ERROR,
				new String[] {"Continue"},0);
		dialog.open();
	}

	/**
	 * Routine to delete a patient. This routine brings up a message dialog to
	 * confirm that the user wants to delete the object. The deletion is not
	 * reversible.
	 * 
	 * @param patient
	 */
	public void deletePatient(final Patient patient) {
			deletePatientWithoutWarning(patient);
	}
	
	/**
	 * Deletes a patient without a warning dialog for the user.  This
	 * method is public primarily for testing.  Programmers should
	 * use deletePatient because that routine posts a dialog.
	 * 
	 * @param patient
	 */
	public void deletePatientWithoutWarning(final Patient patient) {
		makeTransient(patient);
	}

	/**
	 * Returns a list of all valid decisions for this patient.  
	 * 
	 */
//	@SuppressWarnings("unchecked")
//	public List<ClinicalDecision> getAllValidDecisions(final Patient patient) {
//		Query q = getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONSBYPATIENT);
//		q.setParameter("patient", patient);
//		List<ClinicalDecision> results = (List<ClinicalDecision>) q.list();
//		return results;
//	}

	@SuppressWarnings("unchecked")
	public List<ClinicalDecision> getAllDecisionsIncludingInvalid(final Patient patient) {
		Query q = getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", patient);
		List<ClinicalDecision> results = (List<ClinicalDecision>) q.list();
		return results;
	}
	

	@SuppressWarnings("unchecked")
	public List<BasicLaboratoryObject> getAllLabsIncludingInvalid(final Patient patient) {
		Query q = getSession().getNamedQuery(BasicLaboratoryObject.GETALLLABRESULTSBYPATIENTINCLUDINGINVALIDEXP);
		q.setParameter("patient",patient);
		List<BasicLaboratoryObject> results = (List<BasicLaboratoryObject>) q.list();
		return results;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<ClinicalDecision> getDecisionsForPatient(Patient patient, Boolean valid){
//		Query q;
//		if (valid){
//			 q = getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONSBYPATIENT);
//		} else {
//			 q = getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
//		}
//		q.setParameter("patient", patient);
//		List<ClinicalDecision> results = (List<ClinicalDecision>) q.list();
//		return results;
//	}
	

	
	public List<Patient> getAllPatients() {
		List<Patient> returnList = findAll();
		return returnList;
	}

	/**
	 * Returns the last patient entered in the databse, based on timestamp.
	 * 
	 * @return The last patient to be entered into the database.
	 */
	public Patient getLastPatient() {
		Patient returnPatient = (Patient) getSession().getNamedQuery(
				Patient.GETLASTPATIENT).uniqueResult();
		return returnPatient;
	}
	
	/**
	 * Routine to return the patient, if any, that has the specified Study ID
	 * number. If more than one patient has the same number, this routine will
	 * fail dismally, meteors will descend on earth, and time will end.
	 * 
	 * @param studyID
	 * @return Patient with the specific Study ID number (unique per patient)
	 */
	private Patient getPatientByStudyID(final String studyID) {
		logger.debug("Getting patient by studyID: " + studyID);
		try {
			Patient result = (Patient) getSession()
					.getNamedQuery(Patient.GETALLPATIENTSBYSTUDYID)
					.setString("studyID", studyID).uniqueResult();
			logger.debug("Query return object " + result);
			return result;
		} catch (HibernateException e) {
			logger.info("Result was not unique");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the total number of patients in the database.
	 * 
	 * @return Number of patients in the database.
	 */
	public Long patientCount() {
		Long returnLong = (Long) getSession().getNamedQuery(
				Patient.PATIENTCOUNT).uniqueResult();
		return returnLong;
	}
	
	/**
	 * Routine to determine if a Study ID number has already been used. If this
	 * is the case then the number cannot be used again.
	 * 
	 * @param trialDbNumber
	 * @return Boolean to indicate if the Study ID number is already used (True)
	 *         or is new (False)
	 */
	public boolean studyIDAlreadyExists(final String studyID) {
		Patient patient = getPatientByStudyID(studyID);
		if (patient != null) {
			return true;
		} else
			return false;
	}

	/**
	 * Routine to update patient, fire property changes, and make sure that the
	 * update does not violate validation requirements for the patient object.
	 * This routine fires a message dialog if there is a failure.
	 * 
	 * @param patient
	 * @return Boolean to indicate if patient was updated (True) or failed to be
	 *         updated (False).
	 */
//	public boolean updatePatient(final Patient patient) {
//		if (updatePatientValues(patient)) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	/**
	 * Routine to update a patient object; checks to determine if the updated
	 * object remains valid according to the Hibernate ClassValidator.
	 * 
	 * @param patient
	 * @return Boolean to indicate if patient was updated (True) or failed to be
	 *         updated (False).
	 */
	public boolean updatePatientValues(final Patient patient) {
		personValidator = new ClassValidator<Patient>(Patient.class);
		validationMessages = personValidator.getInvalidValues(patient);
		logger.debug("Returned from validator with error length: "+validationMessages.length);
		if (validationMessages.length == 0) {
			logger.debug("Validation errors zero so about to make persistent in updatePatientValues");
			makePersistent(patient);
			return true;
		} else
		errorMessage = HibernateValidationHandler.handleValidationMessages(validationMessages);
		MessageDialog.openError(null, "Error in decision data entry", errorMessage);
		return false;
	}

}
