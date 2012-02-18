package glucose.decision.object;

import glucose.preferences.GlucosePreferenceConstants;

import java.util.Calendar;
import java.util.GregorianCalendar;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import activator.Activator;

import core.decision.object.DomainFieldsCompositeAdded;
import core.field.validators.Verify;

public class GlucoseFieldsAddedToDomainComposite extends DomainFieldsCompositeAdded {
	private static final String[] CARBOHYDRATE_STATUS = { "",
		"No decrease",
		"Decreased or off",
		"Increased or started" };

	private Combo carbohydrateStatusCombo;
	private Text currentGlucoseText;
	private Label carbohydrateComboLabel;
	private DateTime obsTimeWidget;
	private Label observationTimeLabel;
	private DateTime obsDateWidget;
	private Label observationDateLabel;
	private Text currentInsulinText;
	private Label createCurrentInsulinLabel;
	private Label currentGlucoseLabel;

	private Button refreshDataButton;
	private IPreferenceStore preferences;


	public GlucoseFieldsAddedToDomainComposite(final Composite parent, final int style) {
		super(parent, style);
		GridLayout gridLayout = (GridLayout) domainFieldsComposite.getLayout();
		gridLayout.makeColumnsEqualWidth = false;
		createDateTimeRefreshButton();
		createObservationDateLabel();
		createObsDateWidget();
		createObservationTimeLabel();
		createObsTimeWidget();
		//createDateTimeRefreshButton();
		createCurrentGlucoseLabel();
		createCurrentGlucoseText();
		createCurrentInsulinLabel();
		createCurrentInsulinText();
		createCarbohydrateComboLabel();
		createCarbohydrateStatusCombo();


	}



	public Button getRefreshDataButton() {
		return refreshDataButton;
	}



	public void setRefreshDataButton(Button refreshDataButton) {
		this.refreshDataButton = refreshDataButton;
	}



	private void createCurrentGlucoseLabel() {
		currentGlucoseLabel = new Label(domainFieldsComposite, SWT.NONE);
		currentGlucoseLabel.setAlignment(SWT.RIGHT);
		GridData currentGlucoseLabelLData = new GridData();
		currentGlucoseLabelLData.widthHint = 102;
		currentGlucoseLabelLData.grabExcessHorizontalSpace = true;
		currentGlucoseLabelLData.horizontalAlignment = GridData.END;
		currentGlucoseLabel.setLayoutData(currentGlucoseLabelLData);
		currentGlucoseLabel.setText("Serum glucose:");
	}

	private void createCurrentGlucoseText() {
		currentGlucoseText = new Text(domainFieldsComposite, SWT.NONE);
		GridData gd_currentGlucoseText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_currentGlucoseText.widthHint = 67;
		currentGlucoseText.setLayoutData(gd_currentGlucoseText);
		currentGlucoseText
				.setToolTipText("Enter the serum glucose value (mg/dL)");
		currentGlucoseText.setTextLimit(6);
		addListenersCurrentGlucoseText();
	}

	private void addListenersCurrentGlucoseText() {
		currentGlucoseText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent evt) {
				clearAdvice();
				enableCorrectButtonCombination();
			}
		});
		currentGlucoseText.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent evt) {
				Verify.verifyIntegerText(evt);
			}
		});
	}

	private void createCurrentInsulinLabel() {
		createCurrentInsulinLabel = new Label(domainFieldsComposite, SWT.NONE);
		createCurrentInsulinLabel.setAlignment(SWT.CENTER);
		GridData currentInsulinLabelLData = new GridData();
		currentInsulinLabelLData.widthHint = 100;
		currentInsulinLabelLData.grabExcessHorizontalSpace = true;
		currentInsulinLabelLData.horizontalAlignment = SWT.RIGHT;
		createCurrentInsulinLabel.setLayoutData(currentInsulinLabelLData);
		preferences = Activator.getDefault().getPreferenceStore();
			createCurrentInsulinLabel.setText("Insulin (U/hr):");
		if (preferences.getString(GlucosePreferenceConstants.INSULIN_UNITS).equals("pediatric")){
			createCurrentInsulinLabel.setText("Insulin (U/kg/hr):");
			}

	}

	private void createCurrentInsulinText() {
		currentInsulinText = new Text(domainFieldsComposite, SWT.NONE);
		GridData currentInsulinTextLData = setupCurrentInsulinTextLData();
		currentInsulinText.setLayoutData(currentInsulinTextLData);
		preferences = Activator.getDefault().getPreferenceStore();
		if (preferences.getString(GlucosePreferenceConstants.INSULIN_UNITS).equals("pediatric")){
			currentInsulinText
			.setToolTipText("Enter the insulin drip rate in Units/kg/hr (usual for pediatric ICU)");
			} else {
		currentInsulinText
				.setToolTipText("Enter the insulin drip rate in total Units/hr (usual for adult ICU)");
			}
		currentInsulinText.setTextLimit(6);
		addListenersCurrentInsulinText();
	}

	private GridData setupCurrentInsulinTextLData() {
		GridData currentInsulinTextLData = new GridData();
		currentInsulinTextLData.widthHint = 60;
		currentInsulinTextLData.grabExcessHorizontalSpace = true;
		currentInsulinTextLData.horizontalAlignment = GridData.FILL;
		return currentInsulinTextLData;
	}

	private void addListenersCurrentInsulinText() {
		currentInsulinText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent evt) {
				clearAdvice();
				enableCorrectButtonCombination();
			}
		});
		currentInsulinText.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent evt) {
				Verify.verifyFloatText(evt);
			}
		});
	}

	private void createCarbohydrateComboLabel() {
		carbohydrateComboLabel = new Label(domainFieldsComposite, SWT.NONE);
		GridData carbohydrateComboLData = new GridData();
		carbohydrateComboLData.grabExcessHorizontalSpace = true;
		carbohydrateComboLData.widthHint = 112;
		carbohydrateComboLData.heightHint = 15;
		carbohydrateComboLData.horizontalAlignment = GridData.END;
		carbohydrateComboLabel.setLayoutData(carbohydrateComboLData);
		carbohydrateComboLabel.setText("Carbohydrates?");
		carbohydrateComboLabel.setAlignment(SWT.RIGHT);
	}

	private void createCarbohydrateStatusCombo() {
		carbohydrateStatusCombo = new Combo(domainFieldsComposite, SWT.V_SCROLL);
		carbohydrateStatusCombo.setItems(CARBOHYDRATE_STATUS);
		carbohydrateStatusCombo.select(0);
		GridData carbohydrateButtonLData = new GridData();
		carbohydrateButtonLData.grabExcessHorizontalSpace = true;
		carbohydrateStatusCombo.setLayoutData(carbohydrateButtonLData);
		carbohydrateStatusCombo.setToolTipText("Indicate changes in carbohydrate status in past hour:");
		addListenersCarbohydrateStatusCombo();
	}

	private void addListenersCarbohydrateStatusCombo() {
		carbohydrateStatusCombo.addSelectionListener(new SelectionAdapter() {		
			public void widgetSelected(SelectionEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();
			}			
		});
		
	}

	private void createObservationDateLabel() {
		observationDateLabel = new Label(domainFieldsComposite, SWT.NONE);
		GridData gd_observationDateLabel = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gd_observationDateLabel.widthHint = 101;
		observationDateLabel.setLayoutData(gd_observationDateLabel);
		observationDateLabel.setText("Observation Date:");
	}

	private void createObsDateWidget() {
		obsDateWidget = new DateTime(domainFieldsComposite, SWT.NONE);
		obsDateWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		obsDateWidget
				.setToolTipText("Enter the date of the serum glucose measurement (mm/dd/yyyy);  "
						+ "this is overriden by the checkbox option to the right.");
		addListenersObsDateWidget();
	}

	private void addListenersObsDateWidget() {
		obsDateWidget.addSelectionListener(new SelectionAdapter() {		
			public void widgetSelected(SelectionEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
	}

	private void createObservationTimeLabel() {
		observationTimeLabel = new Label(domainFieldsComposite, SWT.NONE);
		GridData gd_observationTimeLabel = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gd_observationTimeLabel.widthHint = 90;
		observationTimeLabel.setLayoutData(gd_observationTimeLabel);
		observationTimeLabel.setText("Time (hh:mm):");
	}

	private void createObsTimeWidget() {
		obsTimeWidget = new DateTime(domainFieldsComposite, SWT.BORDER | SWT.TIME | SWT.SHORT);
		obsTimeWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		obsTimeWidget
				.setToolTipText("Enter the time of the serum glucose measurement (hh:mm);  "
						+ "for example, 01:23 or 11:34.  Hours must have TWO digits.");
		addListenersObsTimeWidget();
	}

	private void addListenersObsTimeWidget() {
		obsTimeWidget.addSelectionListener(new SelectionAdapter() {		
			public void widgetSelected(SelectionEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
	}
	
	private void createDateTimeRefreshButton() {
		refreshDataButton = new Button(domainFieldsComposite, SWT.CENTER);
		GridData gd_refreshDataButton = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		gd_refreshDataButton.horizontalSpan = 2;
		refreshDataButton.setLayoutData(gd_refreshDataButton);
		refreshDataButton.setText("Refresh Decision Data");
		refreshDataButton.setToolTipText("Click this button to refresh information for a new decision.");
		addListenersRefreshDataButton();
	}

	private void addListenersRefreshDataButton() {
		refreshDataButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				clearAdvice();
				currentInsulinText.setText(EMPTY_STRING);
				currentGlucoseText.setText(EMPTY_STRING);
				carbohydrateStatusCombo.select(0);
				GregorianCalendar temp = new GregorianCalendar();
				obsDateWidget.setDate(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH), temp.get(Calendar.DAY_OF_MONTH));
				obsTimeWidget.setTime(temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.MINUTE), 0);
				currentGlucoseText.setFocus();
				enableCorrectButtonCombination();
			}
		});		
	}



	public final Combo getCarbohydrateStatusCombo() {
		return carbohydrateStatusCombo;
	}

	public final Text getCurrentGlucoseText() {
		return currentGlucoseText;
	}

	public final Text getCurrentInsulinText() {
		return currentInsulinText;
	}

	public final DateTime getObsDateWidget() {
		return obsDateWidget;
	}

	public final DateTime getObsTimeWidget() {
		return obsTimeWidget;
	}

	private boolean checkDateAgainstBirth() {
		// TODO Create this routine and check all dates against the
		// patient birthdate to make sure everything happens after
		// patient is born.
		return true;
	}

	public final boolean allRequiredFieldsFilledIn() {
		return 
				
				(currentGlucoseText.getText().length() > 0)
				&& (currentInsulinText.getText().length() > 0)
				&& (checkDateAgainstBirth() == true)
				&& (getCarbohydrateStatusCombo().getSelectionIndex() > 0);
	}
}
