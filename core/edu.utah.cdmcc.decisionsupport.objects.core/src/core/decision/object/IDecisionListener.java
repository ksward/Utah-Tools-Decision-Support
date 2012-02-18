package core.decision.object;




/**
 * 
 * IDecisionListener is part of the Utah Decision Support Tools.
 * @author J. Michael Dean, M.D., M.B.A.
 * <P>
 * November 20, 2006  12:22 PM
 * <P>
 * University of Utah School of Medicine, Salt Lake City, Utah
 * <P>
 * Copyright 2005 - 2006.  All rights reserved.
 * <P>
 * Purpose of the class: Adds property change listener to ClinicalDecision.
 * This is to allow text fields in editors and views to update themselves
 * from a specific decision after the rules engine fires.
 * <P>
 *
 */
public interface IDecisionListener {
	 void decisionChanged(final ClinicalDecision decision);
	 void decisionStored(final ClinicalDecision decision);
}
