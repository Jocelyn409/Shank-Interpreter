import java.util.ArrayList;
import java.util.HashMap;

public class Lexer extends Exception {
    private enum state {baseState, identifierState, numberState, stringLiteral, characterLiteral, commentState, punctuationState}
    private state currentState = state.baseState;

    public ArrayList<Token> tokenList = new ArrayList<>();
    public HashMap<String, Token.tokenType> knownWords = new HashMap<>();

    private StringBuilder tokenValue = new StringBuilder();
    private final String singularPunctuation = "[]()*/-+=;,";
    private final String multiPunctuation = "><:";
    private int currentLine = 0;
    private int previousIndentCount = 0;

    private void initializeHashMap() {
        knownWords.put("define", Token.tokenType.DEFINE);
        knownWords.put("constants", Token.tokenType.CONSTANTS);
        knownWords.put("variables", Token.tokenType.VARIABLES);
        knownWords.put("write", Token.tokenType.WRITE);
        knownWords.put("mod", Token.tokenType.MOD);
        knownWords.put("for", Token.tokenType.FOR);
        knownWords.put("while", Token.tokenType.WHILE);
        knownWords.put("until", Token.tokenType.UNTIL);
        knownWords.put("repeat", Token.tokenType.REPEAT);
        knownWords.put("if", Token.tokenType.IF);
        knownWords.put("else", Token.tokenType.ELSE);
        knownWords.put("elsif", Token.tokenType.ELSIF);
        knownWords.put("then", Token.tokenType.THEN);
        knownWords.put("of", Token.tokenType.OF);
        knownWords.put("to", Token.tokenType.TO);
        knownWords.put("string", Token.tokenType.STRING);
        knownWords.put("array", Token.tokenType.ARRAY);
        knownWords.put("character", Token.tokenType.CHARACTER);
        knownWords.put("boolean", Token.tokenType.BOOLEAN);
        knownWords.put("integer", Token.tokenType.INTEGER);
        knownWords.put("real", Token.tokenType.REAL);
        knownWords.put("from", Token.tokenType.FROM);
        knownWords.put("and", Token.tokenType.AND);
        knownWords.put("not", Token.tokenType.NOT);
        knownWords.put("or", Token.tokenType.OR);
        knownWords.put("var", Token.tokenType.VAR);
    }

    // For the current line, outputs 1 dedent token for every 1 less indent found on the previous line.
    public void dedentCheck(int indentCount) {
        while(indentCount < previousIndentCount) {
            tokenList.add(new Token(Token.tokenType.DEDENT, "", currentLine));
            previousIndentCount--;
        }
    }

    private void outputSingularPunctuation(char analyze) {
        switch (analyze) {
            case '[' -> tokenList.add(new Token(Token.tokenType.LEFTBRACKET, "", currentLine));
            case ']' -> tokenList.add(new Token(Token.tokenType.RIGHTBRACKET, "", currentLine));
            case '(' -> tokenList.add(new Token(Token.tokenType.LEFTPAREN, "", currentLine));
            case ')' -> tokenList.add(new Token(Token.tokenType.RIGHTPAREN, "", currentLine));
            case '*' -> tokenList.add(new Token(Token.tokenType.MULTIPLY, "", currentLine));
            case '/' -> tokenList.add(new Token(Token.tokenType.DIVIDE, "", currentLine));
            case '+' -> tokenList.add(new Token(Token.tokenType.PLUS, "", currentLine));
            case '-' -> tokenList.add(new Token(Token.tokenType.MINUS, "", currentLine));
            case '=' -> tokenList.add(new Token(Token.tokenType.EQUALS, "", currentLine));
            case ',' -> tokenList.add(new Token(Token.tokenType.COMMA, "", currentLine));
            case ';' -> tokenList.add(new Token(Token.tokenType.SEMICOLON, "", currentLine));
        }
    }

    private void outputMultiPunctuation(char analyze) {
        switch (analyze) {
            case '<' -> tokenList.add(new Token(Token.tokenType.LESSTHAN, "", currentLine));
            case '>' -> tokenList.add(new Token(Token.tokenType.GREATERTHAN, "", currentLine));
            case '=' -> tokenList.add(new Token(Token.tokenType.EQUALS, "", currentLine));
            case ':' -> tokenList.add(new Token(Token.tokenType.COLON, "", currentLine));
        }
    }

    /*  This function's main purpose is to avoid consuming or adding characters
     *  unnecessarily by changing the state while also resetting tokenValue
     *  and appending to it if the character is not a whitespace. */
    private void changeState(char charValue) throws SyntaxErrorException {
        if(Character.isLetter(charValue)) {
            currentState = state.identifierState;
        }
        else if(Character.isDigit(charValue) || charValue == '.') {
            currentState = state.numberState;
        }
        else if(Character.isWhitespace(charValue)) {
            currentState = state.baseState;
        }
        else if(charValue == '"') { // Reset tokenValue and return to avoid adding the double quotation to tokenValue.
            currentState = state.stringLiteral;
            tokenValue.setLength(0);
            return;
        }
        else if(charValue == '\'') { // Same reason as for the previous statement, but for singular quotation.
            currentState = state.characterLiteral;
            tokenValue.setLength(0);
            return;
        }
        else if(charValue == '{') {
            currentState = state.commentState;
        }
        else if(singularPunctuation.indexOf(charValue) != -1 || multiPunctuation.indexOf(charValue) != -1) {
            currentState = state.punctuationState;
        }
        else {
            throw new SyntaxErrorException("Invalid character on line " + currentLine + ": " + charValue);
        }

        // Reset the tokenValue since we are in a new state now and just output a token, then append the charValue to tokenValue.
        tokenValue.setLength(0);
        if(!Character.isWhitespace(charValue)) {
            tokenValue.append(charValue);
        }
    }

    /* Goes through each character in a string and accumulates them into tokens.
     * If a state is to change, the token currently being worked on will output.
     * If the current state is changed back to the base state instead of using changeState(),
     * that means that we don't want the current charValue to be appended to the token this iteration. */
    public void lex(String line) throws SyntaxErrorException {
        int spaceCount = 0;
        int indentCount = 0;
        boolean endOfIndentation = false;

        initializeHashMap();
        currentLine++;

        for(int charPosition = 0; charPosition <= line.length()-1; charPosition++) {
            char charValue = line.charAt(charPosition);

            // Calculates the indentation for the line currently being lexed if we haven't reached the endOfIndentation.
            if(!endOfIndentation && currentState != state.commentState) {
                if(charValue == ' ') { // If the character value is a space, add to the spaceCount and continue.
                    spaceCount++;
                    if(spaceCount == 4) { // If the spaceCount is 4 (equal to a tab), reset it then add to the indentCount.
                        spaceCount = 0;
                        indentCount++;
                    }
                    continue;
                }
                else if(charValue == '\t') { // If the character value is a tab, add to the indentCount.
                    indentCount++;
                    continue;
                }
                else { // endOfIndentation is found, so we output the number of indent tokens needed, then we dedentCheck().
                    endOfIndentation = true;
                    while(indentCount > previousIndentCount) {
                        tokenList.add(new Token(Token.tokenType.INDENT, "", currentLine));
                        previousIndentCount++;
                    }
                    dedentCheck(indentCount);
                }
            }

            switch(currentState) {
                case baseState -> changeState(charValue);
                case identifierState -> {
                    if(Character.isLetterOrDigit(charValue)) {
                        tokenValue.append(charValue);
                    }
                    else {
                        if(knownWords.containsKey(tokenValue.toString())) { // If the identifier is a keyword, output that keyword.
                            Token.tokenType keywordToken = knownWords.get(tokenValue.toString());
                            tokenList.add(new Token(keywordToken, "", currentLine));
                        }
                        else {
                            tokenList.add(new Token(Token.tokenType.IDENTIFIER, tokenValue.toString(), currentLine));
                        }
                        changeState(charValue);
                    }
                }
                case numberState -> {
                    if(Character.isDigit(charValue)) {
                        tokenValue.append(charValue);
                    }
                    else if(charValue == '.' && !tokenValue.toString().contains(".")) { // Only add a period to tokenValue if it doesn't already have one.
                        tokenValue.append(charValue);
                    }
                    else {
                        tokenList.add(new Token(Token.tokenType.NUMBER, tokenValue.toString(), currentLine));
                        changeState(charValue);
                    }
                }
                case stringLiteral -> {
                    if(charValue == '"') { // Found closing double quotation mark for the string literal.
                        tokenList.add(new Token(Token.tokenType.STRINGLITERAL, tokenValue.toString(), currentLine));
                        currentState = state.baseState;
                    }
                    else {
                        tokenValue.append(charValue);
                    }
                }
                case characterLiteral -> {
                    if(tokenValue.toString().length() > 1) {
                        throw new SyntaxErrorException("Character literal '" + tokenValue + "' is of incorrect size on line " + currentLine);
                    }
                    else if(charValue == '\'') { // Found closing quotation mark for the character literal.
                        tokenList.add(new Token(Token.tokenType.CHARACTERLITERAL, tokenValue.toString(), currentLine));
                        currentState = state.baseState;
                    }
                    else {
                        tokenValue.append(charValue);
                    }
                }
                case punctuationState -> {
                    /* If we find singularPunctuation, simply output it and changeState().
                     *  Else, figure out what multi-punctuation needs to be output. */
                    if (singularPunctuation.contains(tokenValue.toString())) {
                        outputSingularPunctuation(tokenValue.toString().charAt(0));
                        changeState(charValue);
                    }
                    else {
                        switch(tokenValue.toString()) { // Multi-punctuation
                            case "<" -> {
                                if(charValue == '=') {
                                    tokenList.add(new Token(Token.tokenType.LESSEQUALS, "", currentLine));
                                    currentState = state.baseState;
                                }
                                else if(charValue == '>') {
                                    tokenList.add(new Token(Token.tokenType.NOTEQUAL, "", currentLine));
                                    currentState = state.baseState;
                                }
                                else {
                                    tokenList.add(new Token(Token.tokenType.LESSTHAN, "", currentLine));
                                    changeState(charValue);
                                }
                            }
                            case ">" -> {
                                if(charValue == '=') {
                                    tokenList.add(new Token(Token.tokenType.GREATEREQUALS, "", currentLine));
                                    currentState = state.baseState;
                                }
                                else {
                                    tokenList.add(new Token(Token.tokenType.GREATERTHAN, "", currentLine));
                                    changeState(charValue);
                                }
                            }
                            case ":" -> {
                                if(charValue == '=') {
                                    tokenList.add(new Token(Token.tokenType.ASSIGNMENT, "", currentLine));
                                    currentState = state.baseState;
                                }
                                else if(Character.isWhitespace(charValue)) {
                                    tokenList.add(new Token(Token.tokenType.COLON, "", currentLine));
                                    changeState(charValue);
                                }
                                else {
                                    tokenValue.append(charValue);
                                    throw new SyntaxErrorException("Invalid operand " + tokenValue + " on line " + currentLine);
                                }
                            }
                            default -> {
                                tokenValue.append(charValue);
                                throw new SyntaxErrorException("Invalid operand " + tokenValue + " on line " + currentLine);
                            }
                        }
                    }
                }
                case commentState -> {
                    if(charValue == '}') { // End of comment found; go back to baseState.
                        currentState = state.baseState;
                    }
                    else if(charValue == '{') {
                        throw new SyntaxErrorException("Nested comment found on line " + currentLine);
                    }
                }
            }
        }

        // Handles the last token not accounted for by the for loop.
        switch(currentState) {
            case identifierState -> {
                if(knownWords.containsKey(tokenValue.toString())) {
                    Token.tokenType keywordToken = knownWords.get(tokenValue.toString());
                    tokenList.add(new Token(keywordToken, "", currentLine));
                }
                else {
                    tokenList.add(new Token(Token.tokenType.IDENTIFIER, tokenValue.toString(), currentLine));
                }
                currentState = state.baseState;
            }
            case numberState -> {
                tokenList.add(new Token(Token.tokenType.NUMBER, tokenValue.toString(), currentLine));
                currentState = state.baseState;
            }
            case stringLiteral ->
                    throw new SyntaxErrorException("Unfinished string literal found on line " + currentLine);
            case characterLiteral ->
                    throw new SyntaxErrorException("Unfinished character literal found on line " + currentLine);
            case punctuationState -> {
                if(singularPunctuation.contains(tokenValue.toString())) {
                    outputSingularPunctuation(tokenValue.toString().charAt(0));
                }
                else if(multiPunctuation.contains(tokenValue.toString())) {
                    outputMultiPunctuation(tokenValue.toString().charAt(0));
                }
                else {
                    throw new SyntaxErrorException("Invalid operand " + tokenValue + " on line " + currentLine);
                }
                currentState = state.baseState;
            }
            case commentState -> {
                tokenValue.setLength(0);
                return; // If in comment state, return instead of outputting an ENDOFLINE token.
            }
        }
        tokenList.add(new Token(Token.tokenType.ENDOFLINE, "", currentLine)); // Reached end of line.
        tokenValue.setLength(0); // Reset tokenValue.
    }
}