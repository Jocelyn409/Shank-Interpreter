public class IntegerDataType extends InterpreterDataType {

    public IntegerDataType(int data) {
        this.data = data;
    }

    private final int data;

    public int getData() {
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
