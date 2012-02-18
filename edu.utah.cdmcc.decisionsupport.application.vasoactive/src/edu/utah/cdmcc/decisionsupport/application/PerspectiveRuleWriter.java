package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import edu.utah.cdmcc.decisionsupport.clock.core.Clock;
import edu.utah.cdmcc.jface.viewers.DecisionTableView;
import edu.utah.cdmcc.views.drools.AuditView;


public class PerspectiveRuleWriter implements IPerspectiveFactory {

	public static final String ID = "edu.utah.cdmcc.decisionsupport.glucose.perspective.ruleWriter";

	public void createInitialLayout(final IPageLayout layout) {
		layout.setEditorAreaVisible(true);

		layout.addView(Clock.ID, IPageLayout.BOTTOM, 0.7f,
				IPageLayout.ID_PROP_SHEET);
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, layout.getEditorArea());
		bottom.addView(AuditView.ID);
		bottom.addView(DecisionTableView.ID);
		layout.addShowViewShortcut(Clock.ID);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(DecisionTableView.ID);
		layout.addShowViewShortcut(AuditView.ID);
		layout.addPerspectiveShortcut(PerspectiveNormalUser.ID);
	}
}
