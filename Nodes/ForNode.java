import java.util.ArrayList;

public class ForNode extends StatementNode {

    public ForNode(Node from, Node to, ArrayList<StatementNode> statements, String counter) {
        this.from = from;
        this.to = to;
        this.statements = statements;
        this.counter = counter;
    }

    private final Node from, to;
    private final ArrayList<StatementNode> statements;
    private final String counter;

    public Node getFrom() {
        return from;
    }
    public Node getTo() {
        return to;
    }
    public ArrayList<StatementNode> getStatements() {
        return statements;
    }
    public String getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "ForNode(from " + from + " to " + to + ", with statements: " + statements + ", with counter " + counter + ")";
    }
}
