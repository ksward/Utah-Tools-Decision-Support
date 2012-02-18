package core.decision.object;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This composite must be overridden and the domain specific fields added to it.  The
 * top section is ready to receive domain specific widgets in a six column format;
 * if the user wishes a different column layout, then this composite cannot be used.
 * In that instance, the user can extend DecisionFieldsAddedComposite, but this will 
 * require more work.
 * 
 * J. Michael Dean, M.D.
 * December 31, 2009
 * 
 */
public abstract class DomainFieldsCompositeAdded extends DecisionFieldsAddedComposite {
	public Composite domainFieldsComposite;

	public DomainFieldsCompositeAdded(final Composite parent, final int style) {
		super(parent, style);
		domainFieldsComposite = new Composite(this, SWT.BORDER);
		setupDomainGridLayout();
	}

	private FormData setupDomainCompositeFormData() {
		FormData fd_domainFieldsComposite = new FormData();
		fd_domainFieldsComposite.top = new FormAttachment(getDeclineButton(), 0, SWT.TOP);
		fd_domainFieldsComposite.width = 719;
		fd_domainFieldsComposite.height = 75;
		fd_domainFieldsComposite.left = new FormAttachment(10, 1000, 0);
		fd_domainFieldsComposite.right = new FormAttachment(991, 1000, 0);
		fd_domainFieldsComposite.bottom = new FormAttachment(305, 1000, 0);
		return fd_domainFieldsComposite;
	}

	private void setupDomainGridLayout() {
		GridLayout domainCompositeLayout = new GridLayout();
		domainCompositeLayout.numColumns = 6;
		domainCompositeLayout.makeColumnsEqualWidth = true;
		FormData domainCompositeLData = setupDomainCompositeFormData();
		domainFieldsComposite.setLayoutData(domainCompositeLData);
		domainFieldsComposite.setLayout(domainCompositeLayout);
	}

	public final void enableCorrectButtonCombination() {
		if (allRequiredFieldsFilledIn()) {
			if (getDecisionFiredFlag()) {
				setAcceptDeclineOn();
			} else {
				setAcceptDeclineOff();
			}
		} else {
			turnOffAllButtons();
		}
	}

	public abstract boolean allRequiredFieldsFilledIn();


	public final void setAcceptDeclineOn() {
		getAcceptButton().setEnabled(true);
		getChartButton().setEnabled(false);
		getDecisionButton().setEnabled(false);
		getDeclineButton().setEnabled(true);
	}

	public final void setAcceptDeclineOff() {
		getAcceptButton().setEnabled(false);
		getChartButton().setEnabled(true);
		getDecisionButton().setEnabled(true);
		getDeclineButton().setEnabled(false);
	}
}
