#!/bin/bash

###############################################################################
# Run the Calculator program
#
# name:     run-calculator.sh   
# author:   Ryan Chapin
# created:  2014-04-18
#
###############################################################################
# USAGE:

function about {
   echo "run-calculator.sh - runs the calculator program"
}

function usage {
   echo "usage: run-calculator.sh [input-file] [output-file]"
	echo "       run the calculator program.  Required args are the fully"
	echo "       qualified path to the input (ASCII) text file and an output"
	echo "       file.  Both must be readable, and the latter must be r/w by"
	echo "       the user executing the program."
}

###############################################################################

# java -cp target/classes/:target/calculator-1.0.0.0-SNAPSHOT-jar-with-dependencies.jar com.ryanchapin.tests.finra.calculator.Calculator ./target/test-classes/sample_problems_large.txt /tmp/cal.txt

if [[ -z "$1" ]]; then
	echo "No input file path provided.  Exiting..."
	usage
	exit -1
fi
INPUT_FILE_PATH=$1

if [[ -z "$2" ]]; then
	echo "No output file path provided.  Exiting..."
	usage
	exit -1
fi
OUTPUT_FILE_PATH=$2

# Determine the jar file to run
JAR=`ls lib/*.jar`

java -cp conf/:${JAR}: \
com.ryanchapin.tests.finra.calculator.Calculator \
$INPUT_FILE_PATH $OUTPUT_FILE_PATH

