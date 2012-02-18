package core.decision.object;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class DecisionFieldsAddedComposite extends DecisionCompositeButtonsOnly implements IDecisionListener {
	protected static final String EMPTY_STRING = "";
	private SashForm decisionComposite;
	private Group decisionGroup;
	private StyledText decisionText;
	private Group explainGroup;
	private StyledText explanationText;
	private Boolean decisionFiredFlag;


	public DecisionFieldsAddedComposite(Composite parent, int style) {
		super(parent, style);
		makeDecisionComposite();
	}

	private void makeDecisionComposite() {
		createDecisionComposite();
		createDecisionGroup();
		createExplainGroup();
	}

	private void createDecisionComposite() {
		decisionComposite = new SashForm(this, SWT.VERTICAL | SWT.V_SCROLL | SWT.BORDER);
		FormData decisionCompositeLData = setupDecisionCompositeFormData();
		decisionComposite.setLayoutData(decisionCompositeLData);
	}

	private FormData setupDecisionCompositeFormData() {
		FormData decisionCompositeLData = new FormData();
		decisionCompositeLData.width = 719;
		decisionCompositeLData.height = 278;
		decisionCompositeLData.left = new FormAttachment(10, 1000, 0);
		decisionCompositeLData.right = new FormAttachment(991, 1000, 0);
		decisionCompositeLData.top = new FormAttachment(319, 1000, 0);
		decisionCompositeLData.bottom = new FormAttachment(899, 1000, 0);
		return decisionCompositeLData;
	}

	private void createDecisionGroup() {
		decisionGroup = new Group(decisionComposite, SWT.NONE);
		FormData decisionGroupLData = setupDecisionGroupGridLayout();
		decisionGroup.setLayoutData(decisionGroupLData);
		decisionGroup.setText("Decision Recommended by Protocol:");
		createDecisionText();
	}

	private FormData setupDecisionGroupGridLayout() {
		GridLayout decisionGroupLayout = new GridLayout();
		decisionGroupLayout.makeColumnsEqualWidth = true;
		decisionGroup.setLayout(decisionGroupLayout);
		FormData decisionGroupLData = setupDecisionFormData();
		return decisionGroupLData;
	}

	private FormData setupDecisionFormData() {
		FormData decisionGroupLData = new FormData();
		decisionGroupLData.width = 692;
		decisionGroupLData.height = 78;
		decisionGroupLData.left = new FormAttachment(0, 1000, 7);
		decisionGroupLData.right = new FormAttachment(993, 1000, 0);
		decisionGroupLData.top = new FormAttachment(21, 1000, 0);
		decisionGroupLData.bottom = new FormAttachment(304, 1000, 0);
		return decisionGroupLData;
	}

	private void createExplainGroup() {
		explainGroup = new Group(decisionComposite, SWT.NONE);
		FormData explainGroupLData = setupExplainGroupGridLayout();
		explainGroup.setLayoutData(explainGroupLData);
		explainGroup.setText("Why did the protocol make this recommendation?");
		createExplanationText();
	}

	private FormData setupExplainGroupGridLayout() {
		GridLayout explainGroupLayout = new GridLayout();
		explainGroupLayout.makeColumnsEqualWidth = true;
		explainGroup.setLayout(explainGroupLayout);
		FormData explainGroupLData = setupExplanationFormData();
		return explainGroupLData;
	}

	private FormData setupExplanationFormData() {
		FormData explainGroupLData = new FormData();
		explainGroupLData.width = 692;
		explainGroupLData.height = 92;
		explainGroupLData.left = new FormAttachment(10, 1000, 0);
		explainGroupLData.right = new FormAttachment(993, 1000, 0);
		explainGroupLData.top = new FormAttachment(324, 1000, 0);
		explainGroupLData.bottom = new FormAttachment(646, 1000, 0);
		return explainGroupLData;
	}

	private void createDecisionText() {
		decisionText = new StyledText(decisionGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData decisionTextLData = new GridData();
		decisionTextLData.grabExcessHorizontalSpace = true;
		decisionTextLData.grabExcessVerticalSpace = true;
		decisionTextLData.horizontalAlignment = GridData.FILL;
		decisionTextLData.verticalAlignment = GridData.FILL;
		decisionTextLData.verticalSpan = 3;
		decisionText.setLayoutData(decisionTextLData);
		decisionText.setText(EMPTY_STRING);
		decisionText.setToolTipText("The recommended clinical decision. ");
	}

	private void createExplanationText() {
		explanationText = new StyledText(explainGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData explanationTextLData = new GridData();
		explanationTextLData.verticalAlignment = GridData.FILL;
		explanationTextLData.horizontalAlignment = GridData.FILL;
		explanationTextLData.verticalSpan = 3;
		explanationTextLData.grabExcessHorizontalSpace = true;
		explanationTextLData.grabExcessVerticalSpace = true;
		explanationText.setLayoutData(explanationTextLData);
		explanationText.setText(EMPTY_STRING);
		explanationText.setToolTipText("The explanation for the recommended decision.");
	}

	public void clearOutputEditorFields() {
		getDecisionText().setText(EMPTY_STRING);
		getExplanationText().setText(EMPTY_STRING);
	}

	public StyledText getDecisionText() {
		return decisionText;
	}

	public void clearAdvice() {
		clearOutputEditorFields();
		setDecisionFiredFlag(false);
	}

	public void setDecisionFiredFlag(Boolean decisionFiredFlag) {
		this.decisionFiredFlag = decisionFiredFlag;
	}

	public Boolean getDecisionFiredFlag() {
		return decisionFiredFlag;
	}

	public StyledText getExplanationText() {
		return explanationText;
	}

	public void decisionChanged(ClinicalDecision decision) {
		if (decision == null) {
			getDecisionText().setText("");
			getExplanationText().setText("");
		} else {
			getDecisionText().setText(decision.getAdviceText());
			getExplanationText().setText(decision.getRationaleText());
		}
	}

	public void decisionStored(ClinicalDecision decision) {
	}

}
