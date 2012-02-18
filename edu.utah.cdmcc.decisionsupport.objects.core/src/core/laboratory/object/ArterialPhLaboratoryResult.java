package core.laboratory.object;

public class ArterialPhLaboratoryResult extends LaboratoryPanelComponent {
	public static final String LOINC_CODE = "2744-1";
	public static final String CONVENTIONAL_UNITS = "";
	
	public ArterialPhLaboratoryResult() {
		this.setLabelName("pH");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
	}
	
	public ArterialPhLaboratoryResult(String result){
		this.setLabelName("pH");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
		this.setConventionalTextResult(result);
	}
	
	
//	public Double getDoubleResult() {
//		return Double.parseDouble(getConventionalTextResult());
//	}
	
	public String toString() {
		String s = getLabelName() + "\n" + getConventionalTextResult();
		return s;
	}	
}
