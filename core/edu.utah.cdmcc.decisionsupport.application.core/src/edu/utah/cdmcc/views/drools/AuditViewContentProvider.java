package edu.utah.cdmcc.views.drools;

import java.util.List;

import edu.utah.cdmcc.views.drools.AuditView.Event;
/**
 * Provides content to the debug views.  This code is unchanged
 * from the original Drools distribution.
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah)
 * Copyright 2005 - 2008.  All Rights Reserved.
 *
 */
public class AuditViewContentProvider extends DroolsDebugViewContentProvider {

    protected String getEmptyString() {
    	return "The selected audit log is empty.";
    }

	public Object[] getChildren(Object obj) {
		if (obj instanceof List) {
			return ((List<?>) obj).toArray();
		}
        if (obj instanceof Event) {
    		return ((Event) obj).getSubEvents();
        }
        return new Object[0];
    }
    
    public boolean hasChildren(Object obj) {
        if (obj instanceof Event) {
    		return ((Event) obj).hasSubEvents();
        }
        return false;
    }
}
