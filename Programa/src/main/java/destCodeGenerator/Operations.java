package destCodeGenerator;

/**
 * Clase que sirve como estructura a una operacion
 */
public class Operations {
    public String operation; // la operacion como tal (+, -, <. &&....)
    public String operand1; // operando uno (registro donde se almacena)
    public String operand2; // operando dos (registro donde se almacena)
    public String result; // resultado (registro donde se almacena)

    /**
     * Constructor que inicializa una operación con sus operandos y resultado.
     *
     * @param operation La operación a realizar (ejemplo: +, -, <, &&, etc.).
     * @param operand1  El primer operando de la operación.
     * @param operand2  El segundo operando de la operación.
     * @param result    El resultado de la operación, almacenado en un registro.
     */
    public Operations(String operation, String operand1, String operand2, String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }
}
