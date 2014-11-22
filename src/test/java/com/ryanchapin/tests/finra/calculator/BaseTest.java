package com.ryanchapin.tests.finra.calculator;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnit4Mockery;

public class BaseTest
{
	protected static final String SPACE = " ";
	
	protected static final String INPUT_QUEUE_NAME  = "InputQueue";
	protected static final String OUTPUT_QUEUE_NAME = "OutputQueue";
	
	protected static final String PROBLEM1           = "1 + 1 + 2";
	protected static final String PROBLEM1_ANSWER    = PROBLEM1 + " = 4";
	protected static final int    PROBLEM1_ANSER_INT = 4;
	
	protected static final String PROBLEM2           = "2 + 3 - 4";
	protected static final String PROBLEM2_ANSWER    = PROBLEM2 + " = 1";
	protected static final int    PROBLEM2_ANSER_INT = 1;
	
	protected static final String PROBLEM3           = "4 - 9 + 11 + 4";
	protected static final String PROBLEM3_ANSWER    = PROBLEM3 + " = 10";
	protected static final int    PROBLEM3_ANSER_INT = 10;
	
	protected static final String PROBLEM4           = "50 - 75 + 20 - 10";
	protected static final String PROBLEM4_ANSWER    = PROBLEM4 + " = -15";
	protected static final int    PROBLEM4_ANSER_INT = -15;

	protected static final String PROBLEM5           = "1061 - 655 + 10905 - 14 + 17068 + 81";
	protected static final String PROBLEM5_ANSWER    = PROBLEM4 + " = 28446";
	protected static final int    PROBLEM5_ANSER_INT = 28446;
	
	protected JUnit4Mockery mockery = new JUnit4Mockery();
	
	@Mock
	protected ProblemQueue mockInputQueue;
	
	@Mock
	protected ProblemQueue mockOutputQueue;
}
