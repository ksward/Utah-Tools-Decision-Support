package edu.utah.cdmcc.decisionsupport.controller.core;

public class HSQLDBController {
	private Boolean hsqldbIsRunning = false;

	public HSQLDBController() {
	}	
	
	public Boolean getHsqldbIsRunning() {
		return hsqldbIsRunning;
	}

	public void setHsqldbIsRunning(Boolean hsqldbIsRunning) {
		this.hsqldbIsRunning = hsqldbIsRunning;
	}



}
