import java.util.ArrayList;

public class BuiltInRight extends BuiltInFunction {

    public BuiltInRight(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType stringInput = nodeCollection.get(0), lengthInput = nodeCollection.get(1);

        if(stringInput instanceof StringDataType && lengthInput instanceof IntegerDataType && nodeCollection.get(2) instanceof StringDataType) {
            nodeCollection.set(2, new StringDataType(((StringDataType)stringInput).getData().substring(((IntegerDataType)lengthInput).getData())));
        }
    }

}
