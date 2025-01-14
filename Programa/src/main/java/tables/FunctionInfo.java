package tables;

import java.util.*;

public class FunctionInfo extends SymbolInfo {
    public Stack<HashMap<String, SymbolInfo>> scopes;
    private List<SymbolInfo> params;
    public boolean retornoEncontrado = false;

    public FunctionInfo(String name, String type, int line, int column, List<SymbolInfo> params) {
        super(name, type, line, column);
        this.params = params;
        scopes = new Stack<>();
        if (this.getName().equals("global")) {
            retornoEncontrado = true;
        }
    }

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
        if (!scopes.isEmpty()) {
            HashMap<String, SymbolInfo> scope = scopes.peek();
            if (scope.containsKey(name)) {
                return false;
            }
            scope.put(name, info);
            return true;
        } else {
            System.err.println("No hay ámbitos para insertar.");
            return false;
        }
    }

    public boolean insertParamList(List<SymbolInfo> params) {
        for (SymbolInfo param : params) {
            if (!insert(param.getName(), param)) {
                return false;
            }
        }
        return true;
    }

    /** Busca un símbolo en el ámbito actual y superiores */
    public SymbolInfo lookup(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            HashMap<String, SymbolInfo> scope = scopes.get(i);
            if (scope.containsKey(name)) {
                SymbolInfo info = scope.get(name);
                return info;
            }
        }
        return null;
    }

    public void printScopes() {
        System.out.println("\n=== Tabla de símbolos de función '" + this.getName() + "' ===\n");
        for (int i = 0; i < scopes.size(); i++) {
            System.out.print("Ámbito " + i + ": ");
            HashMap<String, SymbolInfo> scope = scopes.get(i);
            List<String> symbols = new ArrayList<>();

            for (Map.Entry<String, SymbolInfo> entry : scope.entrySet()) {
                String name = entry.getKey();
                String type = entry.getValue().getType();
                String line = String.valueOf(entry.getValue().getLine());
                String column = String.valueOf(entry.getValue().getColumn());
                symbols.add(name + ": " + type + " (" + line + ", " + column + ")");
            }

            System.out.println(symbols);
        }
        System.out.println();
    }


    public Set<String> getCurrentScopeSymbols() {
        if (!scopes.isEmpty()) {
            return scopes.peek().keySet();
        }
        return Collections.emptySet();
    }

    public List<SymbolInfo> getParams() {
        return params;
    }

    public int getParamCount() {
        return params.size();
    }
}
