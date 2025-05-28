package at.fhtw.webshop.exception;

public class UserAlreadyReviewedException extends RuntimeException {
    public UserAlreadyReviewedException(int userId, int productId) {
        super("User " + userId + " has already been reviewed for product " + productId);
    }
}
