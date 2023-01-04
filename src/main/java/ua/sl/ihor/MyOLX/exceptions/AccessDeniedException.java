package ua.sl.ihor.MyOLX.exceptions;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
