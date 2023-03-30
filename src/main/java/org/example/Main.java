package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String PATTERN = "[a-z0-9()+-/* ]{1,}";
    private static Map<String, SyntaxTree> variableMap = new LinkedHashMap<>();
    private static String expression = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            cleanData();
            System.out.println("Enter the expression:");

            expression = scanner.nextLine().trim();
            if (!validateExpression()) {
                continue;
            }

            SyntaxTree ast = createSyntaxTree();
            if (ast == null) {
                continue;
            }

            definingVariableValues(scanner);

            boolean next = false;
            while (!next) {
                System.out.println("The following commands are available:");
                System.out.println("    quit    - completion of the program");
                System.out.println("    calc    - calculation output");
                System.out.println("    print   - output of the syntax tree structure");
                System.out.println("    any key - calculation of a new expression");

                String command = scanner.nextLine().trim();

                switch (command) {
                    case "calc":
                        String seral = serialTree(ast);
                        SyntaxTree copyAst;
                        try {
                            copyAst = mapper.readValue(seral, SyntaxTree.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(calculateExpression(copyAst));
                        break;
                    case "print":
                        System.out.println(serialTree(ast));
                        break;
                    case "quit":
                        return;
                    default:
                        next = true;
                }
            }
        }
    }

    private static boolean validateExpression() {
        boolean match = expression.matches(PATTERN);
        if (!match) {
            System.out.println("The entered expression contains invalid characters");
        }
        return match;
    }

    private static void cleanData() {
        expression = "";
        variableMap = new LinkedHashMap<>();
    }

    private static String serialTree(SyntaxTree ast) {
        String mapToString;
        try {
            mapToString = mapper.writeValueAsString(ast);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mapToString;
    }

    public static SyntaxTree createSyntaxTree() {
        SyntaxTree ast = null;
        try {
            ast = parseAddSub();
            System.out.println("Ok");
        } catch (Throwable e) {
            System.out.println("Expression parsing error. Please enter the correct expression.");
        }
        return ast;
    }

    public static void definingVariableValues(Scanner scanner) {
        System.out.println("Enter the values of the variables from the expression:");
        while (!variableMap.isEmpty()) {
            String key = variableMap.keySet().stream().findFirst().get();
            System.out.println("Enter a value for: " + key);
            short varValue;
            try {
                varValue = Short.parseShort(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Error: Incorrect value entered.");
                continue;
            }

            SyntaxTree node = variableMap.get(key);
            node.setValue(varValue);

            variableMap.remove(key);
            System.out.println("Ok");
        }
        System.out.println("The values of the variables are defined.");
    }

    public static int calculateExpression(SyntaxTree ast) {

        if (ast.getOperation() == null) {
            return ast.getValue();
        }

        if (ast.getLeftNode() != null) {
            calculateExpression(ast.getLeftNode());
        }
        if (ast.getRightNode() != null) {
            calculateExpression(ast.getRightNode());
        }

        switch (ast.getOperation()) {
            case ADD:
                ast.setValue((short) (ast.getLeftNode().getValue() + ast.getRightNode().getValue()));
                break;
            case SUB:
                ast.setValue((short) (ast.getLeftNode().getValue() - ast.getRightNode().getValue()));
                break;
            case MOD:
                ast.setValue((short) (ast.getLeftNode().getValue() / ast.getRightNode().getValue()));
                break;
            case MUL:
                ast.setValue((short) (ast.getLeftNode().getValue() * ast.getRightNode().getValue()));
                break;
        }
        return ast.getValue();
    }


    public static String deleteSpace(String exp) {
        int numberOfSpace = 0;

        while (exp.length() > numberOfSpace && exp.charAt(numberOfSpace) == ' ') {
            numberOfSpace++;
        }
        return exp.substring(numberOfSpace);
    }

    public static short parseShortValue() {
        int numberOfSpace = 0;
        while (expression.length() > numberOfSpace
                && expression.charAt(numberOfSpace) >= 48 && expression.charAt(numberOfSpace) <= 57) {
            numberOfSpace++;
        }
        try {
            String digit = expression.substring(0, numberOfSpace);
            short result = Short.parseShort(digit);
            expression = cutExpression(expression, numberOfSpace);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error parse digit");
        }
    }

    public static Operation parseOperator(String exp) {
        return Operation.getBySymbol(exp.charAt(0));
    }

    private static String cutExpression(String exp, int cutLen) {
        return deleteSpace(exp.substring(cutLen));
    }

    public static SyntaxTree parseAtom() {
        if (expression.charAt(0) >= 97) {
            SyntaxTree variableNode = new SyntaxTree((short) 0);
            variableMap.put(parseVariable(), variableNode);
            return variableNode;
        } else if (expression.charAt(0) == 40) {
            expression = cutExpression(expression, 1);
            SyntaxTree node = parseAddSub();
            if (expression.charAt(0) != 41) {
                throw new IllegalArgumentException("Expected closing bracket");
            }
            expression = cutExpression(expression, 1);
            return node;
        }
        return new SyntaxTree(parseShortValue());
    }

    public static String parseVariable() {
        int numberOfSpace = 0;
        while (expression.length() > numberOfSpace) {
            if (expression.charAt(numberOfSpace) >= 48 && expression.charAt(numberOfSpace) <= 57
                    || expression.charAt(numberOfSpace) >= 97 && expression.charAt(numberOfSpace) <= 122) {
                numberOfSpace++;
            }
        }
        String result = expression.substring(0, numberOfSpace);
        expression = cutExpression(expression, numberOfSpace);
        return result;
    }

    public static SyntaxTree parseMulDiv() {
        SyntaxTree left = parseAtom();

        while (!expression.isBlank()) {
            Operation op = parseOperator(expression);
            if (op == null || (!Operation.MUL.equals(op) && !Operation.MOD.equals(op))) {
                return left;
            }

            expression = cutExpression(expression, 1);
            SyntaxTree right = parseAtom();

            SyntaxTree node = new SyntaxTree();
            node.setLeftNode(left);
            node.setRightNode(right);
            node.setOperation(op);

            left = node;
        }
        return left;
    }

    public static SyntaxTree parseAddSub() {
        SyntaxTree left = parseMulDiv();

        while (!expression.isBlank()) {
            Operation op = parseOperator(expression);

            if (op == null || (!Operation.ADD.equals(op) && !Operation.SUB.equals(op))) {
                return left;
            }

            expression = cutExpression(expression, 1);
            SyntaxTree right = parseMulDiv();

            SyntaxTree node = new SyntaxTree();
            node.setLeftNode(left);
            node.setRightNode(right);
            node.setOperation(op);

            left = node;
        }
        return left;
    }

}