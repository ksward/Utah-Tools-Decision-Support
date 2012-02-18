package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.hibernate.HibernateException;

import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.laboratory.object.SerumGlucoseLaboratoryResult;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.views.GetExperimentalLaboratoryResult;

public class NewGlucoseLaboratoryHandler extends AbstractHandler {
	
	private static final String EMPTY = "";
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(ApplicationControllers.getPatientController().getActivePatient() != null){
			SerumGlucoseLaboratoryResult glucoseLabResult = new SerumGlucoseLaboratoryResult();
			Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
			GetExperimentalLaboratoryResult labResultWidget = new GetExperimentalLaboratoryResult(shell, glucoseLabResult, 0, 2000);
			labResultWidget.open();
			if (glucoseLabResult.getConventionalTextResult() != EMPTY){
				ApplicationControllers.getPatientController().getActivePatient().addExperimentalLabResult(glucoseLabResult);
			}
			updatePatient(ApplicationControllers.getPatientController().getActivePatient());
		} else {
			MessageDialog.openInformation(null, "No active patient has been selected.", "Please select an active patient first.");		
		}
		return null;
	}

	private void updatePatient(Patient patient) {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatientValues(patient);
			patientDAO.getSession().flush();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}
