import java.util.*;

public class GrammarFromFile extends Grammar{

    int [][][] leftToRight;
    int [][] rightToLeft;
    char [] tFromN;
    HashMap<Character, ArrayList<Integer>> nFromT;
    HashMap<Character, Integer> NTIndex;
    public int ruleCount;

    ArrayList<String> rules;

    private GrammarFromFile(int ruleCount,
                            HashMap<Character, Integer> nonTerminalIndexMap,
                            int[][][] leftToRight,
                            int[][] rightToLeft,
                            char[] tFromN,
                            HashMap<Character,
                            ArrayList<Integer>> nFromT){

        this.ruleCount = ruleCount;
        this.NTIndex = nonTerminalIndexMap;
        this.leftToRight = leftToRight;
        this.rightToLeft = rightToLeft;
        this.nFromT = nFromT;
        this.tFromN = tFromN;

    }


    public GrammarFromFile(Scanner input) {
        ruleCount = 1;
        rules = new ArrayList<>();
        NTIndex = new HashMap<>();
        nFromT = new HashMap<>();


        while (input.hasNextLine()) {
            String data = input.nextLine();
            for (char c: data.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    if (!NTIndex.containsKey(c)) {
                        NTIndex.put(c, ruleCount);
                        ruleCount++;
                    }
                }
            }
            rules.add(data);
        }
        leftToRight = new int[ruleCount][0][0];
        tFromN = new char[ruleCount];
        rightToLeft = new int[ruleCount][ruleCount];

        for (String rule : rules) {
            String [] split = rule.split("\\s+");

            char outputOne = split[1].charAt(0);
            char inputOne = split[0].charAt(0);

            // Non-terminal rule
            if(Character.isUpperCase(outputOne)){

                int index = this.NTIndex.get(inputOne);
                leftToRight[index] = Arrays.copyOf(leftToRight[index],
                        leftToRight[index].length + 1);

                if (split[1].length() == 2){
                    char outputTwo = split[1].charAt(1);
                    leftToRight[index][leftToRight[index].length - 1] = new int[]
                            {this.NTIndex.get(outputOne), this.NTIndex.get(outputTwo)};

                    rightToLeft[this.NTIndex.get(outputOne)][this.NTIndex.get(outputTwo)] = index;
                } else {
                    rightToLeft[this.NTIndex.get(outputOne)][0] = index;
                    leftToRight[index][leftToRight[index].length - 1] =
                            new int[] {this.NTIndex.get(outputOne)};
                }

            // Terminal rule
            } else {
                int index = this.NTIndex.get(inputOne);
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

        HashMap<Character, Integer> NTIndex = linearGrammar.getNTIndex();
        char[] terminals = linearGrammar.getTerminal();
        int [][][] leftTerminals = linearGrammar.getLeftTerminal();
        int [][][] rightTerminals = linearGrammar.getRightTerminal();
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
                terminals.length + terminalsToAdd.size() + 1);

        for (char terminal: terminalsToAdd) {
            nRules++;
            tFromN[nRules] = terminal;
        }

        for (int i = 0; i < tFromN.length; i++){
            ArrayList<Integer> newList = new ArrayList<>();
            newList.add(i);
            nFromT.put(tFromN[i], newList);
        }


        int [][][] nonTerminalToNonTerminals = new int[nRules][0][0];
        int [][] nonTerminalsToNonTerminal = new int[nRules+1][nRules+1];

        for(int i = 0; i < leftTerminals.length; i++){
            for(int j = 0; j < leftTerminals[i].length; j++){
                int[] rule = leftTerminals[i][j];
                ArrayList<Integer> convertedN = nFromT.get((char) rule[0]);
                Integer nonTerminal = convertedN.get(0);
                nonTerminalToNonTerminals[i] = Arrays.copyOf(nonTerminalToNonTerminals[i],
                        nonTerminalToNonTerminals[i].length + 1);
                nonTerminalToNonTerminals[i][nonTerminalToNonTerminals[i].length - 1] = new int[]
                        {nonTerminal, rule[1]};
                nonTerminalsToNonTerminal[nonTerminal][rule[1]] = i;
            }
        }
        for(int i = 0; i < rightTerminals.length; i++){
            for(int j = 0; j < rightTerminals[i].length; j++){
                int[] rule = rightTerminals[i][j];
                int convertedN = nFromT.get((char) rule[1]).get(0);
                nonTerminalToNonTerminals[i] = Arrays.copyOf(nonTerminalToNonTerminals[i],
                        nonTerminalToNonTerminals[i].length + 1);
                nonTerminalToNonTerminals[i][nonTerminalToNonTerminals[i].length - 1] = new int[]
                        {rule[0], convertedN};
                nonTerminalsToNonTerminal[rule[0]][convertedN] = i;
            }
        }

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int i = 1; i < tFromN.length; i++){
            if(!NTIndex.containsValue(i)){
                for (char c: alphabet.toCharArray()) {
                    if(!NTIndex.containsKey(c)){
                        NTIndex.put(c, i);
                        break;
                    }
                }
            }
        }
        return new GrammarFromFile(nRules, NTIndex, nonTerminalToNonTerminals, nonTerminalsToNonTerminal, tFromN, nFromT);
    }

    public char[] getTRuleFromNRuleArray() {
        return tFromN;
    }

    public int getRuleCount(){
        return ruleCount;
    }

    public int[][][] getArraysFromNRuleArray(){
        return leftToRight;
    }

    @Override
    // Will return 0 if not found.
    public int[][] getRuleFromArray() {
        return rightToLeft;
    }
    public HashMap<Character, ArrayList<Integer>> getNFromT(){
        return nFromT;
    }
}
