package exceptions;

public class CantAcceptNaNsException extends NumberFormatException {
    public CantAcceptNaNsException(String s) {
        super(s);
    }
    
}
