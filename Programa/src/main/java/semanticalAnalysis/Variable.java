package semanticalAnalysis;

import java.util.*;
import tables.*;


/**
 * Clase que proporciona métodos para la verificación de variables en análisis semántico.
 */
public class Variable {

    /**
     * Verifica si una variable ya ha sido declarada en el ámbito actual.
     *
     * @param variable     La variable a verificar.
     * @param currentScope El ámbito actual de la función donde se está evaluando la variable.
     */
    public static void checkRepeated(SymbolInfo variable, FunctionInfo currentScope) {
        try {
            boolean found = currentScope.lookup(variable.getName()) != null;
            if (found) {
                System.err.println("Error semantico: la variable '" + variable.getName() + "' ya existe. " +
                        "En la linea: " + variable.getLine() + " y columna: " + variable.getColumn());
            }
        } catch (NullPointerException e) {
            System.err.println("Se intentó revisar si una variable ya existia, pero ocurrio un erro inesperado");
        }
    }

    /**
     * Verifica si una variable existe en el ámbito actual.
     *
     * @param variable     La variable a verificar.
     * @param currentScope El ámbito actual de la función donde se está evaluando la variable.
     */
    public static void checkExistance(SymbolInfo variable, FunctionInfo currentScope) {
        try {
            boolean found = currentScope.lookup(variable.getName()) != null;
            if (!found) {
                System.err.println("Error semantico: la variable '" + variable.getName() + "' no existe en el scope actual. " +
                        "En la linea: " + variable.getLine() + " y columna: " + variable.getColumn());
            }
        } catch (NullPointerException e) {
            System.err.println("Se intentó revisar si una variable ya existia, pero ocurrio un erro inesperado");
        }
    }

    /**
     * Verifica si el tipo de una variable coincide con el tipo de la expresión asignada.
     *
     * @param variable         La variable a verificar.
     * @param expressionResult La expresión que se está asignando a la variable.
     * @param currentScope     El ámbito actual de la función donde se está evaluando la variable.
     * @param symbolTable      La tabla de símbolos donde se pueden buscar funciones y variables.
     */
    public static void checkType(SymbolInfo variable, SymbolInfo expressionResult, FunctionInfo currentScope, SymbolTable symbolTable) {
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
                String exprType = SymbolTableExpressionResult.getType();
                if (!varType.equals(exprType)) {
                    System.err.println("Error semantico: Tipos incompatibles. Se esperaba '" + varType + "' pero se encontró '" + exprType + "'. " +
                            "En la linea: " + expressionResult.getLine() + " y columna: " + expressionResult.getColumn());
                }
            }

            else {
                SymbolInfo function = symbolTable.lookupFunction(expressionResult.getName());
                if (function != null) {
                    String funcType = function.getType();
                    if (!varType.equals(funcType)) {
                        System.err.println("Error semantico: Tipos incompatibles. Se esperaba '" + varType + "' pero se encontró '" + funcType + "'. " +
                                "En la linea: " + function.getLine() + " y columna: " + function.getColumn());
                    }
                }
             }

        } catch (NullPointerException e) {
            if (SymbolTableVariable != null) {
                System.err.println("Error semantico: se intentó pasar algo que no es un token valido a la variable: " + SymbolTableVariable.getName() + ". Se esperaba tipo: " + SymbolTableVariable.getType() +
                        " en la linea: " + (SymbolTableVariable.getLine() + 1) + " y columna: " + (SymbolTableVariable.getColumn() + 1));
            } else if (expressionResult != null) {
                System.err.println("Error semantico: se intentó pasar algo que no es un token valido a la variable (desconocido). Se esperaba tipo: " + "(desconocido)" +
                        " en la linea: " + expressionResult.getLine() + " y columna: " + expressionResult.getColumn());
            }
        }

    }

    /**
     * Verifica que el tamaño de un array sea un número entero válido y positivo.
     *
     * @param array      La variable que representa el array.
     * @param expression La expresión que representa el tamaño del array.
     * @param currentScope El ámbito actual de la función donde se está evaluando la variable.
     * @param symbolTable  La tabla de símbolos donde se pueden buscar funciones y variables.
     */
    public static void checkArraySize(SymbolInfo array, SymbolInfo expression, FunctionInfo currentScope, SymbolTable symbolTable) {
        SymbolInfo SymbolTableVariable = currentScope.lookup(array.getName());
        try {
            SymbolInfo SymbolTableExpressionResult = currentScope.lookup(expression.getName());
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");

            if (basicTypes.contains(expression.getName()) && !expression.getType().equals("int")) {
                System.err.println("Error semantico: se le está pasando un tipo distinto a int como tamaño de array al array: " +  array.getName() +
                        ", en la linea: " + array.getLine() + " y columna: " + array.getColumn());
            } else if (expression.getName().equals("int") && expression.getValue() instanceof Integer && (int) expression.getValue() < 0) {
                System.err.println("Error semantico: no se puede declarar un array con un tamaño negativo, esto pasa en el array: " +
                        array.getName() + ", en la linea: " + array.getLine() + " y columna: " + array.getColumn());
            }

            if (SymbolTableExpressionResult != null) {
                String exprType = SymbolTableExpressionResult.getType();
                if (!exprType.equals("int")) {
                    System.err.println("Error semantico: se le está pasando un tipo distinto a int como tamaño de array al array: " +  array.getName() +
                            ", en la linea: " + array.getLine() + " y columna: " + array.getColumn());
                }
            }

            else {
                SymbolInfo function = symbolTable.lookupFunction(expression.getName());
                if (function != null) {
                    String funcType = function.getType();
                    if (!funcType.equals("int")) {
                        System.err.println("Error semantico: se le está pasando un tipo distinto a int como tamaño de array al array: " +  array.getName() +
                                ", en la linea: " + array.getLine() + " y columna: " + array.getColumn());
                    }
                }
            }

        } catch (NullPointerException e) {
            if (SymbolTableVariable != null) {
                System.err.println("Error semantico: al declarar el tamaño del array: " + array.getName() + ", se le pasó un caracter invalido y no reconocible, linea: " +
                        array.getLine() + " y columna: " + array.getColumn());
            } else {
                System.err.println("Error semantico: al declarar el tamaño del array: (desconocido) se le pasó un caracter invalido y no reconocible");
            }
        }
    }

    
    /**
     * Marca una variable como declarada dentro del ámbito actual.
     *
     * @param name         El nombre de la variable a inicializar.
     * @param currentScope El ámbito actual de la función donde se está declarando la variable.
     */
    public static void initializeVar(String name, FunctionInfo currentScope) {
        SymbolInfo variable = currentScope.lookup(name);
        if (variable != null) {
            variable.setDeclared(true);
        }

    }
}
