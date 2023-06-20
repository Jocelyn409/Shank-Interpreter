import java.util.Objects;

public class Token {

    public Token(tokenType token, String value, int lineNumber) {
        this.token = token;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public enum tokenType {
        IDENTIFIER, NUMBER, ENDOFLINE,
        STRINGLITERAL, CHARACTERLITERAL,
        INDENT, DEDENT, PUNCTUATION,

        // Keywords
        DEFINE, CONSTANTS, VARIABLES, WRITE,
        FOR, WHILE, UNTIL, REPEAT, IF, ELSE, ELSIF, THEN, OF, TO, FROM,
        STRING, ARRAY, CHARACTER, BOOLEAN, INTEGER, REAL, VAR, TRUE, FALSE,

        // Math and Operators
        PLUS, MINUS, MULTIPLY, DIVIDE, MOD, EQUALS, ASSIGNMENT,
        GREATERTHAN, LESSTHAN, GREATEREQUALS, LESSEQUALS, NOTEQUAL,
        AND, NOT, OR,

        // Punctuation
        LEFTBRACKET, RIGHTBRACKET, LEFTPAREN, RIGHTPAREN, COMMA, COLON, SEMICOLON
    }

    private final tokenType token;
    private final String value;
    private final int lineNumber;

    public tokenType getToken() {
        return this.token;
    }
    public String getValue() {
        return this.value;
    }
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public String toString() {
        // If value is blank i.e. is an ENDOFLINE token, return just the token.
        if(!Objects.equals(value, "")) {
            return token + "(" + value + ")";
        }
        return token.toString();
    }

}
