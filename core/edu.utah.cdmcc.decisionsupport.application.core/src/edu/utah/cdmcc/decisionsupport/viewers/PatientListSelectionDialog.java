package edu.utah.cdmcc.decisionsupport.viewers;

import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.SelectionDialog;

import core.patient.object.PatientSelectLabelProvider;
import core.patient.object.PatientTable;

public class PatientListSelectionDialog extends SelectionDialog {

	private TableViewer fTableViewer;

	public PatientListSelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	public TableViewer getTableViewer() {
		return fTableViewer;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Table table = new PatientTable(parent).getTable();
		fTableViewer = new TableViewer(table);
		fTableViewer.setLabelProvider(new PatientSelectLabelProvider());
		fTableViewer.setContentProvider(new PatientContentProvider());
		fTableViewer.setInput(ApplicationControllers.getPatientController().getPatients());
		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
		return parent;
	}

	@Override
	protected org.eclipse.swt.widgets.Label createMessageArea(Composite composite) {
		return null;};	

	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer.getSelection();
		setResult(selection.toList());
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(560, 410);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select the Active Patient");
	}
}
