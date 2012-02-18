package core.decision.object;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class GetInvalidDecisionFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		ClinicalDecision decision = (ClinicalDecision) element;
		return decision.getValid();
	}

}
