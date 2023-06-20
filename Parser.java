import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    public Parser(ArrayList<Token> tokenCollection) {
        this.tokenCollection = tokenCollection;
    }

    public ArrayList<Token> tokenCollection;
    public HashMap<String, FunctionNode> functionList = new HashMap<>();

    private Token matchAndRemove(Token.tokenType tokenCheck) {
        if(tokenCheck == peek(0)) {
            System.out.print(tokenCheck + " ");
            return tokenCollection.remove(0);
        }
        else {
            return null;
        }
    }

    private void expectEndsOfLine() throws SyntaxErrorException {
        boolean EOLFound = false;
        while(matchAndRemove(Token.tokenType.ENDOFLINE) != null) {
            EOLFound = true;
        }
        if(!EOLFound) {
            throw new SyntaxErrorException("No ENDOFLINE token found");
        }
    }

    private Token.tokenType peek(int lookAt) {
        if(lookAt < tokenCollection.size()) {
            return tokenCollection.get(lookAt).getToken();
        }
        else {
            return null;
        }
    }

    // matchAndRemove()'s for PLUS and MINUS, then returns a mathOperator if found.
    private MathOpNode.mathOperators checkPlusMinus() {
        if(matchAndRemove(Token.tokenType.PLUS) != null) {
            return MathOpNode.mathOperators.PLUS;
        }
        else if(matchAndRemove(Token.tokenType.MINUS) != null) {
            return MathOpNode.mathOperators.MINUS;
        }
        return null;
    }

    // matchAndRemove()'s for MULTIPLY, DIVIDE, and MOD, then returns a mathOperator if found.
    private MathOpNode.mathOperators checkMultiplyDivideMod() {
        if(matchAndRemove(Token.tokenType.MULTIPLY) != null) {
            return MathOpNode.mathOperators.MULTIPLY;
        }
        else if(matchAndRemove(Token.tokenType.DIVIDE) != null) {
            return MathOpNode.mathOperators.DIVIDE;
        }
        else if(matchAndRemove(Token.tokenType.MOD) != null) {
            return MathOpNode.mathOperators.MOD;
        }
        return null;
    }

    // matchAndRemove()'s for boolean operators, then returns a booleanOperator if found.
    private BooleanCompareNode.booleanOperators checkBooleanOp() {
        if(matchAndRemove(Token.tokenType.GREATERTHAN) != null) {
            return BooleanCompareNode.booleanOperators.GREATERTHAN;
        }
        else if(matchAndRemove(Token.tokenType.LESSTHAN) != null) {
            return BooleanCompareNode.booleanOperators.LESSTHAN;
        }
        else if(matchAndRemove(Token.tokenType.GREATEREQUALS) != null) {
            return BooleanCompareNode.booleanOperators.GREATEREQUALS;
        }
        else if(matchAndRemove(Token.tokenType.LESSEQUALS) != null) {
            return BooleanCompareNode.booleanOperators.LESSEQUALS;
        }
        else if(matchAndRemove(Token.tokenType.EQUALS) != null) {
            return BooleanCompareNode.booleanOperators.EQUALS;
        }
        else if(matchAndRemove(Token.tokenType.NOTEQUAL) != null) {
            return BooleanCompareNode.booleanOperators.NOTEQUALS;
        }
        return null;
    }

    private Node boolCompare() throws SyntaxErrorException {
        Node left = expression(), right;
        BooleanCompareNode.booleanOperators opType;

        /* Similar to term() and expression(), we create a left side, this time using expression(),
         * then if there is a boolean operator, we look for and create the right side then
         * return a BooleanCompareNode. If there isn't a boolean operator, return the left side. */
        if((opType = checkBooleanOp()) != null) {
            if((right = expression()) == null) {
                throw new SyntaxErrorException("Expression expected after boolean operator.");
            }
            return new BooleanCompareNode(opType, left, right);
        }
        return left;
    }

    private StatementNode statement() throws SyntaxErrorException {
        StatementNode statement;

        if((statement = assignment()) != null) {
            return statement;
        }
        if((statement = parseIf()) != null) {
            return statement;
        }
        if((statement = parseWhile()) != null) {
            return statement;
        }
        if((statement = parseFor()) != null) {
            return statement;
        }
        if((statement = parseFunctionCalls()) != null) {
            return statement;
        }
        if((statement = parseWrite()) != null) {
            return statement;
        }
        return null;
    }

    private ArrayList<StatementNode> statements() throws SyntaxErrorException {
        StatementNode statementNode;
        ArrayList<StatementNode> statementCollection = new ArrayList<>();

        if(matchAndRemove(Token.tokenType.INDENT) == null) {
            throw new SyntaxErrorException("Indent expected for statements or possible missing identifier.");
        }
        while((statementNode = statement()) != null) {
            statementCollection.add(statementNode);
        }
        if(matchAndRemove(Token.tokenType.DEDENT) == null) {
            throw new SyntaxErrorException("Dedent expected after statement or possible missing identifier.");
        }
        return statementCollection;
    }

    private VariableReferenceNode arrayIndex() throws SyntaxErrorException {
        Token identifierName;
        Node expressionNode;

        if((identifierName = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
            return null;
        }
        if(matchAndRemove(Token.tokenType.LEFTBRACKET) == null) {
            throw new SyntaxErrorException("Left bracket expected after identifier.");
        }

        // Recursively call arrayIndex(). If it comes back null, call expression().
        if((expressionNode = arrayIndex()) == null) {
            if((expressionNode = expression()) == null) {
                throw new SyntaxErrorException("Expression expected after left bracket.");
            }
        }
        if(matchAndRemove(Token.tokenType.RIGHTBRACKET) == null) {
            throw new SyntaxErrorException("Right bracket expected after expression.");
        }
        return new VariableReferenceNode(identifierName.getValue(), expressionNode);
    }
    
    private AssignmentNode assignment() throws SyntaxErrorException {
        Token identifierName;
        Node expressionNode = null, comparison;

        if((identifierName = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
            return null;
        }
        if(matchAndRemove(Token.tokenType.LEFTBRACKET) != null) {
            // Since a left bracket was found, call arrayIndex() to evaluate the array.
            if((expressionNode = arrayIndex()) == null) {
                throw new SyntaxErrorException("Expression or identifier expected after left bracket.");
            }
            if(matchAndRemove(Token.tokenType.RIGHTBRACKET) == null) {
                throw new SyntaxErrorException("Right bracket expected after expression.");
            }
        }
        if(matchAndRemove(Token.tokenType.ASSIGNMENT) == null) {
            throw new SyntaxErrorException("Assignment expected after " + identifierName);
        }
        if((comparison = boolCompare()) == null) { // Evaluate right side of assignment.
            throw new SyntaxErrorException("Assignment with missing right side.");
        }
        return new AssignmentNode(new VariableReferenceNode(identifierName.getValue(), expressionNode), comparison);
    }

    private Node expression() throws SyntaxErrorException {
        MathOpNode.mathOperators opType;
        Node left = term(), right;

        /* If opType is plus or minus, have the right node recursively call expression().
         * This eventually returns the left node when opType is null, thus acting as a term() call. */
        if((opType = checkPlusMinus()) != null) {
            if((right = expression()) == null) {
                throw new SyntaxErrorException("Expression expected after + or - operator.");
            }
            return new MathOpNode(opType, left, right);
        }
        return left;
    }

    private Node term() throws SyntaxErrorException {
        MathOpNode.mathOperators opType;
        Node left = factor(), right;

        /* If opType is a multiply, divide, or mod, have the right node recursively call term().
         * This eventually returns the left node when opType is null, thus acting as a factor() call. */
        if((opType = checkMultiplyDivideMod()) != null) {
            if((right = term()) == null) {
                throw new SyntaxErrorException("Term expected after *, /, or % operator.");
            }
            return new MathOpNode(opType, left, right);
        }
        return left;
    }

    private Node factor() throws SyntaxErrorException {
        Token analyzeToken;
        Node expressionNode;
        int negativeFlipper = 1;

        // Flips negativeFlipper each time a MINUS token is found, thus dealing with multiple negatives/minuses.
        while(matchAndRemove(Token.tokenType.MINUS) != null) {
            negativeFlipper = -negativeFlipper;
        }

        /* If a NUMBER token is found, return either a RealNode or IntegerNode depending on if the
         * value of the NUMBER token has a decimal point or not. Else if the token is a LEFTPAREN,
         * call expression then look for a RIGHTPAREN. Else if it is an IDENTIFIER, look for
         * an expression or return the identifier. */
        if((analyzeToken = matchAndRemove(Token.tokenType.NUMBER)) != null) {
            String numberValue = analyzeToken.getValue();
            if(numberValue.contains(".")) {
                return new RealNode(Float.parseFloat(numberValue)*negativeFlipper);
            }
            return new IntegerNode(Integer.parseInt(numberValue)*negativeFlipper);
        }
        else if(matchAndRemove(Token.tokenType.LEFTPAREN) != null) {
            if((expressionNode = expression()) == null) {
                throw new SyntaxErrorException("Expression expected after left parentheses.");
            }
            if(matchAndRemove(Token.tokenType.RIGHTPAREN) == null) {
                throw new SyntaxErrorException("Right parentheses expected after expression.");
            }
            return expressionNode;
        }
        else if((analyzeToken = matchAndRemove(Token.tokenType.IDENTIFIER)) != null) {
            if(matchAndRemove(Token.tokenType.LEFTBRACKET) != null) {
                if((expressionNode = expression()) == null) {
                    throw new SyntaxErrorException("Expression expected after left bracket.");
                }
                if(matchAndRemove(Token.tokenType.RIGHTBRACKET) == null) {
                    throw new SyntaxErrorException("Right bracket expected after expression.");
                }
                return new VariableReferenceNode(analyzeToken.getValue(), expressionNode);
            }
            else {
                return new VariableReferenceNode(analyzeToken.getValue(), null);
            }
        }
        return null;
    }

    private Node determineNumber() {
        Token analyzeToken;
        int negativeFlipper = 1;

        // Flips negativeFlipper each time a MINUS token is found, thus dealing with multiple negatives/minuses.
        while(matchAndRemove(Token.tokenType.MINUS) != null) {
            negativeFlipper = -negativeFlipper;
        }

        /* If a NUMBER token is found, return either a RealNode or IntegerNode depending on if the
         * value of the NUMBER token has a decimal point or not. */
        if((analyzeToken = matchAndRemove(Token.tokenType.NUMBER)) != null) {
            String numberValue = analyzeToken.getValue();
            if(numberValue.contains(".")) {
                return new RealNode(Float.parseFloat(numberValue)*negativeFlipper);
            }
            return new IntegerNode(Integer.parseInt(numberValue)*negativeFlipper);
        }
        return null;
    }

    private TypeNode.dataType determineType() {
        if(matchAndRemove(Token.tokenType.INTEGER) != null) {
            return TypeNode.dataType.INTEGER;
        }
        else if(matchAndRemove(Token.tokenType.REAL) != null) {
            return TypeNode.dataType.REAL;
        }
        else if(matchAndRemove(Token.tokenType.STRING) != null) {
            return TypeNode.dataType.STRING;
        }
        else if(matchAndRemove(Token.tokenType.ARRAY) != null) {
            return TypeNode.dataType.ARRAY;
        }
        else if(matchAndRemove(Token.tokenType.CHARACTER) != null) {
            return TypeNode.dataType.CHARACTER;
        }
        else if(matchAndRemove(Token.tokenType.BOOLEAN) != null) {
            return TypeNode.dataType.BOOLEAN;
        }
        return null; // No type found, so return null.
    }

    private ArrayList<VariableNode> parameterDeclarations() throws SyntaxErrorException {
        Token analyzeToken;
        TypeNode.dataType type;
        ArrayList<VariableNode> parameterCollection = new ArrayList<>();
        ArrayList<String> identifierCollection = new ArrayList<>();
        boolean identifierFound = false, varStatus = false;

        do { // Do While looks for any amount of parameter declarations and stops once a semicolon isn't found.
            if(matchAndRemove(Token.tokenType.VAR) != null) {
                varStatus = true; // Keeps track of if var is the first token found, which we use later.
            }
            do { // Do While looks for any amount of identifiers and stops once a comma isn't found.
                if((analyzeToken = matchAndRemove(Token.tokenType.IDENTIFIER)) != null) {
                    identifierCollection.add(analyzeToken.getValue()); // This helps group all identifiers with the same data type.
                    identifierFound = true;
                }
                else if(!identifierFound){
                    return null; // No identifiers/parameter declarations found, thus return.
                }
                else {
                    throw new SyntaxErrorException("Incorrect parameter declaration or possible extra comma.");
                }
            } while(matchAndRemove(Token.tokenType.COMMA) != null);
            if(matchAndRemove(Token.tokenType.COLON) == null) {
                throw new SyntaxErrorException("Colon expected in parameter declaration.");
            }
            if((type = determineType()) == TypeNode.dataType.ARRAY) {
                if(matchAndRemove(Token.tokenType.OF) == null) {
                    throw new SyntaxErrorException("Incorrect declaration of array: \"of\" expected.");
                }
                if((type = determineType()) == null) {
                    throw new SyntaxErrorException("Incorrect declaration of array: type for array expected.");
                }
                /*type = switch(type) { // Figure out what data type the array is.
                    case INTEGER -> VariableNode.dataType.ARRAYOFINTEGER;
                    case REAL -> VariableNode.dataType.ARRAYOFREAL;
                    case STRING -> VariableNode.dataType.ARRAYOFSTRING;
                    case BOOLEAN -> VariableNode.dataType.ARRAYOFBOOLEAN;
                    case CHARACTER -> VariableNode.dataType.ARRAYOFCHARACTER;
                    default -> type;
                };*/
            }
            else if(type == null){
                throw new SyntaxErrorException("Type expected in parameter declaration.");
            }
            for(String name : identifierCollection) {
                parameterCollection.add(new VariableNode(name, new TypeNode(type), varStatus, varStatus));
            }
            identifierCollection.clear(); // Reset list of identifiers since data type was found.
            varStatus = false; // Reset varStatus since we are possibly starting a new parameter declaration, and it may not be var.
        } while(matchAndRemove(Token.tokenType.SEMICOLON) != null);
        return parameterCollection;
    }

    private ArrayList<VariableNode> constants() throws SyntaxErrorException {
        Token analyzeToken, constantName;
        ArrayList<VariableNode> constantsCollection = new ArrayList<>();
        Node dataTypeNode;
        TypeNode.dataType type = null;

        if(matchAndRemove(Token.tokenType.CONSTANTS) == null) {
            return null;
        }
        do { // Do While gets each constant in the current line until there are no more.
            if((analyzeToken = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
                throw new SyntaxErrorException("Incorrect constant name declaration or possible extra comma.");
            }
            constantName = analyzeToken; // Store constant name.

            // Determine data type.
            if(matchAndRemove(Token.tokenType.EQUALS) == null) {
                throw new SyntaxErrorException("Incorrect declaration of constants: = expected.");
            }
            if((analyzeToken = matchAndRemove(Token.tokenType.STRINGLITERAL)) != null) {
                dataTypeNode = new StringNode(analyzeToken.getValue());
                type = TypeNode.dataType.STRING;
            }
            else if((analyzeToken = matchAndRemove(Token.tokenType.CHARACTERLITERAL)) != null) {
                dataTypeNode = new CharacterNode(analyzeToken.getValue().charAt(0));
                type = TypeNode.dataType.CHARACTER;
            }
            else if(matchAndRemove(Token.tokenType.TRUE) != null) {
                dataTypeNode = new BooleanNode(true);
                type = TypeNode.dataType.BOOLEAN;
            }
            else if(matchAndRemove(Token.tokenType.FALSE) != null) {
                dataTypeNode = new BooleanNode(false);
                type = TypeNode.dataType.BOOLEAN;
            }
            else if((dataTypeNode = determineNumber()) == null) {
                throw new SyntaxErrorException("Incorrect declaration of constants: no valid data type.");
            }
            constantsCollection.add(new VariableNode(constantName.getValue(), new TypeNode(type), false, false, dataTypeNode, null, null));
        } while(matchAndRemove(Token.tokenType.COMMA) != null);
        expectEndsOfLine();
        return constantsCollection;
    }

    private ArrayList<VariableNode> variables() throws SyntaxErrorException {
        Token analyzeToken;
        Node from = null, to = null;
        TypeNode.dataType type;
        ArrayList<VariableNode> variableCollection = new ArrayList<>();
        ArrayList<String> identifierCollection = new ArrayList<>();

        if(matchAndRemove(Token.tokenType.VARIABLES) == null) {
            return null;
        }
        do { // Do While looks for any amount of single line variable declarations.
            do { // Do While looks for any amount of identifiers by stopping once a comma isn't found.
                if((analyzeToken = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
                    throw new SyntaxErrorException("Incorrect variable name declaration or possible extra comma.");
                }
                identifierCollection.add(analyzeToken.getValue()); // This helps group all identifiers with the same data type.
            } while(matchAndRemove(Token.tokenType.COMMA) != null);
            if(matchAndRemove(Token.tokenType.COLON) == null) {
                throw new SyntaxErrorException("Colon expected in variable declaration.");
            }
            if((type = determineType()) == null) {
                throw new SyntaxErrorException("Type expected in variable declaration.");
            }
            if(matchAndRemove(Token.tokenType.FROM) != null) { // Look for type limits
                if((from = determineNumber()) == null) {
                    throw new SyntaxErrorException("Number expected after \"from\" in variable declaration.");
                }
                if(matchAndRemove(Token.tokenType.TO) == null) {
                    throw new SyntaxErrorException("\"to\" expected in variable declaration.");
                }
                if((to = determineNumber()) == null) {
                    throw new SyntaxErrorException("Number expected after \"to\" in variable declaration.");
                }
                if(type == TypeNode.dataType.STRING || type == TypeNode.dataType.INTEGER) {
                    if(!(to instanceof IntegerNode || from instanceof IntegerNode)) {
                        throw new SyntaxErrorException("Type limit for integer and string should be integer values.");
                    }
                }
                else if(type == TypeNode.dataType.REAL) {
                    if(!(to instanceof RealNode || from instanceof RealNode)) {
                        throw new SyntaxErrorException("Type limit for real should be real values.");
                    }
                }
                else {
                    throw new SyntaxErrorException("Data type given can not have type limit.");
                }
            }
            if(type == TypeNode.dataType.ARRAY) {
                TypeNode.dataType arrayType;
                if((arrayType = determineType()) == null) {
                    throw new SyntaxErrorException("Array of invalid data types in variable declaration.");
                }
                /*type = switch(arrayType) { // Figure out what data type the array is.
                    case INTEGER -> VariableNode.dataType.ARRAYOFINTEGER;
                    case REAL -> VariableNode.dataType.ARRAYOFREAL;
                    case STRING -> VariableNode.dataType.ARRAYOFSTRING;
                    case BOOLEAN -> VariableNode.dataType.ARRAYOFBOOLEAN;
                    case CHARACTER -> VariableNode.dataType.ARRAYOFCHARACTER;
                    default -> throw new SyntaxErrorException("Array of invalid data types in variable declaration.");
                };*/
            }
            for(String name : identifierCollection) {
                variableCollection.add(new VariableNode(name, new TypeNode(type), true, false, null, to, from));
            }
            identifierCollection.clear(); // Reset list of identifiers since data type was found.
        } while(matchAndRemove(Token.tokenType.SEMICOLON) != null);
        expectEndsOfLine();
        return variableCollection;
    }

    private FunctionNode function() throws SyntaxErrorException {
        Token functionName;
        ArrayList<VariableNode> parameters, transferNodes, constantsAndVariables = new ArrayList<>();

        if(matchAndRemove(Token.tokenType.DEFINE) == null) {
            return null;
        }
        if((functionName = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
            throw new SyntaxErrorException("Function defined with no name.");
        }
        if(matchAndRemove(Token.tokenType.LEFTPAREN) == null) {
            throw new SyntaxErrorException("Function defined with no left parentheses.");
        }
        parameters = parameterDeclarations(); // Get function parameters.
        if(matchAndRemove(Token.tokenType.RIGHTPAREN) == null) {
            throw new SyntaxErrorException("Function defined with no right parentheses.");
        }
        expectEndsOfLine();
        while(true) {
            if((transferNodes = constants()) != null) {
                constantsAndVariables.addAll(transferNodes);
            }
            else if((transferNodes = variables()) != null) {
                constantsAndVariables.addAll(transferNodes);
            }
            else {
                break;
            }
        }
        return new FunctionNode(functionName.getValue(), parameters, constantsAndVariables, statements());
    }

    private IfNode parseElsifElse() throws SyntaxErrorException {
        Node booleanExpression;
        //ArrayList<StatementNode> statements;
        //IfNode next;

        // Process elsif statements. Will return null once no more are found.
        if(matchAndRemove(Token.tokenType.ELSIF) != null) {
            if ((booleanExpression = boolCompare()) == null) {
                throw new SyntaxErrorException("Missing boolean expression in elsif statement.");
            }
            if (matchAndRemove(Token.tokenType.THEN) == null) {
                throw new SyntaxErrorException("\"then\" missing from elsif statement.");
            }
            //statements = statements();
            //next = parseElsifElse(); // Recursively call parseElsif().
            expectEndsOfLine();
            return new IfNode(booleanExpression, statements(), parseElsifElse());
        }
        else if(matchAndRemove(Token.tokenType.ELSE) != null) { // Found else statement instead of elseif.
            expectEndsOfLine();
            return new IfNode(null, statements(), null);
        }
        return null;
    }

    private IfNode parseIf() throws SyntaxErrorException {
        Node booleanExpression;
        //ArrayList<StatementNode> statements;
        //IfNode next;

        if(matchAndRemove(Token.tokenType.IF) == null) {
            return null;
        }
        if((booleanExpression = boolCompare()) == null) {
            throw new SyntaxErrorException("Missing boolean expression in if statement.");
        }
        if(matchAndRemove(Token.tokenType.THEN) == null) {
            throw new SyntaxErrorException("\"then\" missing from if statement.");
        }
        //statements = statements();
        //next = parseElsifElse(); // Look for elseif and else statement(s).
        expectEndsOfLine();
        return new IfNode(booleanExpression, statements(), parseElsifElse());
    }

    private WhileNode parseWhile() throws SyntaxErrorException {
        Node booleanExpression;
        //ArrayList<StatementNode> statements;

        if(matchAndRemove(Token.tokenType.WHILE) == null) {
            return null;
        }
        if((booleanExpression = boolCompare()) == null) {
            throw new SyntaxErrorException("Missing boolean expression from while statement.");
        }
        expectEndsOfLine();
        return new WhileNode(booleanExpression, statements());
    }

    private ForNode parseFor() throws SyntaxErrorException {
        Node from, to;
        Token analyzeToken;

        if(matchAndRemove(Token.tokenType.FOR) == null) {
            return null;
        }
        if((analyzeToken = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
            throw new SyntaxErrorException("Counter in for loop not found.");
        }
        if(matchAndRemove(Token.tokenType.FROM) == null) {
            throw new SyntaxErrorException("No \"from\" in for loop.");
        }
        if((from = expression()) == null) {
            throw new SyntaxErrorException("No \"from\" value specified in for loop.");
        }
        if(matchAndRemove(Token.tokenType.TO) == null) {
            throw new SyntaxErrorException("No \"to\" in for loop.");
        }
        if((to = expression()) == null) {
            throw new SyntaxErrorException("No \"to\" value specified in for loop.");
        }
        expectEndsOfLine();
        return new ForNode(from, to, statements(), analyzeToken.getValue());
    }

    private FunctionCallNode parseWrite() throws SyntaxErrorException {
        Token analyzeToken;
        ArrayList<VariableNode> parameters = new ArrayList<>();

        if(matchAndRemove(Token.tokenType.WRITE) == null) {
            return null;
        }
        do {
            if((analyzeToken = matchAndRemove(Token.tokenType.STRINGLITERAL)) != null) {
                parameters.add(new VariableNode(new StringNode(analyzeToken.getValue())));
            }
            else if((analyzeToken = matchAndRemove(Token.tokenType.IDENTIFIER)) != null) {
                parameters.add(new VariableNode(new VariableReferenceNode(analyzeToken.getValue())));
            }
            else {
                throw new SyntaxErrorException("No string literal or variable found to write.");
            }
        } while(matchAndRemove(Token.tokenType.COMMA) != null);
        expectEndsOfLine();
        return new FunctionCallNode("BuiltInWrite", parameters);
    }

    private FunctionCallNode parseFunctionCalls() throws SyntaxErrorException {
        Token functionName, identifierName;
        Node boolCompareNode;
        ArrayList<VariableNode> parameters = new ArrayList<>();

        if((functionName = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
            return null;
        }
        while(true) { // Adds parameters to ArrayList until no more parameters are found.
            if(matchAndRemove(Token.tokenType.VAR) != null) {
                if((identifierName = matchAndRemove(Token.tokenType.IDENTIFIER)) == null) {
                    throw new SyntaxErrorException("Identifier expected after var in function call.");
                }
                parameters.add(new VariableNode(identifierName.getValue(), false, true));
            }
            else if((boolCompareNode = boolCompare()) != null) {
                parameters.add((VariableNode)boolCompareNode);
            }
            else {
                break;
            }
        }
        return new FunctionCallNode(functionName.toString(), parameters);
    }

    public ProgramNode parse() throws SyntaxErrorException {
        FunctionNode functionNode;

        while((functionNode = function()) != null) {
            functionList.put(functionNode.getName(), functionNode);
        }
        return new ProgramNode(functionList);
    }
}
