import java.util.ArrayList;
import java.util.Map;

public class SemanticAnalysis {

    private void typeMatcher(TypeNode.dataType correctSideType, Node input) throws RunTimeException {
        if(input instanceof VariableNode && (((VariableNode)input).getType().getDataType()) != correctSideType) {
            throw new RunTimeException("Variable " + ((VariableNode)input).getName() + " of type "
                    + ((VariableNode)input).getType() + " incorrectly assigned to variable of type " + correctSideType + ".");
        }
        else if(input instanceof MathOpNode) {
            typeMatcher(correctSideType, ((MathOpNode)input).getLeft());
            typeMatcher(correctSideType, ((MathOpNode) input).getRight());
        }
        else if(input instanceof StringNode && !(correctSideType == TypeNode.dataType.STRING)) {
            throw new RunTimeException("String incorrectly assigned to variable of type " + correctSideType + ".");
        }
        else if(input instanceof BooleanNode && !(correctSideType == TypeNode.dataType.BOOLEAN)) {
            throw new RunTimeException("Boolean incorrectly assigned to variable of type " + correctSideType + ".");
        }
        else if(input instanceof CharacterNode && !(correctSideType == TypeNode.dataType.CHARACTER)) {
            throw new RunTimeException("Character incorrectly assigned to variable of type " + correctSideType + ".");
        }
        throw new RunTimeException("Non valid type found.");
    }

    // remove function and simply paste code at its call?
    private void checkAssignmentType(AssignmentNode input, ArrayList<VariableNode> constantsAndVariables) throws RunTimeException {
        VariableNode analyzeNode = null;
        for(VariableNode currentNode : constantsAndVariables) { // Check if AssignmentNode is using a variable that has been declared.
            if(currentNode.getName().equals(input.getTarget().getReferenceName())) {
                analyzeNode = currentNode;
                break;
            }
        }
        if(analyzeNode == null) {
            throw new RunTimeException("Variable name used which was not previously declared.");
        }

        // Then check that the types are matching.
        typeMatcher(analyzeNode.getType().getDataType(), input.getValue());
    }

    private void checkStatements(StatementNode statement, ArrayList<VariableNode> variableInput) throws RunTimeException {
        if(statement instanceof AssignmentNode) {
            checkAssignmentType((AssignmentNode)statement, variableInput);
        }
        else if(statement instanceof IfNode) {
            for(StatementNode ifSubStatement : ((IfNode)statement).getStatements()) {
                checkStatements(ifSubStatement, variableInput);
            }
            if(((IfNode)statement).getNextIf() != null) {
                checkStatements(((IfNode)statement).getNextIf(), variableInput);
            }
        }
        else if(statement instanceof WhileNode) {
            for(StatementNode whileSubStatement : ((WhileNode)statement).getStatements()) {
                checkStatements(whileSubStatement, variableInput);
            }
        }
        else if(statement instanceof ForNode) {
            for(StatementNode forSubStatement : ((ForNode)statement).getStatements()) {
                checkStatements(forSubStatement, variableInput);
            }
        }
        else if(statement instanceof RepeatNode) {
            for(StatementNode repeatSubStatement : ((RepeatNode)statement).getStatements()) {
                checkStatements(repeatSubStatement, variableInput);
            }
        }
    }

    public void checkAssignments(ProgramNode input) throws RunTimeException {
        for(Map.Entry<String, FunctionNode> function : input.getFunctionNodeCollection().entrySet()) {
            for(StatementNode statement : function.getValue().getStatements()) {
                checkStatements(statement, function.getValue().getConstantsAndVariables());
            }
        }
    }

}
