public class IntegerNode extends Node {

    public IntegerNode(int intValue) {
        this.intValue = intValue;
    }

    private final int intValue;

    public int getIntValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return "IntegerNode(" + intValue + ")";
    }
}
