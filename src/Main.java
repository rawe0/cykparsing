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
                String randomString = "()()()";
                GrammarFromFile grammar = new GrammarFromFile(myReader);
                Parser parser = new Parser();
                System.out.println(parser.ParseBU(randomString, grammar));


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