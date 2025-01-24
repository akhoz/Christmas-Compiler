package destCodeGenerator;

import java.util.*;


public class CodeGenerator {
    public static List<String> data;
    public static List<String> text;
    public static List<String> encabezadoFuncion;
    public static List<String> cuerpoFuncion;
    public static List<String> functionScope;
    public static List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");

    public CodeGenerator() {
        data = new ArrayList<>();
        text = new ArrayList<>();

        // Inicializar los segmentos de datos y texto
        data.add(".data");
        text.add(".text");

        // Inicializar el segmento de texto con la dirección de retorno
        text.add(".globl main");
        text.add("main:");
    }

    private static void addStringToDataSegment(String name, String value) {
        data.add(name + ": .asciiz \"" + value + "\"");
    }

    public static void createFunction(String functionName, List<String> parameters) {
        functionScope = new ArrayList<>();
        encabezadoFuncion = new ArrayList<>();
        cuerpoFuncion = new ArrayList<>();

        functionName = functionName.replace("_", "");
        encabezadoFuncion.add(functionName + ":");
        functionScope.addAll(parameters);

        functionScope.add("ra");

        // Pushear el ra en el stack
        pushToStack(0, "$ra", (functionScope.size() - 1) * 4);
    }

    public static void assignValueToIdentifier(String identifierName, Object value) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + identifierName + " = " + value);
        // En caso de ser literal, se pushea directamente
        if (basicTypes.contains(identifierName) && (value instanceof Integer || value instanceof Character) ) {
            cuerpoFuncion.add("li $s7, " + value);
            pushToStack(value, "$s7", (functionScope.size() - 1) * 4 );
        }
    }

    public static void addToFunctionScope(String item) {
        functionScope.add(item);
    }

    public static void closeFunction() {
        cuerpoFuncion.add("addu $sp, $sp, " + functionScope.size() * 4);
        List<String> fullFunction = new ArrayList<>();
        fullFunction.addAll(encabezadoFuncion);
        fullFunction.addAll(cuerpoFuncion);
        text.addAll(fullFunction);
        System.out.println("\n aasaasaasasasasaspaspapaps \n" + text);
    }

    public static void addFinalCode() {
        text.add("li $v0, 10");
        text.add("syscall");
        System.out.println(data + "\n" + text);
    }

    public static void pushToStack(Object item, String register, int index) {
        StringBuilder code = new StringBuilder();
        if (item instanceof Integer || item instanceof Character) {
            code.append("sw " + register + ", " + index + "($sp)");
        } else if (item instanceof Float) {
            code.append("s.s " + register + ", " + index + "($sp)");
        }

        cuerpoFuncion.add(code.toString());
    }
}