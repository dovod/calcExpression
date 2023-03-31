package org.example;

import org.example.controller.CalcController;
import org.example.controller.ConsoleCalcController;

public class Main {
    private static final String INTRO = "CalcExpression%*+=- \n" +
            "This is a console application for calculating arithmetic expressions. \n";

    public static void main(String[] args) {
        System.out.println(INTRO);
        CalcController controller = new ConsoleCalcController();
    }
}