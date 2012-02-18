package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import edu.utah.cdmcc.wizards.NewPatientWizard;

public class NewPatientHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		NewPatientWizard wizard = new NewPatientWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), wizard);
		dialog.open();
		return null;
	}
}
