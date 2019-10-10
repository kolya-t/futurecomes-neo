package com.futurecomes.neo.wallet.exceptions;

public class InvalidPrivateKeyException extends RuntimeException {
    public InvalidPrivateKeyException() {
    }

    public InvalidPrivateKeyException(String s) {
        super(s);
    }

    public InvalidPrivateKeyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidPrivateKeyException(Throwable throwable) {
        super(throwable);
    }

    public InvalidPrivateKeyException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
