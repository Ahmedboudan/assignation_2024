package ma.nemo.assignment.exceptions;

public class SupplyValidationException extends Exception{
    private static final long serialVersionUID = 1L;

    public SupplyValidationException() {
    }

    public SupplyValidationException(String message) {
        super(message);
    }
}
