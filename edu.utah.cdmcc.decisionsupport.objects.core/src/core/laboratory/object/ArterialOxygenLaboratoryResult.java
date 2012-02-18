package core.laboratory.object;

public class ArterialOxygenLaboratoryResult extends
		LaboratoryPanelComponent  {
	public static final String LOINC_CODE = "2703-7";
	public static final String CONVENTIONAL_UNITS = "mmHg";

	public ArterialOxygenLaboratoryResult() {
		this.setLabelName("pO2");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
	}

	public ArterialOxygenLaboratoryResult(String result) {
		this.setLabelName("pO2");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
		this.setConventionalTextResult(result);
	}


//	public Integer getIntegerResult() {
//		return Integer.parseInt(getConventionalTextResult());
//	}

	@Override
	public String toString() {
		String s = getLabelName() + "\n" + getConventionalTextResult();
		return s;
	}

}
