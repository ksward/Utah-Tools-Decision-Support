package edu.utah.cdmcc.views;

import java.util.GregorianCalendar;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import core.field.validators.Verify;
import core.laboratory.object.SingleValueLaboratoryObject;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class GetExperimentalLaboratoryResult extends TitleAreaDialog {

	private SingleValueLaboratoryObject labResult;
	private Integer maxRangeInteger;
	private Integer minRangeInteger;
	private Double maxRangeDouble;
	private Double minRangeDouble;
	private Text valueText;
	private DateTime timeSampleCollected;
	private DateTime dateSampleCollected;
		
	/**
	 * Create the dialog.
	 */
	public GetExperimentalLaboratoryResult(Shell parentShell, 
			SingleValueLaboratoryObject labResult,
			Object minRange, 
			Object maxRange) {
		super(parentShell);
		this.labResult = labResult;
		if (maxRange instanceof Integer && minRange instanceof Integer){
			maxRangeInteger = (Integer) maxRange;
			minRangeInteger = (Integer) minRange;
		} else if (maxRange instanceof Double && minRange instanceof Double){
			maxRangeDouble = (Double) maxRange;
			minRangeDouble = (Double) minRange;
		} else {
			Throwable cause = new Throwable("Error in laboratory range parameters");
			throw new IllegalArgumentException(cause);
		}
	}
	
	@Override
	public void create(){
		super.create();
		setMessage("Enter the date, time, and value for " + labResult.getLabelName() + ".");
		setTitle("Laboratory Result (" + labResult.getLabelName() + ")");		
	}
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		timeSampleCollected = new DateTime(container, SWT.BORDER | SWT.TIME | SWT.SHORT);
		timeSampleCollected.setBounds(92, 53, 88, 22);
		
		dateSampleCollected = new DateTime(container, SWT.BORDER);
		dateSampleCollected.setToolTipText("Date the sample was obtained");
		dateSampleCollected.setBounds(92, 23, 88, 22);
		
		Label lblDate = new Label(container, SWT.NONE);
		lblDate.setAlignment(SWT.RIGHT);
		lblDate.setBounds(10, 23, 59, 14);
		lblDate.setText("Date");
		
		Label lblTime = new Label(container, SWT.NONE);
		lblTime.setAlignment(SWT.RIGHT);
		lblTime.setBounds(10, 53, 59, 14);
		lblTime.setText("Time");
		
		Label lblValue = new Label(container, SWT.NONE);
		lblValue.setAlignment(SWT.RIGHT);
		lblValue.setBounds(10, 86, 59, 14);
		lblValue.setText("Value");
		
		valueText = new Text(container, SWT.BORDER);
	
		if (maxRangeInteger != null) {
			valueText.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent evt) {
					Verify.verifyIntegerText(evt);
				}
			});
			valueText.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (valueText.getText().length() == 0){
						setErrorMessage("Enter value for "+ labResult.getLabelName() + ".");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					}else if (Integer.valueOf(valueText.getText())<minRangeInteger)
					{
						setErrorMessage("The minimum permissible value is "+ minRangeInteger.toString()+".");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					}else if (Integer.valueOf(valueText.getText())>maxRangeInteger){
						setErrorMessage("The maximum permissible value is "+ maxRangeInteger.toString()+".");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					} else {
						setErrorMessage(null);
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				}
			});
		}
		if (maxRangeDouble != null) {
			valueText.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (valueText.getText().length() == 0 || valueText.getText()=="."){
						setErrorMessage("Enter value for "+ labResult.getLabelName() + ".");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					}else if (Double.valueOf(valueText.getText())<minRangeDouble)
					{
						setErrorMessage("The minimum permissible value is "+ minRangeDouble.toString()+".");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					}else if (Double.valueOf(valueText.getText())>maxRangeDouble){
						setErrorMessage("The maximum permissible value is "+ maxRangeDouble.toString()+".");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					} else {
						setErrorMessage(null);
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				}
			});
			valueText.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent evt) {
					Verify.verifyFloatText(evt);
				}
			});
		}
		
		valueText.setBounds(92, 81, 88, 19);
		
		Label label_3 = new Label(container, SWT.NONE);
		label_3.setBounds(186, 86, 59, 14);
		label_3.setText(labResult.getConventionalUnits());
		container.setTabList(new Control[]{valueText, dateSampleCollected, timeSampleCollected});

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
		return new Point(382, 257);
	}
	
	@Override
	protected void okPressed() {
		labResult.setConventionalTextResult(valueText.getText());
		labResult.setTimeOfSpecimenCollection(calculateDate());
		if (ApplicationControllers.getUserController().getCurrentUser() != null) {
			labResult.setAccountName(ApplicationControllers.getUserController().getCurrentUser().getAccountName());
		}
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	};

	private GregorianCalendar calculateDate() {
		GregorianCalendar dateTime = new GregorianCalendar(dateSampleCollected.getYear(),
				dateSampleCollected.getMonth(),
				dateSampleCollected.getDay(),
				timeSampleCollected.getHours(),
				timeSampleCollected.getMinutes(),
				timeSampleCollected.getSeconds());
		
		return dateTime;
	}
	
	public Integer getMaxRangeInteger() {
		return maxRangeInteger;
	}

	public Integer getMinRangeInteger() {
		return minRangeInteger;
	}

	public Double getMaxRangeDouble() {
		return maxRangeDouble;
	}

	public Double getMinRangeDouble() {
		return minRangeDouble;
	}
	
}
