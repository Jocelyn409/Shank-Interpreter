public class CharacterDataType extends InterpreterDataType {

    public CharacterDataType(char data) {
        this.data = data;
    }

    private final char data;

    public char getData() {
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
