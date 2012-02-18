package hypertonic.decision.object;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class GenericLaboratoryTable {
	private Table table;

	public GenericLaboratoryTable(Composite parent){
		table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.SINGLE | SWT.CHECK | SWT.FULL_SELECTION );
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableColumn SAMPLE_DATE = new TableColumn(table, SWT.CENTER);
		SAMPLE_DATE.setText(LaboratoryColumn.SAMPLE_DATE.name);
		SAMPLE_DATE.setWidth(LaboratoryColumn.SAMPLE_DATE.width);
		SAMPLE_DATE.setResizable(true);
		TableColumn SODIUM = new TableColumn(table, SWT.CENTER);
		SODIUM.setText(LaboratoryColumn.SODIUM.name);
		SODIUM.setWidth(LaboratoryColumn.SODIUM.width);
		SODIUM.setResizable(true);
		TableColumn OSMOLALITY = new TableColumn(table, SWT.CENTER);
		OSMOLALITY.setText(LaboratoryColumn.OSMOLALITY.name);
		OSMOLALITY.setWidth(LaboratoryColumn.OSMOLALITY.width);
		OSMOLALITY.setResizable(true);	
	}
	
	public Table getTable() {
		return table;
	}
	
	private enum LaboratoryColumn {
		SAMPLE_DATE("Laboratory date", 200),
		SODIUM("Sodium", 100),
		OSMOLALITY("Osmolality",100);
		final String name;
		final int width;
		LaboratoryColumn(String name, int width){
			this.name = name;
			this.width = width;
		}
	}
}
