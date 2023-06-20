public class BooleanDataType extends InterpreterDataType{

    public BooleanDataType(Boolean data) {
        this.data = data;
    }

    private final Boolean data;

    public Boolean getData() {
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
