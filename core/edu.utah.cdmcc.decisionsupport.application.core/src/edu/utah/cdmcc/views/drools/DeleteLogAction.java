package edu.utah.cdmcc.views.drools;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.BusyIndicator;

import edu.utah.cdmcc.decisionsupport.application.core.Activator;

/**
 * Action to clear the log.  Altered by J. Michael Dean to fit decision
 * support tools.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class DeleteLogAction extends Action {
    
    private AuditView view;

    public DeleteLogAction(AuditView view) {
        super(null, IAction.AS_PUSH_BUTTON);
        this.view = view;
        setToolTipText("Clear Log");
        setImageDescriptor(DroolsPluginImages.getImageDescriptor(DroolsPluginImages.DELETE_LOG));
        setDisabledImageDescriptor(DroolsPluginImages.getImageDescriptor(DroolsPluginImages.DELETE_LOG_DISABLED));
        setId(Activator.getUniqueIdentifier() + ".ClearLogAction");
    }

    public void run() {
        if (!view.isAvailable()) {
            return;
        }
        view.deleteLog();  
        BusyIndicator.showWhile(view.getViewer().getControl().getDisplay(), new Runnable() {
            public void run() {
            	view.getViewer().refresh();                    
            }
        });         
    }
}
