package compiler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Tester tester = new Tester();
        try {
            // Asegúrate de proporcionar la ruta correcta al archivo
            tester.ejercicioVeranoV2024("src/lex/test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}