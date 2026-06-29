package com.biblioteca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTest {

    private Biblioteca biblioteca;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
    }

    @Test
    void testRegistrarLibro_DebeGuardarLibroYQuedarDisponible() {
        // Arrange
        Libro libro = new Libro("123-456", "El Señor de los Anillos", "J.R.R. Tolkien", 1954);

        // Act
        biblioteca.registrarLibro(libro);

        // Assert
        Optional<Libro> libroGuardado = biblioteca.buscarPorIsbn("123-456");
        assertTrue(libroGuardado.isPresent(), "El libro debería estar registrado en la biblioteca");
        assertTrue(libroGuardado.get().isDisponible(), "El libro nuevo debería estar disponible por defecto");
    }

    @Test
    void testRegistrarLibro_ConIsbnVacio_DebeLanzarExcepcion() {
        Libro libroInvalido = new Libro("", "Dune", "Frank Herbert", 1965);
        assertThrows(IllegalArgumentException.class, () -> biblioteca.registrarLibro(libroInvalido));
    }

    @Test
    void testRegistrarLibro_ConIsbnNulo_DebeLanzarExcepcion() {
        Libro libroInvalido = new Libro(null, "Dune", "Frank Herbert", 1965);
        assertThrows(IllegalArgumentException.class, () -> biblioteca.registrarLibro(libroInvalido));
    }

    @Test
    void testRegistrarLibro_ConTituloVacioOLlenoDeEspacios_DebeLanzarExcepcion() {
        Libro libroInvalido = new Libro("999-999", "   ", "Isaac Asimov", 1951);
        assertThrows(IllegalArgumentException.class, () -> biblioteca.registrarLibro(libroInvalido));
    }

    @Test
    void testRegistrarLibro_ConIsbnDuplicado_DebeLanzarExcepcion() {
        // Arrange
        Libro libro1 = new Libro("111-222", "Fundación", "Isaac Asimov", 1951);
        Libro libro2 = new Libro("111-222", "Fundación e Imperio", "Isaac Asimov", 1952);
        
        biblioteca.registrarLibro(libro1);

        // Act & Assert
        assertThrows(com.biblioteca.excepciones.LibroDuplicadoException.class, () -> biblioteca.registrarLibro(libro2));
    }

    // --- PRUEBAS DE BÚSQUEDA ---

    @Test
    void testBuscarPorIsbn_LibroExistente_DebeRetornarLibro() {
        Libro libro = new Libro("444-555", "1984", "George Orwell", 1949);
        biblioteca.registrarLibro(libro);

        Optional<Libro> resultado = biblioteca.buscarPorIsbn("444-555");

        assertTrue(resultado.isPresent());
        assertEquals("1984", resultado.get().getTitulo());
    }

    @Test
    void testBuscarPorIsbn_LibroInexistente_DebeRetornarOptionalVacio() {
        Optional<Libro> resultado = biblioteca.buscarPorIsbn("999-000");
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorTitulo_CoincidenciaParcialYCaseInsensitive_DebeRetornarLista() {
        biblioteca.registrarLibro(new Libro("1", "El Hobbit", "J.R.R. Tolkien", 1937));
        biblioteca.registrarLibro(new Libro("2", "Harry Potter", "J.K. Rowling", 1997));
        biblioteca.registrarLibro(new Libro("3", "EL HOMBRE ILUSTRADO", "Ray Bradbury", 1951));

        java.util.List<Libro> resultados = biblioteca.buscarPorTitulo(" el h");

        assertEquals(2, resultados.size(), "Debería encontrar 'El Hobbit' y 'EL HOMBRE ILUSTRADO'");
    }

    @Test
    void testListarDisponibles_SoloDebeRetornarLibrosDisponibles() {
        Libro libro1 = new Libro("101", "Libro 1", "Autor", 2000);
        Libro libro2 = new Libro("102", "Libro 2", "Autor", 2000);
        
        // Simulamos que el libro 2 no está disponible (alguien lo prestó)
        libro2.setDisponible(false);

        biblioteca.registrarLibro(libro1);
        biblioteca.registrarLibro(libro2);

        java.util.List<Libro> disponibles = biblioteca.listarDisponibles();

        assertEquals(1, disponibles.size());
        assertEquals("101", disponibles.get(0).getIsbn());
    }

    // --- PRUEBAS DE PRÉSTAMO ---

    @Test
    void testPrestarLibro_LibroDisponible_DebeQuedarNoDisponible() {
        Libro libro = new Libro("prestamo-1", "Clean Code", "Robert C. Martin", 2008);
        biblioteca.registrarLibro(libro);

        biblioteca.prestarLibro("prestamo-1");

        Optional<Libro> libroPrestado = biblioteca.buscarPorIsbn("prestamo-1");
        assertFalse(libroPrestado.get().isDisponible(), "El libro prestado ya no debe estar disponible");
    }

    @Test
    void testPrestarLibro_LibroInexistente_DebeLanzarExcepcion() {
        assertThrows(com.biblioteca.excepciones.LibroNoEncontradoException.class, () -> biblioteca.prestarLibro("invalido"));
    }

    @Test
    void testPrestarLibro_LibroYaPrestado_DebeLanzarExcepcion() {
        Libro libro = new Libro("prestamo-2", "The Pragmatic Programmer", "Andrew Hunt", 1999);
        biblioteca.registrarLibro(libro);
        
        // Lo prestamos por primera vez (esto debería ser exitoso)
        biblioteca.prestarLibro("prestamo-2");

        // Intentamos prestarlo de nuevo
        assertThrows(com.biblioteca.excepciones.LibroNoDisponibleException.class, () -> biblioteca.prestarLibro("prestamo-2"));
    }

    // --- PRUEBAS DE DEVOLUCIÓN ---

    @Test
    void testDevolverLibro_LibroPrestado_DebeQuedarDisponible() {
        Libro libro = new Libro("dev-1", "Design Patterns", "GoF", 1994);
        biblioteca.registrarLibro(libro);
        biblioteca.prestarLibro("dev-1"); // Lo prestamos primero

        // Act
        biblioteca.devolverLibro("dev-1");

        // Assert
        Optional<Libro> libroDevuelto = biblioteca.buscarPorIsbn("dev-1");
        assertTrue(libroDevuelto.get().isDisponible(), "El libro devuelto debe estar disponible nuevamente");
    }

    @Test
    void testDevolverLibro_LibroInexistente_DebeLanzarExcepcion() {
        assertThrows(com.biblioteca.excepciones.LibroNoEncontradoException.class, () -> biblioteca.devolverLibro("invalido"));
    }

    @Test
    void testDevolverLibro_LibroYaDisponible_DebeLanzarExcepcion() {
        Libro libro = new Libro("dev-2", "Refactoring", "Martin Fowler", 1999);
        biblioteca.registrarLibro(libro);
        
        // El libro recién registrado ya está disponible, devolvemos directo
        assertThrows(com.biblioteca.excepciones.LibroYaDisponibleException.class, () -> biblioteca.devolverLibro("dev-2"));
    }

    // --- PRUEBAS DEL DESAFÍO OPCIONAL (USUARIOS) ---

    @Test
    void testPrestarLibroAUsuario_Exitoso() {
        Usuario usuario = new Usuario("u1", "Juan Perez", "juan@mail.com");
        Libro libro = new Libro("usr-1", "Clean Architecture", "Robert C. Martin", 2017);
        
        biblioteca.registrarUsuario(usuario);
        biblioteca.registrarLibro(libro);

        biblioteca.prestarLibroAUsuario("usr-1", "u1");

        java.util.List<Libro> librosDeJuan = biblioteca.listarLibrosDeUsuario("u1");
        assertEquals(1, librosDeJuan.size());
        assertEquals("usr-1", librosDeJuan.get(0).getIsbn());
        assertFalse(biblioteca.buscarPorIsbn("usr-1").get().isDisponible());
    }

    @Test
    void testPrestarLibroAUsuario_UsuarioInexistente_DebeLanzarExcepcion() {
        Libro libro = new Libro("usr-2", "Domain-Driven Design", "Eric Evans", 2003);
        biblioteca.registrarLibro(libro);

        assertThrows(com.biblioteca.excepciones.UsuarioNoEncontradoException.class, 
            () -> biblioteca.prestarLibroAUsuario("usr-2", "fantasma"));
    }

    @Test
    void testPrestarLibroAUsuario_LimiteTresLibros_DebeLanzarExcepcion() {
        Usuario usuario = new Usuario("u2", "Ana Gomez", "ana@mail.com");
        biblioteca.registrarUsuario(usuario);
        
        Libro l1 = new Libro("lim-1", "Libro 1", "A1", 2001);
        Libro l2 = new Libro("lim-2", "Libro 2", "A2", 2002);
        Libro l3 = new Libro("lim-3", "Libro 3", "A3", 2003);
        Libro l4 = new Libro("lim-4", "Libro 4", "A4", 2004);

        biblioteca.registrarLibro(l1);
        biblioteca.registrarLibro(l2);
        biblioteca.registrarLibro(l3);
        biblioteca.registrarLibro(l4);

        // Prestamos 3 libros (valores válidos, límite)
        biblioteca.prestarLibroAUsuario("lim-1", "u2");
        biblioteca.prestarLibroAUsuario("lim-2", "u2");
        biblioteca.prestarLibroAUsuario("lim-3", "u2");

        // El 4to préstamo debe fallar
        assertThrows(IllegalStateException.class, 
            () -> biblioteca.prestarLibroAUsuario("lim-4", "u2"),
            "No debe permitir prestar más de 3 libros a un mismo usuario"
        );
    }
}
