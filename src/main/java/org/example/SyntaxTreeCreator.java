package org.example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SyntaxTreeCreator {
    private final Map<String, SyntaxTree> variableMap = new LinkedHashMap<>();
    private String expression;
    private int position;
    private int expLen;

    public SyntaxTree createTree(String expression) {
        this.expression = expression.replaceAll(" ", "");
        this.position = 0;
        this.expLen = this.expression.length();
        try {
            SyntaxTree syntaxTree = parseAddSub();
            System.out.println("Ok");
            return syntaxTree;
        } catch (Throwable e) {
            System.out.println("Expression parsing error. Please enter the correct expression.");
            return null;
        }
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

        while (position < expLen) {
            Operation op = parseOperator();

            if (op == null || (!Operation.ADD.equals(op) && !Operation.SUB.equals(op))) {
                return left;
            }

            position++;
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

        while (position < expLen) {
            Operation op = parseOperator();
            if (op == null || (!Operation.MUL.equals(op) && !Operation.MOD.equals(op))) {
                return left;
            }

            position++;
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
        if (expression.charAt(position) >= 97) {
            SyntaxTree variableNode = new SyntaxTree();
            variableMap.put(parseVariable(), variableNode);
            return variableNode;
        } else if (expression.charAt(position) == 40) {
            position++;
            SyntaxTree node = parseAddSub();
            if (expression.charAt(position) != 41) {
                throw new IllegalArgumentException("Expected closing bracket");
            }
            position++;
            return node;
        }
        return new SyntaxTree(parseShortValue());
    }

    private String parseVariable() {
        int startPosition = position;
        while (position < expLen) {
            if (expression.charAt(position) >= 48 && expression.charAt(position) <= 57
                    || expression.charAt(position) >= 97 && expression.charAt(position) <= 122) {
                position++;
                continue;
            }
            break;
        }
        return expression.substring(startPosition, position);
    }

    private Short parseShortValue() {
        int startPosition = position;
        while (position < expLen
                && expression.charAt(position) >= 48 && expression.charAt(position) <= 57) {
            position++;
        }
        try {
            String digit = expression.substring(startPosition, position);
            return Short.parseShort(digit);
        } catch (Exception e) {
            throw new RuntimeException("Error parse digit");
        }
    }

    private Operation parseOperator() {
        return Operation.getBySymbol(expression.charAt(position));
    }

}
