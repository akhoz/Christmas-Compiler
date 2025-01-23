package destCodeGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class CodeGenerator {
    public static List<String> data;
    public static List<String> text;
    public static final String[] registers = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8"};
    public static final String[] floatRegisters = {"$f0", "$f1", "$f2", "$f3", "$f4", "$f5", "$f6", "$f7", "$f8", "$f9", "$f10", "$f11", "$f12"};

    public static final boolean[] available = new boolean[registers.length];
    public static final boolean[] floatAvailable = new boolean[floatRegisters.length];
    public static final HashMap<String, String> variableToRegister = new HashMap<>();
    public static final Stack<String> spillStack = new Stack<>();

    public CodeGenerator() {
        data = new ArrayList<>();
        text = new ArrayList<>();

        // Inicializar todos los registros como disponibles
        for (int i = 0; i < available.length; i++) {
            available[i] = true;
        }

        for (int i = 0; i < floatAvailable.length; i++) {
            floatAvailable[i] = true;
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

    public static void addFinalCode() {
        text.add("li $v0, 10");
        text.add("syscall");
        System.out.println(data + "\n" + text);
    }

    public static void assignVariableToRegister(String variable, Object value) {
        String reg = "";
        if (value instanceof Float) {
            reg = RegisterManager.allocateRegister(variable, true);
        } else {
            reg = RegisterManager.allocateRegister(variable, false);
        }

        if (reg == null) {
            // Si no hay registros disponibles, spill al stack
            reg = RegisterManager.loadFromStack(variable, value instanceof Float);
        }

        if (reg != null) {
            // Generar las instrucciones según el tipo de valor
            if (value instanceof Integer) {
                text.add("li " + reg + ", " + value); // Cargar entero
            } else if (value instanceof Float) {
                text.add("mov.s " + reg + ", " + value); // Cargar flotante
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
                System.err.println("Tipo de dato no soportado: " + value);
            }
        }
    }

}