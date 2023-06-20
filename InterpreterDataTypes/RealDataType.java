public class RealDataType extends InterpreterDataType {

    public RealDataType(float data) {
        this.data = data;
    }

    private final float data;

    public float getData() {
        return data;
    }

    @Override
    public String toString() {
        return data + "";
    }

    @Override
    public void FromString(String input) {

    }
}
