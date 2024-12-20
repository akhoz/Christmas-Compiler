package tables;

public class SymbolInfo {
    private String name;
    private String type;
    private Object value;

    public SymbolInfo(String name, String type) {
        this.name = name;
        this.type = type;
        this.value = null;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Object value) {
        System.out.println("Actualizando valor de '" + name + "' a " + value);
        this.value = value;
    }

    //Override toString
    @Override
    public String toString(){
        return "SymbolInfo{name='" + name + "', type='" + type + "', value=" + value + "}";
    }

}
