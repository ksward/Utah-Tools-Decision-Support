package edu.utah.cdmcc.jface.viewers;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Composite;
import edu.utah.cdmcc.views.DecisionTableViewTemplate;
import glucose.decision.object.GlucoseDecisionLabelProvider;
import glucose.decision.object.GlucoseTable;

/**
 * 
 * DecisionTableView is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Purpose of the class: List decisions for a specific patient in a
 *         table.
 *         
 * When creating a new application, the developer must fix the lines below
 * with the correct table and label provider.  All other
 * details are handled in the superclass which is in the application core
 * plugin.
 */
public class DecisionTableView extends DecisionTableViewTemplate{

	public static final String ID = "edu.utah.cdmcc.decisionsupport.decision.table.viewer";
	
	public DecisionTableView() {
		super();
	}

	@Override
	public void createPartControl(final Composite parent) {
		table = new GlucoseTable(parent).getTable();
		decisionTableViewer = new CheckboxTableViewer(table);
		decisionTableViewer.setLabelProvider(new GlucoseDecisionLabelProvider());
		super.createPartControl(parent);

	}
}
