package tables;

public class SymbolInfo {
    private String name;
    private String type;

    public SymbolInfo(String name, String type) {
        this.name = name;
        this.type = type;

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


    //Override toString
    @Override
    public String toString(){
        return "SymbolInfo{name='" + name + "', type='" + type + "' }";
    }

}
