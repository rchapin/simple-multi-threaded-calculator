package com.ryanchapin.tests.finra.calculator;

/**
 * Class that will read from an input queue and generate answers for
 * a set of addition/subtraction problems with multiple terms and
 * operands.
 * 
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public interface ProblemSolver extends Runnable, Worker
{
	/**
	 * 
	 * @param problem - A String array 
	 * @return - The integer value of the math problem.
	 */
	public int processProblem(String[] problem);
	
}
