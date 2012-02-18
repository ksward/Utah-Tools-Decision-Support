package hypertonic.decision.object;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import core.decision.object.DomainFieldsCompositeAdded;
import core.field.validators.Verify;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.custom.CLabel;

public class HypertonicSalineFieldsAddedToDomainComposite extends DomainFieldsCompositeAdded {
	private DateTime obsTimeWidget;
	private Label observationTimeLabel;
	private DateTime obsDateWidget;
	private Label observationDateLabel;
	private Button refreshDataButton;
	private CLabel currentSerumSodiumText;
	private Text currentHypertonicSalineDripRateText;
	private CLabel serumOsmText;
	private Text ICPText;
	private Text MAPText;
	private Text CVPText;
	
	private Label currentSerumSodiumLabel;
	private Label currentHypertonicSalineDripRateLabel;
	private Label serumOsmLabel;
	private Label ICPLabel;
	private Label MAPLabel;
	private Label CVPLabel;
	private Label sodiumDateTimeText;
	private Label osmolalityDateTimeText;
	private Button addSodiumButton;
	private Button addOsmolalityButton;

	

	public HypertonicSalineFieldsAddedToDomainComposite(final Composite parent, final int style) {
		super(parent, style);
		GridLayout gridLayout = (GridLayout) domainFieldsComposite.getLayout();
		gridLayout.makeColumnsEqualWidth = false;
		createDateTimeRefreshButton();
		createObservationDateLabel();
		createObsDateWidget();
		createObservationTimeLabel();
		createObsTimeWidget();
		createSalineWidgets();
	}

	public Button getRefreshDataButton() {
		return refreshDataButton;
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
				currentHypertonicSalineDripRateText.setText (EMPTY_STRING);
				ICPText.setText(EMPTY_STRING);
				MAPText.setText(EMPTY_STRING);
				CVPText.setText(EMPTY_STRING);
				GregorianCalendar temp = new GregorianCalendar();
				obsDateWidget.setDate(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH), temp.get(Calendar.DAY_OF_MONTH));
				obsTimeWidget.setTime(temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.MINUTE), 0);
				resetFocus();
				enableCorrectButtonCombination();
			}
		});		
	}
	
	public void setRefreshDataButton(Button refreshDataButton) {
		this.refreshDataButton = refreshDataButton;
	}
	
	private void createObservationDateLabel() {
		observationDateLabel = new Label(domainFieldsComposite, SWT.NONE);
		GridData gd_observationDateLabel = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gd_observationDateLabel.widthHint = 104;
		observationDateLabel.setLayoutData(gd_observationDateLabel);
		observationDateLabel.setText("Observation Date:");
	}

	private void createObsDateWidget() {
		obsDateWidget = new DateTime(domainFieldsComposite, SWT.NONE);
		obsDateWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		obsDateWidget
				.setToolTipText("Enter the date of the decision.");
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
		observationTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		observationTimeLabel.setText("Time (hh:mm):");
	}

	private void createObsTimeWidget() {
		obsTimeWidget = new DateTime(domainFieldsComposite, SWT.BORDER | SWT.TIME | SWT.SHORT);
		obsTimeWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		obsTimeWidget
				.setToolTipText("Enter the time of the decision.");
		addListenersObsTimeWidget();
	}
	
	private void createSalineWidgets(){
		
		addSodiumButton = new Button(domainFieldsComposite, SWT.NONE);
		addSodiumButton.setToolTipText("Click button to add a laboratory value for serum sodium.");
		addSodiumButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		addSodiumButton.setText("New Sodium");
		currentSerumSodiumLabel = new Label(domainFieldsComposite, SWT.NONE);
		currentSerumSodiumLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		currentSerumSodiumLabel.setText("Serum Sodium");
		
		currentSerumSodiumText = new CLabel(domainFieldsComposite, SWT.BORDER | SWT.CENTER);
		currentSerumSodiumText.setAlignment(SWT.CENTER);
		currentSerumSodiumText.setToolTipText("To add a new sodium, press the New Value button.");
		currentSerumSodiumText.setText("(Not available)");
		GridData gd_currentSerumSodiumText = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_currentSerumSodiumText.widthHint = 110;
		currentSerumSodiumText.setLayoutData(gd_currentSerumSodiumText);
		
		addOsmolalityButton = new Button(domainFieldsComposite, SWT.NONE);
		addOsmolalityButton.setToolTipText("Click button to add a laboratory value for serum osmolality.");
		addOsmolalityButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		addOsmolalityButton.setText("New Osmolality");
		
		serumOsmLabel  = new Label(domainFieldsComposite, SWT.NONE);
		serumOsmLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		serumOsmLabel.setText("Serum Osmolality");
		
		serumOsmText = new CLabel(domainFieldsComposite, SWT.BORDER | SWT.SHADOW_NONE | SWT.CENTER);
		serumOsmText.setAlignment(SWT.CENTER);
		serumOsmText.setToolTipText("To add a new osmolality, press the New Value button.");
		serumOsmText.setText("(Not available)");
		GridData gd_serumOsmText = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_serumOsmText.widthHint = 110;
		serumOsmText.setLayoutData(gd_serumOsmText);
		new Label(domainFieldsComposite, SWT.NONE);
		new Label(domainFieldsComposite, SWT.NONE);
		// The selection listener for the sodium button is added in the editor that uses this
		// composite instead of here.  This is because the listener needs to take actions that
		// are not visible to this package.
		
		sodiumDateTimeText = new Label(domainFieldsComposite, SWT.BORDER);
		sodiumDateTimeText.setAlignment(SWT.CENTER);
		sodiumDateTimeText.setToolTipText("Date and time of most recent serum sodium value from laboratory.");
		sodiumDateTimeText.setFont(SWTResourceManager.getFont("Lucida Grande", 9, SWT.NORMAL));
		sodiumDateTimeText.setText("(Not available)");
		GridData gd_sodiumDateTimeText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_sodiumDateTimeText.widthHint = 75;
		sodiumDateTimeText.setLayoutData(gd_sodiumDateTimeText);
		new Label(domainFieldsComposite, SWT.NONE);
		new Label(domainFieldsComposite, SWT.NONE);
				// The selection listener for the osmolality button is added in the editor that uses this
				// composite instead of here.  This is because the listener needs to take actions that
				// are not visible to this package.
				
				osmolalityDateTimeText = new Label(domainFieldsComposite, SWT.BORDER);
				osmolalityDateTimeText.setAlignment(SWT.CENTER);
				osmolalityDateTimeText.setToolTipText("Date and time of most recent osmolality measurement from laboratory.");
				osmolalityDateTimeText.setFont(SWTResourceManager.getFont("Lucida Grande", 9, SWT.NORMAL));
				osmolalityDateTimeText.setText("(Not available)");
				GridData gd_osmolalityDateTimeText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
				gd_osmolalityDateTimeText.widthHint = 75;
				osmolalityDateTimeText.setLayoutData(gd_osmolalityDateTimeText);
		
		currentHypertonicSalineDripRateLabel = new Label(domainFieldsComposite, SWT.NONE);
		currentHypertonicSalineDripRateLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		currentHypertonicSalineDripRateLabel.setText("3% drip rate (ml/kg/hr)");
		
		currentHypertonicSalineDripRateText = new Text(domainFieldsComposite, SWT.BORDER);
		currentHypertonicSalineDripRateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		currentHypertonicSalineDripRateText.addModifyListener(new ModifyListener() {	
			public void modifyText(ModifyEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
		currentHypertonicSalineDripRateText.addVerifyListener(new VerifyListener() {			
			public void verifyText(VerifyEvent e) {
				Verify.verifyFloatText(e);		
			}
		});
		
						
						MAPLabel  = new Label(domainFieldsComposite, SWT.NONE);
						MAPLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
						MAPLabel.setText("MAP");
		
		MAPText = new Text(domainFieldsComposite, SWT.BORDER);
		GridData gd_MAPText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_MAPText.widthHint = 67;
		MAPText.setLayoutData(gd_MAPText);
		MAPText.addModifyListener(new ModifyListener() {		
			public void modifyText(ModifyEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
		MAPText.addVerifyListener(new VerifyListener() {			
			public void verifyText(VerifyEvent e) {
				Verify.verifyIntegerText(e);		
			}
		});
		new Label(domainFieldsComposite, SWT.NONE);
		new Label(domainFieldsComposite, SWT.NONE);
		
		ICPLabel = new Label(domainFieldsComposite, SWT.NONE);
		ICPLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		ICPLabel.setText("ICP");
		
		ICPText = new Text(domainFieldsComposite, SWT.BORDER);
		ICPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ICPText.addModifyListener(new ModifyListener() {		
			public void modifyText(ModifyEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
		ICPText.addVerifyListener(new VerifyListener() {			
			public void verifyText(VerifyEvent e) {
				Verify.verifyIntegerText(e);		
			}
		});
		
		CVPLabel = new Label(domainFieldsComposite, SWT.NONE);
		CVPLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		CVPLabel.setText("CVP");
		
		CVPText = new Text(domainFieldsComposite, SWT.BORDER);
		CVPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		CVPText.addModifyListener(new ModifyListener() {		
			public void modifyText(ModifyEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
		CVPText.addVerifyListener(new VerifyListener() {			
			public void verifyText(VerifyEvent e) {
				Verify.verifyIntegerText(e);		
			}
		});
		new Label(domainFieldsComposite, SWT.NONE);
		new Label(domainFieldsComposite, SWT.NONE);
		
	}

	public CLabel getCurrentSerumSodiumText() {
		return currentSerumSodiumText;
	}

	public Text getCurrentHypertonicSalineDripRateText() {
		return currentHypertonicSalineDripRateText;
	}

	public Text getICPText() {
		return ICPText;
	}

	public Text getMAPText() {
		return MAPText;
	}

	public Text getCVPText() {
		return CVPText;
	}

	public CLabel getSerumOsmText() {
		return serumOsmText;
	}

	public Label getSodiumDateTimeText() {
		return sodiumDateTimeText;
	}

	public Label getOsmolalityDateTimeText() {
		return osmolalityDateTimeText;
	}

	
	public Button getAddSodiumButton() {
		return addSodiumButton;
	}

	public Button getAddOsmolalityButton() {
		return addOsmolalityButton;
	}

	private void addListenersObsTimeWidget() {
		obsTimeWidget.addSelectionListener(new SelectionAdapter() {		
			public void widgetSelected(SelectionEvent e) {
				clearAdvice();
				enableCorrectButtonCombination();			
			}
		});
	}

	public final DateTime getObsDateWidget() {
		return obsDateWidget;
	}

	public final DateTime getObsTimeWidget() {
		return obsTimeWidget;
	}	
	
	public final boolean allRequiredFieldsFilledIn() {
		// Think this through carefully - you will be requiring an
		// entry for items that might not exist (CVP, for example).
		// May even need to make ICP and MAP optional here but then
		// refuse to do much that is intelligent in the rules.
		return			
		(currentSerumSodiumText.getText().length() > 0)
		&&(currentHypertonicSalineDripRateText.getText().length () > 0 )
		&& (serumOsmText.getText().length() > 0)
		//&& (ICPText.getText().length() > 0)
		&& (MAPText.getText().length() > 0);
		//&& (CVPText.getText() .length() >0);  Will not always have this value!
		
	}

	public void resetFocus() {
		currentHypertonicSalineDripRateText.setFocus ();
	}
}

