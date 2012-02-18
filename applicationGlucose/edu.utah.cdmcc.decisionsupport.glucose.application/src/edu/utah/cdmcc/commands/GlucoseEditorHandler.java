package edu.utah.cdmcc.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import core.patient.object.PatientEditorInput;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.editors.GlucoseDecisionCalculatorEditorBasedOnTemplate;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class GlucoseEditorHandler extends AbstractHandler {
	static Logger logger = Logger.getLogger(GlucoseEditorHandler.class);
	public Object execute(ExecutionEvent event) throws ExecutionException {
		logger.debug("In GlucoseEditorHandler about to get active window");
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		openGlucoseEditor(page);
		return null;
	}
	public void openGlucoseEditor(IWorkbenchPage page) {
		if (page.getEditorReferences().length>0){
			page.closeAllEditors(true);
		}
		PatientEditorInput input = new PatientEditorInput(ApplicationControllers.getPatientController().getActivePatient());
		if (ApplicationControllers.getPatientController().getActivePatient() != null){
		try {
		//	page.openEditor(input, GlucoseDecisionCalculatorEditor.ID);
			page.openEditor(input, GlucoseDecisionCalculatorEditorBasedOnTemplate.ID);
		} catch (PartInitException e) {
			System.out.println("PatientCalculateGlucoseDecisionAction failure");
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.PART_INIT_ERROR, e);
		}
		}
	}

}
