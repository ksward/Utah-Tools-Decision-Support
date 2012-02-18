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
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.views.GetExperimentalLaboratoryResult;

public class NewSerumOsmolalityLaboratoryHandler extends AbstractHandler {

	private static final String EMPTY = "";
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(ApplicationControllers.getPatientController().getActivePatient() != null){
			SerumOsmolalityLaboratoryResult osmolalityLabResult = new SerumOsmolalityLaboratoryResult();
			Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
			GetExperimentalLaboratoryResult labResultWidget = new GetExperimentalLaboratoryResult(shell, osmolalityLabResult, 240, 400);
			labResultWidget.open();
			if (osmolalityLabResult.getConventionalTextResult() != EMPTY){
				ApplicationControllers.getPatientController().getActivePatient().addExperimentalLabResult(osmolalityLabResult);			
				}
			updatePatient(ApplicationControllers.getPatientController().getActivePatient());
			ApplicationControllers.getLaboratoryController().fireLaboratoryChanged();
		} else {
			MessageDialog.openInformation(null, "No active patient has been selected.", "Please select an active patient first.");		
		}
		return null;
	}

	private void updatePatient(Patient patient) {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL);
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatientValues(patient);
			patientDAO.getSession().flush();
			patientDAO.getSession().getTransaction().commit();
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

}
