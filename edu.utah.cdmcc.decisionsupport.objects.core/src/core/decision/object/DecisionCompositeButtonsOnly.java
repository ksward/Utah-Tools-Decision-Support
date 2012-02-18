package core.decision.object;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import com.swtdesigner.SWTResourceManager;

public class DecisionCompositeButtonsOnly extends Composite {

	private Button acceptButton;
	private Button chartButton;
	private Button declineButton;
	private Button decisionButton;
	private Composite buttonComposite;

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public DecisionCompositeButtonsOnly(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		this.setBackground(SWTResourceManager.getColor(169, 173, 240));
		makeDecisionButtonComposite();
		this.layout();

	}

	/**
	 * create buttons/controls for user to calculate, accept, decline or chart a
	 * decision for selected patient
	 */
	private void makeDecisionButtonComposite() {
		createButtonComposite();
		createCalculateDecisionButton();
		createAcceptDecisionButton();
		createDeclineDecisionButton();
		createChartDataButton();
	}

	private void createButtonComposite() {
		buttonComposite = new Composite(this, SWT.NONE);
		setupButtonFormLayout();
		buttonComposite.setBackground(SWTResourceManager.getColor(83, 102, 240));
	}

	private void setupButtonFormLayout() {
		FormLayout buttonCompositeLayout = new FormLayout();
		FormData buttonCompositeLData = setupButtonCompositeFormData();
		buttonComposite.setLayoutData(buttonCompositeLData);
		buttonComposite.setLayout(buttonCompositeLayout);
	}

	private FormData setupButtonCompositeFormData() {
		FormData buttonCompositeLData = new FormData();
		buttonCompositeLData.width = 721;
		buttonCompositeLData.height = 49;
		buttonCompositeLData.left = new FormAttachment(10, 1000, 0);
		buttonCompositeLData.right = new FormAttachment(991, 1000, 0);
		buttonCompositeLData.top = new FormAttachment(885, 1000, 0);
		buttonCompositeLData.bottom = new FormAttachment(1000, 1000, -7);
		return buttonCompositeLData;
	}

	private void createAcceptDecisionButton() {
		acceptButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
		setAcceptDecisionButtonLayout();
		acceptButton.setText("Accept Decision");
		acceptButton.setEnabled(false);
	}

	private void setAcceptDecisionButtonLayout() {
		FormData acceptButtonLData = new FormData();
		acceptButtonLData.width = 126;
		acceptButtonLData.height = 28;
		acceptButtonLData.left = new FormAttachment(285, 1000, 0);
		acceptButtonLData.right = new FormAttachment(461, 1000, 0);
		acceptButtonLData.top = new FormAttachment(178, 1000, 0);
		acceptButtonLData.bottom = new FormAttachment(845, 1000, 0);
		acceptButton.setLayoutData(acceptButtonLData);
	}

	private void createDeclineDecisionButton() {
		declineButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
		setDeclineDecisionButtonLayout();
		declineButton.setText("Decline Decision");
		declineButton.setEnabled(false);
	}

	private void setDeclineDecisionButtonLayout() {
		FormData declineButtonLData = new FormData();
		declineButtonLData.width = 133;
		declineButtonLData.height = 28;
		declineButtonLData.left = new FormAttachment(530, 1000, 0);
		declineButtonLData.right = new FormAttachment(716, 1000, 0);
		declineButtonLData.top = new FormAttachment(178, 1000, 0);
		declineButtonLData.bottom = new FormAttachment(845, 1000, 0);
		declineButton.setLayoutData(declineButtonLData);
	}

	private void createCalculateDecisionButton() {
		decisionButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
		setCalculateDecisionButtonLayout();
		decisionButton.setText("Calculate Decision");
		decisionButton.setAlignment(SWT.CENTER);
		decisionButton.setEnabled(false);
	}

	private void setCalculateDecisionButtonLayout() {
		FormData decisionButtonLData = new FormData();
		decisionButtonLData.width = 140;
		decisionButtonLData.height = 28;
		decisionButtonLData.left = new FormAttachment(30, 1000, 0);
		decisionButtonLData.right = new FormAttachment(226, 1000, 0);
		decisionButtonLData.top = new FormAttachment(178, 1000, 0);
		decisionButtonLData.bottom = new FormAttachment(845, 1000, 0);
		decisionButton.setLayoutData(decisionButtonLData);
	}

	private void createChartDataButton() {
		chartButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
		setChartDataButtonLayout();
		chartButton.setText("Charting Only");
		chartButton.setEnabled(false);
	}

	private void setChartDataButtonLayout() {
		FormData chartButtonLData = new FormData();
		chartButtonLData.width = 112;
		chartButtonLData.height = 28;
		chartButtonLData.left = new FormAttachment(794, 1000, 0);
		chartButtonLData.right = new FormAttachment(951, 1000, 0);
		chartButtonLData.top = new FormAttachment(178, 1000, 0);
		chartButtonLData.bottom = new FormAttachment(845, 1000, 0);
		chartButton.setLayoutData(chartButtonLData);
	}

	public void turnOffAllButtons() {
		acceptButton.setEnabled(false);
		chartButton.setEnabled(false);
		decisionButton.setEnabled(false);
		declineButton.setEnabled(false);
	}

//	@Override
//	protected void checkSubclass() {
//		// Disable the check that prevents subclassing of SWT components
//	}

	public Button getDecisionButton() {
		return decisionButton;
	}

	public Button getAcceptButton() {
		return acceptButton;
	}

	public Button getDeclineButton() {
		return declineButton;
	}

	public Button getChartButton() {
		return chartButton;
	}

}
