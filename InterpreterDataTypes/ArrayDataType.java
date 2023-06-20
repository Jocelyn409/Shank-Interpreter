import java.util.ArrayList;

public class ArrayDataType<type extends IntegerDataType> extends InterpreterDataType {

    public ArrayDataType(ArrayList<type> data) {
        this.data = data;
    }

    private final ArrayList<type> data;

    public ArrayList<type> getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public void FromString(String input) {

    }
}
