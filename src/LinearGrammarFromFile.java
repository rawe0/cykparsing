import java.util.*;

public class LinearGrammarFromFile{


    HashMap<Integer, List<Character>> terminal;
    int[][][] leftTerminal;
    int[][][] rightTerminal;

    HashMap<Character, Integer> NTIndex;
    ArrayList<String> rules;
    public int ruleCount;

    public LinearGrammarFromFile(Scanner input) {
        ruleCount = 0;
        rules = new ArrayList<>();
        NTIndex = new HashMap<>();

        while (input.hasNextLine()) {
            String data = input.nextLine();
            for (char c: data.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    if (!NTIndex.containsKey(c)) {
                        NTIndex.put(c, ruleCount);
                        ruleCount++;
                    }
                }
            }
            rules.add(data);
        }

        terminal = new HashMap<>();
        leftTerminal = new int[ruleCount][0][0];
        rightTerminal = new int[ruleCount][0][0];

        for (String rule : rules) {
            String[] split = rule.split("\\s+");

            char nonTerminal = split[0].charAt(0);
            char charOne = split[1].charAt(0);
            int index = NTIndex.get(nonTerminal);

            if (split[1].length() == 2) {

                char charTwo = split[1].charAt(1);
                if (Character.isLowerCase(charOne)) {
                    leftTerminal[index] = Arrays.copyOf(leftTerminal[index],
                            leftTerminal[index].length + 1);
                    leftTerminal[index][leftTerminal[index].length - 1] =
                            new int[]{(int) charOne, NTIndex.get(charTwo)};
                } else {
                    rightTerminal[index] = Arrays.copyOf(rightTerminal[index],
                            rightTerminal[index].length + 1);
                    rightTerminal[index][rightTerminal[index].length - 1] =
                            new int[]{NTIndex.get(charOne), (int) charTwo};
                }
                continue;
            }
            if(terminal.containsKey(index)){
                terminal.get(index).add(charOne);
            } else {
                List<Character> list = new ArrayList<>();
                list.add(charOne);
                terminal.put(index, list);
            }
        }
    }
    public HashMap<Character, Integer> getNTIndex(){
        return NTIndex;
    }

    public HashMap<Integer, List<Character>> getTerminal() {
        return terminal;
    }

    public int[][][] getLeftTerminal() {
        return leftTerminal;
    }

    public int[][][] getRightTerminal() {
        return rightTerminal;
    }

    public int getRuleCount() {
        return ruleCount;
    }

}
