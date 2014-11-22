package com.ryanchapin.tests.finra.calculator.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ryanchapin.tests.finra.calculator.ProblemQueue;
import com.ryanchapin.tests.finra.calculator.WorkerImpl;

/**
 * Implementation class for the {@link ProblemWriter} interface.
 * 
 * @author	Ryan Chapin
 * @since	2014-04-14
 *
 */
public class ProblemWriterImpl extends WorkerImpl implements ProblemWriter
{
	private static Logger LOGGER = LoggerFactory.getLogger(ProblemWriter.class);
	private File outputFile;
	private ProblemQueue outputQueue;
	
	public ProblemWriterImpl(File outputFile, ProblemQueue outputQueue)
	{
		this.outputFile  = outputFile;
		this.outputQueue = outputQueue;
	}
	
	public void run()
	{
		FileWriter fw = null;
		try { fw = new FileWriter(outputFile); }
		catch (IOException e) { e.printStackTrace(); }
		
		BufferedWriter bw = null;
		bw = new BufferedWriter(fw);
		
		while (running)
		{
			String problem = outputQueue.getProblem();
			LOGGER.debug("ProblemWriter received problem = {}", problem);
			
			if (problem == null || problem.isEmpty())
			{
				break;
			}
			try
			{
				bw.write(problem);
				bw.newLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try {
			bw.close();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		LOGGER.info("ProblemWriter instance finished....");
	}
}