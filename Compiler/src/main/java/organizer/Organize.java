package organizer;

import java.nio.file.*;
import java.io.IOException;

public class Organize {

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

    public static void orderFiles() {

        organize("src/lex/Lexer.java");
        organize("parser.java");
        organize("sym.java");
    }
}
