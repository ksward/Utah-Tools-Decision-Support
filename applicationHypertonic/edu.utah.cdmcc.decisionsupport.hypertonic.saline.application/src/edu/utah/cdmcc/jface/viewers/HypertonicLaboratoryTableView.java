package edu.utah.cdmcc.jface.viewers;

import hypertonic.decision.object.HypertonicLaboratoryLabelProvider;
import hypertonic.decision.object.HypertonicSalineLaboratoryTable;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Composite;
import edu.utah.cdmcc.views.LaboratoryTableViewTemplate;

/**
 * The developer can create a laboratory view in this method by changing the
 * lines and then letting most of the work be done by the underlying template,
 * which is in the application core plugin.
 * 
 * @author mdean
 * 
 */
public class HypertonicLaboratoryTableView extends LaboratoryTableViewTemplate {

	public static final String ID = "edu.utah.cdmcc.decisionsupport.hypertonic.laboratory.view";

	public HypertonicLaboratoryTableView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		table = new HypertonicSalineLaboratoryTable(parent).getTable();
		labTableViewer = new CheckboxTableViewer(table);
		labTableViewer.setLabelProvider(new HypertonicLaboratoryLabelProvider());
		super.createPartControl(parent);
	}
}
