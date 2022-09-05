import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GrammarFromFile extends Grammar{

    int [][][] nonTerminalToNonTerminals;
    char [] nonTerminalToTerminal;

    HashMap<Integer, Integer> nonTerminalIndexMap;

    ArrayList<String> rules;
    int ruleCount = 0;




    public GrammarFromFile(Scanner input) {
        rules = new ArrayList<>();
        nonTerminalIndexMap = new HashMap<>();


        while (input.hasNextLine()) {
            String data = input.nextLine();
            for (char c: data.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    if (!nonTerminalIndexMap.containsKey((int) c)) {
                        nonTerminalIndexMap.put((int) c, ruleCount);
                        ruleCount++;
                    }
                }
            }
            rules.add(data);
        }
        nonTerminalToNonTerminals = new int[ruleCount][0][0];
        nonTerminalToTerminal = new char[ruleCount];

        for (String rule : rules) {
            String [] split = rule.split("\\s+");

            char outputOne = split[1].charAt(0);
            char inputOne = split[0].charAt(0);

            // Non-terminal rule
            if(Character.isUpperCase(outputOne)){

                int inputValue = (int) inputOne;
                char outputTwo = split[1].charAt(1);
                int outputValueTwo = (int) outputOne;
                int outputValueOne = (int) outputTwo;

                int index = nonTerminalIndexMap.get(inputValue);

                nonTerminalToNonTerminals[index] = Arrays.copyOf(nonTerminalToNonTerminals[index],
                        nonTerminalToNonTerminals[index].length + 1);
                nonTerminalToNonTerminals[index][nonTerminalToNonTerminals[index].length - 1] = new int[] {nonTerminalIndexMap.get(outputValueOne), nonTerminalIndexMap.get(outputValueTwo)};

            // Terminal rule
            } else {
                int inputValue = (int) inputOne;
                int index = nonTerminalIndexMap.get(inputValue);
                nonTerminalToTerminal[index] = outputOne;
            }
        }
        System.out.println(Arrays.toString(nonTerminalToNonTerminals));
        for (int i = 0; i < nonTerminalToNonTerminals.length; i++) {
            System.out.println(Arrays.toString(nonTerminalToNonTerminals[i]));

            for (int j = 0; j < nonTerminalToNonTerminals[i].length; j++) {
                System.out.println(nonTerminalToNonTerminals[i][j][0] + " : " + nonTerminalToNonTerminals[i][j][1]);
            }
        }
        System.out.println(Arrays.toString(nonTerminalToTerminal));

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
