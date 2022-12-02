package exceptions;

public class ManagerSaveException extends RuntimeException {
    //private final String message;

    public ManagerSaveException(final String message) {
        super(message);
        //this.message = message;
    }

}

