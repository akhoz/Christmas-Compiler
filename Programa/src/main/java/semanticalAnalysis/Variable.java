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

    public static void checkType(SymbolInfo variable, SymbolInfo expressionResult) {
        String varType = variable.getType();
        String exprType = expressionResult.getType();

        if (!varType.equals(exprType)) {
            System.err.println("Error semantico: Tipos incompatibles. Se esperaba '" + varType + "' pero se encontró '" + exprType + "'. " +
                    "En la linea: " + expressionResult.getLine() + " y columna: " + expressionResult.getColumn());
        }
    }

}
