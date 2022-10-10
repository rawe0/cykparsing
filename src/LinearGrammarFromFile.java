import jdk.dynalink.beans.StaticClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class LinearGrammarFromFile{


    char[] terminal;
    int[][][] leftTerminal;
    int[][][] rightTerminal;

    HashMap<Integer, Integer> nonTerminalIndexMap;
    ArrayList<String> rules;
    public int ruleCount;

    public LinearGrammarFromFile(Scanner input) {
        ruleCount = 1;
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

        terminal = new char[ruleCount];
        leftTerminal = new int[ruleCount][0][0];
        rightTerminal = new int[ruleCount][0][0];

        for (String rule : rules) {
            String[] split = rule.split("\\s+");

            char nonTerminal = split[0].charAt(0);
            char charOne = split[1].charAt(0);
            int index = nonTerminalIndexMap.get((int) nonTerminal);

            if (split[1].length() == 1) {
                terminal[index] = charOne;
            } else {
                char charTwo = split[1].charAt(1);
                if (Character.isLowerCase(charOne)) {
                    leftTerminal[index] = Arrays.copyOf(leftTerminal[index],
                            leftTerminal[index].length + 1);
                    leftTerminal[index][leftTerminal[index].length - 1] =
                            new int[]{(int) charOne, nonTerminalIndexMap.get((int) charTwo)};
                } else {
                    rightTerminal[index] = Arrays.copyOf(rightTerminal[index],
                            rightTerminal[index].length + 1);
                    rightTerminal[index][rightTerminal[index].length - 1] =
                            new int[]{nonTerminalIndexMap.get((int) charOne), (int) charTwo};
                }
            }
        }
    }

    public char[] getTerminal() {
        return terminal;
    }

    public int[][][] getLeftTerminal() {
        return leftTerminal;
    }

    public int[][][] getRightTerminal() {
        return rightTerminal;
    }

    public int getRuleCount() {
        return ruleCount;
    }

}
