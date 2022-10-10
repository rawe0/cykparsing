import java.util.ArrayList;
import java.util.HashMap;

public abstract class Grammar {
    public abstract int[][] getRuleFromArray();
    public abstract int getRuleCount();
    public abstract int[][][] getArraysFromNRuleArray();
    public abstract char[] getTRuleFromNRuleArray();
    public abstract HashMap<Character, ArrayList<Integer>> getNFromT();

}