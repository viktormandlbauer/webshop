package at.fhtw.webshop.exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(int userId) {
        super("User with ID " + userId + " not found.");
    }
}
