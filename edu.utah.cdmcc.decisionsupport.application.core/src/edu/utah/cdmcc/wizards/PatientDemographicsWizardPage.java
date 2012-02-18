package edu.utah.cdmcc.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class PatientDemographicsWizardPage extends WizardPage implements Listener {

	public PatientDemographicComposite patientDemoEntry;

	protected PatientDemographicsWizardPage(final String pageName) {
		super(pageName);
		setTitle("Demographic Information about Study Patient");
		setDescription("Enter demographic information about the "
				+ "patient who is participating in the research study.");
	}

	public final void createControl(final Composite parent) {
		patientDemoEntry = new PatientDemographicComposite(parent, SWT.NULL);
		setControl(patientDemoEntry);
		addListeners();
	}

	private void addListeners() {
		patientDemoEntry.getFirstNameText().addListener(SWT.Modify, this);
		patientDemoEntry.getLastNameText().addListener(SWT.Modify, this);
		patientDemoEntry.getMedrecnumText().addListener(SWT.Modify, this);
		patientDemoEntry.getWeightText().addListener(SWT.Modify, this);
		patientDemoEntry.getHeightText().addListener(SWT.Modify, this);
	}

	//@Override
	public final void handleEvent(final Event event) {
		StringBuilder errMsg = new StringBuilder();
		if (!isTextNonEmpty(patientDemoEntry.getFirstNameText())) {
			errMsg.append("Patient First Name is not entered yet; ");
		}
		if (!isTextNonEmpty(patientDemoEntry.getLastNameText())) {
			errMsg.append("Patient Last Name is not entered yet; ");
		}
		if (!isTextNonEmpty(patientDemoEntry.getMedrecnumText())) {
			errMsg.append("Patient Medical Record Number is not entered yet; ");
		}
		if (!isTextNonEmpty(patientDemoEntry.getWeightText())) {
			errMsg.append("Patient Weight is not entered yet; ");
		}
		if (!isTextNonEmpty(patientDemoEntry.getHeightText())) {
			errMsg.append("Patient height is not entered yet; ");
		}
		
		setMessage(errMsg.toString());
		getWizard().getContainer().updateButtons();
	}

//	public final boolean canFlipToNextPage() {
//		if (getMessage() == null) {
//			return true;
//		} else if (getMessage().length() > 0) {
//			return false;
//		} else {
//			return false;
//		}
//	}

	private static boolean isTextNonEmpty(final Text t) {
		String s = t.getText();
		if ((s != null) && (s.trim().length() > 0)) {
			return true;
		}
		return false;
	}

}
