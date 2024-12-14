package compiler;

import java.io.IOException;

/**
 * Clase principal del compilador.
 * Proporciona métodos para probar el análisis léxico y generar los archivos de análisis léxico y sintáctico.
 */
public class Main {

    /**
     * Genera los archivos de análisis léxico y sintáctico utilizando JFlex y CUP.
     *
     * @param jflexPath la ruta al archivo JFlex.
     * @param cupPath la ruta al archivo CUP.
     */
    public void generate(String jflexPath, String cupPath) {
        Generator generator = new Generator();
        try {
            generator.inLexParser(jflexPath, cupPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prueba el análisis léxico utilizando un archivo de entrada especificado.
     *
     * @param rutaScanner la ruta al archivo que se usará para probar el análisis léxico.
     */
    public void test(String rutaScanner) {
        Tester tester = new Tester();
        try {
            tester.lexicalAnalysis(rutaScanner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal del programa.
     * Descomenta la línea correspondiente para generar los archivos de análisis
     * o para probar el análisis léxico con un archivo específico.
     *
     * @param args argumentos de línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        Main mainInstance = new Main();

        //mainInstance.generate("src/lex/minijava.jflex", "src/lex/parser.cup");
        mainInstance.test("src/lex/test01.txt");
    }
}