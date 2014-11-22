package com.ryanchapin.tests.finra.calculator;

import java.util.LinkedList;
import java.util.Queue;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProblemSolverTest extends BaseTest
{
	@Before
	public void setUp()
	{
		mockInputQueue  = mockery.mock(ProblemQueue.class, INPUT_QUEUE_NAME);
		mockOutputQueue = mockery.mock(ProblemQueue.class, OUTPUT_QUEUE_NAME);
	}

	/**
	 * Tests whether or not a ProblemQueue instance will properly read, and then
	 * write the correct answer to a ProblemQueue in the expected format. 
	 */
	@Test
	public void runProblemSolverReadAndWrite_success()
	{		
		final ProblemSolver ps = new ProblemSolverImpl(mockInputQueue, mockOutputQueue, 0);
		
		mockery.checking(new Expectations() {{
			exactly(1).of(mockInputQueue).getProblem();
			will(returnValue(PROBLEM1));
			exactly(1).of(mockInputQueue).getProblem();
			will(new callShutdownOnProblemSolver(ps));
		}});
		
		mockery.checking(new Expectations() {{
			exactly(1).of(mockOutputQueue).addProblem(PROBLEM1_ANSWER);
		}});
	
		ps.run();
	}
	
	/**
	 * Tests a ProblemSolver's processProblem algorithm.
	 */
	@Test
	public void problemSolverProcessProblems_success()
	{
		ProblemSolver ps = new ProblemSolverImpl(mockInputQueue, mockOutputQueue, 0);
		
		int answer_1 = ps.processProblem(PROBLEM1.split(SPACE));
		assertEquals(PROBLEM1_ANSER_INT, answer_1);
		
		int answer_2 = ps.processProblem(PROBLEM2.split(SPACE));
		assertEquals(PROBLEM2_ANSER_INT, answer_2);
		
		int answer_3 = ps.processProblem(PROBLEM3.split(SPACE));
		assertEquals(PROBLEM3_ANSER_INT, answer_3);
		
		int answer_4 = ps.processProblem(PROBLEM4.split(SPACE));
		assertEquals(PROBLEM4_ANSER_INT, answer_4);
		
		int answer_5 = ps.processProblem(PROBLEM5.split(SPACE));
		assertEquals(PROBLEM5_ANSER_INT, answer_5);
	}
	
	@After
	public void tearDown()
	{
		mockInputQueue  = null;
		mockOutputQueue = null;
	}
	
	// ------------------------------------------------------------------------
	// Nested Classes:
	
	public class ProblemQueueMock implements ProblemQueue
	{
		public Queue<String> queue;
		
		public ProblemQueueMock() { queue = new LinkedList<String>(); }
		
		public void shutdown() {}

		public void addProblem(String problem) { queue.add(problem); }

		public String getProblem() { return queue.poll(); }

		public boolean isProblemAvailable() { return false; }

		public String getName() { return null; }
	}
	
	/**
	 * Mock Action instance to enable us to send a shutdown call to
	 * a ProblemSolver instance.
	 * 
	 * @author	Ryan Chapin
	 * @since	2014-04-18
	 *
	 */
	public class callShutdownOnProblemSolver implements Action
	{
		private ProblemSolver problemSolver;
		
		public callShutdownOnProblemSolver(ProblemSolver problemSolver)
		{
			this.problemSolver = problemSolver;
		}

		public void describeTo(Description arg0)
		{
			arg0.appendText("Will invoke .shutdown() on the ProblemSolver reference");			
		}

		public Object invoke(Invocation invocation) throws Throwable
		{
			problemSolver.shutdown();
			return null;
		}
	}
}
