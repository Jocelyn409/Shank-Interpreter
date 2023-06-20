import java.util.ArrayList;

public class FunctionNode extends Node {

    public FunctionNode(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        this.name = name;
        this.parameters = parameters;
        this.constantsAndVariables = constantsAndVariables;
        this.statements = statements;
    }

    public FunctionNode(String name) {
        this.name = name;
    }

    protected boolean isVariadic() {
        return false;
    }

    private final String name;
    private ArrayList<VariableNode> parameters;
    private ArrayList<VariableNode> constantsAndVariables;
    private ArrayList<StatementNode> statements;

    public String getName() {
        return name;
    }
    public ArrayList<VariableNode> getParameters() {
        return parameters;
    }
    public ArrayList<VariableNode> getConstantsAndVariables() {
        return constantsAndVariables;
    }
    public ArrayList<StatementNode> getStatements() {
        return statements;
    }

    public void setParameters(ArrayList<VariableNode> input) {
        parameters = input;
    }

    @Override
    public String toString() {
        String functionString = "\nFunctionNode(" + name;
        if(parameters != null) {
            functionString = functionString + "\n" +
                    "               (parameters:\n" +
                    "                   " + parameters + "\n" +
                    "               )";
        }
        if(!constantsAndVariables.isEmpty()) {
            functionString = functionString + "\n" +
                    "               (constants and variables:\n" +
                    "                   " + constantsAndVariables + "\n" +
                    "               )";
        }
        if(!statements.isEmpty()) {
            functionString = functionString + "\n" +
                    "               (statements:\n" +
                    "                   " + statements + "\n" +
                    "               )";
        }
        functionString = functionString + "\n)";
        return functionString;
    }
}
