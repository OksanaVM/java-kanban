package exceptions;

public class ManagerSaveException extends RuntimeException {

    private Exception exception;

    public ManagerSaveException(final String message) {
        super(message);
    }

    public ManagerSaveException(final String message, final Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

}

