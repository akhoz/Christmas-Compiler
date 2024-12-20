package tables;

import java.util.*;

public class SymbolTable {
    private Stack<HashMap<String, SymbolInfo>> scopes;

    public SymbolTable() {
        scopes = new Stack<>();
        // Iniciar con un ámbito global
        beginScope();
    }

    /** Crea un nuevo ámbito (scope) empujando un nuevo HashMap en el stack */
    public void beginScope() {
        scopes.push(new HashMap<String, SymbolInfo>());
    }

    /** Cierra el ámbito actual */
    public void endScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        } else {
            System.err.println("No hay más ámbitos para cerrar.");
        }
    }

    /** Inserta un símbolo en el ámbito actual */
    public boolean insert(String name, SymbolInfo info) {
        HashMap<String, SymbolInfo> currentScope = scopes.peek();
        if (currentScope.containsKey(name)) {
            // Ya existe un símbolo con ese nombre en el ámbito actual
            return false;
        }
        currentScope.put(name, info);
        return true;
    }

    /** Busca un símbolo en el ámbito actual y superiores */
    public SymbolInfo lookup(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            HashMap<String, SymbolInfo> scope = scopes.get(i);
            if (scope.containsKey(name)) {
                SymbolInfo info = scope.get(name);
                System.out.println("Símbolo encontrado: " + info);
                return info;
            }
        }
        System.out.println("Símbolo no encontrado: " + name);
        return null;
    }


    public void printScopes() {
        System.out.println("=== Tabla de símbolos (desde el global al actual) ===");
        for (int i = 0; i < scopes.size(); i++) {
            System.out.println("Ámbito " + i + ": " + scopes.get(i).keySet());
        }
    }

    public Set<String> getCurrentScopeSymbols() {
        if (!scopes.isEmpty()) {
            return scopes.peek().keySet();
        }
        return Collections.emptySet();
    }
}