package com.ryanchapin.tests.finra.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProblemQueueImplTest extends BaseTest {
	
	/**
	 * Tests that we can add problems to the ProblemQueue
	 */
	@Test
	public void addProblemTest_success()
	{
		ProblemQueue pq = new ProblemQueueImpl(INPUT_QUEUE_NAME);
		
		pq.addProblem(PROBLEM1);
		pq.addProblem(PROBLEM2);
		pq.addProblem(PROBLEM3);
		
		assertEquals(true, pq.isProblemAvailable());
	}
	
	/**
	 * Tests that we can retrieve problems in order from
	 * a ProblemQueue
	 */
	@Test
	public void getProblemsInOrderTest_success()
	{
		ProblemQueue pq = new ProblemQueueImpl(INPUT_QUEUE_NAME);
		pq.addProblem(PROBLEM1);
		pq.addProblem(PROBLEM2);
		pq.addProblem(PROBLEM3);
		
		// Each should come off in the order in which they
		// were added.
		assertEquals(PROBLEM1, pq.getProblem());
		assertEquals(PROBLEM2, pq.getProblem());
		assertEquals(PROBLEM3, pq.getProblem());
	}
	
	/**
	 * Tests whether or not the ProblemSolverImpl will return false
	 * when asked if there are problems available from an empty queue.
	 */
	@Test
	public void properyReturnsIsProblemAvailable()
	{
		ProblemQueue pq = new ProblemQueueImpl(INPUT_QUEUE_NAME);
		assertEquals(false, pq.isProblemAvailable());
	}
}
