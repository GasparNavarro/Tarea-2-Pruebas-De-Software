package com.biblioteca;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

public class Biblioteca {
    
    private Map<String, Libro> catalogo;

    public Biblioteca() {
        this.catalogo = new HashMap<>();
    }

    public void registrarLibro(Libro libro) {
        validarLibro(libro);
        catalogo.put(libro.getIsbn(), libro);
    }

    private void validarLibro(Libro libro) {
        if (libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede ser nulo o vacío");
        }
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        if (catalogo.containsKey(libro.getIsbn())) {
            throw new com.biblioteca.excepciones.LibroDuplicadoException("Ya existe un libro con el ISBN " + libro.getIsbn());
        }
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return Optional.ofNullable(catalogo.get(isbn));
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String tituloBuscado = titulo.toLowerCase().trim();
        return catalogo.values().stream()
                .filter(libro -> libro.getTitulo().toLowerCase().contains(tituloBuscado))
                .toList();
    }

    public List<Libro> listarDisponibles() {
        return catalogo.values().stream()
                .filter(Libro::isDisponible)
                .toList();
    }

    public void prestarLibro(String isbn) {
        Libro libro = catalogo.get(isbn);
        if (libro == null) {
            throw new com.biblioteca.excepciones.LibroNoEncontradoException("No se encontró el libro con ISBN: " + isbn);
        }
        if (!libro.isDisponible()) {
            throw new com.biblioteca.excepciones.LibroNoDisponibleException("El libro ya se encuentra prestado.");
        }
        libro.setDisponible(false);
    }

    public void devolverLibro(String isbn) {
        Libro libro = catalogo.get(isbn);
        if (libro == null) {
            throw new com.biblioteca.excepciones.LibroNoEncontradoException("No se encontró el libro con ISBN: " + isbn);
        }
        if (libro.isDisponible()) {
            throw new com.biblioteca.excepciones.LibroYaDisponibleException("El libro ya está disponible.");
        }
        libro.setDisponible(true);
    }

    // --- MÉTODOS DEL DESAFÍO OPCIONAL ---

    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, List<Libro>> prestamosPorUsuario = new HashMap<>();

    public void registrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        prestamosPorUsuario.put(usuario.getId(), new java.util.ArrayList<>());
    }

    public void prestarLibroAUsuario(String isbn, String idUsuario) {
        if (!usuarios.containsKey(idUsuario)) {
            throw new com.biblioteca.excepciones.UsuarioNoEncontradoException("Usuario no existe: " + idUsuario);
        }

        List<Libro> librosDelUsuario = prestamosPorUsuario.get(idUsuario);
        if (librosDelUsuario.size() >= 3) {
            throw new IllegalStateException("El usuario ya tiene el máximo de 3 libros prestados.");
        }

        // Reutilizamos la lógica principal de préstamo para cambiar el estado del libro
        prestarLibro(isbn);

        // Asociamos el libro al usuario
        Libro libro = catalogo.get(isbn);
        librosDelUsuario.add(libro);
    }

    public List<Libro> listarLibrosDeUsuario(String idUsuario) {
        if (!usuarios.containsKey(idUsuario)) {
            throw new com.biblioteca.excepciones.UsuarioNoEncontradoException("Usuario no existe: " + idUsuario);
        }
        // Retornamos una copia para evitar modificaciones externas
        return new java.util.ArrayList<>(prestamosPorUsuario.get(idUsuario));
    }
}
