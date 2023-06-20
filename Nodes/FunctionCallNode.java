import java.util.ArrayList;

public class FunctionCallNode extends StatementNode {

    public FunctionCallNode(String name, ArrayList<VariableNode> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    private final String name;
    private final ArrayList<VariableNode> parameters;

    public String getName() {
        return name;
    }
    public ArrayList<VariableNode> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        if(parameters == null) {
            return name;
        }
        return name + " " + parameters;
    }
}

