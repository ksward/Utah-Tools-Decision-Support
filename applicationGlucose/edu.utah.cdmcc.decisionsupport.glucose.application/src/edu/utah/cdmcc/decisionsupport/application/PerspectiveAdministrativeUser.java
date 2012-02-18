package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveAdministrativeUser implements IPerspectiveFactory {

	public static final String ID = "edu.utah.cdmcc.decisionsupport.glucose.perspective.AdministrativeUser";
	

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);

	}

}
