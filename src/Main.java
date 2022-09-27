import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class Main {

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


    /**
     * Method to run all tests
     * @param nLengths Number of different lengths to run tests on
     * @param nRuns Number of runs to run per test string
     * @throws IOException Exception from file handling
     */
    private static void runAllTests(int nLengths, int nRuns) throws IOException {

        String[] OCStrings = new String[nLengths];
        String[] OOStrings = new String[nLengths];
        String[] SGStrings = new String[nLengths];
        String[] COCStrings = new String[nLengths];
        String[] OOCStrings = new String[nLengths];

        InputStream SGFile = Main.class.getClassLoader().getResourceAsStream("stupid.txt");
        InputStream WPBFile = Main.class.getClassLoader().getResourceAsStream("well_balanced_parenthesis.txt");

        assert SGFile != null;
        Scanner SGReader = new Scanner(SGFile);
        assert WPBFile != null;
        Scanner WPBReader = new Scanner(WPBFile);

        GrammarFromFile balancedParenthesis = new GrammarFromFile(WPBReader);
        GrammarFromFile stupidGrammar = new GrammarFromFile(SGReader);

        Parser parser = new Parser();

        // Generate testStrings
        for (int i = 0; i < nLengths; i++) {
            OCStrings[i] = generateString(1, i);
            OOStrings[i] = generateString(2, i);
            SGStrings[i] = generateString(3, i);
            OOCStrings[i] = generateString(1, i);
            COCStrings[i] = generateString(2, i);
        }
        for(int i = 0; i < nLengths; i++){
            OOCStrings[i] = ")" + OOCStrings[i];
            COCStrings[i] = COCStrings[i]  + "(";
        }
        //String [] testCases = {"BU_OC", "BU_OO", "BU_SG", "BU_OOC", "BU_COC"};
        String[] testCases = {"BU_OC", "BU_OO", "BU_COC", "BU_OOC", "BU_SG",
                            "TD_OC", "TD_OO", "TD_COC", "TD_OOC", "TD_SG"};
        // Run each testCases
        for (String testCase: testCases) {
            String[][] result = new String[0][];
            System.out.println(testCase);

            // Create file
            try{
                File test = new File(testCase + ".csv");
                if(test.createNewFile()){
                    System.out.println("File created " + test.getName());
                }else{
                    System.out.println("File already exists");
                }
            }catch(IOException e){
                System.out.println("Error while creating file");
                e.printStackTrace();
            }

            // Run test
            switch (testCase) {
                case "TD_SG":
                    result = runTest(SGStrings, stupidGrammar, parser, "TD", nLengths, nRuns);
                    break;
                case ("TD_COC"):
                    result = runTest(COCStrings, balancedParenthesis, parser, "TD", nLengths, nRuns);
                    break;
                case "TD_OOC":
                    result = runTest(OOCStrings, balancedParenthesis, parser, "TD", nLengths, nRuns);
                    break;
                case "TD_OC":
                    result = runTest(OCStrings, balancedParenthesis, parser, "TD", nLengths, nRuns);
                    break;
                case ("TD_OO"):
                    result = runTest(OOStrings, balancedParenthesis, parser, "TD", nLengths, nRuns);
                    break;
                case "BU_SG":
                    result = runTest(SGStrings, stupidGrammar, parser, "BU", nLengths, nRuns);
                    break;
                case ("BU_COC"):
                    result = runTest(COCStrings, balancedParenthesis, parser, "BU", nLengths, nRuns);
                    break;
                case "BU_OOC":
                    result = runTest(OOCStrings, balancedParenthesis, parser, "BU", nLengths, nRuns);
                    break;
                case "BU_OC":
                    result = runTest(OCStrings, balancedParenthesis, parser, "BU", nLengths, nRuns);
                    break;
                case ("BU_OO"):
                    result = runTest(OOStrings, balancedParenthesis, parser, "BU", nLengths, nRuns);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + testCase);
            }
            try {
                FileWriter writer = new FileWriter(testCase + ".csv", true);
                for(int l = 0; l < nLengths; l++){
                    for(int n = 0; n < nRuns; n++){
                        // Length, Run, Time, Counter
                        writer.write(String.format("%d, %d, %s\n", ((l+1)*100), (n+1), result[l][n]));
                    }
                }
                writer.close();
            } catch (IOException e){
                System.out.println("An error occurred while writing");
                e.printStackTrace();
            }
        }
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
        long[] counter = {0};
        String [][] result = new String[nLengths][nRuns];

        System.out.println("Method: " + parseMethod + " Max length: " +
                ((nLengths)*100) + " Number of runs: " + nRuns);

        for(int l = 0; l < nLengths; l++){

            // Dry run
            System.out.println("    Dry run for length: " + (l+1)*100);
            counter[0] = 0;


            if(parseMethod.equals("BU")){
                parser.parseBU(testStrings[l], g, counter);
            } else if (parseMethod.equals("TD")){
                parser.parseTD(testStrings[l], g, counter);
            }

            // Real runs
            for(int i = 0; i < nRuns; i++){

                boolean wasParsed = true;

                System.out.println("        Run: " + i + "...");
                // Reset counter and start time
                counter[0] = 0;
                long startTime = System.nanoTime();

                // Parse grammar
                if(parseMethod.equals("BU")){
                    wasParsed = parser.parseBU(testStrings[l], g, counter);
                } else if (parseMethod.equals("TD")){
                    wasParsed = parser.parseTD(testStrings[l], g, counter);
                }

                // Calculate time
                long endTime   = System.nanoTime();
                long totalTime = endTime - startTime;

                // Store result
                result[l][i] = String.format("%d, %d", totalTime, counter[0]);
                if(wasParsed){
                    System.out.println(ANSI_GREEN + "        Time, Counter: " + result[l][i] + ANSI_RESET);
                }else{
                    System.out.println(ANSI_RED + "        Time, Counter: " + result[l][i] + ANSI_RESET);
                }
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
