public class ParameterNode extends Node{

    public ParameterNode(VariableReferenceNode varParameter, Node parameter) {
        this.varParameter = varParameter;
        this.parameter = parameter;
    }

    private final VariableReferenceNode varParameter;
    private final Node parameter;

    @Override
    public String toString() {
        if(varParameter != null) {
            return varParameter.toString();
        }
        return parameter.toString();
    }
}
