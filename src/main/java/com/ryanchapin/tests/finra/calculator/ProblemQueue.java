package com.ryanchapin.tests.finra.calculator;

import com.ryanchapin.tests.finra.calculator.io.ProblemReader;
import com.ryanchapin.tests.finra.calculator.io.ProblemWriter;

/**
 * Blocking Queue to store math problems and synchronize their
 * access between the {@link ProblemReader}, {@link ProblemSolver},
 * and {@link ProblemWriter} threads.
 *   
 * @author	Ryan Chapin
 * @since	2014-04-17
 *
 */
public interface ProblemQueue extends Worker
{
	/**
	 * Adds a problem to the queue.
	 * 
	 * @param problem - problem to be added to the queue.
	 */
	public void addProblem(String problem);
	
	/**
	 * Retrieves a problem from the queue
	 * 
	 * @return - problem
	 */
	public String getProblem();

	/**
	 * Returns a boolean to indicate as to whether or not there
	 * are any problems on the queue to be had.
	 * 
	 * @return - whether any problems are available on the queue
	 */
	public boolean isProblemAvailable();	
	
	/**
	 * Get the name/id for the ProblemQueue instance
	 * 
	 * @return - The name field value
	 */
	public String getName();
	
}
