import java.util.ArrayList;

public class RepeatNode extends StatementNode {

    public RepeatNode(Node comparison, ArrayList<StatementNode> statements) {
        this.comparison = comparison;
        this.statements = statements;
    }

    private final Node comparison;
    private final ArrayList<StatementNode> statements;

    public Node getComparison() {
        return comparison;
    }
    public ArrayList<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return comparison + " " + statements;
    }
}
