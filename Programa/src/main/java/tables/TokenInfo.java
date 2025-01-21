package tables;

/**
 * La clase TokenInfo representa un token del código fuente, incluyendo su valor,
 * y su ubicación en términos de línea y columna.
 */
public class TokenInfo {
    private String value;
    private int line;
    private int column;

    /**
     * Constructor de la clase TokenInfo.
     *
     * @param value  El valor del token.
     * @param line   La línea donde se encuentra el token en el código fuente.
     * @param column La columna donde se encuentra el token en el código fuente.
     */
    public TokenInfo(String value, int line, int column) {
        this.value = value;
        this.line = line;
        this.column = column;
    }

    /**
     * Obtiene el valor del token.
     *
     * @return El valor del token como una cadena.
     */
    public String getValue() {
        return value;
    }

    /**
     * Obtiene la línea donde se encuentra el token.
     *
     * @return El número de línea del token en el código fuente.
     */
    public int getLine() {
        return line;
    }

    /**
     * Obtiene la columna donde se encuentra el token.
     *
     * @return El número de columna del token en el código fuente.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Devuelve una representación en forma de cadena del objeto TokenInfo.
     *
     * @return Una cadena que representa el token, incluyendo su valor, línea y columna.
     */
    @Override
    public String toString() {
        return "TokenInfo{value='" + value + "', line=" + line + ", column=" + column + "}";
    }
}
