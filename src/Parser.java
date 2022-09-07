public class Parser {

    public boolean ParseNaive(String s, Grammar g) {
        return true;
    }
    public boolean ParseBU(String s, Grammar g) {

        int n = s.length();
        int ruleCount = g.getRuleCount();
        boolean[][][] cykTable = new boolean[n][n][ruleCount];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < ruleCount; j++) {
                cykTable[i][i][j] = g.getNRulesFromTRule(s.charAt(i)).contains(j);
            }
        }
        for (int a = 1; a < n; a++){
            for (int b = 0; b < n-a+1; b++){
                for (int c = 1; c < a-1; c++){
                    for (int j = 0; j < ruleCount; j++) {
                        int[][] rules = g.getArraysFromNRule(j);
                        for (int[] rule: rules) {
                            if (cykTable[c][b][rule[0]] && cykTable[a - c][b + c][rule[1]]) {
                                cykTable[a][b][j] = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean parseTD(String s, Grammar g) {
        return true;
    }
}

