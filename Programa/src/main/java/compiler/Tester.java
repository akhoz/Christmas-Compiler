package compiler;

import java.io.*;

import java_cup.runtime.Symbol;

import parser.*;

/**
 * Clase Tester para realizar el análisis léxico en un archivo fuente dado.
 */
public class Tester {
    int i = 0;
    int result = i + 10;
    /**
     * Realiza el análisis léxico en el archivo especificado.
     * Este método lee el archivo, tokeniza su contenido utilizando la clase Lexer,
     * y muestra los detalles de cada token, incluyendo el número de línea, número de columna,
     * tipo de token y su valor. Adicionalmente, almacena en un archivo .txt la informacion
     *
     * @param rutaScanner la ruta al archivo que se analizará léxicamente.
     * @throws IOException si ocurre un error de entrada/salida al leer el archivo.
     */
    public void lexicalAnalysis(String rutaScanner, String rutaSalida) throws IOException {
        Reader reader = new BufferedReader(new FileReader(rutaScanner));
        Lexer lex = new Lexer(reader);

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaSalida))) {
            Symbol token;
            while (true) {
                token = lex.next_token();
                if (token.sym != 0) {

                    String output = "Linea: " + (token.left + 1) +
                            " Columna: " + (token.right + 1) +
                            " Token: " + sym.terminalNames[token.sym] +
                            " Valor: " + (token.value == null ? lex.yytext() : token.value.toString());

                    System.out.println(output);

                    writer.println(output);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Realiza el análisis sintáctico en el archivo especificado.
     * Este método lee el archivo, crea un Lexer, y se lo pasa a la clase parser.
     * Se analiza la sintaxis del archivo y se informa si la sintaxis es correcta o incorrecta.
     *
     * @param rutaScanner la ruta al archivo que se analizará sintácticamente.
     * @throws IOException si ocurre un error de entrada/salida al leer el archivo.
     */
    public void syntacticAnalysis(String rutaScanner) throws IOException {
        Reader reader = new BufferedReader(new FileReader(rutaScanner));
        Lexer lex = new Lexer(reader);
        parser parser = new parser(lex);
        try {
            lexicalAnalysis(rutaScanner, "src/tests/output.txt");
            parser.parse();
            System.out.println("Análisis sintáctico completado correctamente.");
        } catch (Exception e) {
            System.err.println("Error durante el análisis sintáctico:");
            e.printStackTrace();
        }
    }

}