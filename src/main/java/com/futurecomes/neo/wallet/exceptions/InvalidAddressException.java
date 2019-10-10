package com.futurecomes.neo.wallet.exceptions;

public class InvalidAddressException extends RuntimeException {
    public InvalidAddressException() {
    }

    public InvalidAddressException(String s) {
        super(s);
    }

    public InvalidAddressException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidAddressException(Throwable throwable) {
        super(throwable);
    }

    public InvalidAddressException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
