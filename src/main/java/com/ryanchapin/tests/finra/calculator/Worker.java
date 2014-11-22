package com.ryanchapin.tests.finra.calculator;

/**
 * Generic worker interface to provide a shutdown mechanism.
 * 
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public interface Worker
{
	/**
	 * Indicate to the working instance to finish it's current work
	 * and to cleanly shutdown.
	 */
	public void shutdown();
}
