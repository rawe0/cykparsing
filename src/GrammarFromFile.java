import java.util.Scanner;

public class GrammarFromFile extends Grammar{





    public GrammarFromFile(Scanner input) {
        while (input.hasNextLine()) {
            String data = input.nextLine();
            System.out.println(data);
        }
    }

    @Override
    public char getTerminalRule() {
        return 0;
    }

    @Override
    public int getNonTerminalRule() {
        return 0;
    }
}
