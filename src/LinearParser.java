public class LinearParser {
    private final char[] terminals;
    private final int [][][] leftTerminals;
    private final int [][][] rightTerminals;
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
    public boolean parseLinearTD(String s) {
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
        // Assume that the first Non-terminal is the start symbol
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
                // Non-terminal is on the right side
                // Terminal is on the left side
                char t = (char) rule[0];
                if(t == s[start] && parseLinearTD(rule[1], start+1, end, s, table)) {
                    table[start][end][nonTerminal] = true;
                    return true;
                }
            }
            int [][] rightRules = rightTerminals[nonTerminal];
            for (int[] rule : rightRules) {
                // Non-terminal is on the left side
                // Terminal is on the right side
                char t = (char) rule[1];
                if(t == s[end-1] && parseLinearTD(rule[0], start, end - 1, s, table)) {
                    table[start][end][nonTerminal] = true;
                    return true;
                }
            }
        }
        table[start][end][nonTerminal] = false;
        return false;
    }
}
