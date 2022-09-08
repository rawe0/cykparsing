import java.util.Arrays;

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
                cykTable[0][i][j] = g.getNRulesFromTRule(s.charAt(i)).contains(j);
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
                        System.out.println(Arrays.toString(rules));
                        for (int[] rule: rules) {

                        }
                    }
                }
            }
        }
        for (boolean[] array: cykTable[2]) {
            System.out.println(Arrays.toString(array));
        }

        return true;
    }
    public boolean parseTD(String s, Grammar g) {
        return true;
    }
}

