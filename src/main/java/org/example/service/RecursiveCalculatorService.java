package org.example.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.SyntaxTree;
import org.example.SyntaxTreeCreator;

public class RecursiveCalculatorService implements CalculatorService, PrintableStructure {
    private static final String PATTERN = "[a-z0-9()+-/* ]{1,}";
    private static final ObjectMapper mapper = new ObjectMapper();
    private SyntaxTree syntaxTree = null;
    private SyntaxTreeCreator creator = null;

    public RecursiveCalculatorService() {
        init();
    }

    @Override
    public String calc() {
        if (syntaxTree != null && creator.checkVariables()) {
            try {
                return String.valueOf(calculateExpression(syntaxTree));
            } catch (Throwable e) {
                return "Calculation error: " + e.getMessage();
            }
        }
        return "The expression is not ready for calculation";
    }

    @Override
    public void cleanAll() {
        init();
    }

    private void init() {
        syntaxTree = null;
        creator = null;
        System.out.println("Enter the expression:");
    }

    @Override
    public String printStructure() {
        try {
            return mapper.writeValueAsString(syntaxTree);
        } catch (JsonProcessingException e) {
            return "Structure printing error: " + e.getMessage();
        }
    }

    @Override
    public void inputData(String data) {
        if (syntaxTree == null) {
            createSyntaxTree(data);
        } else {
            creator.setVariableValues(data);
        }
    }

    private boolean validateExpression(String data) {
        boolean expressionIsValid = data.matches(PATTERN);
        if (!expressionIsValid) {
            System.out.println("The entered expression contains invalid characters");
            return expressionIsValid;
        }

        expressionIsValid = checkBrackets(data);

        if (!checkBrackets(data)) {
            System.out.println("The brackets are not correctly placed in the entered expression");
            return expressionIsValid;
        }

        return expressionIsValid;
    }

    private boolean checkBrackets(String data) {
        if (data == null || data.isEmpty()) {
            return true;
        }

        int count = 0;
        for (int i = 0; i < data.length(); i++) {
            char letter = data.charAt(i);
            if (letter == '(') {
                count++;
            } else if (letter == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }
        return count == 0;
    }

    private void createSyntaxTree(String expression) {
        if (validateExpression(expression)) {
            creator = new SyntaxTreeCreator();
            syntaxTree = creator.createTree(expression.replaceAll(" ", ""));
        }
    }

    private int calculateExpression(SyntaxTree syntaxTree) {

        if (syntaxTree.getOperation() == null) {
            return syntaxTree.getValue();
        }

        if (syntaxTree.getLeftNode() != null) {
            calculateExpression(syntaxTree.getLeftNode());
        }
        if (syntaxTree.getRightNode() != null) {
            calculateExpression(syntaxTree.getRightNode());
        }

        switch (syntaxTree.getOperation()) {
            case ADD:
                syntaxTree.setValue((short) (syntaxTree.getLeftNode().getValue() + syntaxTree.getRightNode().getValue()));
                break;
            case SUB:
                syntaxTree.setValue((short) (syntaxTree.getLeftNode().getValue() - syntaxTree.getRightNode().getValue()));
                break;
            case MOD:
                syntaxTree.setValue((short) (syntaxTree.getLeftNode().getValue() / syntaxTree.getRightNode().getValue()));
                break;
            case MUL:
                syntaxTree.setValue((short) (syntaxTree.getLeftNode().getValue() * syntaxTree.getRightNode().getValue()));
                break;
        }
        return syntaxTree.getValue();
    }

}
