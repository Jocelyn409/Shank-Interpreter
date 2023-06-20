import java.util.ArrayList;

public class BuiltInIntegerToReal extends BuiltInFunction {

    public BuiltInIntegerToReal(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType input = nodeCollection.get(0);

        if(input instanceof IntegerDataType && nodeCollection.get(1) instanceof RealDataType) {
            nodeCollection.set(1, new RealDataType((float)((IntegerDataType)input).getData()));
        }
    }
}
