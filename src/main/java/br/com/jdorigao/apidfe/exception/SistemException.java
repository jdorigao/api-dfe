package br.com.jdorigao.apidfe.exception;

public class SistemException extends RuntimeException {
    public SistemException(String message) {
        super(message);
    }

    public SistemException(String message, Throwable cause) {
        super(message, cause);
    }
}
