package destCodeGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class CodeGenerator {
    public static List<String> data;
    public static List<String> text;
    public static final String[] registers = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8"};
    public static final boolean[] available = new boolean[registers.length];
    public static final HashMap<String, String> variableToRegister = new HashMap<>();
    public static final Stack<String> spillStack = new Stack<>();

    public CodeGenerator() {
        data = new ArrayList<>();
        text = new ArrayList<>();

        // Inicializar todos los registros como disponibles
        for (int i = 0; i < available.length; i++) {
            available[i] = true;
        }

        // Inicializar los segmentos de datos y texto
        data.add(".data");
        text.add(".text");

        // Inicializar el segmento de texto con la dirección de retorno
        text.add(".globl main");
        text.add("main:");
    }

    // Método para agregar cadenas al segmento de datos
    private static void addStringToDataSegment(String name, String value) {
        data.add(name + ": .asciiz \"" + value + "\"");
    }

    public static void assignVariableToRegister(String variable, Object value) {
        String reg = RegisterManager.allocateRegister(variable);

        if (reg == null) {
            // Si no hay registros disponibles, spill al stack
            reg = RegisterManager.loadFromStack(variable);
        }

        if (reg != null) {
            // Generar las instrucciones según el tipo de valor
            if (value instanceof Integer) {
                text.add("li " + reg + ", " + value); // Cargar entero
            } else if (value instanceof Float) {
                text.add("li.s " + reg + ", " + value); // Cargar flotante
            } else if (value instanceof Boolean) {
                int intValue = (Boolean) value ? 1 : 0;
                text.add("li " + reg + ", " + intValue); // Cargar boolean como 1 o 0
            } else if (value instanceof Character) {
                int charValue = (int) ((Character) value).charValue();
                text.add("li " + reg + ", " + charValue); // Cargar carácter como entero
            } else if (value instanceof String) {
                // Para cadenas, reserva espacio en el segmento de datos
                addStringToDataSegment(variable, (String) value);
                text.add("la " + reg + ", " + variable); // Cargar dirección de la cadena
            } else {
                throw new IllegalArgumentException("Tipo no soportado: " + value.getClass());
            }
        }
    }

}