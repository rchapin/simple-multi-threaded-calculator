package com.ryanchapin.tests.finra.calculator;

/**
 * Base implementation of the {@link Worker} interface.
 * 
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public abstract class WorkerImpl implements Worker
{
	protected boolean running;

	public WorkerImpl() { this.running = true;	}
	
	public void shutdown() { running = false; }
}
