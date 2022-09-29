import java.util.ArrayList;
import java.util.HashMap;

public abstract class Grammar {
    public int ruleCount;
    public abstract char getTRuleFromNRule(int nRule);
    public abstract ArrayList<Integer> getNRulesFromTRule(char tRule);
    public abstract int [][] getArraysFromNRule(int nRule);
    public abstract int[][] getRuleFromArray();
    public abstract int getRuleCount();
    public abstract int[][][] getArraysFromNRuleArray();
    public abstract char[] getTRuleFromNRuleArray();
    public abstract HashMap<Character, ArrayList<Integer>> getNFromT();

}