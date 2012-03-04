package core.patient.object;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * 
 * PatientEditorInput is part of the Utah Decision Support Tools.
 * @author J. Michael Dean, M.D., M.B.A.
 * <P>
 * Aug 4, 2006   8:28:30 PM
 * <P>
 * University of Utah School of Medicine, Salt Lake City, Utah
 * <P>
 * Copyright 2005 - 2006.  All rights reserved.
 * <P>
 * Purpose of the class: Editor input object for glucose decisions.
 * <P>
 *
 */
public class PatientEditorInput implements IEditorInput {
	
	private Patient patient;
	public PatientEditorInput(final Patient patient) {
		super();
		Assert.isNotNull(patient);
		this.patient = patient;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public Patient getPatient(){
		return patient;
	}
	
	public String getName() {
		return patient.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return patient.getName();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(final Class adapter) {
		return null;
	}
	
	public boolean equals(final Object obj){
		if (super.equals(obj))
			return true;
		if (!(obj instanceof PatientEditorInput)) return false;
		PatientEditorInput other = (PatientEditorInput) obj;
		return patient.equals(other.patient);
	}
	
	public int hashCode(){
		return patient.hashCode();
	}
}
