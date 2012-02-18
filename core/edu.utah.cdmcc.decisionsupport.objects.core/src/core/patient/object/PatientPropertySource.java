package core.patient.object;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;


/**
 * 
 * PatientPropertySource is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Aug 4, 2006 8:29:04 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2006. All rights reserved.
 *         <P>
 *         Purpose of the class: Provides patient object to properties view.
 *         Keeps patient object clean of this code.
 *         <P>
 * 
 */
public class PatientPropertySource implements IPropertySource2 {

	private static final String PROPERTY_LAST_NAME = "patient.lastname";
	private static final String PROPERTY_GENDER = "patient.gender";
	private static final String PROPERTY_FIRST_NAME = "patient.firstname";
	private static final String PROPERTY_NAME = "patient.name";
	private static final String PROPERTY_TRIALDB = "patient.trialdb";
	private static final String PROPERTY_BIRTHDATE = "patient.birthdate";
	private static final String PROPERTY_WEIGHT = "patient.weight";
	private static final String PROPERTY_HEIGHT = "patient.height";
	private static final String PROPERTY_MED_REC_NUM = "patient.medrecnum";
	private IPropertyDescriptor[] propertyDescriptors;
	private Patient patient;
	

	/**
	 * Creates PropertySource for the Patient object. Called by the Patient
	 * object method getAdaptor().
	 */
	public PatientPropertySource(final Patient patient) {
		this.patient = patient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/**
	 * Provides property descriptors for each portion of the Patient object.
	 * 
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TextPropertyDescriptors are selectable even if not editable
		// so use PropertyDescriptors for properties that I do not want
		// the user to be able to pretend to edit.
		// As of April 2008 this is everything because I do not want
		// user to edit patients via the property view.
		
		if (propertyDescriptors == null) {
			PropertyDescriptor nameDescriptor = new PropertyDescriptor(
					PROPERTY_NAME, "Full name:");
			nameDescriptor.setCategory("1. Patient Name");
			nameDescriptor.setDescription("Full name of the patient");
			PropertyDescriptor firstDescriptor = new PropertyDescriptor(
					PROPERTY_FIRST_NAME, "First name:");
			firstDescriptor.setCategory("1. Patient Name");
			firstDescriptor.setDescription("First name of the patient");
			PropertyDescriptor lastDescriptor = new PropertyDescriptor(
					PROPERTY_LAST_NAME, "Last name:");
			lastDescriptor.setCategory("1. Patient Name");
			lastDescriptor.setDescription("Last name of the patient");
			PropertyDescriptor birthDescriptor = new PropertyDescriptor(
					PROPERTY_BIRTHDATE, "Birthdate:");
			birthDescriptor.setCategory("2. Parameters");
			birthDescriptor.setDescription("Date of birth of the patient");
			PropertyDescriptor mrnDescriptor = new PropertyDescriptor(
					PROPERTY_MED_REC_NUM, "MRN:");
			mrnDescriptor.setCategory("3. Identifiers");
			mrnDescriptor
					.setDescription("Medical record number of the patient");
			PropertyDescriptor trialDBDescriptor = new PropertyDescriptor(
					PROPERTY_TRIALDB, "TrialDB #:");
			trialDBDescriptor.setCategory("3. Identifiers");
			trialDBDescriptor.setDescription("TrialDB number of the patient");
			PropertyDescriptor heightDescriptor = new PropertyDescriptor(
					PROPERTY_HEIGHT, "Height (cm):");
			heightDescriptor.setCategory("2. Parameters");
			heightDescriptor
					.setDescription("Height of the patient in centimeters");
			PropertyDescriptor weightDescriptor = new PropertyDescriptor(
					PROPERTY_WEIGHT, "Weight (kg):");
			weightDescriptor.setCategory("2. Parameters");
			weightDescriptor
					.setDescription("Weight of the patient in kilograms");
			PropertyDescriptor genderDescriptor = new PropertyDescriptor(
					PROPERTY_GENDER, "Gender:");
			genderDescriptor.setCategory("2. Parameters");
			genderDescriptor.setDescription("Gender of the patient");
			propertyDescriptors = new IPropertyDescriptor[] { nameDescriptor,
					firstDescriptor, lastDescriptor, birthDescriptor,
					mrnDescriptor, trialDBDescriptor, heightDescriptor,
					weightDescriptor, genderDescriptor };
		}
		return propertyDescriptors;
	}

	/**
	 * Provides the value to display for each Patient property.
	 */
	public Object getPropertyValue(final Object id) {
		if (id.equals(PROPERTY_LAST_NAME)) {
			return patient.getLastName();
		}
		if (id.equals(PROPERTY_FIRST_NAME)) {
			return patient.getFirstName();
		}
		if (id.equals(PROPERTY_BIRTHDATE)) {
			return patient.getBirthdateString();
		}
		if (id.equals(PROPERTY_MED_REC_NUM)) {
			return patient.getMedRecNum();
		}
		if (id.equals(PROPERTY_TRIALDB)) {
			return patient.getStudyID();
		}
		if (id.equals(PROPERTY_HEIGHT)) {
			return patient.getHeight().toString();
		}
		if (id.equals(PROPERTY_WEIGHT)) {
			return patient.getWeight().toString();
		}
		if (id.equals(PROPERTY_NAME)) {
			return patient.getName();
		}
		return null;
	}

	/**
	 * Procedure to change certain properties.
	 * I revised this April 15 2008 so it is never
	 * called for editing.  This is to enable better
	 * decoupling as well as because it is a bad user
	 * interface to allow editing via the property view.
	 * 
	 */
	public void setPropertyValue(final Object id, final Object value) {

//		if (id.equals(PROPERTY_FIRST_NAME)) {
//			String st = (String) value;
//			if (st.length() > 2 && st.length() < 20) {
//				patient.setFirstName((String) value);
//			}
//		}
//		if (id.equals(PROPERTY_LAST_NAME)) {
//			String st = (String) value;
//			if (st.length() > 2 && st.length() < 20) {
//				patient.setLastName((String) value);
//			}
//		}
//		if (id.equals(PROPERTY_GENDER)) {
//			String st = (String) value;
//			if (st.charAt(0) == 'm' || st.charAt(0) == 'M') {
//				patient.setGender(Gender.MALE);
//			} else {
//				if (st.charAt(0) == 'f' || st.charAt(0) == 'F') {
//					patient.setGender(Gender.FEMALE);
//				}
//			}
//		}
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		try {
//			patientDAO.getSession().beginTransaction();
//			patientDAO.updatePatient(patient);
//			patientDAO.getSession().getTransaction().commit();
//			DecisionSupportActivator.getDefault().firePatientsChanged(patient);
//		} catch (HibernateException e) {
//			e.printStackTrace();
//			throw new UtahToolboxException(e);
//		}
	}

	/**
	 * Irrelevant at present. Do not call.
	 */
	public boolean isPropertySet(final Object id) {
		return false;
	}

	/**
	 * Irrelevant at present. Do not call.
	 */
	public void resetPropertyValue(final Object id) {
	}

	/**
	 * Irrelevant at present. Do not call.
	 */
	public boolean isPropertyResettable(final Object id) {
		return false;
	}
}
