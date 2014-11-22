package com.ryanchapin.tests.finra.calculator;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link ProblemQueue} interface, which provides a blocking
 * queue in which to provide synchronized access to data between threads.
 * 
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public class ProblemQueueImpl extends WorkerImpl implements ProblemQueue
{
	private static Logger LOGGER = LoggerFactory.getLogger(ProblemQueueImpl.class);
	
	private String name;
	private Queue<String> queue;
	private boolean problemAvailable;
	
	// ------------------------------------------------------------------------
	// Constructor:
	//
	public ProblemQueueImpl(String name)
	{
		super();
		this.name  = name;
		this.queue = new LinkedList<String>();
	}
	
	// ------------------------------------------------------------------------
	// Accessor/Mutators:
	//
	public String getName()
	{
		return name;
	}
	
	public boolean isProblemAvailable()
	{
		return problemAvailable;
	}

	private void setProblemAvailable(boolean problemAvailable)
	{
		this.problemAvailable = problemAvailable;
	}
	
	// ------------------------------------------------------------------------
	// Member Methods:
	//
	public synchronized void addProblem(String problem)
	{
		// Add an item to the queue, indicate that we have work available
		// and then notify any waiting threads.
		LOGGER.debug("problem {} added", problem);
		queue.add(problem);
		setProblemAvailable(true);
		notifyAll();
	}

	public synchronized String getProblem()
	{			
		// If there is no work to be done, invoke wait() on any thread
		// attempting to access the queue.
		while (!isProblemAvailable())
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				LOGGER.error("Unable to invoke Thread.wait(), e = {}", e);
			}
		}
	
		if (queue.isEmpty())
		{
			return null;
		}
		
		// Get the head (first) element in the list
		String problem = queue.poll();
		
		if (queue.isEmpty())
		{
			setProblemAvailable(false);
		}
		
		return problem;
	}

	/**
	 * Enables us to unblock any threads that were waiting on on this object's
	 * monitor which initially waited in the {@link #getProblem()} method.
	 */
	@Override
	public synchronized void shutdown()
	{
		running = false;
		setProblemAvailable(true);
		LOGGER.debug("{} received a shutdown command", name);
		notifyAll();
	}
}
