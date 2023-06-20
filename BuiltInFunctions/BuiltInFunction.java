import java.util.ArrayList;

public abstract class BuiltInFunction extends FunctionNode {

    public BuiltInFunction(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public abstract void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection);
}
