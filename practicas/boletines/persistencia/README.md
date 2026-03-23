# Práctica de persistencia con JPA

El objetivo de esta práctica es aprender a utilizar JPA en dos modalidades:
primero con el API estándar usando `EntityManager`, y después con Spring Boot
aprovechando Spring Data JPA y sus repositorios.

Las tecnologías que se utilizarán son:

* Java 21 — Lenguaje de programación
* Maven — Configuración del proyecto
* Hibernate/JPA — Framework de persistencia
* SQLite — Base de datos para JPA básico
* H2 — Base de datos en memoria para pruebas con Spring Boot
* Spring Boot 4 — Framework para la parte de integración con Spring
* JUnit 5 — Pruebas automatizadas

La práctica se divide en las siguientes partes:

1. [Inspeccionar el proyecto](#proyecto)
2. [Configurar Maven](#maven)
3. [JPA básico: persistir con EntityManager](#entitymanager)
4. [JPA básico: consultas JPQL](#jpql)
5. [JPA con Spring Boot: repositorios](#springboot)
6. [JPA con Spring Boot: consultas con @Query](#queries)


## Ejercicio #1. Inspeccionar el proyecto <a name="proyecto"></a>

El proyecto de partida contiene un modelo de dominio de una biblioteca digital.
Antes de comenzar a programar, estudia las clases que hay en `src/main/java/pds/modelo/`:

| Clase | Descripción |
|-------|-------------|
| `Usuario` | Clase abstracta con herencia `SINGLE_TABLE`. Tiene las subclases `Admin` y `Cliente`. |
| `Admin` | Subclase de `Usuario` con atributo `permisos`. |
| `Cliente` | Subclase de `Usuario` con atributo `direccion`. |
| `Autor` | Tiene una relación `@OneToMany` con `Libro`. |
| `Libro` | Tiene una relación `@ManyToOne` con `Autor` y `@ManyToMany` con `Categoria`. |
| `Categoria` | Tiene una relación `@ManyToMany` con `Libro`. |

Observa que la clase `App.java` contiene el método `main` y que se utiliza `@Autowired` para inyectar
un componente que es donde se ha implementado una prueba simple de JPA.

Para ejecutar el ejemplo (desde Maven):

```bash
mvn compile exec:java -Dexec.mainClass=pds.App
```

Observa el log y verás que JPA crea las tablas automáticamente y muestra las sentencias SQL que ejecuta.
Puedes inspeccionar la base de datos con:

```bash
sqlite3 basedatos.db
.schema
SELECT * FROM libros;
SELECT * FROM usuarios;
```

## Ejercicio #2. Configuración de Maven <a name="maven"></a>

Para añadir soporte a Spring Boot hay que modificar el `pom.xml` (ya está hecho).
Observa las dependencias del bloque `<dependencies>` que configuran el soporte de JPA para Spring.

```xml
<!-- Spring Boot Data JPA: incluye Hibernate, Spring Data y Spring ORM -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Soporte para pruebas con Spring Boot (@DataJpaTest, @SpringBootTest, etc.) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- H2: base de datos en memoria para las pruebas de Spring Boot -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

Además, puesto que se quiere utilizar también el soporte para SQLite3, hay que incluir
dos dependencias específicas: el driver de SQL para JDBC y la extensión de Hibernate para SQLite3. 

```xml
		<!-- SQLite JDBC Driver -->
		<dependency>
			<groupId>org.xerial</groupId>
			<artifactId>sqlite-jdbc</artifactId>
			<scope>runtime</scope>
		</dependency>

		<!-- Para hacer funcionar SQLite con Hibernate -->
		<dependency>
			<groupId>org.hibernate.orm</groupId>
			<artifactId>hibernate-community-dialects</artifactId>
		</dependency>
```



## Ejercicio #3. JPA básico: consultas JPQL <a name="jpql"></a>

JPQL (_Java Persistence Query Language_) es el lenguaje de consulta de JPA. A diferencia de SQL,
opera sobre las **clases de dominio** y sus atributos, no sobre tablas y columnas.

Crea la siguiente clase y observa el resultado al ejecutarla. No olvides configurarla en el `App.java` 
e invocarla en el método main.

```java
package pds.ejemplos;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pds.modelo.Admin;
import pds.modelo.Autor;
import pds.modelo.Categoria;
import pds.modelo.Cliente;
import pds.modelo.Libro;

@Component
public class PersistenciaConsultas {
    @PersistenceContext
    private EntityManager em;

    public void ejecutar() {
	   anhadirDatos();
	   consultarLibrosPorNombre();
	   contarClientes();
	   consultarPorCategoria();
	}

    @Transactional
	public void anhadirDatos() {
    	// Añadir autores
	    Autor rowling = new Autor("J.K. Rowling");
	    em.persist(rowling);
	    em.persist(new Libro("Harry Potter y la piedra filosofal", rowling));
	    em.persist(new Libro("Harry Potter y la cámara secreta", rowling));
    
	    Autor orwell = new Autor("Orwell");
	    em.persist(orwell);

	    Categoria cat = new Categoria("Distópico");
	    em.persist(cat);

	    Libro libro = new Libro("1984", orwell);
	    libro.addCategory(cat);
	    em.persist(libro);

	    
	    // Añadir clientes
	    em.persist(new Cliente("ana", "C/Sol 3"));
	    em.persist(new Cliente("luis", "C/Luna 7"));
	    em.persist(new Admin("root", "ALL"));

    }
    
    public void consultarLibrosPorNombre() {
	    // JPQL: nota que usamos el nombre de la clase Java, no el de la tabla
	    List<Libro> libros = em.createQuery(
	        "SELECT l FROM Libro l WHERE l.autor.name = :nombre", Libro.class)
	        .setParameter("nombre", "J.K. Rowling")
	        .getResultList();

	    for (Libro libro : libros) {
			System.out.println(libro.getTitulo());
		}
	}
	
    public void contarClientes() {
        Long total = em.createQuery(
                "SELECT COUNT(c) FROM Cliente c", Long.class)
                .getSingleResult();
        System.out.println(total);
    }

	public void consultarPorCategoria() {
	    List<Libro> libros = em.createQuery(
	            "SELECT l FROM Libro l JOIN l.categorias c WHERE c.nombre = :cat", Libro.class)
	            .setParameter("cat", "Distópico")
	            .getResultList();

	    System.out.println("Libros por categoria: " + libros.size());
	    for (Libro libro : libros) {
			System.out.println(libro.getTitulo());
		}
	}
}
```

Observa las diferencias respecto a SQL:

| JPQL | SQL equivalente |
|------|----------------|
| `FROM Libro l` | `FROM libros l` |
| `l.autor.name` | `JOIN authors a ON l.autor_id = a.id WHERE a.name` |
| `FROM Cliente c` | `FROM usuarios WHERE tipo_usuario = 'CLIENTE'` |
| `JOIN l.categorias c` | `JOIN libro_categoria lc JOIN categorias c ...` |

JPQL "conoce" las relaciones entre entidades y las resuelve automáticamente.


## Ejercicio #4. JPA con Spring Boot: repositorios <a name="springboot"></a>

Spring Data JPA elimina la necesidad de gestionar el `EntityManager` mediante el patrón Repository.
Basta con declarar una interfaz que extienda `JpaRepository` y Spring genera la implementación.

### 5.2. Repositorios JpaRepository

Crea los interfaces de repositorio en `src/main/java/pds/repositorios/`:

**`AutorRepository.java`**:
```java
package pds.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import pds.modelo.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Spring genera automáticamente: findAll, findById, save, deleteById, count, ...
}
```

**`LibroRepository.java`**:
```java
package pds.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import pds.modelo.Libro;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Spring genera la consulta a partir del nombre del método
    List<Libro> findByTitulo(String titulo);

    List<Libro> findByAutorName(String nombre);
}
```

**`UsuarioRepository.java`**:
```java
package pds.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import pds.modelo.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
```

`JpaRepository<T, ID>` proporciona de serie los métodos más habituales:

| Método | Descripción |
|--------|-------------|
| `save(entidad)` | Inserta o actualiza |
| `findById(id)` | Busca por clave primaria → `Optional<T>` |
| `findAll()` | Devuelve todos los registros |
| `deleteById(id)` | Elimina por clave primaria |
| `count()` | Cuenta el total de registros |
| `existsById(id)` | Comprueba si existe |

Spring Data también puede derivar consultas directamente del nombre del método
(`findByTitulo`, `findByAutorName`), siguiendo una convención de nomenclatura.


## Ejercicio #5. JPA con Spring Boot: consultas con @Query <a name="queries"></a>

Para consultas más complejas que no pueden expresarse con la convención de nombres,
se usa la anotación `@Query` con JPQL. Añade estos métodos a `LibroRepository`:

```java
package pds.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pds.modelo.Libro;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTitulo(String titulo);

    List<Libro> findByAutorName(String nombre);

    // Consulta JPQL explícita (usando parámetros posicionales, ?1)
    @Query("SELECT l FROM Libro l WHERE l.autor.name = ?1")
    List<Libro> buscarPorNombreAutor(String nombreAutor);

    // Consulta con JOIN a colección (usando parámetros nombrados)
    @Query("SELECT l FROM Libro l JOIN l.categorias c WHERE c.nombre = :categoria")
    List<Libro> buscarPorCategoria(@Param("categoria") String categoria);

    // Consulta con agregación
    @Query("SELECT COUNT(l) FROM Libro l WHERE l.autor.name = :nombre")
    Long contarPorAutor(@Param("nombre") String nombre);
}
```

Prueba que entienes la diferencia entre estos dos métodos que hacen lo mismo:

```java
// Derivado del nombre: Spring interpreta "ByAutorName" como l.autor.name
List<Libro> findByAutorName(String nombre);

// Consulta JPQL explícita: equivalente manual
@Query("SELECT l FROM Libro l WHERE l.autor.name = :nombreAutor")
List<Libro> buscarPorNombreAutor(@Param("nombreAutor") String nombreAutor);
```


ç