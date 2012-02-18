package edu.utah.cdmcc.views.drools;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.BusyIndicator;

import edu.utah.cdmcc.decisionsupport.application.core.Activator;


/**
 * Action to refresh the log. Altered by J. Michael Dean to accomodate
 * decision support tools.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class RefreshLogAction extends Action {
    
    private AuditView view;

    public RefreshLogAction(AuditView view) {
        super(null, IAction.AS_PUSH_BUTTON);
        this.view = view;
        setToolTipText("Refresh Log");
        setImageDescriptor(DroolsPluginImages.getImageDescriptor(DroolsPluginImages.REFRESH_LOG));
        setDisabledImageDescriptor(DroolsPluginImages.getImageDescriptor(DroolsPluginImages.REFRESH_LOG_DISABLED));
        setId(Activator.getUniqueIdentifier() + ".RefreshLogAction");
    }

    public void run() {
        if (!view.isAvailable()) {
            return;
        }
        view.refresh();  
        BusyIndicator.showWhile(view.getViewer().getControl().getDisplay(), new Runnable() {
            public void run() {
            	view.getViewer().refresh();                    
            }
        });         
    }
}
