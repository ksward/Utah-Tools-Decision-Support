package core.multiple.object.controllers;

import java.util.GregorianCalendar;
import core.patient.object.Patient;

public interface IPatientDecisionController {
	GregorianCalendar retrievePreviousObservationDateTime(Patient patient);
}
