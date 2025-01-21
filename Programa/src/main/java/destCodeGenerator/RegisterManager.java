package destCodeGenerator;

public class RegisterManager extends CodeGenerator{


    // Asignar un registro a una variable
    public static String allocateRegister(String variable) {
        // Verificar si ya está en un registro
        if (variableToRegister.containsKey(variable)) {
            return variableToRegister.get(variable);
        }

        // Buscar un registro disponible
        for (int i = 0; i < registers.length; i++) {
            if (available[i]) {
                available[i] = false;
                variableToRegister.put(variable, registers[i]);
                return registers[i];
            }
        }

        // Si no hay registros disponibles, hacer spill al stack
        spillVariable(variable);
        return null; // Registro no asignado (spill usado)
    }

    // Liberar un registro
    public static void freeRegister(String variable) {
        if (variableToRegister.containsKey(variable)) {
            String reg = variableToRegister.remove(variable);
            for (int i = 0; i < registers.length; i++) {
                if (registers[i].equals(reg)) {
                    available[i] = true;
                    break;
                }
            }
        }
    }

    // Spill una variable al stack
    private static void spillVariable(String variable) {
        String reg = variableToRegister.remove(variable);
        if (reg != null) {
            // Guardar en el stack
            spillStack.push(variable);
            CodeGenerator.text.add("sw " + reg + ", -" + spillStack.size() * 4 + "($sp)");
            freeRegister(variable);
        }
    }

    // Cargar una variable desde el stack si fue "spilleada"
    public static String loadFromStack(String variable) {
        int position = spillStack.search(variable);
        if (position == -1) {
            throw new IllegalStateException("Variable " + variable + " no está en el stack.");
        }
        String reg = allocateRegister(variable);
        if (reg != null) {
            CodeGenerator.text.add("lw " + reg + ", -" + (position * 4) + "($sp)");
        }
        return reg;
    }

}