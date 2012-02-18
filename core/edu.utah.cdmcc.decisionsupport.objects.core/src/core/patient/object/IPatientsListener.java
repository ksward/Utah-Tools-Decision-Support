package core.patient.object;




/**
 * 
 * IPatientsListener is part of the Utah Decision Support Tools.
 * @author J. Michael Dean, M.D., M.B.A.
 * <P>
 * Aug 4, 2006   8:16:31 PM
 * <P>
 * University of Utah School of Medicine, Salt Lake City, Utah
 * <P>
 * Copyright 2005 - 2006.  All rights reserved.
 * <P>
 * Purpose of the class: Adds property change listener to Patient.
 * This is to notify the patient viewer that the patient list has
 * changed (a new patient added or an old patient removed).  Then
 * the viewer updates itself.
 * <P>
 *
 */
public interface IPatientsListener {
	 void patientsChanged(final Patient patient);
}
