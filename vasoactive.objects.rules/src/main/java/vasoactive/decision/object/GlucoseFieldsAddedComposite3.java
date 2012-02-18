package vasoactive.decision.object;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import core.decision.object.DecisionFieldsAddedComposite;

public class GlucoseFieldsAddedComposite3 extends DecisionFieldsAddedComposite {

	private Text currentGlucoseText;
	private Text obsTimeText;
	private Label observationTimeLabel;
	private Text obsDateText;
	private Label observationDateLabel;
	private Text currentInsulinText;
	private Label createCurrentInsulinLabel;
	private Label currentGlucoseLabel;
	private Composite glucoseComposite;
	private Label lblSbp;
	private Text systolicBloodPressureText;

	public GlucoseFieldsAddedComposite3(final Composite parent, final int style) {
		super(parent, style);
		glucoseComposite = new Composite(this, SWT.BORDER);
		setupGlucoseGridLayout();
		createObservationDateLabel();
		createObsDateText();
		createObservationTimeLabel();
		createObservationTimeText();createCurrentGlucoseLabel();
		createCurrentGlucoseText();
		createCurrentInsulinLabel();
		createCurrentInsulinText();
		
		// my new field 
		{
			lblSbp = new Label(glucoseComposite, SWT.NONE);
			lblSbp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblSbp.setText("SBP:");
		}
		{
			systolicBloodPressureText = new Text(glucoseComposite, SWT.NONE);
			systolicBloodPressureText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			systolicBloodPressureText.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent evt) {
					clearAdvice();
					enableCorrectButtonCombination();
				}
			});
			systolicBloodPressureText.addVerifyListener(new VerifyListener() {
				public void verifyText(final VerifyEvent evt) {
					verifyIntegerText(evt);
				}
			});		
		
		}

	}

	private FormData setupGlucoseCompositeFormData() {
		FormData glucoseCompositeLData = new FormData();
		glucoseCompositeLData.top = new FormAttachment(getDeclineButton(), 0, SWT.TOP);
		glucoseCompositeLData.width = 719;
		glucoseCompositeLData.height = 75;
		glucoseCompositeLData.left = new FormAttachment(10, 1000, 0);
		glucoseCompositeLData.right = new FormAttachment(991, 1000, 0);
		glucoseCompositeLData.bottom = new FormAttachment(305, 1000, 0);
		return glucoseCompositeLData;
	}

	private void setupGlucoseGridLayout() {
		GridLayout glucoseCompositeLayout = new GridLayout();
		glucoseCompositeLayout.makeColumnsEqualWidth = true;
		glucoseCompositeLayout.numColumns = 6;
		FormData glucoseCompositeLData = setupGlucoseCompositeFormData();
		glucoseComposite.setLayoutData(glucoseCompositeLData);
		glucoseComposite.setLayout(glucoseCompositeLayout);
	}

	private void createCurrentGlucoseLabel() {
		// The following unused labels are used to shift fields in the GUI
		new Label(glucoseComposite, SWT.NONE);
		new Label(glucoseComposite, SWT.NONE);
		currentGlucoseLabel = new Label(glucoseComposite, SWT.NONE);
		GridData currentGlucoseLabelLData = new GridData();
		currentGlucoseLabelLData.grabExcessHorizontalSpace = true;
		currentGlucoseLabelLData.horizontalAlignment = GridData.END;
		currentGlucoseLabel.setLayoutData(currentGlucoseLabelLData);
		currentGlucoseLabel.setText("Serum glucose:");
	}

	private void createCurrentGlucoseText() {
		currentGlucoseText = new Text(glucoseComposite, SWT.NONE);
		currentGlucoseText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
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
				verifyIntegerText(evt);
			}
		});
	}

	private void createCurrentInsulinLabel() {
		createCurrentInsulinLabel = new Label(glucoseComposite, SWT.NONE);
		GridData currentInsulinLabelLData = new GridData();
		currentInsulinLabelLData.grabExcessHorizontalSpace = true;
		currentInsulinLabelLData.horizontalAlignment = GridData.END;
		createCurrentInsulinLabel.setLayoutData(currentInsulinLabelLData);
		createCurrentInsulinLabel.setText("Insulin (U/kg/hr):");
	}

	private void createCurrentInsulinText() {
		currentInsulinText = new Text(glucoseComposite, SWT.NONE);
		GridData currentInsulinTextLData = setupCurrentInsulinTextLData();
		currentInsulinText.setLayoutData(currentInsulinTextLData);
		currentInsulinText
				.setToolTipText("Enter the insulin drip rate in total Units/kg/hr (usual for pediatric ICU)");
		currentInsulinText.setTextLimit(6);
		new Label(glucoseComposite, SWT.NONE);
		new Label(glucoseComposite, SWT.NONE);
		new Label(glucoseComposite, SWT.NONE);
		new Label(glucoseComposite, SWT.NONE);
		new Label(glucoseComposite, SWT.NONE);
		new Label(glucoseComposite, SWT.NONE);
		addListenersCurrentInsulinText();
	}

	private GridData setupCurrentInsulinTextLData() {
		GridData currentInsulinTextLData = new GridData();
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
				verifyFloatText(evt);
			}
		});
	}

	private void createObservationDateLabel() {
		observationDateLabel = new Label(glucoseComposite, SWT.NONE);
		observationDateLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		observationDateLabel.setText("Observation Date:");
	}

	private void createObsDateText() {
		obsDateText = new Text(glucoseComposite, SWT.NONE);
		obsDateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		obsDateText
				.setToolTipText("Enter the date of the serum glucose measurement (mm/dd/yyyy);  "
						+ "this is overriden by the checkbox option to the right.");
		obsDateText.setTextLimit(10);
		addListenersObservationDateText();
	}

	private void addListenersObservationDateText() {
		obsDateText.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent evt) {
				verifyDateText(evt);
			}
		});
		obsDateText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent evt) {
				if (obsDateText.getText().trim().length() >= 9) {
					if (checkDateFormat()) {
						enableCorrectButtonCombination();
					}
				} else {
					turnOffAllButtons();
				}
			}
		});
	}

	private void createObservationTimeLabel() {
		observationTimeLabel = new Label(glucoseComposite, SWT.NONE);
		observationTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		observationTimeLabel.setText("Time (hh:mm):");
	}

	private void createObservationTimeText() {
		obsTimeText = new Text(glucoseComposite, SWT.NONE);
		obsTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		obsTimeText
				.setToolTipText("Enter the time of the serum glucose measurement (hh:mm);  "
						+ "for example, 01:23 or 11:34.  Hours must have TWO digits.");
		obsTimeText.setTextLimit(8);
		addListenersObservationTimeText();
	}

	private void addListenersObservationTimeText() {
		obsTimeText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent evt) {
				if (checkTimeFormat()) {
					enableCorrectButtonCombination();
				} else {
					if (obsTimeText.getText().trim().length() >= 5) {
						MessageDialog
								.openError(null, "Time Format Incorrect",
										"The time should be formatted hh:mm, or hh:mm AM or hh:mm PM");
						obsTimeText.selectAll();
						obsTimeText.setFocus();
					}
					turnOffAllButtons();
				}
			}
		});
		obsTimeText.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent evt) {
				verifyTimeText(evt);
			}
		});
	}

	public final Text getCurrentGlucoseText() {
		return currentGlucoseText;
	}

	public final Text getCurrentInsulinText() {
		return currentInsulinText;
	}
	
	public final Text getSystolicBloodPressureText() {
		return systolicBloodPressureText;
	}
	
	public final Text getObsDateText() {
		return obsDateText;
	}

	public final Text getObsTimeText() {
		return obsTimeText;
	}

	private void verifyFloatText(final VerifyEvent evt) {
		char[] chars = getEventChars(evt);
		for (int i = 0; i < chars.length;) {
			if (!(Character.isDigit(chars[i])) && !(chars[i] == '.')) {
				evt.doit = false;
			}
			return;
		}
	}

	private void verifyIntegerText(final VerifyEvent evt) {
		char[] chars = getEventChars(evt);
		for (int i = 0; i < chars.length;) {
			if (!(Character.isDigit(chars[i]))) {
				evt.doit = false;
			}
			return;
		}
	}

	private void verifyDateText(final VerifyEvent evt) {
		char[] chars = getEventChars(evt);
		for (int i = 0; i < chars.length;) {
			if (!(Character.isDigit(chars[i])) && !(chars[i] == '/')) {
				evt.doit = false;
			}
			return;
		}
	}

	private void verifyTimeText(final VerifyEvent evt) {
		char[] chars = getEventChars(evt);
		for (int i = 0; i < chars.length;) {
			if (!(Character.isDigit(chars[i])) && !(chars[i] == ':')
					&& !(Character.isSpaceChar(chars[i])) && !(chars[i] == 'm')
					&& !(chars[i] == 'M') && !(chars[i] == 'p')
					&& !(chars[i] == 'P') && !(chars[i] == 'a')
					&& !(chars[i] == 'A')) {
				evt.doit = false;
			}
			return;
		}
	}

	private char[] getEventChars(final VerifyEvent evt) {
		String string = evt.text;
		char[] chars = new char[string.length()];
		string.getChars(0, chars.length, chars, 0);
		return chars;
	}

	private boolean checkDateFormat() {
		if (obsDateText.getText().length() == 0) {
			return true;
		} else {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			df.setLenient(false);
			try {
				df.parse(obsDateText.getText());
				return true;
			} catch (ParseException e) {
				MessageDialog.openError(null, "Invalid Date Entry",
						"The observation date field (" + obsDateText.getText()
								+ ") is not a valid date.");
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						obsDateText.setFocus();
						obsDateText.selectAll();
						turnOffAllButtons();
					}
				});
				return false;
			}
		}
	}

	private boolean checkDateAgainstBirth() {
		// TODO Create this routine and check all dates against the
		// patient birthdate to make sure everything happens after
		// patient is born.
		return true;
	}

	public boolean checkTimeFormat() {
		String timeText = obsTimeText.getText();
		if (timeText.length() == 0) {
			return false;
		}
		if (timeText.indexOf(":") < 0) {
			return false;
		}
		Pattern priorToEightPM = Pattern
				.compile("[0-1]?[0-9]:[0-5]?[0-9][ ]?[pPaA]?[mM]?");
		Pattern afterEightPMBeforeMidnight = Pattern
				.compile("[2][0-3]:[0-5]?[0-9][ ]?[pP]?[mM]?");

		Matcher matchPriorToEightPM = priorToEightPM.matcher(obsTimeText
				.getText().trim());
		Matcher matchAfterEightPMBeforeMidnight = afterEightPMBeforeMidnight
				.matcher(obsTimeText.getText().trim());
		if (matchPriorToEightPM.matches()
				|| (matchAfterEightPMBeforeMidnight.matches())) {
			return true;
		}
		return false;
	}

	public final void enableCorrectButtonCombination() {
		if (allRequiredFieldsFilledIn()) {
			if (getDecisionFiredFlag()) {
				setAcceptDeclineOn();
			} else {
				setAcceptDeclineOff();
			}
		} else {
			turnOffAllButtons();
		}
	}

	public final boolean allRequiredFieldsFilledIn() {
		return (obsDateText.getText().length() > 0)
				&& (obsTimeText.getText().length() > 0)
				&& (currentGlucoseText.getText().length() > 0)
				&& (systolicBloodPressureText.getText().length() > 0)
				&& (currentInsulinText.getText().length() > 0)
				&& (checkTimeFormat() == true) && (checkDateFormat() == true)
				&& (checkDateAgainstBirth() == true);
	}

	public final void setAcceptDeclineOn() {
		getAcceptButton().setEnabled(true);
		getChartButton().setEnabled(false);
		getDecisionButton().setEnabled(false);
		getDeclineButton().setEnabled(true);
	}

	public final void setAcceptDeclineOff() {
		getAcceptButton().setEnabled(false);
		getChartButton().setEnabled(true);
		getDecisionButton().setEnabled(true);
		getDeclineButton().setEnabled(false);
	}
}
