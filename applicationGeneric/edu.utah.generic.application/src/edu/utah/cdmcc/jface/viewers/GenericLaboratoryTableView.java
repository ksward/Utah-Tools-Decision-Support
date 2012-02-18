package edu.utah.cdmcc.jface.viewers;

import hypertonic.decision.object.GenericLaboratoryLabelProvider;
import hypertonic.decision.object.GenericLaboratoryTable;
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
public class GenericLaboratoryTableView extends LaboratoryTableViewTemplate {

	public static final String ID = "edu.utah.cdmcc.decisionsupport.hypertonic.laboratory.view";

	public GenericLaboratoryTableView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		table = new GenericLaboratoryTable(parent).getTable();
		labTableViewer = new CheckboxTableViewer(table);
		labTableViewer.setLabelProvider(new GenericLaboratoryLabelProvider());
		super.createPartControl(parent);
	}
}
