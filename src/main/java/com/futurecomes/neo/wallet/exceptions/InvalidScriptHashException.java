package com.futurecomes.neo.wallet.exceptions;

public class InvalidScriptHashException extends RuntimeException {
    public InvalidScriptHashException() {
    }

    public InvalidScriptHashException(String s) {
        super(s);
    }

    public InvalidScriptHashException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidScriptHashException(Throwable throwable) {
        super(throwable);
    }

    public InvalidScriptHashException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
