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

            if (!basicTypes.contains(operand1.getType())) {
                operandOneFromTable = currentScope.lookup(operand1.getName());
                operand1 = operandOneFromTable;
            }

            if (!basicTypes.contains(operand2.getType())) {
                operandTwoFromTable = currentScope.lookup(operand2.getName());
                operand2 = operandTwoFromTable;
            }

            if (!operand1.getType().equals(operand2.getType())) {
                System.err.println("Error semantico, operando cosas diferentes: " + operand1.getType() + " y " + operand2.getType() +
                        ", linea: " + operand1.getLine() + ", columna: " + operand1.getColumn());
            }
        } catch (NullPointerException e) {
            String op1 = null;
            String op2 = null;
            if (operand1 != null) {
                op1 = operand1.getType();
            }
            if (operand2 != null) {
                op2 = operand2.getType();
            }
            System.err.println("Error semantico, se intentó operar algo que no es algo valido del lenguaje: " + op1 + " y " + op2);
        }
    }
}
