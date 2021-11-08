package util.exception;

public class GuestEmailExistException extends Exception {
    
    public GuestEmailExistException() {
    }

    public GuestEmailExistException(String msg) {
        super(msg);
    }
}
