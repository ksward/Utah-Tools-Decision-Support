package edu.utah.cdmcc.wizards;

import java.util.GregorianCalendar;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.ibm.icu.util.Calendar;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.field.validators.Verify;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;
import edu.utah.cdmcc.preferences.StudyPreferenceConstants;

public class PatientDemographicComposite extends
		org.eclipse.swt.widgets.Composite {
	private Label label1;
	private Composite composite1;
	private Label label2;
	private Text firstNameText;
	private Composite composite2;
	private Text medrecnumText;
	private Label label9;
	private DateTime birthdateWidget;
	private Label label8;
	private Text lastNameText;
	private Label label7;
	private Label label5;
	private Label label4;
	private Composite composite4;
	private Composite composite3;
	private Label label3;
	private Composite composite;
	private Text studyIDText;
	private Label lblEnterTheWeight;
	private Composite composite_1;
	private Label lblWeightkg;
	private Text weightText;
	private Label lblEnterTheHeight;
	private Composite composite_2;
	private Label lblHeightcm;
	private Text heightText;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args) {
		showGUI();
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		PatientDemographicComposite inst = new PatientDemographicComposite(
				shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if (size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public PatientDemographicComposite(
			org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
		firstNameText.setFocus();
	}

	private void initGUI() {
		try {
			this.setSize(473, 393);
			setLayout(new GridLayout(1, false));

			Label lblStudyIdNumber = new Label(this, SWT.NONE);
			lblStudyIdNumber.setText("Study ID Number (automatic)");
			{
				composite = new Composite(this, SWT.NONE);
				composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 1, 1));
				composite.setLayout(new GridLayout(3, true));

				Label lblStudyIdNumber_1 = new Label(composite, SWT.RIGHT);
				lblStudyIdNumber_1.setLayoutData(new GridData(SWT.RIGHT,
						SWT.CENTER, false, false, 1, 1));
				lblStudyIdNumber_1.setAlignment(SWT.RIGHT);
				lblStudyIdNumber_1.setText("Study ID number:");

				studyIDText = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
				studyIDText.setEnabled(false);
				studyIDText
						.setToolTipText("Study ID number (assigned automatically)");
				studyIDText.setEditable(false);
				studyIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 2, 1));
				populateStudyID();
			}
			{
				label1 = new Label(this, SWT.NONE);
				label1.setText("Enter the first name of the patient:");
			}
			{
				composite1 = new Composite(this, SWT.NONE);
				composite1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 1, 1));
				GridLayout composite1Layout = new GridLayout();
				composite1Layout.makeColumnsEqualWidth = true;
				composite1Layout.numColumns = 3;
				composite1.setLayout(composite1Layout);
				{
					label3 = new Label(composite1, SWT.NONE);
					GridData label3LData = new GridData();
					label3LData.horizontalAlignment = GridData.END;
					label3.setLayoutData(label3LData);
					label3.setText("First name:");
				}
				{
					GridData firstNameTextLData = new GridData();
					firstNameTextLData.grabExcessHorizontalSpace = true;
					firstNameTextLData.horizontalAlignment = GridData.FILL;
					firstNameTextLData.horizontalSpan = 2;
					firstNameText = new Text(composite1, SWT.BORDER);
					firstNameText.setToolTipText("First name of the patient");
					firstNameText.setLayoutData(firstNameTextLData);
					firstNameText.addVerifyListener(new VerifyListener() {
						public void verifyText(VerifyEvent evt) {
							Verify.verifyLettersText(evt);
						}
					});
				}
			}
			{
				label2 = new Label(this, SWT.NONE);
				label2.setText("Enter the last name of the patient:");
			}
			{
				composite2 = new Composite(this, SWT.NONE);
				composite2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 1, 1));
				GridLayout composite2Layout = new GridLayout();
				composite2Layout.numColumns = 3;
				composite2Layout.makeColumnsEqualWidth = true;
				composite2.setLayout(composite2Layout);
				{
					label7 = new Label(composite2, SWT.NONE);
					GridData label7LData = new GridData();
					label7LData.horizontalAlignment = GridData.END;
					label7.setLayoutData(label7LData);
					label7.setText("Last name:");
				}
				{
					GridData lastNameTextLData = new GridData();
					lastNameTextLData.horizontalAlignment = GridData.FILL;
					lastNameTextLData.grabExcessHorizontalSpace = true;
					lastNameTextLData.horizontalSpan = 2;
					lastNameText = new Text(composite2, SWT.BORDER);
					lastNameText.setToolTipText("Last name of the patient");
					lastNameText.setLayoutData(lastNameTextLData);
					lastNameText.addVerifyListener(new VerifyListener() {
						public void verifyText(VerifyEvent evt) {
							Verify.verifyLettersText(evt);
						}
					});
				}
			}
			{
				label4 = new Label(this, SWT.NONE);
				label4.setText("Enter the date of birth of the patient (MM/DD/YYYY):");
			}
			{
				composite3 = new Composite(this, SWT.NONE);
				composite3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 1, 1));
				GridLayout composite3Layout = new GridLayout();
				composite3Layout.numColumns = 3;
				composite3Layout.makeColumnsEqualWidth = true;
				composite3.setLayout(composite3Layout);
				{
					label8 = new Label(composite3, SWT.NONE);
					GridData label8LData = new GridData();
					label8LData.horizontalAlignment = GridData.END;
					label8.setLayoutData(label8LData);
					label8.setText("Date of birth:");
				}
				{
					GridData birthdateWidgetLData = new GridData();
					birthdateWidgetLData.grabExcessHorizontalSpace = true;
					birthdateWidgetLData.horizontalAlignment = GridData.FILL;
					birthdateWidgetLData.horizontalSpan = 2;
					birthdateWidget = new DateTime(composite3, SWT.DATE
							| SWT.BORDER);
					birthdateWidget.setToolTipText("Birthdate of patient");
					birthdateWidget.setLayoutData(birthdateWidgetLData);
				}
			}
			{
				label5 = new Label(this, SWT.NONE);
				label5.setText("Enter the medical record number of the patient:");
			}
			{
				composite4 = new Composite(this, SWT.NONE);
				composite4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, false, 1, 1));
				GridLayout composite4Layout = new GridLayout();
				composite4Layout.numColumns = 3;
				composite4Layout.makeColumnsEqualWidth = true;
				composite4.setLayout(composite4Layout);
				{
					label9 = new Label(composite4, SWT.NONE);
					GridData label9LData = new GridData();
					label9LData.horizontalAlignment = GridData.END;
					label9.setLayoutData(label9LData);
					label9.setText("Medical record number:");
				}
				{
					GridData medrecnumTextLData = new GridData();
					medrecnumTextLData.grabExcessHorizontalSpace = true;
					medrecnumTextLData.horizontalAlignment = GridData.FILL;
					medrecnumTextLData.horizontalSpan = 2;
					medrecnumText = new Text(composite4, SWT.BORDER);
					medrecnumText
							.setToolTipText("Medical record number of patient");
					medrecnumText.setLayoutData(medrecnumTextLData);
					{
						lblEnterTheWeight = new Label(this, SWT.NONE);
						lblEnterTheWeight.setLayoutData(new GridData(SWT.LEFT,
								SWT.TOP, false, false, 1, 1));
						lblEnterTheWeight
								.setText("Enter the weight of the patient (kilograms):");
					}
					{
						composite_1 = new Composite(this, SWT.NONE);
						composite_1.setLayoutData(new GridData(SWT.FILL,
								SWT.CENTER, true, false, 1, 1));
						composite_1.setLayout(new GridLayout(3, true));
						{
							lblWeightkg = new Label(composite_1, SWT.NONE);
							lblWeightkg.setLayoutData(new GridData(SWT.RIGHT,
									SWT.CENTER, false, false, 1, 1));
							lblWeightkg.setText("Weight (kg)::");
						}
						{
							weightText = new Text(composite_1, SWT.BORDER);
							weightText
									.setToolTipText("Weight of the patient in kilograms");
							weightText.setLayoutData(new GridData(SWT.FILL,
									SWT.CENTER, true, false, 2, 1));
							weightText.addVerifyListener(new VerifyListener() {
								public void verifyText(VerifyEvent e) {
									Verify.verifyFloatText(e);
								}
							});
						}
					}
					{
						lblEnterTheHeight = new Label(this, SWT.NONE);
						lblEnterTheHeight
								.setText("Enter the height of the patient (cm):");
					}
					{
						composite_2 = new Composite(this, SWT.NONE);
						composite_2.setLayoutData(new GridData(SWT.FILL,
								SWT.CENTER, false, false, 1, 1));
						composite_2.setLayout(new GridLayout(3, true));
						{
							lblHeightcm = new Label(composite_2, SWT.NONE);
							lblHeightcm.setLayoutData(new GridData(SWT.RIGHT,
									SWT.CENTER, false, false, 1, 1));
							lblHeightcm.setText("Height (cm)::");
						}
						{
							heightText = new Text(composite_2, SWT.BORDER);
							heightText
									.setToolTipText("Weight of the patient in kilograms");
							heightText.setLayoutData(new GridData(SWT.FILL,
									SWT.CENTER, true, false, 2, 1));
							heightText.addVerifyListener(new VerifyListener() {
								public void verifyText(VerifyEvent e) {
									Verify.verifyFloatText(e);
								}
							});
						}
					}
					medrecnumText.addVerifyListener(new VerifyListener() {
						public void verifyText(VerifyEvent evt) {
							String string = evt.text;
							char[] chars = new char[string.length()];
							string.getChars(0, chars.length, chars, 0);
							for (int i = 0; i < chars.length;) {
								if (!(Character.isDigit(chars[i]))
										&& !(chars[i] == '-'))
									evt.doit = false;
								return;
							}
						}
					});
				}
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void populateStudyID() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String study = store.getString(StudyPreferenceConstants.STUDY_PREFIX);
		String site = store.getString(StudyPreferenceConstants.SITE_PREFIX);
		String number = store
				.getString(StudyPreferenceConstants.STUDY_LAST_NUMBER);
		while (number.length() < 4) {
			number = "0" + number;
		}
		Integer temp;
		String newNumber;
		while (studyIDNumberAlreadyExists(study + site + number)) {
			temp = Integer.valueOf(number);
			temp++;
			newNumber = temp.toString();
			while (newNumber.length() < 4) {
				newNumber = "0" + newNumber;
			}
			number = newNumber;
		}
		setStudyIDText(study + site + number);
		store.setValue(StudyPreferenceConstants.STUDY_LAST_NUMBER, number);
	}

	public boolean studyIDNumberAlreadyExists(final String studyIDNumber) {
		Boolean alreadyExists = false;
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
				.getPatientDAO();
		patientDAO.getSession().beginTransaction();
		alreadyExists = patientDAO.studyIDAlreadyExists(studyIDNumber);
		patientDAO.getSession().getTransaction().commit();
		return alreadyExists;
	}

	public DateTime getBirthdateWidget() {
		return birthdateWidget;
	}

	public Text getFirstNameText() {
		return firstNameText;
	}

	public Text getLastNameText() {
		return lastNameText;
	}

	public Text getMedrecnumText() {
		return medrecnumText;
	}

	public void setBirthdateWidget(final GregorianCalendar birthDate) {
		GregorianCalendar dateCopy = birthDate;
		this.birthdateWidget.setDate(dateCopy.get(Calendar.YEAR),
				dateCopy.get(Calendar.MONTH),
				dateCopy.get(Calendar.DAY_OF_MONTH));
	}

	public void setFirstNameText(String value) {
		this.firstNameText.setText(value);
	}

	public void setLastNameText(String value) {
		this.lastNameText.setText(value);
	}

	public void setMedrecnumText(String value) {
		this.medrecnumText.setText(value);
	}

	public Text getStudyIDText() {
		return studyIDText;
	}

	public void setStudyIDText(String value) {
		this.studyIDText.setText(value);
	}

	public Text getWeightText() {
		return this.weightText;
	}

	public Text getHeightText() {
		return heightText;
	}

}
