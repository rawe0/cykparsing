import java.util.*;

public class GrammarFromFile extends Grammar{

    int [][][] nonTerminalToNonTerminals;
    int [][] nonTerminalsToNonTerminal;
    char [] tFromN;
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
    }

    public static fromLinearGrammar(LinearGrammarFromFile linearGrammar){

        char[] terminals = linearGrammar.getTerminal();
        int [][][] leftTerminals = linearGrammar.getLeftTerminal();
        int [][][] rightTerminals = linearGrammar.getLeftTerminal();
        int nRules = linearGrammar.getRuleCount();
        HashMap<Character, Integer> terminalIndexMap = new HashMap<>();
        HashSet<Character> nonTerminalsToCreate = new HashSet<>();

        for (int i = 0; i < terminals.length; i++) {
            terminalIndexMap.put(terminals[i], i);
        }




        for(int i = 0; i < nRules; i++){

            char leftChar;
            char rightChar;

            for(int j = 0; j < leftTerminals[i].length; j++){
                leftChar = (char) leftTerminals[i][j][0];
            }
            for(int j = 0; j < rightTerminals[i].length; j++){
                rightChar = (char) rightTerminals[i][j][0];
            }
            Integer leftIndex = terminalIndexMap.get(leftChar);
            Integer rightIndex = terminalIndexMap.get(rightChar);
            if(leftIndex == null){
                nonTerminalsToCreate.add(leftChar);
            }
            if(rightIndex == null){
                nonTerminalsToCreate.add(leftChar);
            }
            int leftTotal = 0;
            int rightTotal = 0;

            leftTotal += leftTerminals[leftIndex].length;
            leftTotal += rightTerminals[leftIndex].length;
            leftTotal += terminals[leftIndex];

            rightTotal += rightTerminals[rightIndex].length;
            rightTotal += leftTerminals[rightIndex].length;
            rightTotal += terminals[rightIndex];

        }







    }

    public char[] getTRuleFromNRuleArray() {
        return tFromN;
    }

    public int getRuleCount(){
        return ruleCount;
    }

    public int[][][] getArraysFromNRuleArray(){
        return nonTerminalToNonTerminals;
    }

    @Override
    // Will return 0 if not found.
    public int[][] getRuleFromArray() {
        return nonTerminalsToNonTerminal;
    }
    public HashMap<Character, ArrayList<Integer>> getNFromT(){
        return nFromT;
    }
}
