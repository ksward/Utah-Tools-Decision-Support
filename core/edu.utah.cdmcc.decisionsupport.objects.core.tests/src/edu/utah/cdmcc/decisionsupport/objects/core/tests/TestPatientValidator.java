package edu.utah.cdmcc.decisionsupport.objects.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.GregorianCalendar;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Test;
import core.patient.object.Patient;

public class TestPatientValidator {
	private Patient patient;
	private GregorianCalendar birthDate;
	private ClassValidator<Patient> personValidator;
	private InvalidValue[] validationMessages;
	
	@Test
	public void testPatientWasCreated(){
		assertEquals("First name assertion: ", "TestFirst", patient
				.getFirstName());
		assertEquals("Last name assertion: ", "TestLast", patient.getLastName());
		assertEquals("Med rec num assertion: ", "12-34-56", patient
				.getMedRecNum());
		assertEquals("Study ID assertion: ", "ST03CHOM0002", patient
				.getStudyID());
		assertEquals("Birthdate assertion: ", birthDate, patient.getBirthdate());
		assertEquals("Weight expected within range: ", 12.0, (double) patient
				.getWeight(), 0.5);
		assertEquals("height expected within range: ", 25.0, (double) patient
				.getHeight(), 0.5);
		assertNotNull("New patient should have non-null Decision list: ",
				patient.getDecisions());
		assertEquals("Decision list should be zero length: ", 0, patient
				.getDecisions().size());
	}
	
	@Test
	public void testPatientPassedValidations(){
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("There should be no validation errors", 0, validationMessages.length);
	}
	
	
	@Test
	public void testPatientFirstNameTooShortValidation() {
		patient = new Patient("TestLast", "F", "12-34-56", "ST02CHOM0003",
				birthDate,  12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("First name short validation error: ", 1,
				validationMessages.length);
		assertEquals("Wrong first name message","firstName length must be between 2 and 20",validationMessages[0].toString());
	}
	
	@Test
	public void testPatientFirstNameTooLongValidation() {
		patient = new Patient("TestLast", "FrankHasAVeryLongLongLongLongName", "12-34-56", "ST02CHOM0003",
				birthDate,  12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("First name short validation error: ", 1,
				validationMessages.length);
		assertEquals("Wrong first name message","firstName length must be between 2 and 20",validationMessages[0].toString());
	}
	
	@Test
	public void testPatientHeightTooLowValidation() {
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST02CHOM0003",
				birthDate,  12.34, 24.9);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Height too low validation error: ", 1,
				validationMessages.length);
		assertEquals("Wrong height message","height must be between 25 and 200",validationMessages[0].toString());
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST02CHOM0003",
				birthDate,  12.34, 25.0);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Height too low validation error: ", 0,
				validationMessages.length);
	}
	
	@Test
	public void testPatientHeightTooHighValidation() {
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST02CHOM0003",
				birthDate,  12.34, 200.1);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Height too high validation error: ", 1,
				validationMessages.length);
		assertEquals("Wrong height message","height must be between 25 and 200",validationMessages[0].toString());
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST02CHOM0003",
				birthDate,  12.34, 200.0);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Height too high validation error: ", 0,
				validationMessages.length);
	}
	
	@Test
	public void testPatientWeightTooLowValidation() {
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST02CHOM0003",
				birthDate,  2.9, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Weight too low validation error: ", 1,
				validationMessages.length);
		assertEquals("Wrong weight message","weight must be between 3 and 300",validationMessages[0].toString());
	}
	
	@Test
	public void testPatientWeightTooHighValidation() {
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST02CHOM0003",
				birthDate,  300.1, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Weight too high validation error: ", 1,
				validationMessages.length);		
		assertEquals("Wrong weight message","weight must be between 3 and 300",validationMessages[0].toString());
	}
	
	@Before
	public void setUp() throws Exception {

		birthDate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthDate, 12.34, 25.34);
		personValidator = new ClassValidator<Patient>(Patient.class);

	}	
}
