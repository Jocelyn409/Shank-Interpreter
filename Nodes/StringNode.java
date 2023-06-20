public class StringNode extends Node {

    public StringNode(String stringValue) {
        this.stringValue = stringValue;
    }

    private final String stringValue;

    public String getStringValue() {
        return stringValue;
    }

    @Override
    public String toString() {
        return "StringNode(" + stringValue + ")";
    }
}
