package core.laboratory.object;

public class ArterialCarbonDioxideLaboratoryResult extends LaboratoryPanelComponent {
	public static final String LOINC_CODE = "2019-8";
	public static final String CONVENTIONAL_UNITS = "mmHg";
	
	
	public ArterialCarbonDioxideLaboratoryResult() {
		this.setLabelName("pCO2");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
	}
	
	public ArterialCarbonDioxideLaboratoryResult(String result){
		this.setLabelName("pCO2");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
		this.setConventionalTextResult(result);
	}
		

//	public Integer getIntegerResult() {
//		return Integer.parseInt(getConventionalTextResult());
//	}
	
	@Override
	public String toString() {
		String s = getConventionalTextResult();
		return s;
	}	
}
