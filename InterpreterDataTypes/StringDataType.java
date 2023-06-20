public class StringDataType extends InterpreterDataType{

    public StringDataType(String data) {
        this.data = data;
    }

    private final String data;

    public String getData() {
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
