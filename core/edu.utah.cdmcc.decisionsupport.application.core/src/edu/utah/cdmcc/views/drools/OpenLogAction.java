package edu.utah.cdmcc.views.drools;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.FileDialog;

import edu.utah.cdmcc.decisionsupport.application.core.Activator;


/**
 * Action to open a log.  Altered by J. Michael Dean to accomodate
 * decision support tools.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class OpenLogAction extends Action {
    
    private AuditView view;

    public OpenLogAction(AuditView view) {
        super(null, IAction.AS_PUSH_BUTTON);
        this.view = view;
        setToolTipText("Open Log");
        setImageDescriptor(DroolsPluginImages.getImageDescriptor(DroolsPluginImages.OPEN_LOG));
        setId(Activator.getUniqueIdentifier() + ".OpenLogAction");
    }

    public void run() {
        if (!view.isAvailable()) {
            return;
        }
        FileDialog dialog = new FileDialog(view.getSite().getShell());
        dialog.setFilterExtensions(new String[] { "*.log" });
        String fileName = dialog.open();
        view.setLogFile(fileName);  
        BusyIndicator.showWhile(view.getViewer().getControl().getDisplay(), new Runnable() {
            public void run() {
            	view.getViewer().refresh();                    
            }
        });         
    }
}
