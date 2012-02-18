package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.wizards.UpdatePatientWizard;

public class UpdatePatientHandler extends AbstractHandler {
	private IWorkbenchWindow window;
	public Object execute(ExecutionEvent event) throws ExecutionException {
		window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (ApplicationControllers.getPatientController().getActivePatient() != null) {
			UpdatePatientWizard wizard = new UpdatePatientWizard();
			wizard.init(window.getWorkbench(), ApplicationControllers.getPatientController().getActivePatient());
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.create();
			wizard.initializeFields();
			dialog.open();
		} else {
			MessageDialog.openInformation(null, "No active patient has been selected.", "Please select an active patient first.");
		}
		return null;
	}
}
