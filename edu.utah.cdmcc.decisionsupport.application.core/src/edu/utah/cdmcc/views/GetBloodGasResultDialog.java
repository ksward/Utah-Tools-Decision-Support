package edu.utah.cdmcc.views;

import java.util.GregorianCalendar;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import core.field.validators.Verify;
import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.laboratory.object.ArterialCarbonDioxideLaboratoryResult;
import core.laboratory.object.ArterialOxygenLaboratoryResult;
import core.laboratory.object.ArterialPhLaboratoryResult;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import org.eclipse.swt.widgets.Label;

public class GetBloodGasResultDialog extends TitleAreaDialog {

	private DateTime timeSampleCollected;
	private DateTime dateSampleCollected;
	private Text phText;
	private Text pco2Text;
	private Text po2Text;
	private ArterialBloodGasLaboratoryResult abgPanelResult;
	
	
	/**
	 * Create the dialog.
	 * @param parentShell, abgPanelResult to receive results
	 */
	public GetBloodGasResultDialog(Shell parentShell,
			ArterialBloodGasLaboratoryResult abgPanelResult) {			
		super(parentShell);
		this.abgPanelResult = abgPanelResult;
	}

	@Override
	public void create() {
		super.create();
		setMessage("Enter the date, time, and values for the arterial blood gas.");
		setTitle("Arterial Blood Gas Panel Result");
	};
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblDate = new Label(container, SWT.NONE);
		lblDate.setAlignment(SWT.RIGHT);
		lblDate.setBounds(10, 10, 71, 14);
		lblDate.setText("Date");
		
		dateSampleCollected = new DateTime(container, SWT.BORDER);
		dateSampleCollected.setBounds(106, 10, 88, 22);
		dateSampleCollected.setToolTipText("Date the sample was obtained");
		
		Label lblTime = new Label(container, SWT.NONE);
		lblTime.setAlignment(SWT.RIGHT);
		lblTime.setBounds(10, 38, 71, 14);
		lblTime.setText("Time");
		
		timeSampleCollected = new DateTime(container, SWT.BORDER | SWT.TIME | SWT.SHORT);
		timeSampleCollected.setBounds(106, 38, 88, 22);
		timeSampleCollected.setToolTipText("Time the sample was obtained");
		
		Label lblPh = new Label(container, SWT.NONE);
		lblPh.setAlignment(SWT.RIGHT);
		lblPh.setBounds(10, 70, 71, 14);
		lblPh.setText("pH");
		
		Label lblPco = new Label(container, SWT.NONE);
		lblPco.setAlignment(SWT.RIGHT);
		lblPco.setBounds(10, 95, 71, 14);
		lblPco.setText("pCO2");
		
		Label lblPo = new Label(container, SWT.NONE);
		lblPo.setAlignment(SWT.RIGHT);
		lblPo.setBounds(10, 120, 71, 14);
		lblPo.setText("pO2");
		
		phText = new Text(container, SWT.BORDER);
		phText.setBounds(106, 67, 88, 19);
		phText.addVerifyListener(new VerifyListener() {		
			@Override
			public void verifyText(VerifyEvent evt) {
				Verify.verifyFloatText(evt);
			}
		});
		phText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt){
				checkPhRange();
				checkButtonEnablement();
			}
		});
			
		
		pco2Text = new Text(container, SWT.BORDER);
		pco2Text.setBounds(106, 92, 88, 19);
		pco2Text.addVerifyListener(new VerifyListener() {		
			@Override
			public void verifyText(VerifyEvent evt) {
				Verify.verifyIntegerText(evt);
			}
		});
		pco2Text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt){
				checkPco2Range();
				checkButtonEnablement();
			}
		});
		
		po2Text = new Text(container, SWT.BORDER);
		po2Text.setBounds(106, 117, 88, 19);
		
		Label lblMmhg = new Label(container, SWT.NONE);
		lblMmhg.setEnabled(false);
		lblMmhg.setBounds(200, 95, 59, 14);
		lblMmhg.setText("mmHg");
		
		Label lblMmhg_1 = new Label(container, SWT.NONE);
		lblMmhg_1.setEnabled(false);
		lblMmhg_1.setBounds(200, 120, 59, 14);
		lblMmhg_1.setText("mmHg");
		po2Text.addVerifyListener(new VerifyListener() {		
			@Override
			public void verifyText(VerifyEvent evt) {
				Verify.verifyIntegerText(evt);
			}
		});
		po2Text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt){
				checkPo2Range();
				checkButtonEnablement();
			}
		});
		
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(417, 288);
	}
	
	@Override
	protected void okPressed() {
		abgPanelResult.addLaboratoryComponent(new ArterialPhLaboratoryResult(phText.getText()));
		abgPanelResult.addLaboratoryComponent(new ArterialCarbonDioxideLaboratoryResult(pco2Text.getText()));
		abgPanelResult.addLaboratoryComponent(new ArterialOxygenLaboratoryResult(po2Text.getText()));
		abgPanelResult.setTimeOfSpecimenCollection(calculateDate());
		if (ApplicationControllers.getUserController().getCurrentUser() != null) {
			abgPanelResult.setAccountName(ApplicationControllers.getUserController().getCurrentUser().getAccountName());
		}
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	};	
	
	private void checkButtonEnablement(){
		if (!(phText.getText().length() == 0 || pco2Text.getText().length() == 0)){
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
		checkPhRange();
		checkPco2Range();
		if (!(po2Text.getText().length() == 0)){
			checkPo2Range();
		}
	}
	
	private GregorianCalendar calculateDate() {
		GregorianCalendar dateTime = new GregorianCalendar(dateSampleCollected.getYear(),
				dateSampleCollected.getMonth(),
				dateSampleCollected.getDay(),
				timeSampleCollected.getHours(),
				timeSampleCollected.getMinutes(),
				timeSampleCollected.getSeconds());
		
		return dateTime;
	}

	private void checkPhRange() {
		if(phText.getText().length() == 0){
			setErrorMessage("Enter value for arterial pH.");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (Double.valueOf(phText.getText()) < 6.5){
			setErrorMessage("The minimum permissible value of pH is 6.5 (pretty low!).");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (Double.valueOf(phText.getText()) > 7.7){
			setErrorMessage("The maximum permissible value of pH is 7.7 (pretty high!).");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}else {
			setErrorMessage(null);
		}
	}

	private void checkPco2Range() {
		if(pco2Text.getText().length() == 0){
			setErrorMessage("Enter value for arterial pCO2.");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (Double.valueOf(pco2Text.getText()) < 15){
			setErrorMessage("The minimum permissible value of pCO2 is 15 (pretty low!).");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (Double.valueOf(pco2Text.getText()) > 150){
			setErrorMessage("The maximum permissible value of pCO2 is 150 (pretty high!).");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}else {
			setErrorMessage(null);
		}
	}

	private void checkPo2Range() {
		if(po2Text.getText().length() == 0){
			setErrorMessage("Enter value for arterial pO2.");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (Double.valueOf(po2Text.getText()) < 25){
			setErrorMessage("The minimum permissible value of pO2 is 25 (pretty low!).");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else if (Double.valueOf(po2Text.getText()) > 600){
			setErrorMessage("The maximum permissible value of pO2 is 600 (pretty high!).");
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}else {
			setErrorMessage(null);
		}
	}
}
