package tables;

import java.util.*;

/**
 * La clase SymbolTable representa una tabla de símbolos que gestiona el contexto
 * y el alcance de las funciones dentro de un programa. Utiliza una pila para manejar
 * ámbitos de funciones de forma jerárquica.
 */
public class SymbolTable {
    public Stack<FunctionInfo> functionScopes;

    /**
     * Constructor de la clase SymbolTable.
     * Inicializa la pila de ámbitos de funciones.
     */
    public SymbolTable() {
        functionScopes = new Stack<>();
    }

    /**
     * Agrega una nueva función al ámbito actual.
     *
     * @param function La función a agregar a la tabla de símbolos.
     * @return {@code true} si la función se agregó correctamente,
     * {@code false} si ya existe una función con el mismo nombre en el ámbito actual.
     */
    public boolean pushFunction(FunctionInfo function) {
        if (lookupFunction(function.getName()) != null) {
            return false;
        }
        functionScopes.push(function);
        return true;
    }

    /**
     * Busca una función por su nombre en los ámbitos actuales y superiores.
     *
     * @param name El nombre de la función a buscar.
     * @return La información de la función si se encuentra, o {@code null} si no existe.
     */
    public FunctionInfo lookupFunction(String name) {
        for (int i = functionScopes.size() - 1; i >= 0; i--) {
            FunctionInfo function = functionScopes.get(i);
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    /**
     * Obtiene el ámbito actual (la última función agregada).
     *
     * @return La información de la función en el ámbito actual, o {@code null} si no hay funciones en la pila.
     */
    public FunctionInfo getCurrentScope() {
        if (!functionScopes.isEmpty()) {
            return functionScopes.peek();
        }
        return null;
    }

    /**
     * Elimina el ámbito actual (la última función agregada) de la pila.
     * Si la pila está vacía, no realiza ninguna operación.
     */
    public void popFunction() {
        if (!functionScopes.isEmpty()) {
            functionScopes.pop();
        }
    }
}
