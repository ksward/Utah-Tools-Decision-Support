package edu.utah.cdmcc.decisionsupport.clock.core;

//import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import com.swtdesigner.SWTResourceManager;
import core.decision.object.ClinicalDecision;
import core.decision.object.IDecisionListener;
import edu.utah.cdmcc.decisionsupport.clock.core.ClockJob;
import edu.utah.cdmcc.decisionsupport.clock.core.IClockJobListener;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

/**
 * 
 * Clock is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Initial version: Aug 4, 2006 8:14:42 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2008. All rights reserved.
 *         <P>
 *         Purpose of the class: Maintain a large clock to tell nurse when next
 *         action is due.
 *         <P>
 * 
 */
public class Clock extends ViewPart implements IDecisionListener, IClockJobListener {
	private CLabel MinutesLeftText;
	public static final String ID = "edu.utah.cdmcc.decisionsupport.clock";

	/**
	 * Default constructor adds the clock as a listener to decision changes and
	 * to the underlying job that generates the time to display in the clock.
	 */
	public Clock() {
		super();
		ApplicationControllers.getDecisionController().addDecisionFiredListener(this);
		ApplicationControllers.getClockJob().addJobListener(this);
	}

	/**
	 * Create the visible parts of the clock.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(null);
		parent.setToolTipText("Number of minutes until the next decision should be calculated");
		MinutesLeftText = new CLabel(parent, SWT.NONE);// new
		// StyledText(parent,
		// SWT.BORDER);
		final CLabel minutesUntilNextLabel_1 = new CLabel(parent, SWT.NONE);
		minutesUntilNextLabel_1.setFont(SWTResourceManager.getFont("Lucida Grande", 20, SWT.NONE));
		minutesUntilNextLabel_1.setAlignment(SWT.CENTER);
		minutesUntilNextLabel_1.setText("Time remaining:");
		minutesUntilNextLabel_1.setBounds(0, 0, 243, 34);

		MinutesLeftText.setEnabled(false);
		MinutesLeftText.setBackground(SWTResourceManager.getColor(45, 28, 255));
		MinutesLeftText.setAlignment(SWT.CENTER);
		MinutesLeftText.setForeground(SWTResourceManager.getColor(238, 255, 26));
		MinutesLeftText.setFont(SWTResourceManager.getFont("Helvetica", 35, SWT.NONE));
		MinutesLeftText.setBounds(34, 40, 171, 74);

		//initializeToolBar();

	}

	/**
	 * Remove the clock as a listener to decisions or the clock job - this is
	 * important because the user can close the view, and if this happened, then
	 * when the listeners were invoked, the application would have a NPE error
	 * and crash.
	 */
	@Override
	public void dispose() {
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(this);
		ApplicationControllers.getClockJob().removeJobListener(this);
	};

	@Override
	public void setFocus() {
	}

//	private void initializeToolBar() {
//		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
//	}

	/**
	 * Null method to meet interface requirements for decision listener. Not
	 * elegant - but creating an adaptor seems like overkill.
	 */
	public void decisionChanged(final ClinicalDecision decision) {
	}

	/**
	 * When a decision is stored, then the clock should be set to the time that
	 * was recommended. This may need changes because right now I do not
	 * properly handle when the user declines the advice or is simply charting
	 * information.
	 */
	public void decisionStored(final ClinicalDecision decision) {
		// TODO Need to have different action if user declines or is just
		// charting
	}

	/**
	 * When the clock job has executed it fires this routine in all listeners.
	 * This simply updates the clock display to show the time interval. If the
	 * time interval is negative, this means the nurse has waited too long, so
	 * the clock should change to a red color instead of blue. Also, if the user
	 * has not used the program for a very long period, we do not want this to
	 * show a ludicrous number like -8889 minutes, so should have a limit on how
	 * much time can be displayed before it is assumed that the patient came off
	 * the protocol.
	 */
	public void jobChanged(final ClockJob job) {
		String ready = job.readyToShowClock();
		if (ready == null) {
			MinutesLeftText.setBackground(SWTResourceManager.getColor(45, 28, 255));
			MinutesLeftText.setText("n/a");
		} else {
			String time = job.getTimeToNextAction();
			if (time.equalsIgnoreCase("00:00"))
				MinutesLeftText.setBackground(SWTResourceManager.getColor(255, 36, 0));
			else
				MinutesLeftText.setBackground(SWTResourceManager.getColor(45, 28, 255));
			MinutesLeftText.setText(time);
		}
	}
}
