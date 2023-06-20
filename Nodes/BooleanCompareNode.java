public class BooleanCompareNode extends Node {

    public BooleanCompareNode(booleanOperators operator, Node left, Node right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public enum booleanOperators {
        GREATERTHAN, LESSTHAN, GREATEREQUALS, LESSEQUALS, EQUALS, NOTEQUALS
    }

    private final booleanOperators operator;
    private final Node left, right;

    public booleanOperators getOperator() {
        return operator;
    }
    public Node getLeft() {
        return left;
    }
    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}
