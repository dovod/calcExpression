package org.example;

public class SyntaxTree {
    private Short value;
    private Operation operation;
    private SyntaxTree leftNode;
    private SyntaxTree rightNode;

    public SyntaxTree(Operation operation) {
        this.operation = operation;
    }


    public SyntaxTree(Short value) {
        this.value = value;
    }

    public SyntaxTree() {
    }

    public SyntaxTree getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(SyntaxTree leftNode) {
        this.leftNode = leftNode;
    }

    public SyntaxTree getRightNode() {
        return rightNode;
    }

    public void setRightNode(SyntaxTree rightNode) {
        this.rightNode = rightNode;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}
