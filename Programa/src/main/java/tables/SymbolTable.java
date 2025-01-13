package tables;

import java.util.*;

public class SymbolTable {
    public Stack<FunctionInfo> functionScopes;


    public SymbolTable() {
        functionScopes = new Stack<>();
    }

    public boolean pushFunction(FunctionInfo function) {
        if (lookupFunction(function.getName()) != null) {
            System.out.println("pushFunction: Función '" + function.getName() + "' ya existe en la tabla de símbolos.");
            return false;
        }
        functionScopes.push(function);
        System.out.println("pushFunction: Función '" + function.getName() + "' insertada en la tabla de símbolos.");
        return true;
    }



    public FunctionInfo lookupFunction(String name) {
        //System.out.println(functionScopes);
        for (int i = functionScopes.size() - 1; i >= 0; i--) {
            FunctionInfo function = functionScopes.get(i);
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    public void printAllFunctions() {
        System.out.println("=== Todas las funciones en la tabla de símbolos ===");
        for (int i = 0; i < functionScopes.size(); i++) {
            FunctionInfo func = functionScopes.get(i);
            System.out.println("Función " + (i+1) + ": " + func.getName() + " con tipo de retorno " + func.getType());
        }
        System.out.println();
    }


    public FunctionInfo getCurrentScope() {
        if (!functionScopes.isEmpty()) {
            return functionScopes.peek();  // Devuelve el último FunctionInfo añadido
        }
        return null;  // Si la pila está vacía, no hay ninguna función.
    }

    // pop function from stack
    public void popFunction() {
        if (!functionScopes.isEmpty()) {
            functionScopes.pop();
        }
    }

    public void printSymbolTableForFunction(String functionName) {
        FunctionInfo function = lookupFunction(functionName);
        if (function != null) {
            function.printScopes();
        } else {
            System.err.println("Error: Función '" + functionName + "' no encontrada en la tabla de símbolos.");
        }
    }


}