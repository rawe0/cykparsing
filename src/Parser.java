import java.util.ArrayList;

public class Parser {

    public boolean parseNaive(String s, Grammar g) {
        int n = s.length();
        int[] str = new int[n];

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            ArrayList<Integer> rules = g.getNRulesFromTRule(c);
            if (rules != null) {
                for (int rule : rules) {
                    str[i] = rule;
                }
            }
        }

        return parseNaive(1, 0 , str.length, g, str);

    }
    public boolean parseNaive(int nonTerminal, int start, int end, Grammar g, int [] s){
        int[][][] grammar = g.getGrammar();
        if(start == end - 1){
            int[][] rules = g.getArraysFromNRule(nonTerminal);
            for(int i = 0; i < rules.length; i++){
                int [] rule = rules[i];

            }

        }


        return true;
    }

    public boolean parseBU(String s, Grammar g) {

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
    public boolean parseTD(String s, Grammar g) {
        return true;
    }
}

