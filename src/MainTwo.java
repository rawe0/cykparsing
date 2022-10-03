import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class MainTwo {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {

            runAllTests(25, 10);

        } else if (args.length == 1) {

            int nRuns = Integer.parseInt(args[0]);

            runAllTests(25, nRuns);

        } else if (args.length == 2) {

            int nRuns = Integer.parseInt(args[0]);
            int nLength = Integer.parseInt(args[1]);

            runAllTests(nLength, nRuns);

        } else {
            System.out.println("Please provide 0-2 arguments" +
                    "\nUsage: <number of runs> (default 10) <max length> (default 25, would be *100 so 25 = 2500)");
            System.exit(1);
        }
    }
    private static void runNaiveTests(int nLengths, int nRuns){

    }
    /**
     * Method to run all tests
     * @param nLengths Number of different lengths to run tests on
     * @param nRuns Number of runs to run per test string
     * @throws IOException Exception from file handling
     */
    private static void runAllTests(int nLengths, int nRuns) throws IOException {

    }

    /**
     *
     * @param testStrings The test strings
     * @param g The grammar
     * @param parser The parser
     * @param parseMethod The parsing method
     * @param nLengths number of test strings
     * @param nRuns number of test runs per string
     * @return an array of the results
     */
    private static String[][] runTest(String[] testStrings, Grammar g, Parser parser,
                                      String parseMethod, int nLengths, int nRuns){

        String [][] result = new String[nLengths][nRuns];

        for(int l = 0; l < nLengths; l++){

            // Reset counter
            parser.resetCounter();

            // Dry run
            switch (parseMethod) {
                case "BU":
                    parser.parseBU(testStrings[l]);
                    break;
                case "TD":
                    parser.parseTD(testStrings[l]);
                    break;
                case "N":
                    parser.parseNaive(testStrings[l]);
                    break;
            }

            // Real runs
            for(int i = 0; i < nRuns; i++){

                // Reset counter and start time
                long startTime = System.nanoTime();

                // Reset counter
                parser.resetCounter();

                // Parse grammar
                switch (parseMethod) {
                    case "BU":
                        parser.parseBU(testStrings[l]);
                        break;
                    case "TD":
                        parser.parseTD(testStrings[l]);
                        break;
                    case "N":
                        parser.parseNaive(testStrings[l]);
                        break;
                }

                // Calculate time
                long endTime   = System.nanoTime();
                long totalTime = endTime - startTime;

                // Store result
                result[l][i] = String.format("%d, %d", totalTime, parser.getCount());
                System.out.println(ANSI_RED + "        Time, Counter: " + result[l][i] + ANSI_RESET);

            }

        }
        return result;
    }

    /**
     * Private method to generate test strings
     * @param type Type of String
     * @param n Number of repetitions
     * @return the generated string
     */
    private static String generateString(int type, int n){
        StringBuilder base = null;
        if(type == 1){
            base = new StringBuilder("()()()()()()()()()()()()()()()()()()()()()()()()()()()" +
                    "()()()()()()()()()()()()()()()()()()()()()()()()()()()()");
            base.append(String.valueOf(base).repeat(n));
        }else if (type == 2){
            StringBuilder leftBase = new StringBuilder("((((((((((((((((((((((((((((((((((((((((((((((((((");
            StringBuilder rightBase = new StringBuilder("))))))))))))))))))))))))))))))))))))))))))))))))))");
            leftBase.append(String.valueOf(leftBase).repeat(n));
            rightBase.append(String.valueOf(rightBase).repeat(n));
            base = leftBase.append(rightBase);
        }else if (type == 3){
            base = new StringBuilder("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            base.append(String.valueOf(base).repeat(n));
        }
        return String.valueOf(base);
    }

}
