package org.example.controller;

import org.example.service.CalculatorService;
import org.example.service.PrintableStructure;
import org.example.service.RecursiveCalculatorService;

import java.util.Scanner;

public class ConsoleCalcController implements CalcController {
    private final Scanner consoleScanner;
    private final CalculatorService calculator;

    public ConsoleCalcController() {
        consoleScanner = new Scanner(System.in);
        notifyAvailableCommand();
        calculator = new RecursiveCalculatorService();
        listenInput();
    }

    private void listenInput() {
        while (true) {
            String input = consoleScanner.nextLine().trim();

            switch (input) {
                case "quit":
                    if (stopApp()) {
                        return;
                    }
                    break;
                case "calc":
                    calc();
                    break;
                case "print" :
                    print();
                    break;
                case "help" :
                    help();
                    break;
                case "new" :
                    newCalculation();
                    continue;
                default:
                    inputData(input);
            }
        }
    }

    private void help() {
        notifyAvailableCommand();
    }

    private void inputData(String input) {
        calculator.inputData(input);
    }

    private void newCalculation() {
        notifyAvailableCommand();
        calculator.cleanAll();
    }

    private void print() {
        if(calculator instanceof PrintableStructure) {
          String structure = ((PrintableStructure) calculator).printStructure();
          System.out.println(structure);
        }
    }

    private void calc() {
        String result = calculator.calc();
        System.out.println(result);
    }

    private boolean stopApp() {
        System.out.println("Goodbye!");
        return true;
    }

    private void notifyAvailableCommand() {
        System.out.println("The following commands are available: \n"
                + "    help     - help for working with this app\n"
                + "    quit     - completion of the program\n"
                + "    calc     - calculation output\n"
                + "    print    - output of the syntax tree structure\n"
                + "    new      - calculation of a new expression\n");
    }
}
