import java.util.ArrayList;
import java.util.Scanner;

public class BuiltInRead extends BuiltInFunction {

    public BuiltInRead(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        Scanner kb = new Scanner(System.in);
        String input = kb.nextLine();
        String[] separatedInput = input.split(", "); // Split input by commas and put the results into an array

        // For each input separated by a comma, add it to the nodeCollection.
        for(int position = 0; position <= separatedInput.length-1; position++) {
            nodeCollection.set(position, new StringDataType(separatedInput[position]));
        }
    }

    @Override
    protected boolean isVariadic() {
        return true;
    }
}
