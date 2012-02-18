package edu.utah.cdmcc.decisionsupport.application;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1200, 800));
        configurer.setShowMenuBar(true);
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(true);
        configurer.setShowPerspectiveBar(false);
        configurer.setTitle("Generic Decision Support Application");
    }
    
    @Override
    public void postWindowOpen() {
    	IStatusLineManager statusLine = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
    	statusLine.setMessage("Decision support application initialized ...");
    	PreferenceManager preferenceManager = PlatformUI.getWorkbench().getPreferenceManager();
    	preferenceManager.remove("org.eclipse.help.ui.browsersPreferencePage");
    	preferenceManager.remove("org.eclipse.debug.ui.DebugPreferencePage");
    	preferenceManager.remove("org.eclipse.ui.preferencePages.Workbench");
    }
}
