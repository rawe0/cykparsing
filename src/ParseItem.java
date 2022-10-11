public class ParseItem {
    public String parseString;
    public int numberOfErrors;
    public int nonTerminalIndex;

    public ParseItem(String parseString, int numberOfErrors, int nonTerminalIndex){
        this.parseString = parseString;
        this.numberOfErrors = numberOfErrors;
        this.nonTerminalIndex = nonTerminalIndex;
    }
}
