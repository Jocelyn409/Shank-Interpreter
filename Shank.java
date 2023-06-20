import static java.lang.System.exit;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shank {

    public static void main(String[] args) throws IOException, SyntaxErrorException, RunTimeException {

        if(args.length != 1) {
            System.out.print("The program must run with one and only one argument.");
            exit(1);
        }

        // Lexer
        Lexer lexInstance = new Lexer();
        Path filePath = Paths.get(args[0]);
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        lines.forEach(line -> {
            try {
                lexInstance.lex(line);
            }
            catch(SyntaxErrorException exception) {
                System.out.println("Exception: " + exception);
                exit(1);
            }
        });
        lexInstance.dedentCheck(0);
        System.out.println(lexInstance.tokenList);

        // Parser
        Parser parserInstance = new Parser(lexInstance.tokenList);
        ProgramNode program = parserInstance.parse();
        System.out.println(program);

        parserInstance.functionList.put("BuiltInRead", new BuiltInRead("BuiltInRead", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInWrite", new BuiltInWrite("BuiltInWrite", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInLeft", new BuiltInLeft("BuiltInLeft", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInRight", new BuiltInRight("BuiltInRight", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInSubstring", new BuiltInSubstring("BuiltInSubstring", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInSquareRoot", new BuiltInSquareRoot("BuiltInSquareRoot", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInGetRandom", new BuiltInGetRandom("BuiltInGetRandom", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInIntegerToReal", new BuiltInIntegerToReal("BuiltInIntegerToReal", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInRealToInteger", new BuiltInRealToInteger("BuiltInRealToInteger", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInStart", new BuiltInStart("BuiltInStart", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        parserInstance.functionList.put("BuiltInEnd", new BuiltInEnd("BuiltInEnd", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Semantic Analysis
        SemanticAnalysis semanticAnalysisInstance = new SemanticAnalysis();
        semanticAnalysisInstance.checkAssignments(program);

        // Interpreter
        Interpreter interpreterInstance = new Interpreter(parserInstance.functionList);
        for(Map.Entry<String, FunctionNode> function : program.getFunctionNodeCollection().entrySet()) {
            if(!(function instanceof BuiltInFunction)) {
                interpreterInstance.interpretFunction(function.getValue());
            }
        }
    }
}
