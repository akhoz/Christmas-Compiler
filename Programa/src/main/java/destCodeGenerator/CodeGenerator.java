package destCodeGenerator;

import tables.SymbolInfo;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

public class CodeGenerator {
    public static List<String> data;
    public static List<String> text;
    public static List<String> encabezadoFuncion;
    public static List<String> cuerpoFuncion;
    public static LinkedHashMap<String, String> functionScope; // Cambiado a LinkedHashMap
    public static LinkedHashMap<String, String> functionParams; // Cambiado a LinkedHashMap
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

    public static void createFunction(String functionName, List<SymbolInfo> parameters) {
        functionScope = new LinkedHashMap<>(); // Cambiado a LinkedHashMap
        encabezadoFuncion = new ArrayList<>();
        cuerpoFuncion = new ArrayList<>();
        functionParams = new LinkedHashMap<>();

        functionName = functionName.replace("_", "");
        encabezadoFuncion.add(functionName + ":");

        for (SymbolInfo<?> param : parameters) {
            functionParams.put(param.getName(), param.getType());
            functionScope.put(param.getName(), param.getType());
        }

        // Agregar registro de retorno al scope
        functionScope.put("ra", "int");

        // Pushear el registro de retorno al stack
        pushToStack(0, "int", "$ra", (functionScope.size() - 1) * 4);
    }

    public static void createAndAssignValueToIdentifier(SymbolInfo expresion) {
        // En caso de ser literal, se pushea directamente
        if (basicTypes.contains(expresion.getName()) && (expresion.getName().equals("int") || expresion.getValue().equals("char"))) {
            cuerpoFuncion.add("li $s7, " + expresion.getValue());
            pushToStack(expresion.getValue(), expresion.getType(), "$s7", (functionScope.size() - 1) * 4 );
        } else if (basicTypes.contains(expresion.getName()) && expresion.getValue().equals("float")) {
            cuerpoFuncion.add("li.s $f11, " + expresion.getValue());
            pushToStack(expresion.getValue(), expresion.getType(),"$f11", (functionScope.size() - 1) * 4 );
        } else {
            String register = getItemInfoFromStack(expresion.getName());
            pushToStack(expresion.getValue(), expresion.getType(), register, (functionScope.size() - 1) * 4 );
        }
    }

    public static void addToFunctionScope(String name, String type) {
        functionScope.put(name, type);
    }

    public static String getItemInfoFromStack(String itemName) {
        // esta funcion es para obtener el valor de un identifier del stack, se almacena en un registro el cual se retorna
        String register = getRegister();
        int index = getIndexInFunctionScope(itemName);
        String type = functionScope.get(itemName);
        System.out.println(itemName + "AAAAPPPPPPPPPPPPPPPPPPPPPPPPP");
        popFromStack(register, index * 4, type);
        return register; // retorna el registro para poder hacer algo como x = y (s7 = t0), por ejemplp
    }

    public static void closeFunction() {
        int difference = Math.max(functionScope.size() - functionParams.size(), 0) * 4;
        encabezadoFuncion.add("subu $sp, $sp, " + difference);
        cuerpoFuncion.add("addu $sp, $sp, " + functionScope.size() * 4);
        List<String> fullFunction = new ArrayList<>();
        fullFunction.addAll(encabezadoFuncion);
        fullFunction.addAll(cuerpoFuncion);
        text.addAll(fullFunction);
        System.out.println("\n aasaasaasasasasaspaspapaps \n" + text);
        //System.out.println(functionScope);
        //System.out.println(functionParams);
    }

    public static void pushToStack(Object item, String type, String register, int index) {
        StringBuilder code = new StringBuilder();
        System.out.println(item);
        if (type != null) {
            if (type.equals("int") || type.equals("char")) {
                code.append("sw " + register + ", " + index + "($sp)");
            } else if (type.equals("float")) {
                code.append("s.s " + register + ", " + index + "($sp)");
            }
        } else {
            code.append("sw " + register + ", " + index + "($sp)");
        }
        cuerpoFuncion.add(code.toString());
    }

    public static void popFromStack(String register, int index, String type) {
        StringBuilder code = new StringBuilder();
        if (type != null) {
            if (type.equals("int") || type.equals("char") || type.equals("boolean")) {
                code.append("lw " + register + ", " + index + "($sp)");
            } else if (type.equals("float")) {
                code.append("l.s " + register + ", " + index + "($sp)");
            }
        } else {
            code.append("lw " + register + ", " + index + "($sp)");
        }
        cuerpoFuncion.add(code.toString());
    }

    public static void addFinalCode() {
        text.add("li $v0, 10");
        text.add("syscall");
        System.out.println(data + "\n" + text);

        try (FileWriter writer = new FileWriter("src/tests/output.asm")) {
            for (String line : data) {
                writer.write(line + System.lineSeparator());
            }
            for (String line : text) {
                writer.write(line + System.lineSeparator());
            }
            System.out.println("Archivo generado: output.asm");
        } catch (IOException e) {
            System.err.println("Error escribiendo el archivo: " + e.getMessage());
        }
    }

    public static String getRegister() {
        return "$t0"; // Camviar esto, obviamente
    }

    public static int getIndexInFunctionScope(String name) {
        List<String> keys = new ArrayList<>(functionScope.keySet());
        System.out.println(keys);
        System.out.println(functionScope);
        return keys.indexOf(name);
    }

}
