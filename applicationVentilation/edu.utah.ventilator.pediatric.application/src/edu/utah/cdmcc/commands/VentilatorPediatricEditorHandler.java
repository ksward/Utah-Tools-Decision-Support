package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import core.patient.object.PatientEditorInput;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.editors.VentilatorDecisionCalculatorEditorBasedOnTemplate;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
/**
 * Added comments to test GIT.
 * @author mdean
 *
 */
public class VentilatorPediatricEditorHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		if (page.getEditorReferences().length>0){
			page.closeAllEditors(true);
		}
		PatientEditorInput input = new PatientEditorInput(ApplicationControllers.getPatientController().getActivePatient());
		try {
			page.openEditor(input, VentilatorDecisionCalculatorEditorBasedOnTemplate.ID);
		} catch (PartInitException e) {
			System.out.println("PatientCalculatePediatricVentilatorDecisionAction failure");
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.PART_INIT_ERROR, e);
		}
		return null;
	}

}
