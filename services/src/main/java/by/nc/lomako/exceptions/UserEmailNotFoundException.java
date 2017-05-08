/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.exceptions;

/**
 * @author Lomako
 * @version 1.0
 */
public class UserEmailNotFoundException extends Exception {
    public UserEmailNotFoundException() {
    }

    public UserEmailNotFoundException(String message) {
        super(message);
    }

    public UserEmailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserEmailNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
