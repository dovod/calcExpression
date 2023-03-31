package org.example;

import org.example.service.CalculatorService;
import org.example.service.RecursiveCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private CalculatorService calculatorService;
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void genericTest() {
        calculatorService = new RecursiveCalculatorService();
        String expression = "(12 + 2)/ x2 - y*z  + 2 * 3";
        String expected = "9";
        calculatorService.inputData(expression);
        calculatorService.inputData("x2=2");
        calculatorService.inputData("y= 1");
        calculatorService.inputData(" z = 4 ");
        String actual = calculatorService.calc();
        assertEquals(expected, actual);
    }

    @Test
    public void redefiningVariablesTest() {
        calculatorService = new RecursiveCalculatorService();
        String expression = "(12 + 2)/ x2 - y*z + (2+ 2) * 3";
        String expected = "15";
        calculatorService.inputData(expression);
        calculatorService.inputData("x2=2");
        calculatorService.inputData("y= 1");
        calculatorService.inputData(" z = 4 ");
        String actual = calculatorService.calc();
        assertEquals(expected, actual);

        expected = "12";
        calculatorService.inputData("x2 = 7");
        calculatorService.inputData("z = 2");
        actual = calculatorService.calc();
        assertEquals(expected, actual);
    }

    @Test
    public void redefiningExpressionTest() {
        calculatorService = new RecursiveCalculatorService();
        String expression = "(12 + 2)/ x2 - y*z  + 2 * 3";
        String expected = "9";
        calculatorService.inputData(expression);
        calculatorService.inputData("x2=2");
        calculatorService.inputData("y= 1");
        calculatorService.inputData(" z = 4 ");
        String actual = calculatorService.calc();
        assertEquals(expected, actual);

        expression = "(12 + 2)/ a - b*c + (2+ 2) * 3";
        expected = "6";
        calculatorService.cleanAll();
        calculatorService.inputData(expression);
        calculatorService.inputData("a=7");
        calculatorService.inputData("b= 4");
        calculatorService.inputData(" c = 2 ");
        actual = calculatorService.calc();
        assertEquals(expected, actual);
    }

    @Test
    public void expressionContainsBigInteger() {
        calculatorService = new RecursiveCalculatorService();
        String expression = "50000 + 1";
        calculatorService.inputData(expression);
        String expected = "Enter the expression:\n" +
                "Expression parsing error. Please enter the correct expression. ";
        assertEquals(expected.length(), output.toString().trim().length());
    }

    @Test
    public void inputVariable_BigInteger() {
        calculatorService = new RecursiveCalculatorService();
        String expression = "(12 + 2)/ x2 - y*z  + 2 * 3";
        int expected = 58;
        calculatorService.inputData(expression);
        calculatorService.inputData("x2=50000");
        assertEquals(expected, output.toString().trim().length());
    }
}