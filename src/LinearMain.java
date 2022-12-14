import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class to simply run the scripts one by one
 */
public class LinearMain {
    public static void main(String[] args) throws IOException {

        if (args.length == 4) {

            String grammarLocation = args[0];
            String stringLocation = args[1];
            String parseMethod = args[2];
            int n = Integer.parseInt(args[3]);

            File grammarFile = new File(grammarLocation);
            File stringFile = new File(stringLocation);

            Scanner grammarInput = new Scanner(grammarFile);
            Scanner stringInput = new Scanner(stringFile);


            LinearGrammarFromFile grammar = new LinearGrammarFromFile(grammarInput);
            LinearParser linearParser = new LinearParser(grammar);

            GrammarFromFile CNFGrammar = GrammarFromFile.fromLinearGrammar(grammar);
            Parser CNFParser = new Parser(CNFGrammar);

            ArrayList<String> strings = new ArrayList<>();

            while (stringInput.hasNextLine()) {
                strings.add(stringInput.nextLine());
            }
            String[] stringArray = strings.toArray(new String[0]);

            runTest(stringArray, CNFParser, linearParser, parseMethod, stringArray.length, n);


        } else {
            System.out.println("Please provide 04arguments" +
                    "\nUsage: <Input file for grammar to run>" +
                    " <Input file for strings to be parsed>" +
                    " <Method to parse strings with>" +
                    " <Number of times to parse each string>");
            System.exit(1);
        }
    }

    /**
     *
     * @param testStrings The test strings
     * @param g The grammar
     * @param CNFParser The CNFParser
     * @param parseMethod The parsing method
     * @param nStrings number of test strings
     * @param nRuns number of test runs per string
     * @return an array of the results
     */
    private static String[][] runTest(String[] testStrings, Parser CNFParser, LinearParser linearParser,
                                      String parseMethod, int nStrings, int nRuns){

        String [][] result = new String[nStrings][nRuns];

        for(int l = 0; l < nStrings; l++){

            // Reset counter
            CNFParser.resetCounter();
            linearParser.resetCounter();
            // Dry run
            switch (parseMethod) {
                case "TD_L":
                    linearParser.parseLinearTD(testStrings[l]);
                    break;
                case "BU_C":
                    CNFParser.parseBU(testStrings[l]);
                    break;
                case "TD_C":
                    CNFParser.parseTD(testStrings[l]);
                    break;
                case "N_C":
                    CNFParser.parseNaive(testStrings[l]);
                    break;
            }

            // Real runs
            for(int i = 0; i < nRuns; i++){

                // Reset counter and start time
                long startTime = System.nanoTime();

                // Reset counters
                CNFParser.resetCounter();
                linearParser.resetCounter();

                // Parse grammar
                switch (parseMethod) {
                    case "TD_L":
                        linearParser.parseLinearTD(testStrings[l]);
                        break;
                    case "BU_C":
                        CNFParser.parseBU(testStrings[l]);
                        break;
                    case "TD_C":
                        CNFParser.parseTD(testStrings[l]);
                        break;
                    case "N_C":
                        CNFParser.parseNaive(testStrings[l]);
                        break;
                }

                // Calculate time
                long endTime   = System.nanoTime();
                long totalTime = endTime - startTime;

                long count; // Number of operations
                if(parseMethod.equals("TD_L")){
                    count = linearParser.getCount();
                } else {
                    count = CNFParser.getCount();
                }
                // Store result
                result[l][i] = String.format("%d, %d, %d, %d",testStrings[l].length(), (i+1), totalTime, count);
                System.out.println(result[l][i]);
            }
        }
        return result;
    }


}
