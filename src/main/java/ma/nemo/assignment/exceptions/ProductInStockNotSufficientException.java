package ma.nemo.assignment.exceptions;

public class ProductInStockNotSufficientException extends Exception{
    private static final long serialVersionUID = 1L;

    public ProductInStockNotSufficientException() {
    }

    public ProductInStockNotSufficientException(String message) {
        super(message);
    }
}
