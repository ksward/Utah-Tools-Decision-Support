package core.dao;

import java.util.List;
import core.decision.object.ClinicalDecision;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.Patient;

/**
 * This interface describes how patient addition, deletion,
 * and other CRUD functions are handled.  At the present time,
 * only PatientDAOHibernate.java implements this interface.
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah)
 * Copyright 2005 - 2008.  All Rights Reserved.
 *
 */
public interface IPatientDAO extends IGenericDAO<Patient, String> {
	boolean addPatient(Patient patient);
	//boolean updatePatient(Patient patient);
	void deletePatient(Patient patient);
	void deletePatientWithoutWarning(Patient patient);
	boolean studyIDAlreadyExists(String studyID);
	//Patient getPatientByStudyID(String studyID);
	Patient getLastPatient();
	List<Patient> getAllPatients();
	//List<ClinicalDecision> getAllValidDecisions(Patient patient);
	List<ClinicalDecision> getAllDecisionsIncludingInvalid(Patient patient);
	//List<ClinicalDecision> getDecisionsForPatient(Patient patient, Boolean valid);
	Long patientCount();
	boolean createPatient(Patient newPatient);
	boolean updatePatientValues(Patient patient);
	List<BasicLaboratoryObject> getAllLabsIncludingInvalid(Patient patient);
}
