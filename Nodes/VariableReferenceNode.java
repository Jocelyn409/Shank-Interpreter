public class VariableReferenceNode extends Node {

    public VariableReferenceNode(String referenceName, Node arrayIndexExpression) {
        this.referenceName = referenceName;
        this.arrayIndexExpression = arrayIndexExpression;
    }

    public VariableReferenceNode(String referenceName) {
        this.referenceName = referenceName;
    }

    private final String referenceName;
    private Node arrayIndexExpression;

    public String getReferenceName() {
        return referenceName;
    }
    public Node getArrayIndexExpression() {
        return arrayIndexExpression;
    }

    @Override
    public String toString() {
        if(arrayIndexExpression == null) {
            return "VariableReferenceNode(" + referenceName + ")";
        }
        return "VariableReferenceNode(" + referenceName + "[" + arrayIndexExpression + "]";
    }
}
