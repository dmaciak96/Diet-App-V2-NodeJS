package pl.daveproject.dietapp.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User not found by email %s".formatted(email));
    }
}
