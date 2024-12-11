

public class Main {
    public static void main(String[] args) {
        Generator generator = new Generator();
        try {
            generator.inLexParser("src/lex/flex.jflex", "src/lex/parser.cup");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}