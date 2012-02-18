package edu.utah.cdmcc.wizards;

import java.util.GregorianCalendar;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class NewPatientWizard extends Wizard implements INewWizard {
	
	private PatientDemographicsWizardPage demoPage = new PatientDemographicsWizardPage(
			"patientDemoEntry");
	IWorkbench workbench = null;
	IStructuredSelection selection = null;

	public NewPatientWizard() {
		super();
		this.setWindowTitle("New Research Patients");
	}

	public boolean foundPatientFormErrors() {
		String errorMessage = "";
		Boolean errorOccurred = false;
		if (demoPage.patientDemoEntry.getFirstNameText().getText().trim().length() == 0) {
			errorOccurred = true;
			errorMessage = errorMessage + "You must enter a first name.\n";
		}
		if (demoPage.patientDemoEntry.getLastNameText().getText().trim().length() == 0) {
			errorOccurred = true;
			errorMessage = errorMessage + "You must enter a last name.\n";
		}
		if (demoPage.patientDemoEntry.getMedrecnumText().getText().trim().length() == 0) {
			errorOccurred = true;
			errorMessage = errorMessage
					+ "You must enter a medical record number.\n";
		}
		if (demoPage.patientDemoEntry.getWeightText().getText().trim().length() == 0) {
			errorOccurred = true;
			errorMessage = errorMessage + "You must enter a weight.\n";
		}
		if (demoPage.patientDemoEntry.getHeightText().getText().trim().length() == 0) {
			errorOccurred = true;
			errorMessage = errorMessage + "You must enter a height.\n";
		}
		if (errorOccurred) {
			MessageDialog.openError(null, "Error in patient entry",
					errorMessage);
		}
		return errorOccurred;
	}

	@Override
	public boolean performFinish() {
		if (foundPatientFormErrors())
			return false;
		
		String trialDbCode = demoPage.patientDemoEntry.getStudyIDText().getText();		
		String lastName = demoPage.patientDemoEntry.getLastNameText().getText().trim();
		String firstName = demoPage.patientDemoEntry.getFirstNameText().getText().trim();
		String medRecNum = demoPage.patientDemoEntry.getMedrecnumText().getText().trim();
		GregorianCalendar theDate = new GregorianCalendar(demoPage.patientDemoEntry.getBirthdateWidget().getYear(),
				demoPage.patientDemoEntry.getBirthdateWidget().getMonth(),
				demoPage.patientDemoEntry.getBirthdateWidget().getDay());
		Double weight = getWeight();
		Double height = getHeight();
		
		Patient newPatient = new Patient(lastName, firstName, medRecNum,
				trialDbCode, theDate, weight, height);
		if (ApplicationControllers.getUserController().getCurrentUser() != null){
		newPatient.setAccountName(ApplicationControllers.getUserController().getCurrentUser().getAccountName());}
		// NPE occurs here if signed in with backdoor name and password because there IS no object
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		boolean result = false;
		try {
			patientDAO.getSession().beginTransaction();
			result = patientDAO.addPatient(newPatient);
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		if(result){
			ApplicationControllers.getPatientController().firePatientsChanged(null);
		}
		return result;
	}
	
	private Double getWeight() {
		return Double.parseDouble(demoPage.patientDemoEntry
				.getWeightText().getText().trim());
	}
	
	private Double getHeight() {
		return  Double.parseDouble(demoPage.patientDemoEntry
				.getHeightText().getText().trim());
	}

	@Override
	public void addPages() {
		addPage(demoPage);
	}

	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}
	
	public boolean canFinish()
	{
		return (demoPage.isPageComplete());
	}
}
