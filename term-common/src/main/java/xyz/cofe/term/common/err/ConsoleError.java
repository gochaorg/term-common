package xyz.cofe.term.common.err;

public class ConsoleError extends Error {
    public ConsoleError(String message) {
        super(message);
    }

    public ConsoleError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoleError(Throwable cause) {
        super(cause);
    }
}
