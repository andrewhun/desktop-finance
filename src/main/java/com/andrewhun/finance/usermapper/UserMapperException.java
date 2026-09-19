package com.andrewhun.finance.usermapper;

/**
 * Signals that a {@link UserMapper} operation failed. Implementations wrap whatever their
 * storage technology raised — a {@code SQLException} from JDBC, say — so that callers never
 * have to name, catch, or declare an exception belonging to a backend they were deliberately
 * kept ignorant of.
 *
 * <p>Unchecked, because a persistence failure is not a condition a caller can retry its way
 * out of; the layer that can turn it into something a user sees catches it explicitly.
 */
public class UserMapperException extends RuntimeException {

    public UserMapperException(String message) {
        super(message);
    }

    public UserMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
