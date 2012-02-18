package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.views.LaboratoryTableViewTemplate;

public class FilterInvalidLaboratoryRecordsHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Boolean showInvalidRecords = ApplicationControllers.getLaboratoryController().getInvalidRecordsShown();
		Patient patient = ApplicationControllers.getPatientController().getActivePatient();
		LaboratoryTableViewTemplate view = (LaboratoryTableViewTemplate) HandlerUtil.getActivePart(event);
		if (showInvalidRecords){
			view.getLabTableViewer().addFilter(view.getGetInvalidFilter());
			}
		else {
			view.getLabTableViewer().removeFilter(view.getGetInvalidFilter());
		}
		ApplicationControllers.getLaboratoryController().setInvalidRecordsShown(!showInvalidRecords);
		ApplicationControllers.getLaboratoryController().updateLabList(patient);
		view.laboratoryChanged();
		return null;
	}

}
