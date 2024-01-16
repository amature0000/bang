package socketTest.socketTestspring.exception.myExceptions.classes;

public class JwtNotFoundException extends RuntimeException {
    public JwtNotFoundException(String message) {
        super(message);
    }
}