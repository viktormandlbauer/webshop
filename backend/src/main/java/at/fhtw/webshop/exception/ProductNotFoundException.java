package at.fhtw.webshop.exception;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(int productId) {
    super("Product with ID " + productId + " not found.");
  }
}
