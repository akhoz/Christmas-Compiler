package tables;

import java.util.*;

public class SymbolTable {
    public Stack<FunctionInfo> functionScopes;


    public SymbolTable() {
        functionScopes = new Stack<>();
    }

    public boolean pushFunction(FunctionInfo function) {
        if (lookupFunction(function.getName()) != null) {
            return false;
        }
        functionScopes.push(function);
        return true;
    }


    public FunctionInfo lookupFunction(String name) {
        System.out.println(functionScopes);
        for (int i = functionScopes.size() - 1; i >= 0; i--) {
            FunctionInfo function = functionScopes.get(i);
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
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


}