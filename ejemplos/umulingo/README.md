# UMULingo

<p align="center">
  <img src="./umulingo.png">
</p>

UMULingo es una plataforma de aprendizaje de idiomas desarrollada en el entorno de la Universidad de Murcia, inspirada en aplicaciones como Duolingo.
El sistema se basa en el aprendizaje progresivo mediante preguntas, donde los usuarios practican un idioma respondiendo distintos tipos de ejercicios.

Esta aplicación permite participar en cursos formados por preguntas de distintos tipos y que los usuarios practiquen respondiendo preguntas mientras se registra su progreso y se actualiza un ranking.

Este proyecto se ha adaptado para ilustrar conceptos de:
- Aplicaciones **cliente-servidor**.
- Diseño dirigido por el dominio (**DDD**).
- **Arquitectura hexagonal** (Ports & Adapters).
- Persistencia con **JPA**.
- Uso de **Spring Boot**.

### Arquitectura del proyecto
El proyecto está dividido en dos módulos Maven:

```
umulingo
├── umulingo-core (Backend de la aplicación)
└── umulingo-cli (Cliente de consola que interactúa con el backend)
```

### Cómo arrancar el proyecto
1. Desde ```umulingo-core``` hay que ejecutar el proyecto con Spring Boot con ```./mvnw spring-boot:run```
2. Desde ```umulingo-cli``` con ```mvn compile exec:java```

El servidor del backend se arrancará normalmente en:
```
http://localhost:8080
```

### Base de datos
La aplicación utiliza una base de datos embebida para facilitar su ejecución en entorno docente. La base de datos se crea automáticamente al arrancar la aplicación.

Dependiendo de la configuración puede utilizar:
- **H2**
- **SQLite**

La base de datos se guarda en la carpeta ```data/```

#### H2
Si se está utilizando H2, se puede abrir la consola web en:
```
http://localhost:8080/h2-console
```

y conectar con:
```
JDBC URL: jdbc:h2:file:./data/umulingo
User: sa
Password:
```
Esto permite inspeccionar las tablas y datos generados por la aplicación.


### Alternativa con SQLite

Se puede ejecutar también utilizando SQLite como base de datos, activando un perfil de Spring Boot.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=sqlite
```

Los datos se pueden consultar en:

```
sqlite3 umulingo-core/data/umulingo.db
```

### Cliente de consola
El módulo `umulingo-cli` proporciona un cliente sencillo que interactúa con el backend.

