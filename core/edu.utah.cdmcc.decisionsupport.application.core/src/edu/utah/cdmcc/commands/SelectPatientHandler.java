package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.decisionsupport.viewers.PatientListSelectionDialog;

public class SelectPatientHandler extends AbstractHandler {
	private Patient patient;
	private IWorkbenchWindow window;
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PatientListSelectionDialog dialog = new PatientListSelectionDialog(HandlerUtil.getActiveShell(event));
		if (dialog.open() == SelectionDialog.OK){
			Object selection[] = dialog.getResult();
			if (selection.length > 0) {
				patient = ((Patient) selection[0]);
				System.out.println(patient.getName());
				ApplicationControllers.getPatientController().setActivePatient(patient);
				ApplicationControllers.getPatientController().firePatientsChanged(patient);
				IHandlerService handlerService = (IHandlerService) HandlerUtil.getActiveSite(event).getService(IHandlerService.class);
				try {
					handlerService.executeCommand("edu.utah.cdmcc.commands.showDomainEditor", null);
					window = HandlerUtil.getActiveWorkbenchWindow(event);
					((ApplicationWindow) window).setStatus("Switched edited patient to " + patient.getName() + " ...");
				} catch (NotDefinedException e) {
					e.printStackTrace();
				} catch (NotEnabledException e) {
					e.printStackTrace();
				} catch (NotHandledException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
