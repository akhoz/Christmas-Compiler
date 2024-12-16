package compiler;

import java.io.IOException;

import java_cup.internal_error;
import jflex.exceptions.SilentExit;
import organizer.Organize;

/**
 * Clase Generator que permite generar los analizadores léxico y sintáctico
 * a partir de archivos de configuración específicos (JFlex y CUP).
 */
public class Generator {

    /**
     * Genera el analizador léxico y el analizador sintáctico utilizando los archivos proporcionados,
     * y organiza los archivos generados.
     *
     * @param rutaLexer la ruta al archivo de configuración de JFlex.
     * @param rutaParser la ruta al archivo de configuración de CUP.
     * @throws internal_error si ocurre un error interno en CUP.
     * @throws Exception si ocurre algún otro error durante la generación.
     */
    public void inLexParser(String rutaLexer, String rutaParser) throws internal_error, Exception {
        GenerateLexer(rutaLexer);
        GenerateParser(rutaParser);
        Organize.orderFiles();
    }

    /**
     * Genera el analizador léxico a partir del archivo de configuración JFlex especificado.
     *
     * @param ruta la ruta al archivo JFlex.
     * @throws IOException si ocurre un error de entrada/salida durante la generación.
     * @throws SilentExit si ocurre un error interno en JFlex durante la generación.
     */
    public void GenerateLexer(String ruta) throws IOException, SilentExit {
        String[] strArr = {ruta};
        jflex.Main.generate(strArr);
    }

    /**
     * Genera el analizador sintáctico a partir del archivo de configuración CUP especificado.
     *
     * @param ruta la ruta al archivo CUP.
     * @throws internal_error si ocurre un error interno en CUP.
     * @throws IOException si ocurre un error de entrada/salida durante la generación.
     * @throws Exception si ocurre cualquier otro error durante la ejecución de CUP.
     */
    public void GenerateParser(String ruta) throws internal_error, IOException, Exception {
        String[] strArr = {ruta};
        java_cup.Main.main(strArr);
    }
}
