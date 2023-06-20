public class AssignmentNode extends StatementNode {

    public AssignmentNode(VariableReferenceNode target, Node value) {
        this.target = target;
        this.value = value;
    }

    private final VariableReferenceNode target;
    private final Node value;

    public VariableReferenceNode getTarget() {
        return target;
    }
    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        return target + " := " + value;
    }
}
