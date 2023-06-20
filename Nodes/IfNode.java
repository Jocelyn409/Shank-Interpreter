import java.util.ArrayList;

public class IfNode extends StatementNode {

    public IfNode(Node comparison, ArrayList<StatementNode> statements, IfNode nextIf) {
        this.comparison = comparison;
        this.statements = statements;
        this.nextIf = nextIf;
    }

    private final Node comparison;
    private final ArrayList<StatementNode> statements;
    private final IfNode nextIf;

    public Node getComparison() {
        return comparison;
    }
    public ArrayList<StatementNode> getStatements() {
        return statements;
    }
    public IfNode getNextIf() {
        return nextIf;
    }

    @Override
    public String toString() {
        if(nextIf == null) {
            return comparison + " " + statements;
        }
         return comparison + " " + statements + " " + nextIf;
    }
}
