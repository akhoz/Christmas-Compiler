package tables;

/**
 * La clase SymbolInfo representa la información asociada a un símbolo,
 * incluyendo su nombre, tipo, y ubicación en el código fuente.
 */
public class SymbolInfo {
    private String name;
    private String type;
    private int line;
    private int column;

    /**
     * Constructor de la clase SymbolInfo.
     *
     * @param name   El nombre del símbolo.
     * @param type   El tipo del símbolo.
     * @param line   La línea donde se encuentra el símbolo en el código fuente.
     * @param column La columna donde se encuentra el símbolo en el código fuente.
     */
    public SymbolInfo(String name, String type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    /**
     * Obtiene el nombre del símbolo.
     *
     * @return El nombre del símbolo.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el tipo del símbolo.
     *
     * @return El tipo del símbolo.
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo del símbolo.
     *
     * @param type El nuevo tipo del símbolo.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene la línea en la que se encuentra el símbolo.
     *
     * @return El número de línea del símbolo en el código fuente.
     */
    public int getLine() {
        return line;
    }

    /**
     * Obtiene la columna en la que se encuentra el símbolo.
     *
     * @return El número de columna del símbolo en el código fuente.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Devuelve una representación en forma de cadena del objeto SymbolInfo.
     *
     * @return Una cadena que representa el símbolo, incluyendo su nombre, tipo, línea y columna.
     */
    @Override
    public String toString() {
        return "SymbolInfo{name='" + name + "', type='" + type + "', line=" + line + ", column=" + column + "}";
    }
}
