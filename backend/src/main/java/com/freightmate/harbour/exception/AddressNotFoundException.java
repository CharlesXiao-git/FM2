package com.freightmate.harbour.exception;

public class AddressNotFoundException extends BadRequestException {
    public AddressNotFoundException() { super(); }

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
