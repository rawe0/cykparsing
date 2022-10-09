import java.util.ArrayList;
import java.util.HashMap;

public class LinearParser {
    private char[] terminals;
    private int [][][] leftTerminals;
    private int [][][] rightTerminals;
    private final int ruleCount;
    private int counter;

    LinearParser(LinearGrammarFromFile grammar){
        this.leftTerminals =  grammar.getLeftTerminal();
        this.rightTerminals= grammar.getRightTerminal();
        this.terminals = grammar.getTerminal();
        this.ruleCount = grammar.getRuleCount();
        counter = 0;
    }
    public void resetCounter(){
        counter = 0;
    }
    public int getCount(){
        return counter;
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
        return parseLinearTD(1, 0, n, string, table);
    }
    public boolean parseLinearTD(int nonTerminal, int start, int end, char [] s, Boolean[][][] table){
        counter++;
        if(table[start][end][nonTerminal] != null){
            return table[start][end][nonTerminal];
        }
        if (start == end - 1) {
            table[start][end][nonTerminal] = terminals[nonTerminal] == s[start];
            return table[start][end][nonTerminal];
        } else {
            int [][] leftRules = leftTerminals[nonTerminal];
            for (int[] rule : leftRules) {
                char t = (char) rule[0];
                int  n = rule[1];
                if(t == )
                for (int i = start + 1; i < end; i++) {
                    if(parseLinearTD(rule[0], start, i, s, table) && parseLinearTD(rule[1], i, end, s, table)){
                        table[start][end][nonTerminal] = true;
                        return true;
                    }
                }
            }
            int [][] rightRules = leftTerminals[nonTerminal];
            for (int[] rule : leftRules) {
                char t = (char) rule[1];
                int  n = rule[0];
                if()
                    for (int i = start + 1; i < end; i++) {
                        if(parseLinearTD(rule[0], start, i, s, table) && parseLinearTD(rule[1], i, end, s, table)){
                            table[start][end][nonTerminal] = true;
                            return true;
                        }
                    }
            }
        }
        table[start][end][nonTerminal] = false;
        return false;
    }
}
