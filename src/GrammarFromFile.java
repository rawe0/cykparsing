import java.util.*;

public class GrammarFromFile{

    int [][][] leftToRight;
    int [][] rightToLeft;
    HashMap<Integer, List<Character>> tFromN;
    HashMap<Character, List<Integer>> nFromT;
    HashMap<Character, Integer> NTIndex;
    public int ruleCount;

    ArrayList<String> rules;

    private GrammarFromFile(int ruleCount,
                            HashMap<Character, Integer> NTIndex,
                            int[][][] leftToRight,
                            int[][] rightToLeft,
                            HashMap<Integer, List<Character>> tFromNMap,
                            HashMap<Character, List<Integer>> nFromT){

        this.ruleCount = ruleCount;
        this.NTIndex = NTIndex;
        this.leftToRight = leftToRight;
        this.rightToLeft = rightToLeft;
        this.nFromT = nFromT;
        this.tFromN = tFromNMap;
    }


    public GrammarFromFile(Scanner grammar) {
        ruleCount = 0;
        rules = new ArrayList<>();
        NTIndex = new HashMap<>();
        nFromT = new HashMap<>();
        tFromN = new HashMap<>();


        while (grammar.hasNextLine()) {
            String data = grammar.nextLine();
            for (char c: data.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    if (!NTIndex.containsKey(c)) {
                        NTIndex.put(c, ruleCount);
                        tFromN.put(ruleCount, new ArrayList<>());
                        ruleCount++;
                    }
                }
            }
            rules.add(data);
        }

        leftToRight = new int[ruleCount][0][0];
        rightToLeft = new int[ruleCount][ruleCount];

        for(int i = 0; i < ruleCount; i++){
            for(int j = 0; j < ruleCount; j++){
                rightToLeft[i][j] = -1;
            }
        }

        for (String rule : rules) {

            String [] split = rule.split("\\s+");

            char outputOne = split[1].charAt(0);
            char input = split[0].charAt(0);

            // Get the index of the non-terminal
            int index = this.NTIndex.get(input);

            // Non-terminal rule
            if(Character.isUpperCase(outputOne)){

                // Must have two productions since it's a non-terminal
                char outputTwo = split[1].charAt(1);


                // Increase size of array for the non-terminal
                leftToRight[index] = Arrays.copyOf(leftToRight[index],
                        leftToRight[index].length + 1);


                leftToRight[index][leftToRight[index].length - 1] = new int[]
                        {this.NTIndex.get(outputOne), this.NTIndex.get(outputTwo)};

                rightToLeft[this.NTIndex.get(outputOne)][this.NTIndex.get(outputTwo)] = index;

                continue;
            }

            // Terminal rule

            // Add terminal to the list of terminals for the non-terminal
            tFromN.get(index).add(outputOne);

            // Add non-terminal to the list of non-terminals for the terminal
            if(!nFromT.containsKey(outputOne)){
                nFromT.put(outputOne, new ArrayList<>());
            }
            nFromT.get(outputOne).add(index);

        }
    }

    public static GrammarFromFile fromLinearGrammar(LinearGrammarFromFile linearGrammar){

        HashMap<Character, Integer> NTIndex = linearGrammar.getNTIndex();

        HashMap<Integer, List<Character>> tFromN = linearGrammar.getTerminal();
        int [][][] leftTerminals = linearGrammar.getLeftTerminal();
        int [][][] rightTerminals = linearGrammar.getRightTerminal();
        int nRules = linearGrammar.getRuleCount();

        HashSet<Character> terminalsToAdd = new HashSet<>();

        for(int i = 0; i < nRules; i++){
            for(int j = 0; j < leftTerminals[i].length; j++){
                char character = (char) leftTerminals[i][j][0];

                Integer index = NTIndex.get(character);
                if(index == null){
                    terminalsToAdd.add(character);
                    continue;
                }
                if(rightTerminals[index] != null ||  leftTerminals[index] != null){
                    terminalsToAdd.add(character);
                }
            }

            for(int j = 0; j < rightTerminals[i].length; j++){
                char character = (char) rightTerminals[i][j][1];
                Integer index = NTIndex.get(character);

                if(index == null){
                    terminalsToAdd.add(character);
                    continue;
                }
                if(rightTerminals[index] != null ||  leftTerminals[index] != null){
                    terminalsToAdd.add(character);
                }
            }
        }

        HashMap<Character, List<Integer>> nFromT =  new HashMap<>();

        // Add old rules
        for(Map.Entry<Integer, List<Character>> e: tFromN.entrySet()){
            for(char c: e.getValue()){
                if(!nFromT.containsKey(c)){
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(e.getKey());
                    nFromT.put(c, list);
                }
            }
        }

        // Add rule in the grammar for each terminal and the reverse
        ArrayList<Integer> addedNTs = new ArrayList<>();

        for (char terminal: terminalsToAdd) {

            addedNTs.add(nRules);

            nFromT.computeIfAbsent(terminal, k -> new ArrayList<>());
            nFromT.get(terminal).add(nRules);

            ArrayList<Character> charList = new ArrayList<>();
            charList.add(terminal);
            tFromN.put(nRules, charList);
            nRules++;
        }


        int [][][] leftToRight = new int[nRules][0][0];
        int [][] rightToLeft = new int[nRules][nRules];
        for(int i = 0; i < nRules; i++){
            for(int j = 0; j < nRules; j++){
                rightToLeft[i][j] = -1;
            }
        }

        for(int i = 0; i < leftTerminals.length; i++){
            for(int j = 0; j < leftTerminals[i].length; j++){

                // Get the right side
                int[] rule = leftTerminals[i][j];

                // Get the production for the terminal we just made
                Integer nonTerminal = null;
                List<Integer> convertedN = nFromT.get((char) rule[0]);
                for (Integer n: convertedN) {
                    if(addedNTs.contains(n)){
                        nonTerminal = n;
                        break;
                    }
                }


                leftToRight[i] = Arrays.copyOf(leftToRight[i], leftToRight[i].length + 1);

                leftToRight[i][leftToRight[i].length - 1] = new int[]{nonTerminal, rule[1]};

                rightToLeft[nonTerminal][rule[1]] = i;
            }
        }

        for(int i = 0; i < rightTerminals.length; i++){
            for(int j = 0; j < rightTerminals[i].length; j++){
                int[] rule = rightTerminals[i][j];


                // Get the production for the terminal we just made
                Integer nonTerminal = null;
                List<Integer> convertedN = nFromT.get((char) rule[1]);
                for (Integer n: convertedN) {
                    if(addedNTs.contains(n)){
                        nonTerminal = n;
                        break;
                    }
                }


                leftToRight[i] = Arrays.copyOf(leftToRight[i], leftToRight[i].length + 1);

                leftToRight[i][leftToRight[i].length - 1] = new int[] {rule[0], nonTerminal};

                rightToLeft[rule[0]][nonTerminal] = i;
            }
        }

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int i = 0; i < nRules; i++){
            if(!NTIndex.containsValue(i)){
                for (char c: alphabet.toCharArray()) {
                    if(!NTIndex.containsKey(c)){
                        NTIndex.put(c, i);
                        break;
                    }
                }
            }
        }
        return new GrammarFromFile(nRules, NTIndex, leftToRight, rightToLeft, tFromN, nFromT);
    }



    public HashMap<Integer, List<Character>> getTFromN() {
        return tFromN;
    }

    public int getRuleCount(){
        return ruleCount;
    }

    public int[][][] getArraysFromNRuleArray(){
        return leftToRight;
    }

    public HashMap<Character, Integer> getNTIndex() {
        return NTIndex;
    }

    // Will return 0 if not found.
    public int[][] getRuleFromArray() {
        return rightToLeft;
    }
    public HashMap<Character, List<Integer>> getNFromT(){
        return nFromT;
    }
}
