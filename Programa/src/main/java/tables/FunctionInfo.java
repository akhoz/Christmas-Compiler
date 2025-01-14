package tables;

import java.util.*;

/**
 * La clase FunctionInfo extiende SymbolInfo y se utiliza para manejar información sobre funciones,
 * incluyendo sus parámetros y ámbitos de símbolos anidados.
 */
public class FunctionInfo extends SymbolInfo {
    private Stack<HashMap<String, SymbolInfo>> scopes;
    private List<SymbolInfo> params;

    /**
     * Constructor de la clase FunctionInfo.
     *
     * @param name   El nombre de la función.
     * @param type   El tipo de la función.
     * @param line   La línea donde se declaró la función.
     * @param column La columna donde se declaró la función.
     * @param params La lista de parámetros de la función.
     */
    public FunctionInfo(String name, String type, int line, int column, List<SymbolInfo> params) {
        super(name, type, line, column);
        this.params = params;
        scopes = new Stack<>();
    }

    /**
     * Inicia un nuevo ámbito para los símbolos.
     */
    public void beginScope() {
        scopes.push(new HashMap<String, SymbolInfo>());
    }

    /**
     * Cierra el ámbito actual, si existe.
     * Si no hay ámbitos para cerrar, se genera un mensaje de error.
     */
    public void endScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        } else {
            System.err.println("No hay más ámbitos para cerrar.");
        }
    }

    /**
     * Inserta un símbolo en el ámbito actual.
     *
     * @param name El nombre del símbolo.
     * @param info La información asociada al símbolo.
     * @return {@code true} si el símbolo se insertó correctamente, {@code false} si el símbolo ya existe
     * en el ámbito actual o si no hay un ámbito disponible.
     */
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

    /**
     * Inserta una lista de parámetros como símbolos en el ámbito actual.
     *
     * @param params La lista de parámetros a insertar.
     * @return {@code true} si todos los parámetros se insertaron correctamente,
     * {@code false} si alguno de los parámetros ya existe en el ámbito actual.
     */
    public boolean insertParamList(List<SymbolInfo> params) {
        for (SymbolInfo param : params) {
            if (!insert(param.getName(), param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Busca un símbolo en el ámbito actual y en los superiores.
     *
     * @param name El nombre del símbolo a buscar.
     * @return La información del símbolo si se encuentra, o {@code null} si no existe.
     */
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

    /**
     * Imprime los ámbitos de símbolos de la función en la consola.
     */
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

    /**
     * Obtiene los nombres de los símbolos en el ámbito actual.
     *
     * @return Un conjunto con los nombres de los símbolos en el ámbito actual, o un conjunto vacío si no hay ámbito.
     */
    public Set<String> getCurrentScopeSymbols() {
        if (!scopes.isEmpty()) {
            return scopes.peek().keySet();
        }
        return Collections.emptySet();
    }

    /**
     * Obtiene la lista de parámetros de la función.
     *
     * @return Una lista con la información de los parámetros de la función.
     */
    public List<SymbolInfo> getParams() {
        return params;
    }
}
