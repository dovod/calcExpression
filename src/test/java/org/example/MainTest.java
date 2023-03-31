package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }


    @Test
    public void genericTest() {
//        Main.createSyntaxTree("(12 + 2)/ x2 - y*z  + 2 * 3");
//        assertEquals("Ok", outputStream.toString().trim());
    }

//    @Test
//    public void deleteFirstSpaces() {
//        String expected = "aba";
//
//        String input = " aba";
//        String actual = Main.deleteSpace(input);
//        assertEquals(expected.toString(), actual.toString());
//
//        input = "  aba";
//        actual = Main.deleteSpace(input);
//        assertEquals(expected, actual);
//
//        input = "         aba";
//        actual = Main.deleteSpace(input);
//        assertEquals(expected, actual);
//
//        actual = Main.deleteSpace(expected);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void parseShortValueTest() {
//        String input = "123   23 12";
//        short expected = 123;
//
//        short actual = Main.parseShortValue(input);
//
//        assertEquals(expected, actual);
//
//        input = "1   23 12";
//        expected = 1;
//
//        actual = Main.parseShortValue(input);
//
//        assertEquals(expected, actual);
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            Main.parseShortValue("123a 34 94");
//        });
//
//        assertEquals("Error parse digit", exception.getMessage());
//    }
//
//    @Test
//    public void parseOperationTest() {
//        String input = "+ 123";
//        Operation expected = Operation.ADD;
//
//        Operation actual = Main.parseOperator(input);
//
//        assertEquals(expected, actual);
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            Main.parseOperator("& 23a 34 94");
//        });
//
//        assertEquals("Operation not found", exception.getMessage());
//    }

}