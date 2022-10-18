import java.util.*;

public class Parser {
    private final int[][][] leftToRight;
    private final int[][] rightToLeft;
    private final HashMap<Integer, List<Character>> tFromNT;
    private final HashMap<Character, List<Integer>> nFromT;
    private final int ruleCount;
    private final HashMap<Character, Integer> NTIndex;
    private long counter;
    Parser(GrammarFromFile grammar){
        this.leftToRight =  grammar.getArraysFromNRuleArray();
        this.rightToLeft = grammar.getRuleFromArray();
        this.tFromNT = grammar.getTFromN();
        this.nFromT = grammar.getNFromT();
        this.ruleCount = grammar.getRuleCount();
        this.NTIndex = grammar.getNTIndex();
        counter = 0;
    }
    public void resetCounter(){
        counter = 0;
    }
    public long getCount(){
        return counter;
    }

    public boolean parseBU(String s) {

        int n = s.length();
        Integer[][][] cykTable = new Integer[n][n][];

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            List<Integer> rules = nFromT.get(c);
            if (rules == null) {
                    return false;
            } else {
                cykTable[0][i] = rules.toArray(new Integer[0]);
            }
        }

        for (int a = 1; a < n; a++){
            for (int b = 0; b < n-a; b++){
                HashSet<Integer> rules = new HashSet<>();
                for (int c = 0; c < a; c++){
                    counter++;
                    int leftIndex = a - c - 1;
                    int rightIndex = b + c + 1;
                    Integer[] leftCell = cykTable[c][b];
                    Integer[] rightCell = cykTable[leftIndex][rightIndex];
                    if (leftCell  != null && rightCell != null) {
                        for (int i : leftCell) {
                            for (int j : rightCell) {
                                if (rightToLeft[i][j] != -1) {
                                    rules.add(rightToLeft[i][j]);
                                }
                            }
                        }
                    }
                }
                if (rules.size() > 0) {
                    cykTable[a][b] = rules.toArray(new Integer[0]);
                }
            }
        }

        // Make the assumption that the start symbol is the first rule in the grammar
        return cykTable[n-1][0] != null && Arrays.asList(cykTable[n-1][0]).contains(0);
    }
    public boolean parseBUErrorCorrection(String s, Boolean silence) {

        int n = s.length();
        HashMap<Integer, ParseItem>[][] cykTable = new HashMap[n][n];

        // Add all the valid rules with error 0
        for (int i = 0; i < n; i++) {
            cykTable[0][i] = new HashMap<>();
            char c = s.charAt(i);
            List<Integer> rules = nFromT.get(c);
            if(rules == null) continue;
            for (int rule : rules) {
                ParseItem item = new ParseItem("" + c,0 , rule);
                cykTable[0][i].put(rule, item);
            }
        }

        // Add invalid rules with error 1
        for (int i = 0; i < n; i++){
            for (Integer key: tFromNT.keySet()) {
                List<Character> terminals = tFromNT.get(key);
                if(terminals.size() != 0){
                    if(!cykTable[0][i].containsKey(key)){
                        cykTable[0][i].put(key, new ParseItem(terminals.get(0) + "", 1, key));
                    }
                }
            }
        }

        // Add lambda rules with error 1
        for (int i = 0; i < n; i++){
                cykTable[0][i].put(-1, new ParseItem("", 1, -1));
        }

        for (int a = 1; a < n; a++){
            for (int b = 0; b < n-a; b++){
                cykTable[a][b] = new HashMap<>();
                for (int c = 0; c < a; c++){
                    counter++;
                    int leftIndex = a - c - 1;
                    int rightIndex = b + c + 1;

                    HashMap<Integer, ParseItem> leftCell = cykTable[c][b];
                    HashMap<Integer, ParseItem> rightCell = cykTable[leftIndex][rightIndex];

                    for (Map.Entry<Integer, ParseItem> leftSet: leftCell.entrySet()){
                        for (Map.Entry<Integer, ParseItem> rightSet: rightCell.entrySet()) {
                            ParseItem leftItem = leftSet.getValue();
                            ParseItem rightItem = rightSet.getValue();
                            // Case where both are lambda
                            if (leftItem.nonTerminalIndex == -1 && rightItem.nonTerminalIndex == -1) {
                                cykTable[a][b].put(-1, new ParseItem("", leftItem.numberOfErrors + rightItem.numberOfErrors, -1));
                                continue;
                            }
                            // Case where left item is lambda
                            if (leftItem.nonTerminalIndex == -1) {
                                int error = leftItem.numberOfErrors + rightItem.numberOfErrors;
                                int ntIndex = rightItem.nonTerminalIndex;
                                ParseItem currentItem = cykTable[a][b].get(ntIndex);
                                if (currentItem == null) {
                                    cykTable[a][b].put(ntIndex, new ParseItem(rightItem.parseString, error, ntIndex));
                                } else {
                                    if (currentItem.numberOfErrors > error) {
                                        cykTable[a][b].put(ntIndex, new ParseItem(rightItem.parseString, error, ntIndex));
                                    }
                                }
                                continue;
                            }
                            // Case where right item is lambda
                            if (rightItem.nonTerminalIndex == -1) {
                                int error = leftItem.numberOfErrors + rightItem.numberOfErrors;
                                int ntIndex = leftItem.nonTerminalIndex;
                                ParseItem currentItem = cykTable[a][b].get(ntIndex);
                                if (currentItem == null) {
                                    cykTable[a][b].put(ntIndex, new ParseItem(leftItem.parseString, error, ntIndex));
                                } else {
                                    if (currentItem.numberOfErrors > error) {
                                        cykTable[a][b].put(ntIndex, new ParseItem(leftItem.parseString, error, ntIndex));
                                    }
                                }
                                continue;
                            }
                            // Case where none of the items are lambda
                            if (rightToLeft[leftItem.nonTerminalIndex][rightItem.nonTerminalIndex] != -1) {
                                int error = leftItem.numberOfErrors + rightItem.numberOfErrors;
                                int ntIndex = rightToLeft[leftItem.nonTerminalIndex][rightItem.nonTerminalIndex];
                                ParseItem currentItem = cykTable[a][b].get(ntIndex);
                                if (currentItem == null) {
                                    cykTable[a][b].put(ntIndex, new ParseItem(leftItem.parseString + rightItem.parseString, error, ntIndex));
                                } else {
                                    if (currentItem.numberOfErrors > error) {
                                        cykTable[a][b].put(ntIndex, new ParseItem(leftItem.parseString + rightItem.parseString, error, ntIndex));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Check if starting symbol has 0 errors, or return the starting symbol with lowest amount of errors and string etc...

        // Make the assumption that the start symbol is the first rule in the grammar
        ParseItem startSymbol = cykTable[n-1][0].get(0);
        if(startSymbol == null){
            return false;
        }

        if(!silence && startSymbol.numberOfErrors != 0){
            System.out.println(startSymbol);
        }
        return startSymbol.numberOfErrors == 0;
    }


    public boolean parseTD(String s) {
        int n = s.length();
        char [] string = s.toCharArray();
        Boolean[][][] table = new Boolean[n+1][n+1][ruleCount];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0; k < ruleCount; k++){
                    table[i][j][k] = null;
                }
            }
        }
        // Assume that the first NON-TERMINAL is the start symbol
        return parseTD(0, 0, n, string, table);
    }
    public boolean parseTD(int nonTerminal, int start, int end, char [] s, Boolean[][][] table){
        counter++;
        if(table[start][end][nonTerminal] != null){
            return table[start][end][nonTerminal];
        }
        if (start == end - 1) {
            if(tFromNT.get(nonTerminal) != null){
                table[start][end][nonTerminal] = tFromNT.get(nonTerminal).contains(s[start]);
                return table[start][end][nonTerminal];
            }
            table[start][end][nonTerminal] = false;
            return false;
        } else {
            int [][] rules = leftToRight[nonTerminal];
            for (int[] rule : rules) {
                for (int i = start + 1; i < end; i++) {
                    if(parseTD(rule[0], start, i, s, table) && parseTD(rule[1], i, end, s, table)){
                        table[start][end][nonTerminal] = true;
                        return true;
                    }
                }
            }
        }
        table[start][end][nonTerminal] = false;
        return false;
    }





    public boolean parseNaive(String s) {
        int n = s.length();
        char [] string = s.toCharArray();
        // Assume that the first NON-TERMINAL is the start symbol
        return parseNaive(0, 0 , n, string);
    }
    public boolean parseNaive(int nonTerminal, int start, int end, char [] s){
        counter++;
        if(start == end - 1){
            if(tFromNT.get(nonTerminal) != null){
                return tFromNT.get(nonTerminal).contains(s[start]);
            }
            return false;
        }
        else{
            int [][] rules = leftToRight[nonTerminal];
            for (int[] rule : rules) {
                for (int i = start + 1; i < end; i++) {
                    if (parseNaive(rule[0], start, i, s) && parseNaive(rule[1], i, end, s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

