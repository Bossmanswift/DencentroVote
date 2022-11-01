package com.metrostate.edu.decentrovote.exceptions;

public class TieException extends RuntimeException {
    public TieException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public TieException (Throwable throwable) {
        super(throwable);
    }
    public TieException(String errorMessage) {
        super(errorMessage);
    }
}
