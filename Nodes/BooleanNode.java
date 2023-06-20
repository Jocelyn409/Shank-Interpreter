public class BooleanNode extends Node {

    public BooleanNode(boolean boolValue) {
        this.boolValue = boolValue;
    }

    private boolean boolValue;

    public boolean getBoolValue() {
        return boolValue;
    }

    @Override
    public String toString() {
        return "" + boolValue;
    }
}
