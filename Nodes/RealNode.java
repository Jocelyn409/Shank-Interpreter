public class RealNode extends Node {

    public RealNode(float floatValue) {
        this.floatValue = floatValue;
    }

    private final float floatValue;

    public float getFloatValue() {
        return floatValue;
    }

    @Override
    public String toString() {
        return "RealNode(" + floatValue + ")";
    }
}
