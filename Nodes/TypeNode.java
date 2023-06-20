import java.util.spi.ToolProvider;

public class TypeNode extends Node {

    public enum dataType {
        INTEGER, REAL, STRING, CHARACTER, BOOLEAN, ARRAY
    }

    public TypeNode(dataType type, Node value) {
        this.type = type;
        this.value = value;
    }

    public TypeNode(dataType type) {
        this.type = type;
    }

    private final dataType type;
    private Node value;

    public dataType getDataType() {
        return type;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        if(value == null) {
            return "TypeNode(Type(" + type + "))";
        }
        return "TypeNode(dataType(" + type + "), Node(" + value + "))";
    }
}
