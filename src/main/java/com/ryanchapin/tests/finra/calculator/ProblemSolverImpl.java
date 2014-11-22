package com.ryanchapin.tests.finra.calculator;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link ProblemSolver} that will solve multi-term,
 * multi-operand addition and subtraction problems of the following format:
 * 
 * 1 + 4 - 9 - 10 + 10
 * 
 * With ints and operands separated by single spaces.  The leading int is
 * always positive.
 *  
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public class ProblemSolverImpl extends WorkerImpl implements ProblemSolver
{
	private static Logger LOGGER = LoggerFactory.getLogger(ProblemSolverImpl.class);
	private static final String SPACE = " ";
	
	private int id;
	private ProblemQueue inputQueue;
	private ProblemQueue outputQueue;
	
	public ProblemSolverImpl(
			ProblemQueue inputQueue,
			ProblemQueue outputQueue,
			int id)
	{
		super();
		this.inputQueue  = inputQueue;
		this.outputQueue = outputQueue;
		this.id          = id;
	}

	public void run() {
		
		while (running)
		{
			// Get a problem from the inputQueue
			String problem = inputQueue.getProblem();
			
			if (problem == null || problem.isEmpty())
			{
				continue;
			}
			
			// Solve the problem
			LOGGER.debug("{} Solving problem {}", id, problem);
			String[] p = problem.split(SPACE);
			int answer = processProblem(p);

			// Write it to the outputQueue
			outputQueue.addProblem(problem + " = " + answer);
		}
		
		LOGGER.info("ProblemSolver instance {}, finished working....", id);
	}
	
	public int processProblem(String[] problem)
	{
		int retVal = 0;
		
		// How many terms and/or operands are in the problem?
		int termOpCount = problem.length;

		// Get the term that we will be operating on
		int x = Integer.valueOf(problem[(termOpCount - 1)]);
		Operand op = null;
		
		if (termOpCount >= 3) {
			//
			// Working from the 'end' of the array to the 'beginning' we will
			// examine each pair of operand and term to determine if we need
			// to change the sign of the term to be negative as all of our
			// calculations will be additions.
			//
			// Get the operand
			op = Operand.getOperand(problem[(termOpCount - 2)]);
			
			// Get the remainder of the problem for the next level of recursion
			String[] problemRemaining = Arrays.copyOfRange(problem, 0, termOpCount - 2);
			
			// We only need to manipulate the term if we are subtracting
			if (op == Operand.SUBTRACT)
			{
				x = -x;
			}

			// Add our value to the result of the remaining problem.
			retVal = x + (processProblem(problemRemaining));
			
		} else {
			// If our termOpCount do not include at least three elements we
			// simply need to return the last (first) number in the problem. 
			return x;
		}
		return retVal;		
	}
	
	/**
	 * Enumerated type that equates to the types of operands that are currently
	 * supported by the {@link ProblemSolver}.
	 * 
	 * @author	Ryan Chapin
	 * @since	2014-04-18
	 * 
	 */
	public enum Operand
	{
		ADD("+"),
		SUBTRACT("-");
		
		private String operand;
		
		private Operand(String name) { this.operand = name; }
		
		public String getName() { return operand; }
		
		public static Operand getOperand(String operand){
			for (Operand op : Operand.values())
			{
				if (op.getName().equals(operand))
				{
					return op;
				}	
			}
			return null;
		}
	}
}
