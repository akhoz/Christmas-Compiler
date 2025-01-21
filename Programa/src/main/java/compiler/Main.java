package compiler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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
     * @param rutaArchivo la ruta al archivo que se usará para probar el análisis léxico.
     */
    public void test(String rutaArchivo) {
        Tester tester = new Tester();
        try {
            tester.syntacticAnalysis(rutaArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test02 () {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hola mundo");
        }
    }

    /**
     * Método de menu, llama a las distintas acciones del programa y espera respuestas para ver cual ejecutar.
     *
     */
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Generar archivos necesarios");
            System.out.println("2. Usar el compilador");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    generate("src/lex/minijava.jflex", "src/lex/parser.cup");
                    break;
                case "2":
                    System.out.print("Ingrese la ruta del archivo de prueba (debe ser un archivo .txt): ");
                    String archivo = scanner.nextLine();
                    if (archivo.equals("")) {
                        archivo = "src/tests/test09.txt";
                    }
                    File file = new File(archivo);
                    if (file.exists() && file.isFile() && archivo.endsWith(".txt")) {
                        System.out.println(archivo);
                        test(archivo);
                    }
                    else {
                        System.out.println("Ruta invalida. Asegurese de que sea un archivo .txt validoa");
                    }
                    scanner.close();
                    return;
                case "3":
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        }

    }

    /**
     * Método principal del programa.
     *
     */
    public static void main(String[] args) {
        Main mainInstance = new Main();
        mainInstance.menu();
    }
}