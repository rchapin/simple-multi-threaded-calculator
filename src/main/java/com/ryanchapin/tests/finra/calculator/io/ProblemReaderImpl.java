package com.ryanchapin.tests.finra.calculator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ryanchapin.tests.finra.calculator.ProblemQueue;
import com.ryanchapin.tests.finra.calculator.WorkerImpl;

/**
 * 
 * @author	Ryan Chapin
 * @since	2014-04-18
 *
 */
public class ProblemReaderImpl extends WorkerImpl implements ProblemReader
{
	private static Logger LOGGER = LoggerFactory.getLogger(ProblemReader.class);
	private File inputFile;
	private ProblemQueue inputQueue;
	
	public ProblemReaderImpl(
			File inputFile,
			ProblemQueue inputQueue)
	{
		super();
		this.inputFile  = inputFile;
		this.inputQueue = inputQueue;
	}
	
	/**
	 * Will read the {@link #inputFile} line by line writing the output to
	 * the {@link #inputQueue}.
	 */
	public void run()
	{
		FileReader fr = null;
		try	{ fr = new FileReader(inputFile); }
		catch (FileNotFoundException e) { e.printStackTrace(); }
		
		BufferedReader br = null;
		br = new BufferedReader(fr);
		
		String line = null;
		try
		{
			while ( (line = br.readLine()) != null )
			{
				LOGGER.debug("line = {}", line);
				inputQueue.addProblem(line);
			}
		} catch (IOException e) {
			LOGGER.error("Unable to continue reading from {}",
					inputFile.getAbsolutePath());
			LOGGER.error("BufferedReader.readLine() threw e = {}", e.toString());
		}

		try
		{
			br.close();
			fr.close();
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
