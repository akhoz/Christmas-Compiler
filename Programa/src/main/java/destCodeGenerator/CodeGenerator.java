package destCodeGenerator;

import jdk.dynalink.Operation;
import tables.SymbolInfo;
import java.io.FileWriter;
import java.io.IOException;
import destCodeGenerator.Operations;

import java.util.*;

/**
 * Clase que proporciona una traducción a código MIPS desde un archivo fuente dado.
 */
public class CodeGenerator {

    public static List<String> data; // contiene todo lo que va en data
    public static List<String> text; // contiene todo lo que va en text
    public static String funcName; // nombre de la funcion actual
    public static List<String> encabezadoFuncion; // encabezado de la funcion actual (etiqueta y resta al sp)
    public static List<String> cuerpoFuncion; // el resto del cuerpo de la funcion
    public static List<String> cuerpoFinal; // cuerpo final de la funcion (contiene el jr ra y otros)
    public static LinkedHashMap<String, String> functionScope; // toda las variables del scope de funcion actual
    public static LinkedHashMap<String, String> functionParams; // todos los parametros de la func actual
    public static List<String> basicTypes = Arrays.asList("int", "char", "boolean", "string", "float"); // lista de tipos basicos

    public static final String[] registers = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8"}; // registros temporales
    public static final boolean[] available = new boolean[registers.length]; // lista de disponibilidad de los registros temporales
    public static final String[] floatRegisters = {
            "$f0", "$f1", "$f2", "$f3", "$f4", "$f5", "$f6", "$f7", "$f8", "$f9", "$f10", "$f11", "$f12" // registros flotantes
    };
    public static final boolean[] floatAvailable = new boolean[floatRegisters.length]; // disponibilidad de los registros flotantes
    public static List<Operations> operations = new ArrayList<>(); // lista de operaciones efectuadas
    public static int labelCounter = 0; // contador de etiquetas
    public static int structuresCounter = 0; // contador de ifs
    public static int whileCounter = 0; // contador de whiles


    /**
     * Constructor, inicializa todas las listas y le pone código inicial a la traducción de MIPS.
     */
    public CodeGenerator() {
        data = new ArrayList<>();
        text = new ArrayList<>();
        cuerpoFinal = new ArrayList<>();

        // Inicializar los segmentos de datos y texto
        data.add(".data");
        data.add("newLine: .asciiz \"\\n\"");
        text.add(".text");

        // Inicializar el segmento de texto con la dirección de retorno
        text.add(".globl main");
        text.add("main:");
        text.add("j global");

        Arrays.fill(available, true);
        Arrays.fill(floatAvailable, true);
    }


    /**
     * Agrega un string a la sección de .data.
     *.
     * @param name     Nombre de la variable a agregar.
     * @param value    Valor del string
     */
    private static void addStringToDataSegment(String name, String value) {
        data.add(name + ": .asciiz \"" + value + "\"");
    }

    /**
     * Crea una nueva funcion, traduce su etiqueta y codigo inciial, limpia todas las listas para mantener un nuevo scope.
     *.
     * @param functionName    Nombre de la funcion a agregar.
     * @param parameters      Lista de parametros de la funcion
     */
    public static void createFunction(String functionName, List<SymbolInfo> parameters) {
        functionScope = new LinkedHashMap<>(); // Cambiado a LinkedHashMap
        encabezadoFuncion = new ArrayList<>();
        cuerpoFuncion = new ArrayList<>();
        functionParams = new LinkedHashMap<>();

        functionName = functionName.replace("_", "");
        funcName = functionName;
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


    /**
     * Agrega un valor simple (literal o identifier) a otro identifier, se encarga de verificar esto y si es flotante. Llama a pushToStack para realizar la asignacion.
     *.
     * @param identifierName     Nombre de la variable a la cual se le va a asignar un valor.
     * @param expresion          Expresion a agregar
     */
    public static void assignValueToIdentifier(String identifierName, SymbolInfo expresion) {
        // En caso de ser literal, se pushea directamente
        if (expresion.getValue() != null) {
            if (basicTypes.contains(expresion.getName()) && (expresion.getName().equals("int") || expresion.getValue().equals("char"))) {
                cuerpoFuncion.add("li $s7, " + expresion.getValue()); // si es int o char lo agrega sin mas
                pushToStack(expresion.getValue(), expresion.getType(), "$s7", getIndexInFunctionScope(identifierName) * 4);
            } else if (basicTypes.contains(expresion.getName()) && expresion.getValue().equals("float")) {
                cuerpoFuncion.add("li.s $f11, " + expresion.getValue()); // si es float lo agrega sin mas pero en los registros float
                pushToStack(expresion.getValue(), expresion.getType(), "$f11", getIndexInFunctionScope(identifierName) * 4);
            } else {
                System.out.println(expresion);
                String register = getItemInfoFromStack(expresion); // si es identifier lo busca en la pila y luego lo pushea en otra posicion
                pushToStack(expresion.getValue(), expresion.getType(), register, getIndexInFunctionScope(identifierName) * 4);
            }
        }
    }

    /**
     * Agrega un valor compuesto (como suma o asi) a una variable. El resultado de esa operacion ya va a estar en operations por lo tanto solo obtiene ese registro y lo pushea en la posicion deseada.
     *.
     * @param identifierName     Nombre de la variable a la cual se le va a asignar un valor.
     */
    public static void assignStmtValueToIdentifier(String identifierName) {
        // System.out.println("\n-----------------BBBBBBBBBBBBBBBBBB-----\n");
        if (!operations.isEmpty()) {

            int index = getIndexInFunctionScope(identifierName) * 4; // obtiene la posicion de la variable a la cual se le va a asignar la op
            String register = operations.getLast().result; // el result es el registro donde se almacena el resultado de toda la operacion

            if (register.contains("$t")) {
                cuerpoFuncion.add("sw " + register + ", " + index + "($sp)");
            } else {
                cuerpoFuncion.add("s.s " + register + ", " + index);
            }
        }
    }


    /**
     * Agrega un identifier al function scope.
     *.
     * @param name    Nombre de la variable.
     * @param type    Tipo de la variable.
     */
    public static void addToFunctionScope(String name, String type) {
        functionScope.put(name, type);
    }

    /**
     * Obtiene un valor desde el stack, este valor lo obtiene a partir del nombre del identifier y tipo. Hace operaciones para mover ese valor de la pila a un registro, el cual se retorna
     *.
     * @param expresion     Identifier a obtener (se pasa todo expresion porque tambien se ocupa su tipo).
     * @return              Devuelve el registro en el cual se almacena el valor deseada
     */
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

    /**
     * Cierra una funcion, agrega el codigo final y el salto al ra para que el main vuelva a tomar el control. Agrega instrucciones a encabezadoFuncion y al cuerpo.
     *.
     */
    public static void closeFunction() {
        int difference = Math.max(functionScope.size() - functionParams.size(), 0) * 4;
        encabezadoFuncion.add("subu $sp, $sp, " + difference);
        cuerpoFuncion.add("addu $sp, $sp, " + functionScope.size() * 4);
        if (funcName != "global") {
            int raIndex = getIndexInFunctionScope("ra") * 4;
            popFromStack("$ra", raIndex, "int");
            cuerpoFuncion.add("jr $ra");
        } else {
            cuerpoFuncion.add("j finalCodigo");
        }
        List<String> fullFunction = new ArrayList<>();
        fullFunction.addAll(encabezadoFuncion);
        fullFunction.addAll(cuerpoFuncion);
        text.addAll(fullFunction);
        System.out.println("\n aasaasaasasasasaspaspapaps \n" + text);
        //System.out.println(functionScope);
        //System.out.println(functionParams);
    }

    /**
     *  Pushea un objeto en el stack, toma precauciones con el tipo, ademas recibe el registro en el cual está el valor, su tipo y el indice al cual se va a pushear.
     *.
     * @param item    No se usa para nada, pero mejor no lo quitamos.
     * @param type    Tipo del objeto a pushear.
     * @param register  Registro en el cual se encuentra el objeto a pushear
     * @param index  Indice al cual pushear
     */
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


    /**
     * Hace pop a un elemento especifico del stack, esto en base al indice del sp, requiere su tipo (para mover normal o con .s) y registro en el cual guardar el objeto popeado.
     *.
     * @param register    Registro donde se va a almacenar el objeto
     * @param type          Tipo del objeto.
     * @param index      Indice donde se encuentra el objeto
     */
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

    /**
     * Agrega el código final de la traduccion, tambien genera el archivo .asm en base al nombre del archivo fuente.
     *.
     * @param rutaArchivo    Nombre del archivo fuente, se usa para darle nombre al .asm
     */
    public static void addFinalCode(String rutaArchivo) {
        text.addAll(cuerpoFinal);
        text.add("finalCodigo:");
        text.add("li $v0, 10");
        text.add("syscall");
        System.out.println(data + "\n" + text);

        String outputFileName = "src/tests/" + rutaArchivo + ".asm";

        try (FileWriter writer = new FileWriter(outputFileName)) {
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


    /**
     * Obtiene un registro entre los registros disponibles. Toma en cuenta el tipo para darle un registro t o uno f
     *.
     * @param expresion    Expresion a la cual se le va a asignar un registro
     * @return             Devuelve un string que contiene el registro obtenido
     */
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


    /**
     * Devuelve el indice de un objeto especifico del function scope, en base al nombre.
     *.
     * @param name    Nombre de la variable.
     * @return        Indice de donde se encuentra
     */
    public static int getIndexInFunctionScope(String name) {
        List<String> keys = new ArrayList<>(functionScope.keySet());
        System.out.println(keys);
        System.out.println(functionScope);
        System.out.println(name);
        return keys.indexOf(name);
    }

    /**
     * Limpia todos los registros (f y t) a excepcion del que se le indique por parametro
     *.
     * @param dontCleanThis   No limpia este registro
     */
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

    /**
     * Limpia un registro especifico
     *.
     * @param cleanThis   Limpia este registro
     */
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

    /**
     * Verifica si es un identificador
     *.
     * @param name  Nombre del objeto
     * @return     True si es identificador, false si no
     */
    public static boolean isIdentifier(String name) {
        return !basicTypes.contains(name);
    }

    /**
     * Se encarga de crear un objeto operacion y agregarlo a la lista de operaciones. Obtiene todos los registros necesario para efectuar la operacion
     *.
     * @param operation  Operacion a realizar
     * @param operand1   Primer operando
     * @param operand2   Segundo operando
     * @param  type      Tipo de la operacion (aritmetica, comparacion o logica)
     */
    public static void createOperation(String operation, SymbolInfo operand1, SymbolInfo operand2, String type) {
        if (operand1 != null && operand2 != null) {
            String register1 = "";
            String register2 = "";
            // x = y + 3
            if (isIdentifier(operand1.getName()) && operand1.getName() != null) {
                register1 = getItemInfoFromStack(operand1); // si el operando1 es un identificador, guarda su valor en este registro
            } else if (operand1.getValue() != null) {
                register1 = getRegister(operand1); // si su valor se conoce pero no es un identificador, es un literal, entonces lo agrega directamente
                if (register1.contains("$f")) {
                    cuerpoFuncion.add("li.s " + register1 + ", " + operand1.getValue());
                } else {
                    cuerpoFuncion.add("li " + register1 + ", " + operand1.getValue());
                }
            } else { // si no es identificador y tampoco se conoce su valor, es una expresion compuesta, se obtiene del operations.getlast
                System.out.println("SKIBIDI ELSEEEE! ----------------- :3 \n");
                if (operations.size() >= 1) {
                    register1 = operations.get(operations.size() - 1).result;
                }
            }

            // los mismos comentarios aplican para el operando2
            if (isIdentifier(operand2.getName()) && operand2.getName() != null ) {
                register2 = getItemInfoFromStack(operand2);
                System.out.println(operand2);
                //  System.out.println("pepe");
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
            cleanRegister(register2); // limpia los registros temporales porque el unico que se debe guardar es el del resultado
            // de esa forma se pueden volver a usar esos registros en otra operacion

            Operations newOperation = new Operations(operation, register1, register2, result);
            operations.add(newOperation);

            // Operations
            if (type == "arithmetic") {
                operate(newOperation);
            } else if (type == "comparison") {
                createComparisonOperation(newOperation);
            } else if (type == "logical") {
                if (newOperation.operation.equals("&&")) {
                    andOperation(newOperation);
                } else if (newOperation.operation.equals("||")) {
                    orOperation(newOperation);
                } else if (newOperation.operation.equals("!")) {
                    notOperation(newOperation);
                }
            }

            // LIMPIAR OPERATIONS CUANDO YA SE HAYA TERMINADO TODA LA EXPRESION !!!! PUEDE SER CUANDO TERMINA CREACION ASIGNACION
        }
    }


    /**
     * Realiza una operacion aritmetica en base a una operacion dada
     *.
     * @param operation Objeto operation que contiene la operacion, los dos registros de los operandos y el registro donde se guarda el resultado
     */
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
                cuerpoFuncion.add("div " + operand1 + ", " + operand2);
                cuerpoFuncion.add("lw lo, " + result);
            } else if (operationType.equals("%")) {
                cuerpoFuncion.add("div " + operand1 + ", " + operand2);
                cuerpoFuncion.add("lw hi, " + result);
            }
        } else {
            if (operationType.equals("+")) {
                cuerpoFuncion.add("add.s " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("-")) {
                cuerpoFuncion.add("sub.s " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("*")) {
                cuerpoFuncion.add("mul.s " + result + ", " + operand1 + ", " + operand2);
            } else if (operationType.equals("/")) {
                cuerpoFuncion.add("div.s " + operand1 + ", " + operand2);
                cuerpoFuncion.add("lw lo, " + result);
            } else if (operationType.equals("%")) {
                cuerpoFuncion.add("div.s " + operand1 + ", " + operand2);
                cuerpoFuncion.add("lw hi, " + result);
            }
        }
    }

    /**
     * Realiza una operacion comparacion en base a una operacion dada
     *.
     * @param operation Objeto operation que contiene la operacion, los dos registros de los operandos y el registro donde se guarda el resultado
     */
    public static void createComparisonOperation(Operations operation) {
        String result = operation.result;
        String operand1 = operation.operand1;
        String operand2 = operation.operand2;
        String operationType = operation.operation;

        if (operationType.equals("==")) {
            cuerpoFuncion.add("beq " + operand1 + ", " + operand2 + ", setTrue" + labelCounter);
        } else if (operationType.equals("!=")) {
            cuerpoFuncion.add("bne " + operand1 + ", " + operand2 + ", setTrue" + labelCounter);
        } else if (operationType.equals(">")) {
            cuerpoFuncion.add("bgt " + operand1 + ", " + operand2 + ", setTrue" + labelCounter);
        } else if (operationType.equals(">=")) {
            cuerpoFuncion.add("bge " + operand1 + ", " + operand2 + ", setTrue" + labelCounter);
        } else if (operationType.equals("<")) {
            cuerpoFuncion.add("blt " + operand1 + ", " + operand2 + ", setTrue" + labelCounter);
        } else if (operationType.equals("<=")) {
            cuerpoFuncion.add("ble " + operand1 + ", " + operand2 + ", setTrue" + labelCounter);
        }

        cuerpoFuncion.add("li " + result + ", " + 0); // Caso false
        cuerpoFuncion.add("comparisonEnd" + labelCounter + ":");
        cuerpoFinal.add("setTrue" + labelCounter + ":");
        cuerpoFinal.add("li " + result + ", " + 1 ); // Caso true
        cuerpoFinal.add("j comparisonEnd" + labelCounter);

        labelCounter++;
    }

    /**
     * Realiza una operacion logica (AND) en base a una operacion dada, usa saltos y por lo tanto se trata en dos funciones
     *.
     * @param operation Objeto operation que contiene la operacion, los dos registros de los operandos y el registro donde se guarda el resultado, solo se usan los operandos
     */
    public static void andOperation(Operations operation) {
        String operand1 = operation.operand1;
        String operand2 = operation.operand2;

        cuerpoFuncion.add("beq " + operand1 + ", " + operand2 + ", setAndFalse" + labelCounter);
    }

    /**
     * Agrega el codigo final a una operacion AND (los saltos para retomar el flujo y el salto de la condicion)
     *.
     */
    public static void andOperationFinalCode() {
        String result = operations.getLast().result;
        cuerpoFuncion.add("li " + result + ", " + 1); // setear true si ninguno era false
        cuerpoFuncion.add("logicalEnd" + labelCounter + ":");

        cuerpoFinal.add("setAndFalse" + labelCounter + ":");
        cuerpoFinal.add("li " + result + ", " + 0); // setear false
        cuerpoFinal.add("j logicalEnd" + labelCounter);
        labelCounter++;
    }

    /**
     * Realiza una operacion logica (OR) en base a una operacion dada, usa saltos y por lo tanto se trata en dos funciones
     *.
     * @param operation Objeto operation que contiene la operacion, los dos registros de los operandos y el registro donde se guarda el resultado, solo se usan los operandos
     */
    public static void orOperation(Operations operation) {
        String operand1 = operation.operand1;
        String operand2 = operation.operand2;

        cuerpoFuncion.add("beq " + operand1 + ", " + operand2 + ", setOrTrue" + labelCounter);
    }

    /**
     * Agrega el codigo final a una operacion OR (los saltos para retomar el flujo y el salto de la condicion)
     *.
     */
    public static void orOperationFinalCode() {
        String result = operations.getLast().result;
        cuerpoFuncion.add("li " + result + ", " + 0); // setear false si ninguno es true
        cuerpoFuncion.add("logicalEnd" + labelCounter + ":");

        cuerpoFinal.add("setOrTrue" + labelCounter + ":");
        cuerpoFinal.add("li " + result + ", " + 1); // setear true
        cuerpoFinal.add("j logicalEnd" + labelCounter);
        labelCounter++;
    }

    /**
     * Realiza una operacion logica (NOT) en base a una operacion dada, usa saltos y por lo tanto se trata en dos funciones
     *.
     * @param operation Objeto operation que contiene la operacion, los dos registros de los operandos y el registro donde se guarda el resultado, solo se usan los operandos. El segundo operando siempre es 0
     */
    public static void notOperation(Operations operation) {
        String operand1 = operation.operand1;
        String operand2 = operation.operand2;

        cuerpoFuncion.add("beq " + operand1 + ", " + operand2 + ", setNotTrue" + labelCounter);
    }


    /**
     * Agrega el codigo final a una operacion NOT (los saltos para retomar el flujo y el salto de la condicion)
     *.
     */
    public static void notOperationFinalCode() {
        String result = operations.getLast().result;
        cuerpoFuncion.add("li " + result + ", " + 0); // setear false si era true
        cuerpoFuncion.add("logicalEnd" + labelCounter + ":");

        cuerpoFinal.add("setNotTrue" + labelCounter + ":");
        cuerpoFinal.add("li " + result + ", " + 1); // setear true si era false
        cuerpoFinal.add("j logicalEnd" + labelCounter);
        labelCounter++;
    }

    /*

    bool x = !false
    bool y = !x


    beq x 0 ponerTrue
    x = 0
    finalNot:

    ponerTrue
    x = 1
    j finalNot
     */

    /**
     *  Escribe el codigo necesario para llamar a una funcion, abre espacio para sus parametros
     *.
     * @param functionName  FUncion a llamar
     * @param paramsQuantity   Cantidad de parametros de la funcion
     */
    public static void callFunction(String functionName, int paramsQuantity) {
        cuerpoFuncion.add("subu $sp, $sp, " + (paramsQuantity * 4));
        functionName = functionName.replace("_", "");
        cuerpoFuncion.add("jal " + functionName);
    }

    /**
     *  Funcion que sirve como puente para un print, en base a la expresion, llama a una funcion especifica para imprimir el valor
     *.
     * @param expression   Expresion a imprimir
     */
    public static void printExpression(SymbolInfo expression) {
        if (expression != null) {
            if (expression.getType() == "int") {
                printInt(expression);
            } else if (expression.getType() == "float") {
                printFloat(expression);
            } else if (expression.getType() == "string") {
                printString(expression);
            }
        }
    }

    /**
     *  Funcion que imprime un float en MIPS
     *.
     * @param expression   Expresion a imprimir
     */
    public static void printFloat(SymbolInfo expression) {
        if (expression != null) {
            if (expression.getSingleObject() && expression.getName() != "float") {
                String register = getItemInfoFromStack(expression); // si es un identificador lo busca en el stack
                cuerpoFuncion.add("mov.s $f12, " + register);
            } else if (!operations.isEmpty()) {
                String register = operations.getLast().result; // si es una expresion compuesta lo obtiene del result del getlast
                cuerpoFuncion.add("mov.s $f12, " + register);
            } else if (expression.getName() == "float") {
                cuerpoFuncion.add("li.s $f12," + expression.getValue()); // si es un literal lo pasa directamente
            }
            cuerpoFuncion.add("li $v0, 2");
            cuerpoFuncion.add("syscall");
        }
    }

    /**
     *  Funcion que imprime un int en MIPS
     *.
     * @param expression   Expresion a imprimir
     */
    public static void printInt(SymbolInfo expression) {
        if (expression != null) {
             if (expression.getSingleObject() && expression.getName() != "int") {
                String register = getItemInfoFromStack(expression);
                cuerpoFuncion.add("move $a0, " + register);
            } else if (!operations.isEmpty()) {
                String register = operations.getLast().result;
                cuerpoFuncion.add("move $a0, " + register);
            } else if (expression.getName() == "int") {
                cuerpoFuncion.add("li $a0, " + expression.getValue());
            }
            cuerpoFuncion.add("li $v0, 1");
            cuerpoFuncion.add("syscall");
        }

    }

    /**
     *  Funcion que imprime un string en MIPS
     *.
     * @param expression   Expresion a imprimir
     */
    public static void printString(SymbolInfo expression) {
        if (expression != null) {
            if (expression.getName() == "newLine") { // si es un newLine usa una variable que tenemos declarada arriba
                cuerpoFuncion.add("li $v0, 4");
                cuerpoFuncion.add("la $a0, newLine");
                cuerpoFuncion.add("syscall");
            } else if (expression.getValue() instanceof String) {
                String name = expression.getName() + labelCounter;
                String value = expression.getValue().toString();
                addStringToDataSegment(name, value);
                cuerpoFuncion.add("la $a0," + name);
                cuerpoFuncion.add("li $v0, 4");
                cuerpoFuncion.add("syscall");
                labelCounter++;
            }
        }
    }

    /**
     *  Funcion que sirve como puente a una comparacion, verifica si se trata de un if o while para llamar a sus respectivas funciones
     *.
     * @param expression   Expresion a comparar
     * @param condition    Condicion con la que se compara
     * @param  structure   if o while
     */
    public static void compareCondition(SymbolInfo expression, SymbolInfo condition, String structure) {
        if (structure.equals("if")) {
            ifCondition(expression, condition);
            return;
        } else {
            whileCondition(expression, condition);
            return;
        }
    }

    /**
     *  Se encarga de escribir una condicion de un if, en base a una expresion y una condicion a cumplir
     *.
     * @param expression   Expresion a comparar
     * @param condition    Condicion con la que se compara
     */
    public static void ifCondition(SymbolInfo expression, SymbolInfo condition) {
        // la condicion es el symbol info que tiene el 0, para que compare si es falso
        if (expression != null) {
            String register = "";
            String register2 = getRegister(condition); // registro para guardar el 0

            ++structuresCounter;
            cuerpoFuncion.add("nextCondition" + structuresCounter + ":");
            cuerpoFuncion.add("li " + register2 + " 0");

            if (expression.getSingleObject() && !basicTypes.contains(expression.getName())) {
                register = getItemInfoFromStack(expression);
            } else if (!operations.isEmpty()) {
                register = operations.getLast().result;
            } else if (basicTypes.contains(expression.getName())) {
                register = getRegister(expression);
                // solo hace li ya que esto se hace unicamente con booleanos (0,1) no tiene sentido hacer el caso de floats
                cuerpoFuncion.add("li " + register + " " + expression.getValue());
            }

            cuerpoFuncion.add("beq " + register + ", " + register2 + ", nextCondition" + (structuresCounter + 1));
            structuresCounter++;
            cleanRegisters("");
        }
    }

    /**
     *  Agega etiquetas finales al final de un if
     *.
     */
    public static void ifEnd() {
        cuerpoFuncion.add("nextCondition" + structuresCounter + ":");
        cuerpoFuncion.add("ifEnd" + structuresCounter + ":");

    }

    /**
     *  Agega etiquetas finales al final de un bloque if
     *.
     */
    public static void gotoIfEnd() {
        cuerpoFuncion.add("j ifEnd" + structuresCounter);
    }

    /**
     *  Limpia todo operations
     *.
     */
    public static void cleanOperations() {
        operations.clear();
    }

    /**
     *  Se encarga de escribir una condicion de un while, en base a una expresion y una condicion a cumplir
     *.
     * @param expression   Expresion a comparar
     * @param condition    Condicion con la que se compara
     */
    public static void whileCondition(SymbolInfo expression, SymbolInfo condition) {
        if (expression != null) {
            String register = "";
            String register2 = getRegister(condition);

            cuerpoFuncion.add("whileLoopCondition" + whileCounter + ":");
            cuerpoFuncion.add("li " + register2 + " 0");

            if (expression.getSingleObject() && !basicTypes.contains(expression.getName())) {
                register = getItemInfoFromStack(expression);
            } else if (!operations.isEmpty()) {
                register = operations.getLast().result;
            } else if (basicTypes.contains(expression.getName())) {
                register = getRegister(expression);
                // solo hace li ya que esto se hace unicamente con booleanos (0,1) no tiene sentido hacer el caso de floats
                cuerpoFuncion.add("li " + register + " " + expression.getValue());
            }

            cuerpoFuncion.add("beq " + register + ", " + register2 + ", whileEnd" + whileCounter);
            cleanRegisters("");
        }
    }

    /**
     *  Agega etiquetas finales al final de un while
     *.
     */
    public static void whileLoopEnd() {
        cuerpoFuncion.add("j whileLoopCondition" + whileCounter);
        cuerpoFuncion.add("whileEnd" + whileCounter + ":");
        whileCounter++;
    }
}