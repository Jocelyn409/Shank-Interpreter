import java.util.ArrayList;
import java.util.Random;

public class BuiltInGetRandom extends BuiltInFunction {

    public BuiltInGetRandom(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        InterpreterDataType upperBound = nodeCollection.get(0);
        Random rand = new Random();

        if(upperBound instanceof IntegerDataType) {
            nodeCollection.set(0, new IntegerDataType(rand.nextInt(((IntegerDataType)upperBound).getData())));
        }
    }
}
