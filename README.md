# Sistema de Biblioteca (Tarea TDD y JUnit 5)

Este proyecto es la implementación de un sistema de gestión para una biblioteca aplicando la metodología **TDD (Test-Driven Development)**, utilizando **Java 17** y **JUnit 5**.

**Autor:** Gaspar Navarro Cornejo  
**Rol:** 202173003.6

## Instrucciones de Ejecución

El proyecto está configurado con **Maven**. 

Debe estar en el directorio raíz del proyecto (donde está el archivo `pom.xml`).

Para ejecutar todas las pruebas unitarias y verificar el estado "Verde" del proyecto, ejecutar el siguiente comando:

```bash
mvn test
```

Si se quiere limpiar compilaciones previas y ejecutar todo desde cero, usar:

```bash
mvn clean test
```

## Casos de Prueba Implementados

Se han implementado **18 pruebas unitarias** que cubren la totalidad de los requerimientos base y el desafío opcional. Las pruebas están divididas en:

1. **Registro:** Libros válidos, validación nulos/vacíos (ISBN y Título), validación de ISBN duplicado.
2. **Búsqueda:** Por ISBN existente/inexistente, búsqueda por Título (case-insensitive y parcial), listado de disponibles.
3. **Préstamo y Devolución:** Flujo feliz, excepciones por libros no encontrados, libros ya prestados y libros ya disponibles.
4. **Desafío Opcional (Usuarios):** Préstamo asociado a un ID de usuario y la validación estricta de límite de préstamos (máximo 3).

## Decisiones de Diseño Adoptadas

1. **Uso de Mapas (`HashMap`):** Se utilizó `Map<String, Libro>` para el catálogo principal, permitiendo búsquedas por ISBN en tiempo constante `O(1)`.
2. **Excepciones Personalizadas:** Se crearon excepciones específicas heredando de `RuntimeException` (ej. `LibroNoDisponibleException`) para mantener un código declarativo y no forzar el manejo de excepciones chequeadas (`try-catch`) en todo el flujo.
3. **Separación de Responsabilidades:** El controlador `Biblioteca` no expone sus estructuras de datos internas. Los métodos de búsqueda por título o listado retornan *copias* inmutables o listas nuevas usando `Streams` (`.toList()`) para proteger la encapsulación y evitar que se altere el estado original desde afuera.

---

## Reflexión sobre el uso de TDD

### Pregunta 1
**Describe brevemente cómo aplicaste el ciclo TDD (Red → Green → Refactor) durante el desarrollo del ejercicio.**

Aplicamos el ciclo TDD de forma iterativa, funcionalidad por funcionalidad. Un ejemplo claro fue el registro de un ISBN duplicado:
1. **Red:** Escribimos el test `testRegistrarLibro_ConIsbnDuplicado_DebeLanzarExcepcion`, registramos un libro, e intentamos registrar un segundo libro con el mismo ISBN. Al correr el test, falló porque el sistema simplemente lo sobrescribió en el `HashMap`.
2. **Green:** Fuimos a `Biblioteca.java` y agregamos un simple `if (catalogo.containsKey(isbn))` que lanzaba la excepción `LibroDuplicadoException`. El test pasó.
3. **Refactor:** Vimos que el método `registrarLibro` se estaba llenando de validaciones (nulos, vacíos, duplicados), así que extrajimos toda esa lógica hacia un método privado `validarLibro(Libro libro)`. El código principal quedó limpio y las pruebas siguieron pasando, dándonos seguridad de que no rompimos nada.

### Pregunta 2
**¿Qué ventajas y desventajas observaste al desarrollar utilizando TDD en comparación con implementar primero el código y luego las pruebas?**

**Ventajas:**
- **Diseño YAGNI (You Aren't Gonna Need It):** TDD nos obligó a escribir solo el código necesario para pasar la prueba. Evitamos hacer sobre-ingeniería desde el inicio.
- **Depuración casi nula:** Como avanzamos paso a paso, si un test fallaba sabíamos exactamente que el error estaba en las 3 líneas de código que acabábamos de escribir. 

**Desventajas:**
- **Lentitud inicial:** Al principio, configurar el `pom.xml`, estructurar la clase de tests, escribir aserciones y pensar en el diseño desde la perspectiva de la prueba hizo que el arranque del proyecto fuera más lento que simplemente empezar a escupir código.

### Pregunta 3
**Si tuvieras que desarrollar nuevamente este sistema desde cero, ¿continuarías utilizando TDD? ¿Por qué?**

Sí, continuaría usando TDD sin dudarlo. El aprendizaje principal es que la lentitud inicial se compensa con creces a medida que el sistema crece.

- **Calidad y Diseño:** Pensar primero en el test nos forzó a ponernos en los zapatos de quien *usa* la clase `Biblioteca`, lo que resultó en un diseño de métodos mucho más amigable y cohesivo.
- **Detección temprana y Velocidad:** Cuando implementamos el límite de 3 libros (desafío opcional), el análisis de valores límite en la prueba nos salvó de un *bug* lógico clásico (`>` vs `>=`) antes de siquiera desplegar.
- **Confianza:** El refactor, normalmente la parte más temida por el riesgo de romper código legado, fue un proceso libre de estrés. Al cambiar búsquedas manuales por Java Streams, la red de seguridad de nuestras pruebas validó instantáneamente que el comportamiento seguía siendo correcto.
