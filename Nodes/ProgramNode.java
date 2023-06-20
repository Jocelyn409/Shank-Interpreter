import java.util.HashMap;

public class ProgramNode extends Node {

    public ProgramNode(HashMap<String, FunctionNode> functionNodeCollection) {
        this.functionNodeCollection = functionNodeCollection;
    }

    private HashMap<String, FunctionNode> functionNodeCollection;

    public HashMap<String, FunctionNode> getFunctionNodeCollection() {
        return functionNodeCollection;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (String key: functionNodeCollection.keySet()) {
            output.append(functionNodeCollection.get(key));
        }
        return output.toString();
    }
}
