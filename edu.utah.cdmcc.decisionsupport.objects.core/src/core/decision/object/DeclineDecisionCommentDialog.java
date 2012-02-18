package core.decision.object;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DeclineDecisionCommentDialog extends TitleAreaDialog {
	private Text txtDescriptionHere;
	private ClinicalDecision decision;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DeclineDecisionCommentDialog(Shell parentShell, ClinicalDecision decision) {
		super(parentShell);
		this.decision = decision;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Please describe your reasoning so that we can improve the software.");
		setTitle("Reason for choosing to decline the recommended decision.");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		txtDescriptionHere = new Text(container, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtDescriptionHere.setToolTipText("In this text box, type in your reason for declining the decision.");
		txtDescriptionHere.setBounds(10, 10, 430, 138);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	@Override
	protected void okPressed() {
		if (txtDescriptionHere.getText().length() > 0){
			decision.setDeclineComment(this.txtDescriptionHere.getText());
		} else {
			decision.setDeclineComment("User did not provide a comment.");
		}
		
		super.okPressed();
	}
}
