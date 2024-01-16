package socketTest.socketTestspring.exception.myExceptions.classes;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message) {
        super(message);
    }
}