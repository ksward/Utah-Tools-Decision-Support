package edu.utah.cdmcc.wizards;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class UpdatePatientWizard extends Wizard {
	private PatientDemographicsWizardPage demoPage = new PatientDemographicsWizardPage("patientDemoEntry");
	IWorkbench workbench = null;
	IStructuredSelection selection = null;
	Patient oldPatient;

	public UpdatePatientWizard() {
		super();
		this.setWindowTitle("Update Existing Research Patient");
	}

	public boolean foundPatientFormErrors() {
		Boolean errorOccurred = false;
		return errorOccurred;
	}

	@Override
	public boolean performFinish() {
		if (foundPatientFormErrors())
			return false;
		if (Utils.isTextNonEmpty(demoPage.patientDemoEntry.getLastNameText())) {
			oldPatient.setLastName(demoPage.patientDemoEntry.getLastNameText().getText().trim());
		}
		if (Utils.isTextNonEmpty(demoPage.patientDemoEntry.getFirstNameText())) {
			oldPatient.setFirstName(demoPage.patientDemoEntry.getFirstNameText().getText().trim());
		}
		if (Utils.isTextNonEmpty(demoPage.patientDemoEntry.getMedrecnumText())) {
			oldPatient.setMedRecNum(demoPage.patientDemoEntry.getMedrecnumText().getText().trim());
		}
			GregorianCalendar theBirthdate = new GregorianCalendar(demoPage.patientDemoEntry.getBirthdateWidget().getYear(),
					demoPage.patientDemoEntry.getBirthdateWidget().getMonth(),
					demoPage.patientDemoEntry.getBirthdateWidget().getDay());
			oldPatient.setBirthdate(theBirthdate);
		if (Utils.isTextNonEmpty(demoPage.patientDemoEntry.getWeightText())) {
			oldPatient.setWeight(getWeight());
		}
		if (Utils.isTextNonEmpty(demoPage.patientDemoEntry.getHeightText())) {
			oldPatient.setHeight(getHeight());
		}
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		boolean result;
		try {
			patientDAO.getSession().beginTransaction();
			result = patientDAO.updatePatientValues(oldPatient);
			patientDAO.getSession().getTransaction().commit();
			
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		if(result) {
			ApplicationControllers.getPatientController().firePatientsChanged(oldPatient);
		}
		return result;
		
	}

	private Double getWeight() {
		return Double.parseDouble(demoPage.patientDemoEntry
				.getWeightText().getText().trim());
	}
	
	private Double getHeight() {
		return Double.parseDouble(demoPage.patientDemoEntry
				.getHeightText().getText().trim());
	}
	
	@Override
	public void addPages() {
		addPage(demoPage);
	}

	public void init(final IWorkbench workbench, Patient patient) {
		this.workbench = workbench;
		oldPatient = patient;
	}

	public void initializeFields() {
		demoPage.patientDemoEntry.setFirstNameText(oldPatient.getFirstName());
		demoPage.patientDemoEntry.setLastNameText(oldPatient.getLastName());
		demoPage.patientDemoEntry.setMedrecnumText(oldPatient.getMedRecNum());
		demoPage.patientDemoEntry.getBirthdateWidget().setYear(oldPatient.getBirthdate().get(Calendar.YEAR));
		demoPage.patientDemoEntry.getBirthdateWidget().setMonth(oldPatient.getBirthdate().get(Calendar.MONTH));
		demoPage.patientDemoEntry.getBirthdateWidget().setDay(oldPatient.getBirthdate().get(Calendar.DAY_OF_MONTH));	
		demoPage.patientDemoEntry.getHeightText().setText(oldPatient.getHeight() + "");
		demoPage.patientDemoEntry.getWeightText().setText(oldPatient.getWeight() + "");
		
	}
}
