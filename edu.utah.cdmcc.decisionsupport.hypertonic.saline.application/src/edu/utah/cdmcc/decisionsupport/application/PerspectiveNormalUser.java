package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveNormalUser implements IPerspectiveFactory {

	public static final String ID = "edu.utah.cdmcc.decisionsupport.hypertonic.saline.perspective.normalUser";

	public void createInitialLayout(final IPageLayout layout) {
		layout.setEditorAreaVisible(true);
	}
}
