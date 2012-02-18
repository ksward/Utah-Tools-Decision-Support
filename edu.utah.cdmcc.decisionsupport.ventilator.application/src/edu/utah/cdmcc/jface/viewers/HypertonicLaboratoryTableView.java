package edu.utah.cdmcc.jface.viewers;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Composite;

import ventilator.decision.object.VentilatorLaboratoryLabelProvider;
import ventilator.decision.object.VentilatorLaboratoryTable;
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
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		table = new VentilatorLaboratoryTable(parent).getTable();
		labTableViewer = new CheckboxTableViewer(table);
		labTableViewer.setLabelProvider(new VentilatorLaboratoryLabelProvider());
		super.createPartControl(parent);
	}
}
