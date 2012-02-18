package core.tests;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import core.laboratory.object.SerumGlucoseLaboratoryResult;
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;
import core.patient.object.Patient;

public class TestExperimentalSingleValueClasses {

	private Patient patient;

	

	@Test
	public void testInsertExperimentalGlucoseResult(){
		patient.addExperimentalLabResult(new SerumGlucoseLaboratoryResult(new GregorianCalendar(),"200"));
		assertEquals("Experimental lab now should have 1 test",1,
				patient.getExperimentalLabResults().size());	
	}
	
	@Test
	public void testGlucoseResultConstruction(){
		SerumGlucoseLaboratoryResult result = new SerumGlucoseLaboratoryResult(new GregorianCalendar(),"200");
		assertEquals("Loinc failed",  "14749-6",result.getLoincCode());
		assertEquals("Units failed", "mg/dL", result.getConventionalUnits());
		assertEquals("Result failed", result.getConventionalTextResult(), "200");
		assertEquals("Labeling failed", "serum glucose", result.getLabelName());
	}
	
	@Test
	public void testGlucoseResultRetrieval(){
		patient.addExperimentalLabResult(new SerumGlucoseLaboratoryResult(new GregorianCalendar(),"200"));
		assertEquals("Experimental lab now should have 1 glucose",1,
				patient.getExperimentalLabResults().size());
		SerumGlucoseLaboratoryResult result = (SerumGlucoseLaboratoryResult) patient.getExperimentalLabResults().get(0);
		assertEquals("Value of glucose test should be 200",200,result.getNumericResult().intValue());
	}
	
	@Test
	public void testInsertExperimentalSodiumResult(){
		patient.addExperimentalLabResult(new SerumSodiumLaboratoryResult(new GregorianCalendar(), "165"));
		assertEquals("Experimental lab now should have 1 test",1,
				patient.getExperimentalLabResults().size());
	}
	
	@Test
	public void testSodiumResultRetrieval(){
		patient.addExperimentalLabResult(new SerumSodiumLaboratoryResult(new GregorianCalendar(),"165"));
		assertEquals("Experimental lab now should have 1 sodium",1,
				patient.getExperimentalLabResults().size());
		SerumSodiumLaboratoryResult result = (SerumSodiumLaboratoryResult) patient.getExperimentalLabResults().get(0);
		assertEquals("Value of sodium test should be 165",165,result.getNumericResult().intValue());
	}
	
	@Test
	public void testSodiumResultConstruction(){
		SerumSodiumLaboratoryResult result = new SerumSodiumLaboratoryResult(new GregorianCalendar(),"167");
		assertEquals("Loinc failed",  "2951-2",result.getLoincCode());
		assertEquals("Units failed", "mEq/dL", result.getConventionalUnits());
		assertEquals("Result failed", result.getConventionalTextResult(), "167");
		assertEquals("Labeling failed", "serum sodium", result.getLabelName());
	}
	
	@Test
	public void testInsertExperimentalOsmolalityResult(){
		patient.addExperimentalLabResult(new SerumOsmolalityLaboratoryResult(new GregorianCalendar(), "145"));
		assertEquals("Experimental lab now should have 1 core.tests",1,
				patient.getExperimentalLabResults().size());
	}
	
	@Test
	public void testOsmolalityResultConstruction(){
		SerumOsmolalityLaboratoryResult result = new SerumOsmolalityLaboratoryResult(new GregorianCalendar(),"266");
		assertEquals("Loinc failed",  "2692-2",result.getLoincCode());
		assertEquals("Units failed", "mmol/L", result.getConventionalUnits());
		assertEquals("Result failed", result.getConventionalTextResult(), "266");
		assertEquals("Labeling failed", "serum osmolality", result.getLabelName());
	}
	
	@Test
	public void testOsmolalityResultRetrieval(){
		patient.addExperimentalLabResult(new SerumOsmolalityLaboratoryResult(new GregorianCalendar(),"266"));
		assertEquals("Experimental lab now should have 1 osmolality",1,
				patient.getExperimentalLabResults().size());
		SerumOsmolalityLaboratoryResult result = (SerumOsmolalityLaboratoryResult) patient.getExperimentalLabResults().get(0);
		assertEquals("Value of osmolality test should be 266",266,result.getNumericResult().intValue());
	}
	
	@Before
	public void setUp() throws Exception {
		patient = new Patient("Sample");
	
	}
}
