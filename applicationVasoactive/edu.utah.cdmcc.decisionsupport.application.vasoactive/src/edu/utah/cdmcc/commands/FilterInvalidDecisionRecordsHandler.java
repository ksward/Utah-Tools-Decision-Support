package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.jface.viewers.DecisionTableView;

public class FilterInvalidDecisionRecordsHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Boolean showInvalidRecords = ApplicationControllers.getDecisionController().getInvalidRecordsShown();
		Patient patient = ApplicationControllers.getPatientController().getActivePatient();
		DecisionTableView view = (DecisionTableView) HandlerUtil.getActivePart(event);	
		if (showInvalidRecords){
			view.getDecisionTableViewer().addFilter(view.getInvalidFilter());
		} else {
			view.getDecisionTableViewer().removeFilter(view.getInvalidFilter());
		}
		ApplicationControllers.getDecisionController().setInvalidRecordsShown(!showInvalidRecords);
		ApplicationControllers.getDecisionController().updateDecisionList(patient);
		view.patientsChanged(patient);
		return null;
	}
}
