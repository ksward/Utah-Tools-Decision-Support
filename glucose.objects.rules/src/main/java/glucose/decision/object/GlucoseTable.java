package glucose.decision.object;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class GlucoseTable {
	private Table table;
	public GlucoseTable(Composite parent) {
		table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.SINGLE | SWT.CHECK | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] STD_HEADINGS = { "Decision Date", "Glucose", "Insulin", "Advice",
				"User Action", "Rationale for Advice"};
		TableColumn dateColumn = new TableColumn(table, SWT.None);
		dateColumn.setText(STD_HEADINGS[0]);
		dateColumn.setWidth(200);
		dateColumn.setResizable(true);
		TableColumn glucoseColumn = new TableColumn(table, SWT.None);
		glucoseColumn.setText(STD_HEADINGS[1]);
		glucoseColumn.setWidth(75);
		glucoseColumn.setResizable(true);
		TableColumn insulinColumn = new TableColumn(table, SWT.None);
		insulinColumn.setText(STD_HEADINGS[2]);
		insulinColumn.setWidth(75);
		insulinColumn.setResizable(true);
		TableColumn adviceColumn = new TableColumn(table, SWT.None);
		adviceColumn.setText(STD_HEADINGS[3]);
		adviceColumn.setWidth(250);
		adviceColumn.setResizable(true);
		TableColumn actionColumn = new TableColumn(table, SWT.None);
		actionColumn.setText(STD_HEADINGS[4]);
		actionColumn.setWidth(75);
		actionColumn.setResizable(true);
		TableColumn reasonColumn = new TableColumn(table, SWT.None);
		reasonColumn.setText(STD_HEADINGS[5]);
		reasonColumn.setWidth(200);
		reasonColumn.setResizable(true);
	}
	public Table getTable() {
		return table;
	}


}
