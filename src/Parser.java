import java.util.ArrayList;

public class Parser {

    public boolean parseBU(String s, Grammar g, int[] counter) {

        int n = s.length();
        int ruleCount = g.getRuleCount();
        boolean[][][] cykTable = new boolean[n][n][ruleCount];

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            ArrayList<Integer> rules = g.getNRulesFromTRule(c);
            if (rules != null) {
                for (int rule : rules) {
                    cykTable[0][i][rule] = true;
                }
            }
        }
        for (int a = 1; a < n; a++){
            for (int b = 0; b < n-a; b++){
                for (int c = 0; c < a; c++){
                    int leftIndex = a - c - 1;
                    int rightIndex = b + c + 1;
                    boolean[] leftCell = cykTable[c][b];
                    boolean[] rightCell = cykTable[leftIndex][rightIndex];
                    for (int j = 1; j < ruleCount; j++) {
                        int[][] rules = g.getArraysFromNRule(j);
                        for (int[] rule: rules) {
                            counter[0]++;
                            if (leftCell[rule[0]] && rightCell[rule[1]]) {
                                cykTable[a][b][j] = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        // Make the assumption that the start symbol is the
        // first symbol in the grammar
        for (int i = 0; i < n; i++) {
            if(cykTable[n-1][0][1]){
                return true;
            }
        }
        return false;
    }
    public boolean parseTD(String s, Grammar g, int[] counter) {
        int n = s.length();
        char [] string = s.toCharArray();
        int ruleCount = g.getRuleCount();
        Boolean[][][] table = new Boolean[ruleCount][n+1][n+1];
        for(int i = 0; i < ruleCount; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0; k < n; k++){
                    table[i][j][k] = null;
                }
            }
        }
        // Assume that the first NON-TERMINAL is the start symbol
        return parseTD(1, 0, n, g, string, table, counter);
    }
    public boolean parseTD(int nonTerminal, int start, int end, Grammar g, char [] s, Boolean[][][] table, int[] counter){
        counter[0]++;
        if(start == end - 1){
            ArrayList<Integer> rules = g.getNRulesFromTRule(s[start]);
            for (int rule : rules) {
                if (rule == nonTerminal) {
                    return true;
                }
            }
        }
        else{
            int [][] rules = g.getArraysFromNRule(nonTerminal);
            for (int[] rule : rules) {
                for (int i = start + 1; i < end; i++) {

                    if(table[rule[0]][start][i] == null){
                        table[rule[0]][start][i] = parseTD(rule[0], start, i, g, s, table, counter);
                    }

                    if(table[rule[1]][i][end] == null){
                        table[rule[1]][i][end] = parseTD(rule[1], i, end, g, s, table, counter);
                    }

                    if(table[rule[0]][start][i] && table[rule[1]][i][end]){
                            return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean parseNaive(String s, Grammar g, int[] counter) {
        int n = s.length();
        char [] string = s.toCharArray();
        // Assume that the first NON-TERMINAL is the start symbol
        return parseNaive(1, 0 , n, g, string, counter);
    }
    public boolean parseNaive(int nonTerminal, int start, int end, Grammar g, char [] s, int[] counter){
        counter[0]++;
        if(start == end - 1){
            ArrayList<Integer> rules = g.getNRulesFromTRule(s[start]);
            for (int rule : rules) {
                if (rule == nonTerminal) {
                    return true;
                }
            }
        }
        else{
            int [][] rules = g.getArraysFromNRule(nonTerminal);
            for (int[] rule : rules) {
                for (int i = start + 1; i < end; i++) {
                    if (parseNaive(rule[0], start, i, g, s, counter) && parseNaive(rule[1], i, end, g, s, counter)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

