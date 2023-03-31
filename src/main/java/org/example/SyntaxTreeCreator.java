package org.example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SyntaxTreeCreator {
    private final Map<String, SyntaxTree> variableMap = new LinkedHashMap<>();
    private String expression;

    public SyntaxTree createTree(String expression) {
        this.expression = expression;
        SyntaxTree syntaxTree = null;
        try {
            syntaxTree = parseAddSub();
            System.out.println("Ok");
        } catch (Throwable e) {
            System.out.println("Expression parsing error. Please enter the correct expression.");
        }
        return syntaxTree;
    }

    public void setVariableValues(String variable) {
        String[] variableArray = variable.split("=");

        String key = variableArray[0].trim();

        SyntaxTree node = variableMap.get(key);
        if (node != null) {
            try {
                node.setValue(Short.parseShort(variableArray[1].trim()));
            } catch (Exception e) {
                System.out.println("Error: Incorrect value entered.");
                return;
            }
        } else {
            System.out.println("The variable [" + key + "] is not used in the expression");
        }
        System.out.println("Ok");
        printVariables();
    }

    public boolean checkVariables() {
        boolean result = variableMap.entrySet().stream().noneMatch(e -> e.getValue().getValue() == null);
        if (!result) {
            System.out.println("Not all variables matter.");
            printVariables();
        }
        return result;
    }

    private void printVariables() {
        String varSet = variableMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue().getValue())
                .collect(Collectors.joining("; "));
        System.out.println("Current state of variables: " + varSet);
    }

    private SyntaxTree parseAddSub() {
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

    private SyntaxTree parseMulDiv() {
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

    private SyntaxTree parseAtom() {
        if (expression.charAt(0) >= 97) {
            SyntaxTree variableNode = new SyntaxTree();
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

    private String parseVariable() {
        int numberOfSpace = 0;
        while (expression.length() > numberOfSpace) {
            if (expression.charAt(numberOfSpace) >= 48 && expression.charAt(numberOfSpace) <= 57
                    || expression.charAt(numberOfSpace) >= 97 && expression.charAt(numberOfSpace) <= 122) {
                numberOfSpace++;
                continue;
            }
            break;
        }
        String result = expression.substring(0, numberOfSpace);
        expression = cutExpression(expression, numberOfSpace);
        return result;
    }

    private String cutExpression(String exp, int cutLen) {
        return deleteSpace(exp.substring(cutLen));
    }

    private short parseShortValue() {
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

    private Operation parseOperator(String exp) {
        return Operation.getBySymbol(exp.charAt(0));
    }

    private String deleteSpace(String exp) {
        int numberOfSpace = 0;

        while (exp.length() > numberOfSpace && exp.charAt(numberOfSpace) == ' ') {
            numberOfSpace++;
        }
        return exp.substring(numberOfSpace);
    }
}
