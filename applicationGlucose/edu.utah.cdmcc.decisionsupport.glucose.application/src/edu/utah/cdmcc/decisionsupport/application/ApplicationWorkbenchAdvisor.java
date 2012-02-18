package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String NORMAL_PERSPECTIVE_ID = "edu.utah.cdmcc.decisionsupport.glucose.perspective.normalUser";
	private static final String ADMINISTRATIVE_PERSPECTIVE_ID = "edu.utah.cdmcc.decisionsupport.glucose.perspective.administrativeUser";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(false);
	}

	public String getInitialWindowPerspectiveId() {
		System.out.println("Administrative status is "+ ApplicationControllers.getUserController().isAdministrativeUser());
		if (ApplicationControllers.getUserController().isAdministrativeUser()){
			System.out.println("Returning administrative perspective ID");
			return ADMINISTRATIVE_PERSPECTIVE_ID;
		} else
			System.out.println("Returning normal perspective ID");
			return NORMAL_PERSPECTIVE_ID;
	}

}
