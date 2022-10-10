import java.util.*;

public class GrammarFromFile extends Grammar{

    int [][][] nonTerminalToNonTerminals;
    int [][] nonTerminalsToNonTerminal;
    char [] tFromN;
    HashMap<Character, ArrayList<Integer>> nFromT;
    HashMap<Integer, Integer> nonTerminalIndexMap;
    public int ruleCount;

    ArrayList<String> rules;

    private GrammarFromFile(int ruleCount,
                            HashMap<Integer, Integer> nonTerminalIndexMap,
                            int[][][] nonTerminalToNonTerminals,
                            int[][] nonTerminalsToNonTerminal, char[] tFromN,
                            HashMap<Character, ArrayList<Integer>> nFromT){

        this.ruleCount = ruleCount;
        this.nonTerminalIndexMap = nonTerminalIndexMap;
        this.nonTerminalToNonTerminals = nonTerminalToNonTerminals;
        this.nonTerminalsToNonTerminal = nonTerminalsToNonTerminal;
        this.nFromT = nFromT;
        this.tFromN = tFromN;

    }


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

    public static GrammarFromFile fromLinearGrammar(LinearGrammarFromFile linearGrammar){
        HashMap<Integer, Integer> nonTerminalIndexMap = linearGrammar.getNonTerminalIndexMap();
        char[] terminals = linearGrammar.getTerminal();
        int [][][] leftTerminals = linearGrammar.getLeftTerminal();
        int [][][] rightTerminals = linearGrammar.getLeftTerminal();
        int nRules = linearGrammar.getRuleCount();
        HashMap<Character, Integer> terminalIndexMap = new HashMap<>();
        HashSet<Character> terminalsToAdd = new HashSet<>();

        for (int i = 0; i < terminals.length; i++) {
            terminalIndexMap.put(terminals[i], i);
        }


        for(int i = 0; i < nRules; i++){
            for(int j = 0; j < leftTerminals[i].length; j++){
                char  character = (char) leftTerminals[i][j][0];

                Integer index = terminalIndexMap.get(character);
                if(index == null){
                    terminalsToAdd.add(character);
                    continue;
                }
                if(rightTerminals[index] != null ||  leftTerminals[index] != null){
                    terminalsToAdd.add(character);
                }
            }

            for(int j = 0; j < rightTerminals[i].length; j++){
                char  character = (char) rightTerminals[i][j][1];
                Integer index = terminalIndexMap.get(character);

                if(index == null){
                    terminalsToAdd.add(character);
                    continue;
                }
                if(rightTerminals[index] != null ||  leftTerminals[index] != null){
                    terminalsToAdd.add(character);
                }
            }
        }

        HashMap<Character, ArrayList<Integer>> nFromT =  new HashMap<>();

        char[] tFromN = Arrays.copyOf(terminals,
                terminals.length + terminalsToAdd.size());

        for (char terminal: terminalsToAdd) {
            nRules++;
            tFromN[nRules] = terminal;
        }

        for (int i = 0; i < terminals.length; i++){
            ArrayList<Integer> newList = new ArrayList<>();
            newList.add(i);
            nFromT.put(terminals[i], newList);
        }


        HashMap<Integer, Integer> nonTerminalIndexMap;



        int [][][] nonTerminalToNonTerminals = new int[nRules][0][0];
        int [][] nonTerminalsToNonTerminal = new int[nRules][nRules];

        for(int i = 0; i < leftTerminals.length; i++){
            for(int j = 0; j < leftTerminals[i].length; j++){
                int[] rule = leftTerminals[i][j];
                int convertedN = nFromT.get((char) rule[0]).get(0);
                nonTerminalToNonTerminals[i][]

            }
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
