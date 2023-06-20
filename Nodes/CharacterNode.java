public class CharacterNode extends Node {

    public CharacterNode(char charValue) {
        this.charValue = charValue;
    }

    private final char charValue;

    public char getCharValue() {
        return charValue;
    }

    @Override
    public String toString() {
        return "" + charValue;
    }
}
