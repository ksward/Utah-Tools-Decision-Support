package hypertonic.decision.object;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class GenericTable {
	private Table table;
	public GenericTable(Composite parent) {
		table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.SINGLE | SWT.CHECK | SWT.FULL_SELECTION );
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableColumn DECISION_DATE = new TableColumn(table, SWT.CENTER);
		DECISION_DATE.setText(SalineTableColumn.DECISION_DATE.name);
		DECISION_DATE.setWidth(SalineTableColumn.DECISION_DATE.width);
		DECISION_DATE.setResizable(true);
		TableColumn SODIUM = new TableColumn(table, SWT.CENTER);
		SODIUM.setText(SalineTableColumn.SODIUM.name);
		SODIUM.setWidth(SalineTableColumn.SODIUM.width);
		SODIUM.setResizable(true);
		TableColumn OSMOLALITY = new TableColumn(table, SWT.CENTER);
		OSMOLALITY.setText(SalineTableColumn.OSMOLALITY.name);
		OSMOLALITY.setWidth(SalineTableColumn.OSMOLALITY.width);
		OSMOLALITY.setResizable(true);
		TableColumn ICP = new TableColumn(table, SWT.CENTER);
		ICP.setText(SalineTableColumn.ICP.name);
		ICP.setWidth(SalineTableColumn.ICP.width);
		ICP.setResizable(true);
		TableColumn CVP = new TableColumn(table, SWT.CENTER);
		CVP.setText(SalineTableColumn.CVP.name);
		CVP.setWidth(SalineTableColumn.CVP.width);
		CVP.setResizable(true);
		TableColumn MAP = new TableColumn(table, SWT.CENTER);
		MAP.setText(SalineTableColumn.MAP.name);
		MAP.setWidth(SalineTableColumn.MAP.width);
		MAP.setResizable(true);
		TableColumn OLD_RATE = new TableColumn(table, SWT.CENTER);
		OLD_RATE.setText(SalineTableColumn.OLD_RATE.name);
		OLD_RATE.setWidth(SalineTableColumn.OLD_RATE.width);
		OLD_RATE.setResizable(true);
		TableColumn NEW_RATE = new TableColumn(table, SWT.CENTER);
		NEW_RATE.setText(SalineTableColumn.NEW_RATE.name);
		NEW_RATE.setWidth(SalineTableColumn.NEW_RATE.width);
		NEW_RATE.setResizable(true);
		TableColumn SALINE_BOLUS = new TableColumn(table, SWT.CENTER);
		SALINE_BOLUS.setText(SalineTableColumn.SALINE_BOLUS.name);
		SALINE_BOLUS.setWidth(SalineTableColumn.SALINE_BOLUS.width);
		SALINE_BOLUS.setResizable(true);
		TableColumn MANNITOL_BOLUS = new TableColumn(table, SWT.CENTER);
		MANNITOL_BOLUS.setText(SalineTableColumn.MANNITOL_BOLUS.name);
		MANNITOL_BOLUS.setWidth(SalineTableColumn.MANNITOL_BOLUS.width);
		MANNITOL_BOLUS.setResizable(true);
	}
	public Table getTable() {
		return table;
	}
	
	private enum SalineTableColumn {
		DECISION_DATE("Decision date",175),
		SODIUM("Sodium",60),
		OSMOLALITY("Osmolality",70),
		ICP("ICP",60),
		CVP("CVP",60),
		MAP("MAP",60),
		OLD_RATE("Old 3% Rate",75),
		NEW_RATE("New 3% Rate",75),
		SALINE_BOLUS("Bolus 3%",60),
		MANNITOL_BOLUS("Bolus mannitol",90);
		final String name;
		final int width;
		SalineTableColumn(String name, int width){
			this.name = name;
			this.width = width;
		}
	}
}
