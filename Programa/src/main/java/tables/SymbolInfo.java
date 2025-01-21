package tables;

public class SymbolInfo<T>  {
    private String name;
    private String type;
    private int line;
    private int column;
    private T value;

    public SymbolInfo(String name, String type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    @Override
    public String toString(){
        return "SymbolInfo{name='" + name + "', type='" + type + "', line=" + line + ", column=" + column + "}";
    }

}
