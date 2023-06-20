import java.util.ArrayList;

public class BuiltInSubstring extends BuiltInFunction {

    public BuiltInSubstring(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType stringInput = nodeCollection.get(0), indexInput = nodeCollection.get(1), lengthInput = nodeCollection.get(2);

        if(stringInput instanceof StringDataType && indexInput instanceof IntegerDataType && lengthInput instanceof IntegerDataType && nodeCollection.get(3) instanceof StringDataType) {
            int indexValue = ((IntegerDataType) indexInput).getData();
            nodeCollection.set(2, new StringDataType(((StringDataType)stringInput).getData().substring(indexValue, indexValue + ((IntegerDataType)lengthInput).getData())));
        }
    }
}
