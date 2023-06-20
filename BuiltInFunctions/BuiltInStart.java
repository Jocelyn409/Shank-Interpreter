import java.util.ArrayList;

public class BuiltInStart extends BuiltInFunction {

    public BuiltInStart(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType array = nodeCollection.get(0), input = nodeCollection.get(1);

        if(array instanceof ArrayDataType<?> && nodeCollection.get(2) instanceof IntegerDataType) {
            nodeCollection.set(1, new IntegerDataType(((ArrayDataType<?>)array).getData().indexOf(input)));
        }
    }
}
