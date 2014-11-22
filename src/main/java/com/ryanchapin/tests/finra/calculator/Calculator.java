package com.ryanchapin.tests.finra.calculator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ryanchapin.tests.finra.calculator.io.ProblemReader;
import com.ryanchapin.tests.finra.calculator.io.ProblemReaderImpl;
import com.ryanchapin.tests.finra.calculator.io.ProblemWriter;
import com.ryanchapin.tests.finra.calculator.io.ProblemWriterImpl;

/**
 * A class that will read math problems from an ASCII text file, line by line
 * passing them to a blocking queue, from which a pool of threads will solve
 * the problems and then write them to another queue from which a writer thread
 * will write them out to another text file.
 * 
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public class Calculator
{
	private static Logger LOGGER = LoggerFactory.getLogger(Calculator.class);
	
	private static final String PROP_FILE = "calculator.properties";
	private static final String PROBLEM_SOLVER_INSTANCES_KEY = "problemsolver.instances";
	
	private static final String INPUT_QUEUE_NAME = "InputQueue";
	private ProblemQueue inputQueue;
	
	private static final String OUTPUT_QUEUE_NAME = "OutputQueue";
	private ProblemQueue outputQueue;
	
	private ProblemReader problemReader;
	private ProblemWriter problemWriter;
	
	private Properties properties;
	
	private String inputFilePath;
	private File   inputFile;
	
	private String outputFilePath;
	private File   outputFile;

	/**
	 * If there is not a properties file available, and/or it is improperly
	 * configured, the default number of {@link ProblemSolver} instances
	 * that should be instantiated.
	 */
	private final static int DEFAULT_NUM_PROBLEM_SOLVERS = 3;
	
	/**
	 * Collection in which we will store the 'worker' instances.
	 */
	private Map<Integer, ProblemSolver> problemSolvers;
	
	/**
	 * Collection in which we will store the 'worker' threads.
	 */
	private List<Thread> problemSolverThreads;
	
	/**
	 * The number of 'worker' threads to instantiate.
	 */
	private int problemSolversCount;
	
	// ------------------------------------------------------------------------
	// Constructor:
	//
	/**
	 * Constructs a newly instantiated Calculator instance
	 * 
	 * @param inputFilePath - The fully qualified path to the input file.
	 * @param outputFilePath - The fully qualified path to the output file.
	 */
	public Calculator(String inputFilePath, String outputFilePath)
	{
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
	}
	
	// ------------------------------------------------------------------------
	// Member Methods:
	//
	/**
	 * Initializes the Calculator instance preparing it for a run.
	 */
	public void init()
	{
		// Read in properties
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream propFis = null;
		propFis = loader.getResourceAsStream(PROP_FILE);
		
		properties = new Properties();
		try	{ properties.load(propFis); }
		catch (IOException e)
		{
			LOGGER.error("Unable to load properties from class loader");
			LOGGER.error("e = {}", e.toString());
		}
		
		try { propFis.close(); }
		catch (IOException e)
		{
			LOGGER.error("Unable to close InputStream propFis");
			LOGGER.error("e = {}", e.toString());
		}
		
		problemSolversCount = DEFAULT_NUM_PROBLEM_SOLVERS;
		String probSolverInstancesString = properties.getProperty(PROBLEM_SOLVER_INSTANCES_KEY);
		if (probSolverInstancesString != null && !(probSolverInstancesString.isEmpty()))
		{
			try { problemSolversCount = Integer.valueOf(probSolverInstancesString); }
			catch (NumberFormatException e)
			{
				LOGGER.warn("Improper input format for {} property", PROBLEM_SOLVER_INSTANCES_KEY);
			}
		}
		LOGGER.info("Configured to instantiate {} number of ProblemSolvers", problemSolversCount);
		
		// Check for our input file
		inputFile = new File(inputFilePath);
		if (!inputFile.isFile())
		{
			LOGGER.error("Input file at {}, does not exist!", inputFilePath);
			System.exit(-1);
		}
		
		inputQueue  = new ProblemQueueImpl(INPUT_QUEUE_NAME);
		outputQueue = new ProblemQueueImpl(OUTPUT_QUEUE_NAME);
	
		// Create our output file, deleting it if it already exists.
		outputFile = new File(outputFilePath);
		if (outputFile.exists())
		{
			outputFile.delete();
		}
		 
		problemReader = new ProblemReaderImpl(inputFile, inputQueue);
		problemWriter = new ProblemWriterImpl(outputFile, outputQueue);

		problemSolvers = new HashMap<Integer, ProblemSolver>();
		for (int i = 0; i < problemSolversCount; i++)
		{
			problemSolvers.put(i, new ProblemSolverImpl(inputQueue, outputQueue, i));
		}
		
		problemSolverThreads = new ArrayList<Thread>();
	}
	
	/**
	 * Will start the various threads running and solve the problems in
	 * the input file.
	 */
	public void calculate()
	{
		// Instantiate and start the reader and writer threads
		Thread readerThread = new Thread(problemReader);
		Thread writerThread = new Thread(problemWriter);
		readerThread.start();
		writerThread.start();
		
		// Instantiate and start the ProblemSolver threads
		for (Map.Entry<Integer, ProblemSolver> entry : problemSolvers.entrySet())
		{
			Thread t = new Thread(entry.getValue());
			problemSolverThreads.add(t);
			t.start();
		}
	
		// Join on the reader thread.  Waiting until it has finished reading the
		// entirety of the input file.
		try	{ readerThread.join(); }
		catch (InterruptedException e) { e.printStackTrace(); }
	
		LOGGER.info("readerThread has finished");

		// Make sure the input queue has drained
		QueueWatcher qwInputQueue = new QueueWatcher(inputQueue);
		Thread qwInputQueueThread = new Thread(qwInputQueue);
		qwInputQueueThread.start();
		try { qwInputQueueThread.join(); }
		catch (InterruptedException e1) { e1.printStackTrace(); }
		LOGGER.info("inputQueue is drained");
		
		// Shutdown the inputQueue and the problem threads
		inputQueue.shutdown();
		for (Map.Entry<Integer, ProblemSolver> entry : problemSolvers.entrySet())
		{
			entry.getValue().shutdown();
		}
		
		for (Thread t : problemSolverThreads)
		{
			try { t.join(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		// Make sure the write queue has drained
		QueueWatcher qwOutputQueue = new QueueWatcher(outputQueue);
		Thread qwOutputQueueThread = new Thread(qwOutputQueue);
		qwOutputQueueThread.start();
		try { qwOutputQueueThread.join(); }
		catch (InterruptedException e1) { e1.printStackTrace(); }
		LOGGER.info("outputQueue is drained");
		
		// Shutdown the outPutQueue writer thread.  This will have
		// the effect of enabling the writerThread to finish
		outputQueue.shutdown();

		try	{ writerThread.join(); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
		LOGGER.info("Calculator.calculate is finished.");
	}
	
	// ------------------------------------------------------------------------
	// Main:
	// 
	public static void main(String[] args)
	{
		// Ensure that we were passed the required number of arguments
		if (args.length < 2)
		{
			LOGGER.error("Insufficient arguments provided.  Exiting");
			System.exit(-1);
		}
		String inputFilePath  = args[0];
		String outputFilePath = args[1];
		LOGGER.info("inputFilePath = '{}', outputFilePath = '{}'",
				inputFilePath, outputFilePath);
		
		Calculator calculator = new Calculator(inputFilePath, outputFilePath);
		calculator.init();
		calculator.calculate();
		
		LOGGER.info("Calculator main is finished");
	}
	
	// ------------------------------------------------------------------------
	// Nested Classes:
	// 
	/**
	 * A Runnable class that will poll a {@link ProblemQueue} waiting until it
	 * no longer contains any elements before running to completion.
	 * 
	 * @author	Ryan Chapin
	 * @since	2014-04-18
	 *
	 */
	public static class QueueWatcher implements Runnable
	{
		private ProblemQueue queue;
		
		/**
		 * Time, in milliseconds that we will wait until polling
		 * the queue to see if it is empty
		 */
		private static final long POLL_WAIT = 100;
		
		public QueueWatcher(ProblemQueue queue)
		{
			this.queue = queue;
		}
		
		/**
		 * Will periodically ping the queue in question until it no longer has
		 * any elements in it.
		 */
		public void run() {
			
			while (queue.isProblemAvailable())
			{
				try { Thread.sleep(POLL_WAIT); }
				catch (InterruptedException e) { e.printStackTrace(); }
			}
			LOGGER.info("Queue {} is empty", queue.getName());
		}
	}
}
