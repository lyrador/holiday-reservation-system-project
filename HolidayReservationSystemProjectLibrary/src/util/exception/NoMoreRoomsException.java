package util.exception;

public class NoMoreRoomsException extends Exception {
    
    public NoMoreRoomsException() {
    }

    public NoMoreRoomsException(String msg) {
        super(msg);
    }
}
