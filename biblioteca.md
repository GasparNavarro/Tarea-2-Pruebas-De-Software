# Tarea: Sistema de Biblioteca usando TDD y JUnit

## Objetivo

Diseñar e implementar, utilizando **Java**, **JUnit 5** y la metodología **Test Driven Development (TDD)**, un sistema sencillo para la administración de libros en una biblioteca.

El objetivo principal de esta actividad es practicar el desarrollo guiado por pruebas (TDD), fortaleciendo tanto las habilidades de programación como la capacidad de diseñar pruebas unitarias efectivas.

NOTA: NO usar IA en este ejercicio

---

# Contexto

Una biblioteca necesita un sistema básico que permita registrar libros, buscar libros disponibles y gestionar préstamos y devoluciones.

El desarrollo debe realizarse siguiendo la metodología **TDD**, aplicando ciclos iterativos de:

1. Escribir una prueba que falle (Red).
2. Implementar el código mínimo para que la prueba pase (Green).
3. Refactorizar manteniendo todas las pruebas exitosas (Refactor).

---

# Requerimientos Funcionales

## 1. Registro de libros

Cada libro debe contener la siguiente información:

* ISBN
* Título
* Autor
* Año de publicación
* Estado de disponibilidad

### Reglas

* No se puede registrar un libro con ISBN vacío o nulo.
* No se puede registrar un libro con título vacío o nulo.
* No se pueden registrar dos libros con el mismo ISBN.
* Todo libro nuevo debe quedar disponible por defecto.

---

## 2. Búsqueda de libros

El sistema debe permitir:

* Buscar un libro por ISBN.
* Buscar libros por título.
* Listar todos los libros disponibles.

### Reglas

* Si no existe un libro para el ISBN solicitado, debe retornarse un resultado vacío.
* La búsqueda por título debe permitir coincidencias parciales.
* La búsqueda por título no debe diferenciar entre mayúsculas y minúsculas.

---

## 3. Préstamo de libros

El sistema debe permitir prestar un libro utilizando su ISBN.

### Reglas

* Solo se puede prestar un libro existente.
* Solo se puede prestar un libro disponible.
* Al prestar un libro, este debe quedar marcado como no disponible.
* Si el libro no existe, debe generarse una excepción.
* Si el libro ya se encuentra prestado, debe generarse una excepción.

---

## 4. Devolución de libros

El sistema debe permitir devolver un libro utilizando su ISBN.

### Reglas

* Solo se puede devolver un libro existente.
* Solo se puede devolver un libro que actualmente esté prestado.
* Al devolver un libro, este debe quedar disponible nuevamente.
* Si el libro no existe, debe generarse una excepción.
* Si el libro ya estaba disponible, debe generarse una excepción.

---

# Diseño sugerido

## Clase Libro

```java
public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;
    private boolean disponible;
}
```

## Clase Biblioteca

```java
public class Biblioteca {

    public void registrarLibro(Libro libro) { }

    public Optional<Libro> buscarPorIsbn(String isbn) { }

    public List<Libro> buscarPorTitulo(String titulo) { }

    public List<Libro> listarDisponibles() { }

    public void prestarLibro(String isbn) { }

    public void devolverLibro(String isbn) { }

}
```

## Excepciones sugeridas

```java
public class LibroNoEncontradoException extends RuntimeException { }

public class LibroNoDisponibleException extends RuntimeException { }

public class LibroYaDisponibleException extends RuntimeException { }

public class LibroDuplicadoException extends RuntimeException { }
```

---

# Casos de Prueba Mínimos

Los estudiantes deberán implementar pruebas unitarias que cubran, al menos, los siguientes escenarios.

## Registro de libros

* Registrar un libro válido.
* Verificar que un libro nuevo queda disponible.
* No permitir registrar un libro con ISBN vacío.
* No permitir registrar un libro con título vacío.
* No permitir registrar dos libros con el mismo ISBN.
* ...

## Búsqueda de libros

* Buscar un libro existente por ISBN.
* Buscar un libro inexistente por ISBN.
* Buscar libros por coincidencia parcial del título.
* Buscar libros sin diferenciar mayúsculas y minúsculas.
* Listar únicamente los libros disponibles.
* ...

## Préstamo de libros

* Prestar un libro disponible.
* Verificar que el libro queda no disponible después del préstamo.
* No permitir prestar un libro inexistente.
* No permitir prestar un libro que ya está prestado.
* ...

## Devolución de libros

* Devolver un libro prestado.
* Verificar que el libro queda disponible después de la devolución.
* No permitir devolver un libro inexistente.
* No permitir devolver un libro que ya estaba disponible.
* ...

---

# Restricciones Técnicas

* Lenguaje de programación: Java.
* Framework de pruebas: JUnit 5.
* El desarrollo debe seguir la metodología TDD.
* No se requiere base de datos.
* Los libros pueden almacenarse en memoria utilizando colecciones de Java.
* No se permite implementar toda la solución antes de escribir las pruebas.
* Cada funcionalidad debe avanzar mediante pequeños ciclos de prueba, implementación y refactorización.

---

# Entregables

Cada grupo deberá entregar:

1. Código fuente completo del proyecto.
2. Pruebas unitarias implementadas con JUnit.
3. Evidencia de ejecución exitosa de las pruebas.
4. Archivo README.md que incluya:

   * Instrucciones de ejecución.
   * Casos de prueba implementados.
   * Decisiones de diseño adoptadas.
   * Descripción breve de cómo se aplicó TDD durante el desarrollo.
5. Respuestas a las preguntas de reflexión indicadas a continuación.

---

# Reflexión sobre el uso de TDD

## Pregunta 1

Describe brevemente cómo aplicaste el ciclo TDD (Red → Green → Refactor) durante el desarrollo del ejercicio.

Incluye un ejemplo concreto de una funcionalidad donde primero escribiste la prueba, luego implementaste el código mínimo para hacerla pasar y finalmente realizaste una refactorización.

---

## Pregunta 2

¿Qué ventajas y desventajas observaste al desarrollar utilizando TDD en comparación con implementar primero el código y luego las pruebas?

Fundamenta tu respuesta utilizando ejemplos de tu experiencia durante el desarrollo de este ejercicio.

---

## Pregunta 3

Si tuvieras que desarrollar nuevamente este sistema desde cero, ¿continuarías utilizando TDD? ¿Por qué?

Explica qué aprendizajes obtuviste respecto a:

* Calidad del software.
* Diseño de clases y métodos.
* Detección temprana de errores.
* Velocidad de desarrollo.
* Confianza al realizar cambios en el código.

---

# Desafío Opcional

Extender el sistema para incorporar usuarios de la biblioteca.

Cada usuario debe tener:

* ID
* Nombre
* Correo electrónico

Nuevas reglas:

* Un usuario puede tener como máximo 3 libros prestados simultáneamente.
* No se puede prestar un libro a un usuario inexistente.
* Debe ser posible listar todos los libros prestados por un usuario.

Las funcionalidades opcionales deben estar acompañadas de sus respectivas pruebas unitarias.

@2026

---
