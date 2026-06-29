package com.biblioteca.excepciones;

public class LibroDuplicadoException extends RuntimeException {
    public LibroDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
