public class VariableNode extends Node {


    public VariableNode(String name, TypeNode type, boolean changeable, boolean var, Node value, Node to, Node from) {
        this.name = name;
        this.type = type;
        this.changeable = changeable;
        this.var = var;
        this.value = value;
        this.to = to;
        this.from = from;
    }

    public VariableNode(String name, TypeNode type, boolean changeable, boolean var) {
        this.name = name;
        this.type = type;
        this.changeable = changeable;
        this.var = var;
    }

    public VariableNode(String name, boolean changeable, boolean var) {
        this.name = name;
        this.changeable = changeable;
        this.var = var;
    }

    public VariableNode(Node value) {
        this.value = value;
    }

    private String name;
    private TypeNode type;
    private boolean changeable, var;
    private Node value;
    private Node to;
    private Node from;

    public String getName() {
        return name;
    }
    public TypeNode getType() {
        return type;
    }
    public boolean getChangeable() {
        return changeable;
    }
    public boolean getVar() {
        return var;
    }
    public Node getValue() {
        return value;
    }
    public Node getTo() {
        return to;
    }
    public Node getFrom() {
        return from;
    }

    public void setValue(Node input) {
        value = input;
    }

    @Override
    public String toString() {
        if(name == null) {
            return value + "";
        }
        else if(value != null) {
            return "constant " + name + " = " + value;
        }
        else if(var) {
            return "var " + name + " : " + type;
        }
        else {
            return "variable " + name + " : " + type;
        }
    }
}
