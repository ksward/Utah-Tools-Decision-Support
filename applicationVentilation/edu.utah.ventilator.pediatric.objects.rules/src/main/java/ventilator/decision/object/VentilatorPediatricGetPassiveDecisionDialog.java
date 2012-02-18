package ventilator.decision.object;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

import core.decision.object.ClinicalDecision;
import core.field.validators.Verify;

public class VentilatorPediatricGetPassiveDecisionDialog extends TitleAreaDialog {
	private Text descriptionText;
	private Text newSalineRateText;
	private Text salineBolusText;
	private Text mannitolBolusText;
	private VentilatorPediatricDecision decision;
	private Button button;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public VentilatorPediatricGetPassiveDecisionDialog(Shell parentShell, VentilatorPediatricDecision decision) {
		super(parentShell);
		this.decision = decision;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Please enter the decisions that were made in the fields below.");
		setTitle("Clinical actions taken at this time.");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group grpClinicalActions = new Group(container, SWT.SHADOW_ETCHED_IN);
		grpClinicalActions.setText("Clinical actions");
		GridData gd_grpClinicalActions = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpClinicalActions.heightHint = 99;
		gd_grpClinicalActions.widthHint = 476;
		grpClinicalActions.setLayoutData(gd_grpClinicalActions);
		grpClinicalActions.setLayout(new GridLayout(2, true));
		
		Label lblNewRate = new Label(grpClinicalActions, SWT.NONE);
		lblNewRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewRate.setText("New 3% saline rate (ml/kg/hr):");
		
		newSalineRateText = new Text(grpClinicalActions, SWT.BORDER);
		newSalineRateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		newSalineRateText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				Verify.verifyFloatText(e);
			}
		});
		newSalineRateText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkEnableButton();
			}
		});
		
		Label lblSalineBolus = new Label(grpClinicalActions, SWT.NONE);
		lblSalineBolus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSalineBolus.setText("3% saline bolus (ml/kg):");
		
		salineBolusText = new Text(grpClinicalActions, SWT.BORDER);
		salineBolusText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		salineBolusText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				Verify.verifyFloatText(e);
			}
		});
		salineBolusText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkEnableButton();
			}
		});
		
		Label lblMannitolBolusgkg = new Label(grpClinicalActions, SWT.NONE);
		lblMannitolBolusgkg.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMannitolBolusgkg.setText("Mannitol bolus (g/kg):");
		
		mannitolBolusText = new Text(grpClinicalActions, SWT.BORDER);
		mannitolBolusText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mannitolBolusText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				Verify.verifyFloatText(e);
			}
		});
		mannitolBolusText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkEnableButton();
			}
		});
		
		Group grpDescriptiveComments = new Group(container, SWT.NONE);
		grpDescriptiveComments.setText("Descriptive comments");
		GridData gd_grpDescriptiveComments = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpDescriptiveComments.heightHint = 97;
		grpDescriptiveComments.setLayoutData(gd_grpDescriptiveComments);
		
		descriptionText = new Text(grpDescriptiveComments, SWT.BORDER | SWT.MULTI);
		descriptionText.setToolTipText("Enter comments or description explaining decision entries above.");
		descriptionText.setBounds(10, 10, 454, 74);

		return area;
	}

	private void checkEnableButton() {
		if (allRequiredFieldsFilledIn()){
			button.setEnabled(true);
		}
		
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		button.setEnabled(false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(498, 393);
	}
	
	private final boolean allRequiredFieldsFilledIn() {
		return newSalineRateText.getText().length() > 0
				&& salineBolusText.getText().length() > 0
				&& mannitolBolusText.getText().length() > 0;
	}
	
@Override
protected void okPressed() {
	decision.setRecommendedHypertonicSalineBolus(Double.valueOf(salineBolusText.getText()));
	decision.setRecommendedHypertonicSalineDripRate(Double.valueOf(newSalineRateText.getText()));
	decision.setRecommendedMannitolBolus(Double.valueOf(mannitolBolusText.getText()));
	decision.setOtherComment(descriptionText.getText());
	decision.setAdviceText("Click on the Accept Button below.  \nThank you for entering data.\n" +
			"Please enter information again in two hours.");
	decision.setRationaleText("This application is collecting data without active computer decisions.");
	decision.setUserAction(ClinicalDecision.PASSIVE);
	decision.setMinutesToNextEvaluation(ClinicalDecision.TWO_HOUR);
	super.okPressed();
}
}
