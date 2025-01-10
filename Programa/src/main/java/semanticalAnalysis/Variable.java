package semanticalAnalysis;

import java.util.*;
import tables.*;

public class Variable {

    public static void checkRepeated(SymbolInfo variable, FunctionInfo currentScope) {
        boolean found = currentScope.lookup(variable.getName()) != null;
        if (found) {
            System.err.println("Error semantico: la variable '" + variable.getName() + "' ya existe. " +
                    "En la linea: " + variable.getLine() + " y columna: " + variable.getColumn());
        }
    }

    public static void checkExistance(SymbolInfo variable, FunctionInfo currentScope) {
        boolean found = currentScope.lookup(variable.getName()) != null;
        if (!found) {
            System.err.println("Error semantico: la variable '" + variable.getName() + "' no existe en el scope actual. " +
                    "En la linea: " + variable.getLine() + " y columna: " + variable.getColumn());
        }
    }

    public static void checkType(SymbolInfo variable, SymbolInfo expressionResult, FunctionInfo currentScope) {
        SymbolInfo SymbolTableVariable = currentScope.lookup(variable.getName());
        try {
            SymbolInfo SymbolTableExpressionResult = currentScope.lookup(expressionResult.getName());
            String varType = SymbolTableVariable.getType();

            // Lista de tipos “básicos” que pueden aparecer como literales:
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");

            // Si es un literal no existe en la tabla de simbolos, entonces obtenemos su tipo directamente
            if (basicTypes.contains(expressionResult.getName())) {
                String exprType = expressionResult.getType();
                if (!varType.equals(exprType)) {
                    System.err.println("Error semantico: Tipos incompatibles. Se esperaba '" + varType + "' pero se encontró '" + exprType + "'. " +
                            "En la linea: " + expressionResult.getLine() + " y columna: " + expressionResult.getColumn());
                }
            }

            // Si no es un literal (es un identifier) existe en la tabla de simbolos, entonces lo bscamos primero y luego comparamos tipos
            if (SymbolTableExpressionResult != null) {
                System.out.println(SymbolTableExpressionResult.getName() + SymbolTableVariable + "-------");
                String exprType = SymbolTableExpressionResult.getType();
                if (!varType.equals(exprType)) {
                    System.err.println("Error semantico: Tipos incompatibles. Se esperaba '" + varType + "' pero se encontró '" + exprType + "'. " +
                            "En la linea: " + expressionResult.getLine() + " y columna: " + expressionResult.getColumn());
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Error semantico: se intentó pasar algo que no es un token valido a la variable. Se esperaba tipo: " + SymbolTableVariable.getName() +
                    " en la linea: " + SymbolTableVariable.getLine() + " y columna: " + SymbolTableVariable.getColumn());
        }
    }
}
