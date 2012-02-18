package patient.laboratory.controller;

import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;
import core.patient.object.Patient;

public interface IPatientLaboratoryController {
	SerumSodiumLaboratoryResult retrieveCurrentSodiumLabResult(Patient patient);
	SerumSodiumLaboratoryResult retrievePreviousSodiumLabResult(Patient patient);
	SerumOsmolalityLaboratoryResult retrieveCurrentOsmolalityLabResult(Patient patient);
	SerumOsmolalityLaboratoryResult retrievePreviousOsmolalityLabResult(Patient patient);
}
