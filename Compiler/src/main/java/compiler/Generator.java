package compiler;

import java.io.IOException;

import java_cup.internal_error;
import jflex.exceptions.SilentExit;
import organizer.Organize;


public class Generator {

    public void inLexParser(String rutaLexer, String rutaParser) throws internal_error, Exception {
        GenerateLexer(rutaLexer);
        GenerateParser(rutaParser);
        Organize.orderFiles();
    }


    public void GenerateLexer(String ruta) throws IOException, SilentExit {
        String[] strArr = {ruta};
        jflex.Main.generate(strArr);
    }

    public void GenerateParser(String ruta) throws internal_error, IOException, Exception {
        String[] strArr = {ruta};
        java_cup.Main.main(strArr);
    }
}
