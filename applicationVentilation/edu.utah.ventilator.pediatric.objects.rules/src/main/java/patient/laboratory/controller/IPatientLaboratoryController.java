package patient.laboratory.controller;

import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.patient.object.Patient;

public interface IPatientLaboratoryController {
	ArterialBloodGasLaboratoryResult retrieveCurrentArterialBloodGasResult(Patient patient);
	ArterialBloodGasLaboratoryResult retrievePreviousArterialBloodGasResult(Patient patient);
}
