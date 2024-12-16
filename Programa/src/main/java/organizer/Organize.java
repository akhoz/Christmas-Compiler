package organizer;

import java.nio.file.*;
import java.io.IOException;

/**
 * Clase Organize que permite organizar y mover archivos generados
 * durante el proceso de compilación a sus ubicaciones correspondientes.
 */
public class Organize {

    /**
     * Mueve un archivo desde su ubicación original a un directorio objetivo.
     *
     * @param path la ruta del archivo que se desea mover.
     */
    public static void organize(String path) {
        Path source = Paths.get(path);
        Path target = Paths.get("src/main/java/parser");

        try {
            Files.move(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo movido con éxito");
        } catch (IOException e) {
            System.err.println("Error al mover el archivo: " + e.getMessage());
        }
    }

    /**
     * Organiza los archivos generados durante el proceso de análisis léxico y sintáctico,
     * moviéndolos a sus ubicaciones respectivas.
     * <p>
     * Este método utiliza el método organize para mover los archivos
     * "Lexer.java", "parser.java", y "sym.java".
     */
    public static void orderFiles() {

        organize("src/lex/Lexer.java");
        organize("parser.java");
        organize("sym.java");
    }
}
