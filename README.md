# Sample Calculator Java Application

**By:** Ryan Chapin <rchapin@nbinteractive.com>, 2014-04-19

This a sample project built as a coding test for FINRA.

The program is comprised of three sets of threads:

1. **Reader Thread**:  Will read math problems from an ASCII text file, line-by-line, writing them to an input queue.
2. **Problem Solver Threads**:  N number of ProblemSolver threads will read from the input queue, solving problems and then writing them to an output queue.  The number of threads is configurable, see the **calculator.properties**, problemsolver.instances property in the ```conf/``` dir of the distribution and in the ```src/main/resources dir``` in the source tree.
3. **Writer Thread**:  Will read from the output queue and write the solved problems, one line at a time, to an output file.

## Development Environment Set-up

This project was developed with Eclipse Kepler, but can be compiled from the command line with maven.

### To set up to develop and run from within Eclipse (requires m2e and m2e slf4j Eclipse plugins)

1. From within Eclipse, go to File -> Import and select, Maven -> Existing Maven Projects and click 'Next'.
2. Then browse to the calculator/ dir and select it.

### Running from within Eclipse

There is an eclipse launcher file included in ```src/test/resources/launchers/Calculator.launch```.  To be able to use it properly, define the following String Substitution variables:

1.  Go to Window -> Preferences and Click on Run/Debug -> String Substitution.
2.  Add the following String variables:

	- **CALCULATOR_INFILE** The fully qualified path to the input file.  Must have read permissions.
	- **CALCULATOR_OUTFILE** The fully qualified path to the output file.  Must have read/write permissions.

Inside the src/main/resources directory are a set of sample input files.

## Building a Distribution

To build, simply run the following command in the calculator directory

```
# mvn clean package && mvn assembly:assembly
```

This will build the project and create a distribution .zip file in the target/ directory

## To Run

1.  Unpack the zip file: ```# unzip calculator-n.n.n.n-SNAPSHOT-dist.zip```
2.  ```#  cd calculator-n.n.n.n-SNAPSHOT/```
3.  ```#  /run-calculator.sh [path-to-input-file] [path-to-output-file]```
4.  If running on a Windows machine cd into the ```calculator-n.n.n.n-SNAPSHOT\``` directory and invoke the following (changing the version number to match the version of your distro).
```
> java -cp conf\;lib\calculator-1.0.0.0-SNAPSHOT-jar-with-dependencies.jar com.ryanchapin.tests.finra.calculator.Calculator sample_data\input.txt .\out.txt
```

