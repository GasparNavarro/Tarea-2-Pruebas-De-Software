package com.biblioteca.excepciones;

public class LibroYaDisponibleException extends RuntimeException {
    public LibroYaDisponibleException(String mensaje) {
        super(mensaje);
    }
}
