import java.util.*;

public class GrammarFromFile extends Grammar{

    int [][][] nonTerminalToNonTerminals;
    char [] tFromN;

    int [][] nonTerminalsToNonTerminal;
    HashMap<Character, ArrayList<Integer>> nFromT;

    HashMap<Integer, Integer> nonTerminalIndexMap;
    ArrayList<String> rules;
    public int ruleCount;




    public GrammarFromFile(Scanner input) {
        ruleCount = 1;
        rules = new ArrayList<>();
        nonTerminalIndexMap = new HashMap<>();
        nFromT = new HashMap<>();


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
        tFromN = new char[ruleCount];
        nonTerminalsToNonTerminal = new int[ruleCount][ruleCount];

        for (String rule : rules) {
            String [] split = rule.split("\\s+");

            char outputOne = split[1].charAt(0);
            char inputOne = split[0].charAt(0);

            // Non-terminal rule
            if(Character.isUpperCase(outputOne)){

                int index = nonTerminalIndexMap.get((int) inputOne);
                nonTerminalToNonTerminals[index] = Arrays.copyOf(nonTerminalToNonTerminals[index],
                        nonTerminalToNonTerminals[index].length + 1);

                if (split[1].length() == 2){
                    char outputTwo = split[1].charAt(1);
                    nonTerminalToNonTerminals[index][nonTerminalToNonTerminals[index].length - 1] = new int[]
                            {nonTerminalIndexMap.get((int) outputOne), nonTerminalIndexMap.get((int) outputTwo)};

                    nonTerminalsToNonTerminal[nonTerminalIndexMap.get((int) outputOne)][nonTerminalIndexMap.get((int) outputTwo)] = index;
                } else {
                    nonTerminalsToNonTerminal[nonTerminalIndexMap.get((int) outputOne)][0] = index;
                    nonTerminalToNonTerminals[index][nonTerminalToNonTerminals[index].length - 1] =
                            new int[] {nonTerminalIndexMap.get((int) outputOne)};
                }

            // Terminal rule
            } else {
                int index = nonTerminalIndexMap.get((int) inputOne);
                tFromN[index] = outputOne;
            }
        }

        // Create the reverse mapping for terminals
        for (int i = 0; i < tFromN.length; i++) {
            ArrayList<Integer> nRules = nFromT.computeIfAbsent(tFromN[i], k -> new ArrayList<>());
            nRules.add(i);
        }
        for(Map.Entry<Character, ArrayList<Integer>> entry: nFromT.entrySet()){
        }
    }

    @Override
    public char getTRuleFromNRule(int nRule) {
        return tFromN[nRule];
    }

    public int getRuleCount(){
        return ruleCount;
    }

    @Override
    public ArrayList<Integer> getNRulesFromTRule(char tRule) {
        return nFromT.get(tRule);
    }

    @Override
    public int[][] getArraysFromNRule(int nRule) {
        return nonTerminalToNonTerminals[nRule];
    }

    @Override
    // Will return 0 if not found.
    public int[][] getRuleFromArray() {
        return nonTerminalsToNonTerminal;
    }
}
