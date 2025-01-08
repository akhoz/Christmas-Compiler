package tables;


public class TokenInfo {
    private String value;
    private int line;
    private int column;

    public TokenInfo(String value, int line, int column) {
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "TokenInfo{value='" + value + "', line=" + line + ", column=" + column + "}";
    }
}

