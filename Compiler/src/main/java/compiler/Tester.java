package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
    public void lexicalAnalysis(String rutaScanner) throws IOException {
        Reader reader = new BufferedReader(new FileReader(rutaScanner));
        reader.read();
        Lexer lex = new Lexer(reader);
        int i = 0;
        Symbol token;
        while (true) {
            token = lex.next_token();
            if (token.sym != 0) {
                System.out.println("Linea: " + (token.left + 1) + " Columna: " + (token.right + 1) + " Token: " + sym.terminalNames[token.sym] + " Valor: " +
                        (token.value == null ? lex.yytext() : token.value.toString()));
            } else {
                break;
            }
        }
    }
}