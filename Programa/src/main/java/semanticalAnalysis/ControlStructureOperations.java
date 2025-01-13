package semanticalAnalysis;

import tables.*;

import java.util.Arrays;
import java.util.List;

public class ControlStructureOperations {
    public static void checkOperandsType(SymbolInfo operand1, SymbolInfo operand2, FunctionInfo currentScope) {
        try {

            SymbolInfo operandOneFromTable = null;
            SymbolInfo operandTwoFromTable = null;
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");

            int line = operand1.getLine();
            int column = operand1.getColumn();


            if (!basicTypes.contains(operand1.getType())) {
                operandOneFromTable = currentScope.lookup(operand1.getName());
                operand1 = operandOneFromTable;
            }

            if (!basicTypes.contains(operand2.getType()) && !operand1.getType().equals(operand2.getType())) {
                operandTwoFromTable = currentScope.lookup(operand2.getName());
                operand2 = operandTwoFromTable;
            }

            if (!operand1.getType().equals(operand2.getType())) {
                System.err.println("Error semantico, operando/comparando cosas diferentes: " + operand1.getType() + " y " + operand2.getType() +
                        ", linea: " + line + ", columna: " + column);
            }
        } catch (NullPointerException e) {
            String op1 = null;
            String op2 = null;
            if (operand1 != null) {
                op1 = operand1.getType();
                System.err.println("Error semantico, se intentó operar algo que no es algo valido del lenguaje: " + op1 + " y " + op2 + " linea: " + operand1.getLine() + ", columna: " + operand1.getColumn());
            }
            if (operand2 != null) {
                op2 = operand2.getType();
                System.err.println("Error semantico, se intentó operar algo que no es algo valido del lenguaje: " + op1 + " y " + op2 + "; linea: " + operand2.getLine() + ", columna: " + operand2.getColumn());
            }

        }
    }
    public static void checkUnaryOperandType(SymbolInfo operand, FunctionInfo currentScope) {
        try {
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");
            if (!basicTypes.contains(operand.getType())) {
                SymbolInfo operandFromTable = currentScope.lookup(operand.getName());
                if (!operandFromTable.getType().equals("int")) {
                    System.err.println("Error semantico: se le está pasando un algo que no es de tipo int a una operacion unaria, linea: " + operand.getLine() + ", columna: " + operand.getColumn());
                }
            } else {
                System.err.println("Error semantico: las operaciones unarias de incremento/decremento solo funcionan con variables, linea: " + operand.getLine() + ", columna: " + operand.getColumn());
            }

        } catch (NullPointerException e) {
            System.err.println("Error semantico: se le intentó pasar a una operacion unaria algo desconocido para el lenguaje, se esperaba un int ");
        }
    }

    public static void checkNegationType(SymbolInfo operand) {
        try {
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");
            if (basicTypes.contains(operand.getType())) {
                if (!operand.getType().equals("int") || !operand.getType().equals("float")) {
                    System.err.println("Error semantico: se le está pasando un algo que no es de tipo int o float a una negación, linea: " + operand.getLine() + ", columna: " + operand.getColumn());
                }
            } else {
                System.err.println("Error semantico: se intentó pasar algo a una negación que no es un literal entero o flotante, linea: " + operand.getLine() + ", columna: " + operand.getColumn());
            }
        } catch (NullPointerException e) {
            System.err.println("Error semantico: se le intentó pasar a una negación algo desconocido para el lenguaje, se esperaba un int ");
        }
    }
}
