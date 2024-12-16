package compiler;

import java.io.*;

import java_cup.runtime.Symbol;

import parser.*;

/**
 * Clase Tester para realizar el análisis léxico en un archivo fuente dado.
 */
public class Tester {

    /**
     * Realiza el análisis léxico en el archivo especificado.
     * Este método lee el archivo, tokeniza su contenido utilizando la clase Lexer,
     * y muestra los detalles de cada token, incluyendo el número de línea, número de columna,
     * tipo de token y su valor.
     *
     * @param rutaScanner la ruta al archivo que se analizará léxicamente.
     * @throws IOException si ocurre un error de entrada/salida al leer el archivo.
     */
    public void lexicalAnalysis(String rutaScanner, String rutaSalida) throws IOException {
        Reader reader = new BufferedReader(new FileReader(rutaScanner));
        Lexer lex = new Lexer(reader);

        // Crear archivo de salida
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaSalida))) {
            Symbol token;
            while (true) {
                token = lex.next_token();
                if (token.sym != 0) {
                    // Formatear la salida
                    String output = "Linea: " + (token.left + 1) +
                            " Columna: " + (token.right + 1) +
                            " Token: " + sym.terminalNames[token.sym] +
                            " Valor: " + (token.value == null ? lex.yytext() : token.value.toString());

                    // Imprimir en consola
                    System.out.println(output);

                    // Guardar en archivo
                    writer.println(output);
                } else {
                    break;
                }
            }
        }
    }
}