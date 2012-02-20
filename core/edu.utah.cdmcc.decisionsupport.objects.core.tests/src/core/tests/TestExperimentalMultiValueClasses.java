package core.tests;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.laboratory.object.ArterialCarbonDioxideLaboratoryResult;
import core.laboratory.object.ArterialOxygenLaboratoryResult;
import core.laboratory.object.ArterialPhLaboratoryResult;
import core.laboratory.object.LaboratoryPanelComponent;
import core.laboratory.object.MultipleValueLaboratoryObject;
import core.patient.object.Patient;

public class TestExperimentalMultiValueClasses {

	protected Patient patient;
	protected ArterialBloodGasLaboratoryResult abgPanelResult1, abgPanelResult2;
	protected ArterialOxygenLaboratoryResult po2Result1, po2Result2;
	protected ArterialPhLaboratoryResult phResult1, phResult2;
	protected ArterialCarbonDioxideLaboratoryResult pco2Result1, pco2Result2;
	protected int oldRecordCOunt, newRecordCount;
	protected GregorianCalendar birthdate,labDate,secondLabDate;
	
	@Test
	public void testMultipleValueLabObjectHasLabelAndAccount(){
		MultipleValueLaboratoryObject lab = new MultipleValueLaboratoryObject();
		assertEquals("Multiple lab should be called unlabeled","unlabeled",lab.getLabelName());
		assertEquals("Unassigned account should be called unassigned","unassigned", lab.getAccountName());
	}
	
	@Test
	public void testMultipleValueLabObjectHasPanelComponentsSet(){
		MultipleValueLaboratoryObject lab = new MultipleValueLaboratoryObject();
		assertNotNull("panel components should not be null", lab.getComponents());
	}
	
	@Test
	public void testMultipleValueLabObjectHasPanelComponentsSetEmptyOnInit(){
		MultipleValueLaboratoryObject lab = new MultipleValueLaboratoryObject();
		assertNotNull("panel components should not be null", lab.getComponents());
		assertEquals("Initial components panel should be empty",0, lab.getComponents().size());
	}
	
	@Test
	public void testAddPanelComponentToMultipleLabObject(){
		MultipleValueLaboratoryObject lab = new MultipleValueLaboratoryObject();
		assertNotNull("panel components should not be null", lab.getComponents());
		assertEquals("Initial components panel should be empty",0, lab.getComponents().size());
		LaboratoryPanelComponent component = new LaboratoryPanelComponent();
		lab.addLaboratoryComponent(component);
		assertEquals("Components should now have one component",1, lab.getComponents().size());		
	}
	
	@Test
	public void testAddedPanelHasNonNullValues(){
		LaboratoryPanelComponent component = new LaboratoryPanelComponent();
		assertNotNull("Component loinc should not be null", component.getLoincCode());
		assertNotNull("Component label should not be null", component.getLabelName());
		assertNotNull("Component conventional units should not be null", component.getConventionalUnits());
		assertNotNull("Component conventional result should not be null", component.getConventionalTextResult());
	}
	
	@Test
	public void testClearingPanelComponents(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		assertEquals("There should only be one laboratory result",1,
				patient.getExperimentalLabResults().size());
		assertEquals("There should one component in the panel",1, 
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().size());
		((MultipleValueLaboratoryObject) patient.getExperimentalLabResults()
		.get(0)).getComponents().clear();
		assertEquals("There should still be one laboratory result",1,
				patient.getExperimentalLabResults().size());
		assertEquals("There should zero components in the panel",0, 
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().size());
	}
	
	@Test
	public void testEmptyBloodGasPanelInsertion(){

		patient.addExperimentalLabResult(abgPanelResult1);
		assertEquals("Blood gas panel was inserted without panel but should count",1,
				patient.getExperimentalLabResults().size());
		assertEquals("There should zero components in the panel",0, 
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().size());
	}
	
	@Test
	public void testAddingOxygenComponentToBloodGasPanel(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		assertEquals("There should only be one laboratory result",1,
				patient.getExperimentalLabResults().size());
		assertEquals("There should one component in the panel",1, 
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().size());
	}
	
	@Test
	public void testAddingOxygenComponentToBloodGasPanelTwiceShouldFail(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(po2Result2);
		abgPanelResult1.addLaboratoryComponent(po2Result2);
		assertEquals("There should only be one laboratory result",1,
				patient.getExperimentalLabResults().size());
		assertEquals("There should one component in the panel",1, 
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().size());
		assertEquals("The value of the single pO2 should not be replaced by second attempt","75",
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().iterator().next().getConventionalTextResult());
	}
	
	@Test
	public void testAddingAllBloodGasComponents(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(phResult1);
		abgPanelResult1.addLaboratoryComponent(pco2Result1);
		assertEquals("There should three components in the panel",3, 
				((MultipleValueLaboratoryObject) patient
						.getExperimentalLabResults()
						.get(0)).getComponents().size());
		
	}
	
	@Test
	public void testGetPhValue(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(phResult1);
		abgPanelResult1.addLaboratoryComponent(pco2Result1);
		assertEquals("The pH value should be ",6.89, abgPanelResult1.getPhValue(),0.01);
	}

	@Test
	public void testGetPo2Value(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(phResult1);
		abgPanelResult1.addLaboratoryComponent(pco2Result1);
		assertEquals("The pO2 value should be ",75, abgPanelResult1.getPo2Value(),0);		
	}
	
	@Test
	public void testGetPco2Value(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(phResult1);
		abgPanelResult1.addLaboratoryComponent(pco2Result1);
		assertEquals("The pCO2 value should be ",36, abgPanelResult1.getPco2Value(),0);		
	}
	
	@Test
	public void testGetMultipleValuesFromBloodGasPanel(){
		patient.addExperimentalLabResult(abgPanelResult1);
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(phResult1);
		abgPanelResult1.addLaboratoryComponent(pco2Result1);
		assertEquals("The pCO2 value should be ",36, abgPanelResult1.getPco2Value(),0);	
		assertEquals("The pO2 value should be ",75, abgPanelResult1.getPo2Value(),0);
		assertEquals("The pH value should be ",6.89, abgPanelResult1.getPhValue(),0.01);
	}
	
	@Test
	public void testABGOverallPanelHasCorrectLoinc(){
		assertEquals("ABG panel has LOINC 24336-0","24336-0", abgPanelResult1.getLoincCode());
	}
	@Test
	public void testABGPanelAndComponentHasCorrectLoinc(){
		assertEquals("ABG panel has LOINC 24336-0","24336-0", abgPanelResult1.getLoincCode());
		abgPanelResult1.addLaboratoryComponent(po2Result1);
		abgPanelResult1.addLaboratoryComponent(phResult1);
		abgPanelResult1.addLaboratoryComponent(pco2Result1);
		assertEquals("ABG panel has LOINC 24336-0","24336-0", abgPanelResult1.getLoincCode());
		assertEquals("Each component has correct loinc","2744-1", abgPanelResult1.getPhComponent().getLoincCode());
		assertEquals("Each component has correct loinc","2019-8", abgPanelResult1.getPco2Component().getLoincCode());
		assertEquals("Each component has correct loinc","2703-7", abgPanelResult1.getPo2Component().getLoincCode());
	}
	
	@Before
	public void setUp() throws Exception {
		birthdate = new GregorianCalendar(1999,12,12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);
		labDate = new GregorianCalendar(2000,12,12);
		secondLabDate = new GregorianCalendar(2000,12,12);
		abgPanelResult1 = new ArterialBloodGasLaboratoryResult(labDate);
		abgPanelResult2 = new ArterialBloodGasLaboratoryResult(secondLabDate);
		po2Result1 = new ArterialOxygenLaboratoryResult("75");
		po2Result2 = new ArterialOxygenLaboratoryResult("135");
		phResult1 = new ArterialPhLaboratoryResult("6.89");
		phResult2 = new ArterialPhLaboratoryResult("7.45");
		pco2Result1 = new ArterialCarbonDioxideLaboratoryResult("36");
		pco2Result2 = new ArterialCarbonDioxideLaboratoryResult("130");
	
	}
}
