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
                String randomString = "()";
                GrammarFromFile grammar = new GrammarFromFile(myReader);
                Parser parser = new Parser();

                int[] counter = {0};

                long startTime = System.nanoTime();
                System.out.println(parser.parseNaive(randomString, grammar, counter));
                long endTime   = System.nanoTime();
                long totalTime = endTime - startTime;
                System.out.println("time: " + totalTime);
                System.out.println("count: " + counter[0] + "\n");

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

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Please provide one or arguments");
        }
    }
}