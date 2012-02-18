package ventilator.decision.object;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class VentilatorPediatricLaboratoryTable {
	private Table table;

	public VentilatorPediatricLaboratoryTable(Composite parent){
		table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.SINGLE | SWT.CHECK | SWT.FULL_SELECTION );
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableColumn SAMPLE_DATE = new TableColumn(table, SWT.CENTER);
		SAMPLE_DATE.setText(LaboratoryColumn.SAMPLE_DATE.name);
		SAMPLE_DATE.setWidth(LaboratoryColumn.SAMPLE_DATE.width);
		SAMPLE_DATE.setResizable(true);
		TableColumn PH = new TableColumn(table, SWT.CENTER);
		PH.setText(LaboratoryColumn.PH.name);
		PH.setWidth(LaboratoryColumn.PH.width);
		PH.setResizable(true);
		TableColumn PCO2 = new TableColumn(table, SWT.CENTER);
		PCO2.setText(LaboratoryColumn.PCO2.name);
		PCO2.setWidth(LaboratoryColumn.PCO2.width);
		PCO2.setResizable(true);	
		TableColumn PO2 = new TableColumn(table, SWT.CENTER);
		PO2.setText(LaboratoryColumn.PO2.name);
		PO2.setWidth(LaboratoryColumn.PO2.width);
		PO2.setResizable(true);
	}
	
	public Table getTable() {
		return table;
	}
	
	private enum LaboratoryColumn {
		SAMPLE_DATE("Laboratory date", 200),
		PH("pH", 100),
		PCO2("pCO2",100),
		PO2("pO2",100);
		final String name;
		final int width;
		LaboratoryColumn(String name, int width){
			this.name = name;
			this.width = width;
		}
	}
}
