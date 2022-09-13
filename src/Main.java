import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        if (args.length == 0){

        }
        else if(args.length == 1){
            try {
                File myObj = new File(args[0]);
                Scanner myReader = new Scanner(myObj);
                String randomString = ")()()()()()";
                GrammarFromFile grammar = new GrammarFromFile(myReader);
                Parser parser = new Parser();

                int[] counter = {0};

                long startTime = System.nanoTime();
                System.out.println(parser.parseNaive(randomString, grammar, counter));
                long endTime   = System.nanoTime();
                long totalTime = endTime - startTime;
                System.out.println("time: " + totalTime);
                System.out.println("count: " + counter[0] + "\n");

                String[] closedStrings = new String[25];
                String[] openStrings = new String[25];
                String[] stupidGrammarStrings = new String[25];
                for (int i = 0; i < 25; i++) {
                    closedStrings[i] = generateString(1, i);
                    openStrings[i] = generateString(2, i);
                    stupidGrammarStrings[i] = generateString(3, i);
                }

                counter[0] = 0;
                startTime = System.nanoTime();
                System.out.println(parser.parseTD(randomString, grammar, counter));
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.println("time: " + totalTime);
                System.out.println("count: " + counter[0] + "\n");


                counter[0] = 0;
                startTime = System.nanoTime();
                System.out.println(parser.parseBU(randomString, grammar, counter));
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.println("time: " + totalTime);
                System.out.println("count: " + counter[0] + "\n");

                myReader.close();
                System.out.println(generateString(1, 0));
                System.out.println(generateString(1, 1));
                System.out.println(generateString(2, 0));
                System.out.println(generateString(2, 1));
                System.out.println(generateString(3, 0));
                System.out.println(generateString(3, 1));

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Please provide one or arguments");
        }
    }
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
