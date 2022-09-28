import java.util.ArrayList;
import java.util.HashSet;

public class Parser {
    public boolean parseBU(String s, Grammar g, long[] counter) {

        int [][] nonTerminalsToNonTerminals = g.getRuleFromArray();

        int n = s.length();
        Integer[][][] cykTable = new Integer[n][n][];

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            ArrayList<Integer> rules = g.getNRulesFromTRule(c);
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
                    counter[0]++;
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
        // Make the assumption that the start symbol is the
        // first symbol in the grammar
        for (int i = 0; i < n; i++) {
            if(cykTable[n-1][0] != null){
                return true;
            }
        }
        return false;
    }
    public boolean parseTD(String s, Grammar g, long[] counter) {
        int n = s.length();
        char [] string = s.toCharArray();
        int ruleCount = g.getRuleCount();
        Boolean[][][] table = new Boolean[n+1][n+1][ruleCount];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0; k < ruleCount; k++){
                    table[i][j][k] = null;
                }
            }
        }
        int[][][] nonTerminalToNonTerminals =  g.getArraysFromNRuleArray();
        char[] TRuleFromNRuleArray = g.getTRuleFromNRuleArray();
        // Assume that the first NON-TERMINAL is the start symbol
        return parseTD(1, 0, n, string, table, counter, TRuleFromNRuleArray, nonTerminalToNonTerminals);
    }
    public boolean parseTD(int nonTerminal, int start, int end, char [] s, Boolean[][][] table, long[] counter, char[] tRuleFromNRuleArray, int[][][] nonTerminalToNonTerminals){
        counter[0]++;
        if(table[start][end][nonTerminal] != null){
            return table[start][end][nonTerminal];
        }
        if (start == end - 1) {
            return tRuleFromNRuleArray[nonTerminal] == s[start];
        } else {
            int [][] rules = nonTerminalToNonTerminals[nonTerminal];
            for (int[] rule : rules) {
                for (int i = start + 1; i < end; i++) {

                    if(table[start][i][rule[0]] == null){
                        table[start][i][rule[0]] = parseTD(rule[0], start, i, s, table, counter, tRuleFromNRuleArray, nonTerminalToNonTerminals);
                    }

                    if(table[i][end][rule[1]] == null){
                        table[i][end][rule[1]] = parseTD(rule[1], i, end, s, table, counter, tRuleFromNRuleArray, nonTerminalToNonTerminals);
                    }

                    if(table[start][i][rule[0]] && table[i][end][rule[1]]){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean parseNaive(String s, Grammar g, long[] counter) {
        int n = s.length();
        char [] string = s.toCharArray();
        int[][][] nonTerminalToNonTerminals =  g.getArraysFromNRuleArray();
        char[] TRuleFromNRuleArray = g.getTRuleFromNRuleArray();
        // Assume that the first NON-TERMINAL is the start symbol
        return parseNaive(1, 0 , n, string, counter, nonTerminalToNonTerminals, TRuleFromNRuleArray);
    }
    public boolean parseNaive(int nonTerminal, int start, int end, char [] s, long[] counter, int[][][] nonTerminalToNonTerminals, char[] TRuleFromNRuleArray){
        counter[0]++;
        if(start == end - 1){
            return TRuleFromNRuleArray[nonTerminal] == s[start];
        }
        else{
            int [][] rules = nonTerminalToNonTerminals[nonTerminal];
            for (int[] rule : rules) {
                for (int i = start + 1; i < end; i++) {
                    if (parseNaive(rule[0], start, i, s, counter, nonTerminalToNonTerminals, TRuleFromNRuleArray) && parseNaive(rule[1], i, end, s, counter, nonTerminalToNonTerminals, TRuleFromNRuleArray)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

