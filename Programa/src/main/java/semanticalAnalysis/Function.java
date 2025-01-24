package semanticalAnalysis;

import tables.*;

import java.util.Arrays;
import java.util.List;

public class Function {
    public static void checkReturnType(SymbolInfo returnSymbol, FunctionInfo function) {
        try {
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");

            if (!basicTypes.contains(returnSymbol.getType())) {
                SymbolInfo varFromTable =  function.lookup(returnSymbol.getName());
                if (!varFromTable.getType().equals(function.getType())) {
                    System.err.println("Error semantico: el tipo de retorno no coincide con el especificado, se esperaba: " + function.getType() + " y se obtuvo: " +
                            varFromTable.getType() + ", en la linea: " + returnSymbol.getLine() + ", en la columna: " + returnSymbol.getColumn());
                }

            } else {
                if (!returnSymbol.getType().equals(function.getType())) {
                    System.err.println("Error semantico: el tipo de retorno no coincide con el especificado, se esperaba: " + function.getType() + " y se obtuvo: " +
                            returnSymbol.getType() + ", en la linea: " + returnSymbol.getLine() + ", en la columna: " + returnSymbol.getColumn());
                }
            }

        } catch (NullPointerException e) {
            System.err.println("Error semantico: se le intentó pasar un retorno de algo desconocido por el lenguaje");
        }
    }

    public static void checkParamsTypes(SymbolInfo param, SymbolInfo arg, FunctionInfo currentScope) {
        try {
            List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");
            if (!basicTypes.contains(arg.getType())) {
                SymbolInfo varFromTable =  currentScope.lookup(arg.getName());
                if (!varFromTable.getType().equals(param.getType())) {
                    System.err.println("Error semantico: el tipo del parametro no coincide con el argumento pasado, se esperaba: " + param.getType() + " y se recibió: " + varFromTable.getType() +
                            ", en la linea: " + arg.getLine() + " y columna: " + arg.getColumn());
                }
            } else {
                if (!arg.getType().equals(param.getType())) {
                    System.err.println("Error semantico: el tipo del parametro no coincide con el argumento pasado, se esperaba: " + param.getType() + " y se recibió: " + arg.getType() +
                            ", en la linea: " + arg.getLine() + " y columna: " + arg.getColumn());
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Error semantico: se le intentó pasar un parametro de algo desconocido por el lenguaje");
        }

    }
}
