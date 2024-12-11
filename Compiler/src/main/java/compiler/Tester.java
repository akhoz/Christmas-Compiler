package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.Lexer;
import java_cup.runtime.Symbol;

public class Tester {
    public void ejercicioVeranoV2024(String rutaScanner) throws IOException {
        Reader reader = new BufferedReader(new FileReader(rutaScanner));
        reader.read();
        Lexer lex = new Lexer(reader);
        int i = 0;
        Symbol token;
        while (true) {
            token = lex.next_token();
            if (token.sym != 0) {
                System.out.println("Token: " + token.sym + ", Valor: " +
                        (token.value == null ? lex.yytext() : token.value.toString()));
            } else {
                break;
            }
        }
    }
}
