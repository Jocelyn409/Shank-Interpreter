import java.util.ArrayList;

public class BuiltInRealToInteger extends BuiltInFunction {

    public BuiltInRealToInteger(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType input = nodeCollection.get(0);

        if(input instanceof RealDataType && nodeCollection.get(1) instanceof IntegerDataType) {
            nodeCollection.set(1, new IntegerDataType((int)((RealDataType)input).getData()));
        }
    }
}
