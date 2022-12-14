public class ParseItem {
    public String parseString;
    public int numberOfErrors;
    public int nonTerminalIndex;

    @Override
    public boolean equals(Object obj) {
        return nonTerminalIndex == ((ParseItem) obj).nonTerminalIndex;
    }

    @Override
    public String toString() {
        return "\nString: " + parseString + "\nCorrections: " + numberOfErrors + "\nStarting terminal: " + nonTerminalIndex + "\n";
    }

    public ParseItem(String parseString, int numberOfErrors, int nonTerminalIndex){
        this.parseString = parseString;
        this.numberOfErrors = numberOfErrors;
        this.nonTerminalIndex = nonTerminalIndex;
    }
}
