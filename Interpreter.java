import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Interpreter {

    public Interpreter(HashMap<String, FunctionNode> functionCollection) {
        this.functionCollection = functionCollection;
    }

    public HashMap<String, FunctionNode> functionCollection;

    private InterpreterDataType expression(Node input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        if(input instanceof VariableReferenceNode) {
            return variableReferenceNode((VariableReferenceNode)input, variables);
        }
        else if(input instanceof VariableNode && ((VariableNode)input).getType() == null) {
            return checkType(((VariableNode)input).getValue()); // Constant was found since the VariableNode type is null.
        }
        else if(input instanceof MathOpNode) {
            return mathOpNode((MathOpNode)input, variables);
        }
        else if(input instanceof BooleanCompareNode) {
            return booleanCompare((BooleanCompareNode)input, variables);
        }
        else if(checkType(input) == null) {
            throw new RunTimeException("Invalid Expression");
        }
        return null;
    }

    private InterpreterDataType checkType(Node value) {
        if(value instanceof IntegerNode) {
            return new IntegerDataType(((IntegerNode)value).getIntValue());
        }
        else if(value instanceof RealNode) {
            return new RealDataType(((RealNode)value).getFloatValue());
        }
        else if(value instanceof StringNode) {
            return new StringDataType(((StringNode)value).getStringValue());
        }
        else if(value instanceof CharacterNode) {
            return new CharacterDataType(((CharacterNode)value).getCharValue());
        }
        else if(value instanceof BooleanNode) {
            return new BooleanDataType(((BooleanNode)value).getBoolValue());
        }
        else {
            return null;
        }
    }

    private BooleanDataType booleanCompare(BooleanCompareNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        InterpreterDataType leftCompare = expression(input.getLeft(), variables), rightCompare = expression(input.getRight(), variables);
        BooleanCompareNode.booleanOperators operator = input.getOperator();

        if(leftCompare instanceof IntegerDataType) {
            if(!(rightCompare instanceof IntegerDataType)) {
                throw new RunTimeException("BooleanCompare with mismatching left and right side types.");
            }
            return switch(operator) {
                case EQUALS -> new BooleanDataType(((IntegerDataType)leftCompare).getData() == ((IntegerDataType)rightCompare).getData());
                case NOTEQUALS -> new BooleanDataType(((IntegerDataType)leftCompare).getData() != ((IntegerDataType)rightCompare).getData());
                case LESSTHAN -> new BooleanDataType(((IntegerDataType)leftCompare).getData() < ((IntegerDataType)rightCompare).getData());
                case GREATERTHAN -> new BooleanDataType(((IntegerDataType)leftCompare).getData() > ((IntegerDataType)rightCompare).getData());
                case LESSEQUALS -> new BooleanDataType(((IntegerDataType)leftCompare).getData() <= ((IntegerDataType)rightCompare).getData());
                case GREATEREQUALS -> new BooleanDataType(((IntegerDataType)leftCompare).getData() >= ((IntegerDataType)rightCompare).getData());
            };
        }
        else if(leftCompare instanceof RealDataType) {
            if(!(rightCompare instanceof RealDataType)) {
                throw new RunTimeException("BooleanCompare with mismatching left and right side types.");
            }
            return switch(operator) {
                case EQUALS -> new BooleanDataType(((RealDataType)leftCompare).getData() == ((RealDataType)rightCompare).getData());
                case NOTEQUALS -> new BooleanDataType(((RealDataType)leftCompare).getData() != ((RealDataType)rightCompare).getData());
                case LESSTHAN -> new BooleanDataType(((RealDataType)leftCompare).getData() < ((RealDataType)rightCompare).getData());
                case GREATERTHAN -> new BooleanDataType(((RealDataType)leftCompare).getData() > ((RealDataType)rightCompare).getData());
                case LESSEQUALS -> new BooleanDataType(((RealDataType)leftCompare).getData() <= ((RealDataType)rightCompare).getData());
                case GREATEREQUALS -> new BooleanDataType(((RealDataType)leftCompare).getData() >= ((RealDataType)rightCompare).getData());
            };
        }
        else if(leftCompare instanceof StringDataType) {
            if(!(rightCompare instanceof StringDataType)) {
                throw new RunTimeException("BooleanCompare with mismatching left and right side types.");
            }
            return switch(operator) {
                case EQUALS -> new BooleanDataType(Objects.equals(((StringDataType)leftCompare).getData(), ((StringDataType)rightCompare).getData()));
                case NOTEQUALS -> new BooleanDataType(!Objects.equals(((StringDataType)leftCompare).getData(), ((StringDataType)rightCompare).getData()));
                default -> throw new RunTimeException("String booleanCompares can only use EQUALS and NOTEQUALS operators.");
            };
        }
        throw new RunTimeException("BooleanCompare with incorrect data types.");
    }

    private InterpreterDataType variableReferenceNode(VariableReferenceNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        String referenceName = input.getReferenceName();

        if(!variables.containsKey(referenceName)) {
            throw new RunTimeException("Variable " + referenceName + " referenced which does not exist.");
        }
        return checkType(input);
    }

    private InterpreterDataType mathOpNode(MathOpNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        InterpreterDataType leftCompare = expression(input.getLeft(), variables), rightCompare = expression(input.getRight(), variables);
        MathOpNode.mathOperators operator = input.getOperator();

        if(leftCompare instanceof IntegerDataType) {
            if(!(rightCompare instanceof IntegerDataType)) {
                throw new RunTimeException("MathOpNode with mismatching left and right side types.");
            }
            return switch (operator) {
                case PLUS -> new IntegerDataType(((IntegerDataType)leftCompare).getData() + ((IntegerDataType)rightCompare).getData());
                case MINUS -> new IntegerDataType(((IntegerDataType)leftCompare).getData() - ((IntegerDataType)rightCompare).getData());
                case MULTIPLY -> new IntegerDataType(((IntegerDataType)leftCompare).getData() * ((IntegerDataType)rightCompare).getData());
                case DIVIDE -> new IntegerDataType(((IntegerDataType)leftCompare).getData() / ((IntegerDataType)rightCompare).getData());
                case MOD -> new IntegerDataType(((IntegerDataType)leftCompare).getData() % ((IntegerDataType)rightCompare).getData());
            };
        }
        else if(leftCompare instanceof RealDataType) {
            if(!(rightCompare instanceof RealDataType)) {
                throw new RunTimeException("MathOpNode with mismatching left and right side types.");
            }
            return switch(operator) {
                case PLUS -> new RealDataType(((RealDataType)leftCompare).getData() + ((RealDataType)rightCompare).getData());
                case MINUS -> new RealDataType(((RealDataType)leftCompare).getData() - ((RealDataType)rightCompare).getData());
                case MULTIPLY -> new RealDataType(((RealDataType)leftCompare).getData() * ((RealDataType)rightCompare).getData());
                case DIVIDE -> new RealDataType(((RealDataType)leftCompare).getData() / ((RealDataType)rightCompare).getData());
                case MOD -> new RealDataType(((RealDataType)leftCompare).getData() % ((RealDataType)rightCompare).getData());
            };
        }
        else if(leftCompare instanceof StringDataType) {
            if(!(rightCompare instanceof StringDataType)) {
                throw new RunTimeException("MathOpNode with mismatching left and right side types.");
            }
            if(operator == MathOpNode.mathOperators.PLUS) {
                return new StringDataType(((StringDataType)leftCompare).getData() + ((StringDataType)rightCompare).getData());
            }
            throw new RunTimeException("MathOpNode can only apply PLUS operator to strings.");
        }
        throw new RunTimeException("MathOpNode with incorrect data types.");
    }

    private void ifNode(IfNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        if(booleanCompare((BooleanCompareNode)input.getComparison(), variables).getData()) { // Check if boolean is true or false.
            interpretBlock(variables, input.getStatements());
        }
        else if(input.getNextIf() != null) { // If not at the end of the if chain, call ifNode recursively.
            ifNode(input.getNextIf(), variables);
        }
    }

    private void forNode(ForNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        if(!variables.containsKey(input.getCounter())) {
            throw new RunTimeException("Counter variable in for loop must have been declared before.");
        }
        for(int counter = ((IntegerNode)input.getFrom()).getIntValue(); counter <= ((IntegerNode)input.getTo()).getIntValue(); counter++) {
            // update counter here.
            interpretBlock(variables, input.getStatements());
        }
    }

    private void repeatNode(RepeatNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        do {
            interpretBlock(variables, input.getStatements());
        } while(booleanCompare((BooleanCompareNode)input.getComparison(), variables).getData());
    }

    private void whileNode(WhileNode input, HashMap<String, InterpreterDataType> variables) throws RunTimeException {
        while(booleanCompare((BooleanCompareNode)input.getComparison(), variables).getData()) {
            interpretBlock(variables, input.getStatements());
        }
    }

    private void functionCallNode(FunctionCallNode input, HashMap<String, FunctionNode> functions) throws RunTimeException {
        if(!functions.containsKey(input.getName())) { // Check function exists.
            throw new RunTimeException("Function called which does not exist.");
        }

        // Get FunctionNode of the function called, then check to make sure the number of parameters are correct.
        FunctionNode functionValue = functions.get(input.getName());
        if(!((input.getParameters().size() == functionValue.getParameters().size()) || (functionValue.isVariadic()))) {
            throw new RunTimeException("Incorrect number of parameters while calling function " + functionValue.getName() + ".");
        }

        // Create ArrayList of FunctionCallNode's parameters as IDTs.
        ArrayList<InterpreterDataType> parameterValues = new ArrayList<>();
        for(Node parameter : input.getParameters()) {
            parameterValues.add(expression(parameter, null)); // problem with expression, probably.
        }

        // Call execute() if function is a BuiltInFunction; else call interpretFunction() on it.
        if(functionValue instanceof BuiltInFunction) {
            ((BuiltInFunction)functionValue).execute(input, parameterValues);
        }
        else {
            interpretFunction(functionValue);
        }

        // Ensure that any parameter values that were changed by the function call are updated.
        int index = 0;
        for(InterpreterDataType paramIDT : parameterValues) {
            if(functionValue.isVariadic()
                    || (functionValue.getParameters().get(index).getVar()
                            && input.getParameters().get(index).getVar())) {
                if(paramIDT instanceof IntegerDataType) {
                    input.getParameters().get(index).setValue(new IntegerNode(((IntegerDataType)paramIDT).getData()));
                }
                else if(paramIDT instanceof StringDataType) {
                    input.getParameters().get(index).setValue(new StringNode(((StringDataType)paramIDT).getData()));
                }
                else if(paramIDT instanceof RealDataType) {
                    input.getParameters().get(index).setValue(new RealNode(((RealDataType)paramIDT).getData()));
                }
                else if(paramIDT instanceof BooleanDataType) {
                    input.getParameters().get(index).setValue(new BooleanNode(((BooleanDataType)paramIDT).getData()));
                }
                else if(paramIDT instanceof CharacterDataType) {
                    input.getParameters().get(index).setValue(new CharacterNode(((CharacterDataType)paramIDT).getData()));
                }
            }
            index++;
        }

    }

    private void interpretBlock(HashMap<String, InterpreterDataType> variables, ArrayList<StatementNode> statements) throws RunTimeException {
        for(Node analyzeNode : statements) {
            if(analyzeNode instanceof BooleanCompareNode) {
                booleanCompare((BooleanCompareNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof VariableReferenceNode) {
                variableReferenceNode((VariableReferenceNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof MathOpNode) {
                mathOpNode((MathOpNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof IfNode) {
                ifNode((IfNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof ForNode) {
                forNode((ForNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof RepeatNode) {
                repeatNode((RepeatNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof WhileNode) {
                whileNode((WhileNode)analyzeNode, variables);
            }
            else if(analyzeNode instanceof AssignmentNode) {
                String name = ((AssignmentNode)analyzeNode).getTarget().getReferenceName();
                if(!variables.containsKey(name)) {
                    variables.put(name, expression(((AssignmentNode)analyzeNode).getValue(), variables));
                }
            }
            else if(analyzeNode instanceof FunctionCallNode) {
                functionCallNode((FunctionCallNode)analyzeNode, functionCollection);
            }
            else {
                checkType(analyzeNode);
            }
        }

    }

    public void interpretFunction(FunctionNode input) throws RunTimeException {
        HashMap<String, InterpreterDataType> localVariables = new HashMap<>();

        for(VariableNode loopNode : input.getConstantsAndVariables()) {
            TypeNode.dataType checkType = loopNode.getType().getDataType();

            if(checkType != null) {
                switch(checkType) {
                    case INTEGER -> localVariables.put(loopNode.getName(), new IntegerDataType(0));
                    case REAL -> localVariables.put(loopNode.getName(), new RealDataType(0.0F));
                    case STRING -> localVariables.put(loopNode.getName(), new StringDataType(""));
                    case CHARACTER -> localVariables.put(loopNode.getName(), new CharacterDataType('\0'));
                    case BOOLEAN -> localVariables.put(loopNode.getName(), new BooleanDataType(null));
                    default -> throw new RunTimeException("Variable with invalid data type.");
                }
            }
            else {
                if(loopNode.getValue() instanceof IntegerNode) {
                    localVariables.put(loopNode.getName(), new IntegerDataType(
                            ((IntegerNode)loopNode.getValue()).getIntValue()
                    ));
                }
                else if(loopNode.getValue() instanceof RealNode) {
                    localVariables.put(loopNode.getName(), new RealDataType(
                            ((RealNode)loopNode.getValue()).getFloatValue()
                    ));
                }
                else if(loopNode.getValue() instanceof StringNode) {
                    localVariables.put(loopNode.getName(), new StringDataType(
                            ((StringNode)loopNode.getValue()).getStringValue()
                    ));
                }
                else if(loopNode.getValue() instanceof CharacterNode) {
                    localVariables.put(loopNode.getName(), new CharacterDataType(
                            ((CharacterNode)loopNode.getValue()).getCharValue()
                    ));
                }
                else if(loopNode.getValue() instanceof BooleanNode) {
                    localVariables.put(loopNode.getName(), new BooleanDataType(
                            ((BooleanNode)loopNode.getValue()).getBoolValue()
                    ));
                }
                else {
                    throw new RunTimeException("Constant with invalid data type.");
                }
            }


        }
        interpretBlock(localVariables, input.getStatements());
    }

}
