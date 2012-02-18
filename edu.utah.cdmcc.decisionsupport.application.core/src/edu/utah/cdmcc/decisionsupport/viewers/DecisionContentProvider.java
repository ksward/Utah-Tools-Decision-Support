package edu.utah.cdmcc.decisionsupport.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class DecisionContentProvider implements IStructuredContentProvider {

	public Object[] getElements(final Object inputElement) {
		return ApplicationControllers.getDecisionController().getDecisionsForPatient().toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
