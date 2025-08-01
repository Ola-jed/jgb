package io.github.olajed.jgb.number;

/**
 * Exception thrown to indicate errors related to numeric operations
 * within the algebraic framework.
 */
public class NumericException extends RuntimeException {
    /**
     * Constructs a new {@code NumericException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public NumericException(String message) {
        super(message);
    }
}
