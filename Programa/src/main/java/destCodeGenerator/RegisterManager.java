package destCodeGenerator;

public class RegisterManager extends CodeGenerator{


    // Asignar un registro a una variable
    public static String allocateRegister(String variable, boolean isFloat) {
        // Verificar si ya está en un registro
        if (variableToRegister.containsKey(variable)) {
            return variableToRegister.get(variable);
        }

        String[] regSet = isFloat ? floatRegisters : registers;
        boolean[] availableSet = isFloat ? floatAvailable : available;

        // Buscar un registro disponible
        for (int i = 0; i < regSet.length; i++) {
            if (availableSet[i]) {
                availableSet[i] = false;
                variableToRegister.put(variable, regSet[i]);
                return regSet[i];
            }
        }

        // Si no hay registros disponibles, hacer spill al stack
        spillVariable(variable, isFloat);
        return null; // Registro no asignado (spill usado)
    }

    // Liberar un registro
    public static void freeRegister(String variable, boolean isFloat) {
        if (variableToRegister.containsKey(variable)) {
            String reg = variableToRegister.remove(variable);
            if (isFloat) {
                for (int i = 0; i < floatRegisters.length; i++) {
                    if (floatRegisters[i].equals(reg)) {
                        floatAvailable[i] = true;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < registers.length; i++) {
                    if (registers[i].equals(reg)) {
                        available[i] = true;
                        break;
                    }
                }
            }
        }
    }

    // Spill una variable al stack
    private static void spillVariable(String variable, boolean isFloat) {
        String reg = variableToRegister.remove(variable);
        if (reg != null) {
            // Guardar en el stack
            String instruction = isFloat ? "swc1 " : "sw ";
            CodeGenerator.text.add(instruction + " " + reg + ", -" + spillStack.size() * 4 + "($sp)");
            spillStack.push(variable);
            freeRegister(variable, isFloat);
        }
    }

    // Cargar una variable desde el stack si fue "spilleada"
    public static String loadFromStack(String variable, boolean isFloat) {
        int position = spillStack.search(variable);
        if (position == -1) {
            System.err.println("Variable " + variable + " no está en el stack.");
        }
        String reg = allocateRegister(variable, isFloat);
        if (reg != null) {
            String instruction = isFloat ? "lwc1 " : "lw ";
            CodeGenerator.text.add(instruction + " " + reg + ", -" + (position * 4) + "($sp)");
        }
        return reg;
    }

}