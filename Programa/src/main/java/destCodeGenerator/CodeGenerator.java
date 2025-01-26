package destCodeGenerator;

import jdk.dynalink.Operation;
import tables.SymbolInfo;
import java.io.FileWriter;
import java.io.IOException;
import destCodeGenerator.Operations;

import java.util.*;

public class CodeGenerator {
    public static List<String> data;
    public static List<String> text;
    public static List<String> encabezadoFuncion;
    public static List<String> cuerpoFuncion;
    public static LinkedHashMap<String, String> functionScope; // Cambiado a LinkedHashMap
    public static LinkedHashMap<String, String> functionParams; // Cambiado a LinkedHashMap
    public static List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float");

    public static final String[] registers = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8"};
    public static final boolean[] available = new boolean[registers.length];
    public static final String[] floatRegisters = {
            "$f0", "$f1", "$f2", "$f3", "$f4", "$f5", "$f6", "$f7", "$f8", "$f9", "$f10", "$f11", "$f12"
    };
    public static final boolean[] floatAvailable = new boolean[floatRegisters.length];
    public static List<Operations> operations = new ArrayList<>();


    public CodeGenerator() {
        data = new ArrayList<>();
        text = new ArrayList<>();

        // Inicializar los segmentos de datos y texto
        data.add(".data");
        text.add(".text");

        // Inicializar el segmento de texto con la dirección de retorno
        text.add(".globl main");
        text.add("main:");

        Arrays.fill(available, true);
        Arrays.fill(floatAvailable, true);
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

    public static void assignValueToIdentifier(String identifierName, SymbolInfo expresion) {
        // En caso de ser literal, se pushea directamente
        if (expresion.getValue() != null) {
            if (basicTypes.contains(expresion.getName()) && (expresion.getName().equals("int") || expresion.getValue().equals("char"))) {
                cuerpoFuncion.add("li $s7, " + expresion.getValue());
                pushToStack(expresion.getValue(), expresion.getType(), "$s7", getIndexInFunctionScope(identifierName) * 4);
            } else if (basicTypes.contains(expresion.getName()) && expresion.getValue().equals("float")) {
                cuerpoFuncion.add("li.s $f11, " + expresion.getValue());
                pushToStack(expresion.getValue(), expresion.getType(), "$f11", getIndexInFunctionScope(identifierName) * 4);
            } else {
                System.out.println(expresion);
                String register = getItemInfoFromStack(expresion);
                pushToStack(expresion.getValue(), expresion.getType(), register, getIndexInFunctionScope(identifierName) * 4);
            }
        }
    }

    public static void addToFunctionScope(String name, String type) {
        functionScope.put(name, type);
    }

    public static String getItemInfoFromStack(SymbolInfo expresion) {
        // esta funcion es para obtener el valor de un identifier del stack, se almacena en un registro el cual se retorna
        String itemName = expresion.getName();
        String register = getRegister(expresion);
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

    public static String getRegister(SymbolInfo expresion) {
        String register = "";
        if (basicTypes.contains(expresion.getName()) && expresion.getType().equals("float")) {
            for (int i =0; i < floatRegisters.length; i++) {
                if (floatAvailable[i]) {
                    register = floatRegisters[i];
                    floatAvailable[i] = false;
                    break;
                }
            }
        } else {
            for (int i =0; i < registers.length; i++) {
                if (available[i]) {
                    register = registers[i];
                    available[i] = false;
                    break;
                }
            }

        }

        return register;
    }

    public static int getIndexInFunctionScope(String name) {
        List<String> keys = new ArrayList<>(functionScope.keySet());
        System.out.println(keys);
        System.out.println(functionScope);
        System.out.println(name);
        return keys.indexOf(name);
    }

    public static void cleanRegisters(String dontCleanThis) {
        for (int i = 0; i < registers.length; i++) {
            if (!registers[i].equals(dontCleanThis)) {
                available[i] = true;
            }
        }

        for (int i = 0; i < floatRegisters.length; i++) {
            if (!floatRegisters[i].equals(dontCleanThis)) {
                floatAvailable[i] = true;
            }
        }
    }

    public static void cleanRegister(String cleanThis) {
        for (int i = 0; i < registers.length; i++) {
            if (registers[i].equals(cleanThis)) {
                available[i] = true;
            }
        }

        for (int i = 0; i < floatRegisters.length; i++) {
            if (floatRegisters[i].equals(cleanThis)) {
                floatAvailable[i] = true;
            }
        }
    }

    public static boolean isIdentifier(String name) {
        return !basicTypes.contains(name);
    }

    public static void createOperation(String operation, SymbolInfo operand1, SymbolInfo operand2) {
        if (operand1 != null && operand2 != null) {
            String register1 = "";
            String register2 = "";
            // x = y + 3
            if (isIdentifier(operand1.getName())) {
                register1 = getItemInfoFromStack(operand1);
            } else if (operand1.getValue() != null) {
                register1 = getRegister(operand1);
                if (register1.contains("$f")) {
                    cuerpoFuncion.add("li.s " + register1 + ", " + operand1.getValue());
                } else {
                    cuerpoFuncion.add("li " + register1 + ", " + operand1.getValue());
                }
            } else {
                System.out.println("SKIBIDI ELSEEEE! ----------------- :3 \n");
                if (operations.size() >= 1) {
                    register1 = operations.get(operations.size() - 1).result;
                }
            }

            if (isIdentifier(operand2.getName())) {
                register2 = getItemInfoFromStack(operand2);
            } else if (operand2.getValue() != null) {
                register2 = getRegister(operand2);
                if (register2.contains("$f")) {
                    cuerpoFuncion.add("li.s " + register2 + ", " + operand2.getValue());
                } else {
                    cuerpoFuncion.add("li " + register2 + ", " + operand2.getValue());
                }
            } else {
                System.out.println("SKIBIDI ELSEEEE! ----------------- :3 \n");
                if (operations.size() >= 1) {
                    register2 = operations.get(operations.size() - 1).result;
                }
            }

            String result = getRegister(operand1);
            cleanRegister(register1);
            cleanRegister(register2);

            Operations newOperation = new Operations(operation, register1, register2, result);
            operations.add(newOperation);

            // Operations
            operate(newOperation);

            // LIMPIAR OPERATIONS CUANDO YA SE HAYA TERMINADO TODA LA EXPRESION !!!! PUEDE SER CUANDO TERMINA CREACION ASIGNACION
        }
    }

    public static void operate(Operations operation) {
        String result = operation.result;
        String operand1 = operation.operand1;
        String operand2 = operation.operand2;
        String operationType = operation.operation;

        if (result.contains("$t")) {
            if (operationType.equals("+")) {
                cuerpoFuncion.add("add " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("-")) {
                cuerpoFuncion.add("sub " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("*")) {
                cuerpoFuncion.add("mul " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("/")) {
                cuerpoFuncion.add("div " + result + ", " + operand1 + ", " + operand2);
            }
        } else {
            if (operationType.equals("+")) {
                cuerpoFuncion.add("add.s " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("-")) {
                cuerpoFuncion.add("sub.s " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("*")) {
                cuerpoFuncion.add("mul.s " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("/")) {
                cuerpoFuncion.add("div.s " + result + ", " + operand1 + ", " + operand2);
            }
        }
    }
}