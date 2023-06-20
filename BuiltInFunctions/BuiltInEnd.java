import java.util.ArrayList;

public class BuiltInEnd extends BuiltInFunction {

    public BuiltInEnd(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType array = nodeCollection.get(0), input = nodeCollection.get(1);

        if(array instanceof ArrayDataType<?> && nodeCollection.get(2) instanceof IntegerDataType) {
            nodeCollection.set(1, new IntegerDataType(((ArrayDataType<?>)array).getData().lastIndexOf(input)));
        }
        /*
        InterpreterDataType arrayInput = nodeCollection.get(0);

        if(arrayInput instanceof ArrayDataType<?> && nodeCollection.get(1) instanceof IntegerDataType) {
            nodeCollection.set(1, new IntegerDataType());
        }*/
    }
}
