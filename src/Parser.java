import java.util.*;

public class Parser {
    private final int[][][] nonTerminalToNonTerminals;
    private final int[][] nonTerminalsToNonTerminals;
    private final char[] tRuleFromNRuleArray;
    private final HashMap<Character, ArrayList<Integer>> nFromTRule;
    private final int ruleCount;
    private int counter;
    Parser(Grammar grammar){
        this.nonTerminalToNonTerminals =  grammar.getArraysFromNRuleArray();
        this.nonTerminalsToNonTerminals = grammar.getRuleFromArray();
        this.tRuleFromNRuleArray = grammar.getTRuleFromNRuleArray();
        this.nFromTRule = grammar.getNFromT();
        this.ruleCount = grammar.getRuleCount();
        counter = 0;
    }
    public void resetCounter(){
        counter = 0;
    }
    public int getCount(){
        return counter;
    }

    public boolean parseBU(String s) {

        int n = s.length();
        Integer[][][] cykTable = new Integer[n][n][];

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            ArrayList<Integer> rules = nFromTRule.get(c);
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
                                if (nonTerminalsToNonTerminals[i][j] != 0) {
                                    rules.add(nonTerminalsToNonTerminals[i][j]);

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
        return cykTable[n-1][0] != null && Arrays.asList(cykTable[n-1][0]).contains(1);
    }
    public boolean parseBUErrorCorrection(String s) {

        int n = s.length();
        List<ParseItem>[][] cykTable = new List<>[n][n];

        // Add all the valid rules with error 0
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            ArrayList<Integer> rules = nFromTRule.get(c);
            for (int rule : rules) {
                ParseItem item = new ParseItem("" + c,0 , rule);
                cykTable[0][i] = new ArrayList<>();
                cykTable[0][i].add(item);
            }
        }

        // Add invalid rules with error 1
        for (int i = 0; i < n; i++){
            for (int j = 0; j < tRuleFromNRuleArray.length; j++){
                if(!cykTable[0][i].contains(new ParseItem("", 1, j))){
                    cykTable[0][i].add(new ParseItem(tRuleFromNRuleArray[j] + "", 1, j));
                }
            }
        }

        // Add lambda rules with error 1
        for (int i = 0; i < n; i++){
            if(!cykTable[0][i].contains(new ParseItem("", 1, -1))){
                cykTable[0][i].add(new ParseItem("", 1, -1));
            }
        }

        for (int a = 1; a < n; a++){
            for (int b = 0; b < n-a; b++){
                HashSet<Integer> rules = new HashSet<>();
                for (int c = 0; c < a; c++){
                    counter++;
                    int leftIndex = a - c - 1;
                    int rightIndex = b + c + 1;
                    List<ParseItem> leftCell = cykTable[c][b];
                    List<ParseItem> rightCell = cykTable[leftIndex][rightIndex];
                    if (leftCell  != null && rightCell != null) {
                        for (int i : leftCell) {
                            for (int j : rightCell) {
                                if (nonTerminalsToNonTerminals[i][j] != 0) {
                                    rules.add(nonTerminalsToNonTerminals[i][j]);
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
        return cykTable[n-1][0] != null && Arrays.asList(cykTable[n-1][0]).contains(1);
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
        return parseTD(1, 0, n, string, table);
    }
    public boolean parseTD(int nonTerminal, int start, int end, char [] s, Boolean[][][] table){
        counter++;
        if(table[start][end][nonTerminal] != null){
            return table[start][end][nonTerminal];
        }
        if (start == end - 1) {
            table[start][end][nonTerminal] = tRuleFromNRuleArray[nonTerminal] == s[start];
            return table[start][end][nonTerminal];
        } else {
            int [][] rules = nonTerminalToNonTerminals[nonTerminal];
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
        return parseNaive(1, 0 , n, string);
    }
    public boolean parseNaive(int nonTerminal, int start, int end, char [] s){
        counter++;
        if(start == end - 1){
            return tRuleFromNRuleArray[nonTerminal] == s[start];
        }
        else{
                int [][] rules = nonTerminalToNonTerminals[nonTerminal];
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

