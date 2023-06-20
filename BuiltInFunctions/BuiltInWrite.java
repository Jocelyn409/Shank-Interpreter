import java.util.ArrayList;

public class BuiltInWrite extends BuiltInFunction {

    public BuiltInWrite(String name, ArrayList<VariableNode> parameters, ArrayList<VariableNode> constantsAndVariables, ArrayList<StatementNode> statements) {
        super(name, parameters, constantsAndVariables, statements);
    }

    public void execute(FunctionCallNode nodeCallValue, ArrayList<InterpreterDataType> nodeCollection) {
        for(int position = 0; position <= nodeCollection.size()-1; position++) {
            if(nodeCollection.get(position) instanceof StringDataType) {
                System.out.print(nodeCollection.get(position).toString());
            }
            else {
                System.out.print("");
            }
            if(position != nodeCollection.size()-1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    @Override
    protected boolean isVariadic() {
        return true;
    }

    @Override
    public String toString() {
        return "BuiltInWrite(" + getParameters() + ")";
    }
}
