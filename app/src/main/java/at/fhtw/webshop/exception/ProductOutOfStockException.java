package at.fhtw.webshop.exception;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String message) {
        super(message);
    }

    public ProductOutOfStockException(int currentStock, int quantity) {
        super("Requested quantity " + quantity + " exceeds current stock of " + currentStock + ".");
    }
}
