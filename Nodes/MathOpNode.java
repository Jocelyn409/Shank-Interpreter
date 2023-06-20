public class MathOpNode extends Node {

    public MathOpNode(mathOperators operator, Node left, Node right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public enum mathOperators {
        PLUS, MINUS, MULTIPLY, DIVIDE, MOD
    }

    private final mathOperators operator;
    private final Node left, right;

    public mathOperators getOperator() {
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
        return "MathOpNode(" + operator + ", " + left + ", " + right + ")";
    }
}
