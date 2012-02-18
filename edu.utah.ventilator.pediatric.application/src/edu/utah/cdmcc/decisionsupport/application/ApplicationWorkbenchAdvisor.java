package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String NORMAL_PERSPECTIVE_ID = "edu.utah.cdmcc.decisionsupport.ventilator.pediatric.perspective.normalUser";
	private static final String ADMINISTRATIVE_PERSPECTIVE_ID = "edu.utah.cdmcc.decisionsupport.ventilator.pediatric.perspective.administrativeUser";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(false);
	}

	public String getInitialWindowPerspectiveId() {
		if (ApplicationControllers.getUserController().isAdministrativeUser()){
			return ADMINISTRATIVE_PERSPECTIVE_ID;
		} else
		return NORMAL_PERSPECTIVE_ID;
	}

}
