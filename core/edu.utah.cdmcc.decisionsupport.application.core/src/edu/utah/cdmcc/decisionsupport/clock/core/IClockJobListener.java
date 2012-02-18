package edu.utah.cdmcc.decisionsupport.clock.core;


/**
 * Interface for any object that wants to listen to the
 * clock that indicates to the nurse how long it will be
 * before the next decision should be made.
 * 
 * @author J. Michael Dean, M.D., M.B.A. (University of Utah)
 * Copyright 2005 - 2008.  All Rights Reserved.
 *
 */
public interface IClockJobListener {
	 void jobChanged(final ClockJob job);
}
