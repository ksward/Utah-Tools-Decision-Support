package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	//private IWorkbenchAction introAction;
	private IWorkbenchAction resetPerspective;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {

		if (Platform.getProduct() != null) {
//			introAction = ActionFactory.INTRO.create(window);
//			register(introAction);
		}
		resetPerspective = ActionFactory.RESET_PERSPECTIVE.create(window);
		register(resetPerspective);
	}
}
