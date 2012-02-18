package edu.utah.cdmcc.views.drools;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import edu.utah.cdmcc.decisionsupport.application.core.Activator;
import edu.utah.cdmcc.views.drools.AuditView.Event;

/**
 * Action to show the cause event of an audit event. Altered by
 * J. Michael Dean to accomodate decision support.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class ShowEventCauseAction extends Action {
    
    private AuditView view;

    public ShowEventCauseAction(AuditView view) {
        super(null, IAction.AS_PUSH_BUTTON);
        this.view = view;
        setToolTipText("Show Cause");
        setText("Show Cause");
        setId(Activator.getUniqueIdentifier() + ".ShowEventCause");
    }

    public void run() {
    	Event event = view.getSelectedEvent();
    	if (event != null) {
    		Event cause = event.getCauseEvent();
    		if (cause != null) {
    			view.showEvent(cause);
    		}
    	}
    }
}
