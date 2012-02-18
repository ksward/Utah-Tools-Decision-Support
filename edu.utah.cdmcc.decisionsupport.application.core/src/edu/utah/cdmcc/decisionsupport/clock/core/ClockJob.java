package edu.utah.cdmcc.decisionsupport.clock.core;

import java.util.GregorianCalendar;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;
import core.decision.object.ClinicalDecision;
import core.decision.object.IDecisionListener;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;


/**
 * This is a class to keep track of how much time remains before the nurse or
 * doctor is supposed to reevaluate the patient in the decision support system.
 * It is a UIJob because the SWT clock object must listen to its events. When
 * this job executes, it fires an event to clock job listeners that can then
 * display the time, etc.
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah) Copyright 2005 -
 *         2008. All Rights Reserved.
 * 
 */
public class ClockJob extends UIJob implements IDecisionListener {
	private Boolean running = true;
	private ListenerList jobListeners;
	private ClinicalDecision clockedDecision;
	private int sleepSeconds = 2;
	//private boolean clockExpired;

	/**
	 * Registers the job to listen to decision changes because when a decision
	 * is charted this clock should start itself.
	 * 
	 * @param name -
	 *            passed to the super
	 */
	public ClockJob(String name) {
		super(name);
		ApplicationControllers.getDecisionController().addDecisionFiredListener(this);
	}

	/**
	 * The meat of the job is here. Calculates the time interval, fires the
	 * event, and then schedules itself to run again in 10 secondsLeft.
	 */
	@Override
	public IStatus runInUIThread(final IProgressMonitor monitor) {
		fireClockJobChanged(this);
		schedule(sleepSeconds * 1000);
		return Status.OK_STATUS;
	}

	/**
	 * This is checked by underlying UIJob code to verify that the job manager
	 * should allow the job to be scheduled.
	 */
	public boolean shouldSchedule() {
		return running;
	}

	/**
	 * Method to stop the job if necessary. Right now I am not sure how this
	 * might be used.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Dummy method to fit interface of decision listener. Again, I could have
	 * made an adaptor for this but seems overkill.
	 */
	public void decisionChanged(final ClinicalDecision decision) {
		this.cancel();
		clockedDecision = decision;
		sleepSeconds = 2;
		//clockExpired = false;
		this.schedule();
	}

	/**
	 * Method when a decision is stored. Sets up the parameters that are needed
	 * for the job run - calculates the target time for the next decision and
	 * then schedules itself to run.
	 */
	public void decisionStored(final ClinicalDecision decision) {
		this.cancel();
		clockedDecision = decision;
		sleepSeconds = 2;
		//clockExpired = false;
		this.schedule();
	}

	/**
	 * Get time interval in minutsLeft
	 * 
	 * @return time interval in minutsLeft
	 */
	public String getTimeToNextAction() {
		int minutesToAction = clockedDecision.getMinutesToNextEvaluation();
		GregorianCalendar decisionTime = clockedDecision.getDecisionTimeStamp();
		GregorianCalendar currentTime = new GregorianCalendar();
		long diffSeconds = (currentTime.getTimeInMillis() - decisionTime.getTimeInMillis()) / 1000;
		long actionSeconds = minutesToAction * 60;
		if (diffSeconds >= actionSeconds)
			return "00:00";
		long totalSecondsLeft = actionSeconds - diffSeconds;
		long minutesLeft = totalSecondsLeft / 60;
		long secondsLeft = totalSecondsLeft % 60;
		String time = "";
		if (minutesLeft < 10)
			time = time + "0" + minutesLeft + ":";
		else
			time = time + minutesLeft + ":";
		if (secondsLeft < 10)
			time = time + "0" + secondsLeft;
		else
			time = time + secondsLeft;
		if (minutesLeft == 0 && secondsLeft < 60)
			sleepSeconds = 1;
		return time;
	}

	/**
	 * Get the list of job listeners
	 * 
	 * @return list of job listeners
	 */
	public ListenerList getJobListeners() {
		return jobListeners;
	}

	/**
	 * Set the job listeners
	 * 
	 * @param jobListeners
	 */
	public void setJoblisteners(final ListenerList jobListeners) {
		this.jobListeners = jobListeners;
	}

	/**
	 * Add a job listener to the listeners list; if the listener list does not
	 * exist, create one first.
	 * 
	 * @param listener
	 */
	public void addJobListener(final IClockJobListener listener) {
		if (jobListeners == null) {
			jobListeners = new ListenerList();
		}
		jobListeners.add(listener);
	}

	/**
	 * Remove listener; if listener list is emptied, then destroy.
	 * 
	 * @param listener
	 */
	public void removeJobListener(final IClockJobListener listener) {
		if (jobListeners != null) {
			jobListeners.remove(listener);
			if (jobListeners.isEmpty())
				jobListeners = null;
		}
	}

	/**
	 * Notify all the listeners.
	 * 
	 * @param job
	 */
	public void fireClockJobChanged(final ClockJob job) {
		if (jobListeners == null)
			return;
		Object[] rls = jobListeners.getListeners();
		for (Object o : rls) {
			IClockJobListener listener = (IClockJobListener) o;
			listener.jobChanged(job);
		}
	}

	public String readyToShowClock() {
		if (clockedDecision == null)
			return null;
		if (clockedDecision.getUserAction().equalsIgnoreCase(ClinicalDecision.ACCEPT))
			return ClinicalDecision.ACCEPT;
		if (clockedDecision.getUserAction().equalsIgnoreCase(ClinicalDecision.DECLINE))
			return ClinicalDecision.DECLINE;
		if (clockedDecision.getUserAction().equalsIgnoreCase(ClinicalDecision.PASSIVE))
			return ClinicalDecision.PASSIVE;
		return null;
	}

}
