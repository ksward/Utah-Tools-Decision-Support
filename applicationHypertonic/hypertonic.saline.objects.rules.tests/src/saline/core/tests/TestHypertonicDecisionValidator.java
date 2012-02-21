package saline.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hypertonic.decision.object.HypertonicSalineDecision;

import java.util.GregorianCalendar;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Test;

import core.patient.object.Patient;

public class TestHypertonicDecisionValidator {
	
	private HypertonicSalineDecision decision;
	private ClassValidator<HypertonicSalineDecision> decisionValidator;
	private InvalidValue[] validationMessages;
	
	@Test
	public void testHypertonicDecisionCreationWithNullFields(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), null, null, null, null);
		assertNotNull(decision);
	}
	
	@Test
	public void testHypertonicDecisionValidationWithNullFields(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), null, null, null, null);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be validation errors for null drip rate and MAP",2,validationMessages.length);
	}
	
	@Test
	public void testHypertonicDecisionValidationWithNonNullMAPAndDripRate(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 5., null, 50, null);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be no validation errors for null drip rate and MAP",0,validationMessages.length);		
	}
	
	@Test
	public void testHypertonicDecisionValidationLowMAP(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 5., null, 29, null);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be  validation errors for MAP below minimum",1,validationMessages.length);
	}
	
	@Test
	public void testHypertonicDecisionValidationHighMAP(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 5., null, 201, null);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be  validation errors for MAP above maximum",1,validationMessages.length);
	}
	
	@Test
	public void testHypertonicDecisionValidationLowCVP(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 5., null, 50, -1);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be  validation errors for MAP above maximum",1,validationMessages.length);
	}
	
	@Test
	public void testHypertonicDecisionValidationHighCVP(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 5., null, 50, 51);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be  validation errors for MAP above maximum",1,validationMessages.length);
	}
	
	@Test
	public void testHypertonicDecisionValidationHighICP(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 5., 121, 50, null);
		decision.setPatient(new Patient("lastname"));
		decisionValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = decisionValidator.getInvalidValues(decision);
		assertEquals("Should be  validation errors for MAP above maximum",1,validationMessages.length);
	}
	
	
	
}
