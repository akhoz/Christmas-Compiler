package compiler;

import java.io.IOException;

public class Main {

    public void test(String rutaScanner) {
        Tester tester = new Tester();
        try {
            // Asegúrate de proporcionar la ruta correcta al archivo
            tester.ejercicioVeranoV2024(rutaScanner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generate(String jflexPath, String cupPath) {
        Generator generator = new Generator();
        try {
            generator.inLexParser(jflexPath, cupPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main mainInstance = new Main();

        //mainInstance.generate("src/lex/minijava.jflex", "src/lex/parser.cup");
        mainInstance.test("src/lex/test.txt");
    }
}
