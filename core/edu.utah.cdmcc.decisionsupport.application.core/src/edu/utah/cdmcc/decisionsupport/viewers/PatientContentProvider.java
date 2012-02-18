package edu.utah.cdmcc.decisionsupport.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class PatientContentProvider implements IStructuredContentProvider {

	public Object[] getElements(final Object inputElement) {
		return ApplicationControllers.getPatientController().getPatients().toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
