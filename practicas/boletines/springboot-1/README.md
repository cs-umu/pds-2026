
# SpringBoot - Arquitectura Hexagonal

En este boletín se practicará con el framework Spring para crear una API REST y para configurar
las dependencias de una aplicación creada siguiendo una arquitectura hexagonal.

Se partirá del ejemplo `tpv-api` que implementa una API simple para la gestión de un Terminal Punto de Venta (TPV).
La implementación actual solo incluye la gestión de productos (stock). En esta práctica se extenderá esta funcionalidad de la siguiente manera:

1. Se creará una implementación alternativa del repositorio de productos (en memoria) y se cambiará la inyección de dependencias para usarla.
2. Se extenderá el proyecto añadiendo la funcionalidad de **ventas**: nuevas entidades de dominio, puertos, adaptadores y endpoints REST.

---

## Parte 0: Configuración

### Configuración de Eclipse

Es recomendable instalar las Spring Tools en Eclipse.

Se puede descargar un Eclipse con ellas ya configuradas aquí: https://spring.io/tools#eclipse

O instalarlas directamente desde el marketplace de Eclipse

![SpringBoot Tools](./sbtoos.png)

### Cliente de HTTP

Es necesario disponer de un cliente de HTTP para poder hacer pruebas. 
Una opción simple es utilizar la herramienta `curl` que está
disponible en la mayoría de los sistemas operativos (aunque en Windows y Linux el uso es ligeramente diferente).
En Windows es necesario poner las comillas con `"\`.

También se puede utilizar [Postman](https://portapps.io/app/postman-portable/) o [Bruno](https://www.usebruno.com/).

### Estructura del proyecto Maven

El proyecto `tpv-api` es un proyecto Maven estándar con Spring Boot. El fichero `pom.xml` define:

- **Spring Boot 4.0.2** como parent, lo que proporciona configuración automática y gestión de versiones de dependencias.
- **Java 21** como versión del compilador.
- Las siguientes dependencias principales:

| Dependencia | Propósito |
|---|---|
| `spring-boot-starter-webmvc` | Soporte para crear controladores REST |
| `spring-boot-starter-data-jpa` | Persistencia con JPA/Hibernate |
| `spring-boot-starter-validation` | Validación de beans (anotaciones `@NotNull`, `@Positive`, etc.) |
| `h2` | Base de datos en memoria para desarrollo |
| `spring-boot-h2console` | Consola web para inspeccionar la base de datos H2 |

### Importar y ejecutar el proyecto

1. Importa el proyecto `tpv-api` en Eclipse como proyecto Maven existente.
2. Ejecuta la clase `TpvApiApplication` (tiene el método `main`).
3. La aplicación arrancará en `http://localhost:8080`.

### Configuración de la aplicación

El fichero `src/main/resources/application.properties` contiene la configuración. Puntos destacables:

- Se definen dos rutas base para los endpoints, una **privada** y una **pública**:
  ```properties
  tpv.private.api=/tpv/private/v1.0
  tpv.public.api=/tpv/public/v1.0
  ```
  Las rutas públicas son aquellas que no requieren autenticación. Las rutas privadas sí la requieren.
  En este ejercicio no haremos autenticación, pero se mantiene la distinción para aprender cómo se haría en un ejemplo real.

- Se configura una base de datos **H2 en memoria** que se reinicializa cada vez que arranca la aplicación. Los ficheros `schema.sql` (estructura) y `data.sql` (datos iniciales) se ejecutan automáticamente.
- Se activa la **consola H2** en `http://localhost:8080/h2-console` (usuario: `sa`, contraseña: `password`, URL JDBC: `jdbc:h2:mem:bbdd`).

---

## Parte 1: Explorando el proyecto

### 1.1 Arquitectura del proyecto

El proyecto sigue una **arquitectura hexagonal** (Puertos y Adaptadores). Para lograr una separación clara entre el núcleo de la aplicación (modelo de dominio y lógica de negocio expresado según DDD) y la infraestructura (Spring Boot, JPA, REST), se ha 
organizado el proyecto en tres subpaquetes diferentes:

- Paquete `domain`. La aplicación construida siguiendo DDD 
  - Subpaquete `model`: Los elementos del modelo de dominio según DDD: entidades, value objects y servicios de dominio.
  - Subpaquete `ports`. La definición de los puertos de la aplicación. Puertos de entrada: servicios y puertos de salida: repositorios 
  **Ninguna clase dentro de `core` tiene anotaciones de Spring** (`@Service`, `@Component`, etc.). Es código Java puro.
- Paquete `application`. La implementación de los servicios de aplicación. **Ojo. Diferente a "Arquitectura Hexagonal pura**.
- Paquete `adapters`. Los adaptadores de la aplicación, implementados con Spring

De esta forma, el proyecto tiene la siguiente estructura:

```
inf.pds.tpv
├── domain/                                    ← Modelo de dominio
│   ├── model/producto/                        ← Modelo de dominio (organizado por "aggregate roots")
│   │   ├── Producto.java
│   │   └── ProductoId.java
│   ├── exceptions/
│   │   └── ProductoNoExistenteException.java
│   └── ports/
│       ├── input/                             ← Servicios de aplicación (casos de uso). Interfaces solo.
│       │   └── stock/                         ← Servicios relacionados con el stock (organizamos por tipo de servicio)
│       │       └── StockService.java
│       └── output/                            ← Puertos de salida (interfaces)
│           └── ProductosRepository.java
├── application/                               ← Implementación de los servicios de apliación (casos de uso)
│   └── usescases/stock/
│               └── StockServiceImpl.java      ← Implementación del servicio utilizando SpringBoot (@Service)
├── adapters/                                  ← Adaptadores (infraestructura)
│   ├── rest/                                  ← Adaptador de entrada (API REST)
│   │   ├── StockEndpoint.java
│   │   └── dto/
│   │       ├── ProductoDTO.java
│   ├── jpa/                                   ← Adaptador de salida (persistencia JPA)
│   │   ├── ProductoRepository.java
│   │   ├── entity/
│   │   │   └── ProductoEntity.java
│   │   └── repository/
│   │       └── ProductoJpaRepository.java
│   └── mappers/
│       └── ProductoMapper.java
```

**Sobre la arquitectura Hexagonal y Spring Boot**. En teoría se ha visto cómo construir una aplicación siguiendo la arquitectura hexagonal sin _ninguna dependencia tecnológica_.
En la práctica, sin embargo, Spring Boot ofrece funcionalidades como la gestión de transacciones y la inyección de dependencias que son muy útiles a la hora de implementar los servicios
de aplicación. Por tanto, la aproximación que seguiremos es definir los servicios de aplicación como interfaces (en la carpeta `domain/ports/input`) que luego se implementan en el lado de Spring Boot.
Esto tiene la desventaja de que la aplicación está ligada a la tecnología de Spring Boot, pero si se considera que Spring Boot forma parte de la arquitectura (lo que no va a cambiar)
entonces es un compromiso aceptable.

<details open>
<summary><strong>🟢 Ejercicio</strong></summary>

Comprueba qué clases usan o implementan los puertos declarados en `domain`.  Utiliza las herramientas de Eclipse: 

* Call hierarchy 
* Open type hierarchy

También, observa cómo se realiza la configuración a través de las diferentes anotaciones. En particular, observa cómo se implementa el puerto `StockService`.
</details>


### 1.2 Probando la API

Arranca la aplicación y prueba los endpoints utilizando `curl` o una herramienta similar (p.ej., [Postman](https://portapps.io/app/postman-portable/)).

Para ejecutar el servicio web hay dos opciones:
- Ejecutar la clase `TpvApiApplication` desde Eclipse.
- Ejecutar `mvn spring-boot:run`

<details open>
<summary><strong>🟢 Ejercicio</strong></summary>
Realiza las invocaciones a la API que se muestran a continuación y para cada uno de ellas comprueba qué método del controlador REST se invoca y cómo declara sus parámetros.

Puedes poner breakpoints en el controlador para inspeccionar los valores que llegan.
</details>


**Listar todos los productos** (GET):

```bash
curl -s http://localhost:8080/tpv/private/v1.0/stock/producto | jq
```

**Obtener un fragmento (los 3 primeros)**:

```bash
curl -s http://localhost:8080/tpv/private/v1.0/stock/producto | jq | head -20
```

El resultado será una lista de objetos JSON como:

```json
[
    {
        "codigo": 101,
        "descripcion": "Manzana",
        "cantidad": 50,
        "precio": 1.2
    },
    ...
]
```

**Crear un producto nuevo** (POST). Observa que no se envía `codigo` porque se genera automáticamente:

```bash
curl -s -X POST http://localhost:8080/tpv/private/v1.0/stock/producto \
  -H "Content-Type: application/json" \
  -d '{"descripcion": "Piña", "cantidad": 15, "precio": 2.50}' | jq
```

Respuesta esperada (el código asignado será 131 o superior porque ya hay algunos productos en la base de datos):

```json
{
    "codigo": 131,
    "descripcion": "Piña",
    "cantidad": 15,
    "precio": 2.5
}
```

**Modificar un producto existente** (PUT):

```bash
curl -s -X PUT http://localhost:8080/tpv/private/v1.0/stock/producto \
  -H "Content-Type: application/json" \
  -d '{"codigo": 1, "descripcion": "Manzana Golden", "cantidad": 100, "precio": 1.50}' | python3 -m json.tool
```

**Eliminar un producto** (DELETE):

```bash
curl -s -X DELETE http://localhost:8080/tpv/private/v1.0/stock/producto/131
```

<details open>
<summary><strong>🟢 Ejercicio</strong></summary>
Verifica que tras eliminar un producto, la lista devuelta por GET ya no lo contiene. Prueba también qué ocurre cuando intentas eliminar un producto que no existe (¿qué código HTTP devuelve?).
</details>

---

## Parte 2: Modificando la inyección de dependencias

En esta parte verás cómo la arquitectura hexagonal permite cambiar la implementación de la persistencia sin modificar ni el dominio ni la capa de aplicación.

### 2.1 Inspeccionar la inyección de dependencias

Actualmente, la inyección de dependencias funciona así:

1. `StockService` necesita un `ProductosRepository` (interfaz definida en `core.ports.output`) en su constructor. **`StockService` no está anotado con `@Service`** (es una clase Java pura dentro del paquete `core`).
2. La clase `Configuracion` (en el paquete `config`) está anotada con `@Configuration` y crea el bean de `StockService` mediante un método `@Bean`:
```java
@Configuration
public class Configuracion {
    @Bean
    public StockService bindStockService(ProductosRepository productoPort) {
        return new StockService(productoPort);
    }
}
```
   Spring le pasa automáticamente la implementación de `ProductosRepository` que encuentre en el contexto.
3. Spring detecta que `ProductoRepository` (en el paquete `adapters.jpa`) está anotado con `@Component` e implementa `ProductosRepository`.
4. Spring inyecta automáticamente la instancia de `ProductoRepository` al método `@Bean` de `Configuracion`, que a su vez la pasa a `StockService`.

De esta forma, el servicio de aplicación (`StockService`) **nunca sabe** que los datos se guardan con JPA/H2. Solo conoce la interfaz `ProductosRepository`. Además, al no tener anotaciones de Spring, el paquete `core` completo es independiente del framework.

<div style="border: 2px solid lightblue; padding: 10px;">
<span style="color:lightblue; font-weight:bold">NOTA</span>: 
La anotación `@Service` es equivalente a `@Component` en cuanto a que declara un objeto manejado por Spring. Se suele utilizar para declarar servicios de infrastructura o bien si no se está implementando una arquitectura hexagonal "pura" y los servicios se implementan en la capa de Spring.
</div>


### 2.2 <strong>🟢 Ejercicio</strong>: Implementar otro repositorio

Con el objetivo de practicar la inyección de dependencias,
crea una nueva implementación de `ProductosRepository` que almacene los productos en un `HashMap` en memoria, sin usar JPA ni base de datos.

Una vez hayas hecho esto tienes **dos** clases que implementan `ProductosRepository`: `ProductoRepository` (JPA) y `ProductoRepositoryInMemory`. Si intentas arrancar la aplicación, Spring fallará porque encuentra dos candidatos para inyectar.

Hay dos opciones para solucionarlo:

**Opción A: Usar `@Primary`**

Añade la anotación `@Primary` a la implementación que quieras que Spring use por defecto:

```java
@Component
@Primary
public class ProductoRepositoryInMemory implements ProductosRepository {
    // ...
}
```

Arranca la aplicación y prueba la API. Observa que ahora los datos iniciales de `data.sql` **no aparecen** (ya que la implementación en memoria empieza vacía). Crea algunos productos con POST y verifica que puedes consultarlos con GET.

**Opción B: Usar perfiles de Spring (`@Profile`)**

Una solución más elegante es usar perfiles de Spring para decidir qué implementación usar según el entorno:

1. Anota `ProductoRepository` (JPA) con `@Profile("jpa")`.
2. Anota `ProductoRepositoryInMemory` con `@Profile("memory")`.
3. Activa el perfil deseado en `application.properties`:
   ```properties
   spring.profiles.active=memory
   ```
4. Prueba a cambiar entre `jpa` y `memory` y observa la diferencia en el comportamiento.

**Pregunta para reflexionar**: ¿Ha sido necesario modificar `StockService` o `StockEndpoint` para cambiar la implementación de persistencia? ¿Qué ventaja supone esto?

---

## Parte 3: Extender el proyecto - Gestión de Ventas

En esta parte extenderás el proyecto para soportar la funcionalidad de **ventas** del TPV. Un cajero podrá consultar productos y registrar ventas. Cada venta se compone de una o más líneas de compra (producto + cantidad).

### 3.1 Modelo de dominio

El modelo está compuesto de dos entidades: `Venta` y `LineaVenta`. Una venta tiene una fecha y una serie de líneas de venta y una línea de venta "apunta" a un producto y tiene un atributo para indicar la cantidad de producto.

Una venta debe tener un método `getTotal()` que calcule la suma de los subtotales de todas las líneas.

### 3.2 Puerto de salida

Crea la interfaz `VentasRepository` en el paquete `core.ports.output`:

```java
public interface VentasRepository {
    Venta guardarVenta(Venta venta);
    Optional<Venta> obtenerVentaPorId(Long id);
    List<Venta> obtenerTodasVentas();
}
```

### 3.3 Servicio de aplicación

Crea la clase `VentaService` en el paquete `core.ports.input`. Recuerda que, al igual que `StockService`, esta clase **no debe tener anotaciones de Spring** (es código puro del core). El protocolo de este servicio es el siguiente:

- Inicia una venta pasándole una línea de venta inicial.
- Añade nuevas líneas de venta a una venta ya iniciada.

La regla de negocio que se debe cumplir es: _para poder añadir una línea de venta debe existir suficiente stock de producto_.

Además, es necesaria una operación para confirmar la venta.
Cuando una venta es confirmada habrá que marcarla como "PAGADA" y reducir el stock de los productos correspondientes.

**Nota**: Para simplificar, permitiremos que en la misma operación se pueda modificar el agregado Venta y el agregado Producto.

### 3.4 Adaptador de persistencia (en memoria)

Implementa el repositorio para Venta como un repositorio en memoria.

### 3.5 Endpoint REST

Completa la clase **`TpvEndpoint`** en `adapters.rest`. Este endpoint debe estar mapeado a la ruta pública (`${tpv.public.api}/tpv`) y exponer las siguientes operaciones:

| Método | Ruta | Descripción | Request Body | Respuesta |
|---|---|---|---|---|
| GET | `/producto/{id}` | Consultar un producto por ID | - | `ProductoDTO` (200) o 404 |
| POST | `/venta` | Registrar una nueva venta | `List<LineaCompraDTO>` | `VentaDTO` (201) o 400 |
| GET | `/venta` | Listar todas las ventas | - | `List<VentaDTO>` (200) |
| GET | `/venta/{id}` | Consultar una venta por ID | - | `VentaDTO` (200) o 404 |

### 3.7 Verificación

Una vez implementada toda la funcionalidad, verifica el siguiente escenario completo:

1. Consulta el stock del producto 1 (Manzana, 50 unidades):
   ```bash
   curl -s http://localhost:8080/tpv/public/v1.0/tpv/producto/101 | jq
   ```
2. Registra una venta de 5 Manzanas y 3 Naranjas.
3. Vuelve a consultar el stock del producto 1 y verifica que ahora tiene **45 unidades**.
4. Consulta la lista de ventas y verifica que aparece la venta registrada con el total correcto.
5. Intenta registrar una venta con un producto inexistente y verifica que devuelve error 400.
6. Intenta registrar una venta con más cantidad de la disponible en stock y verifica que devuelve error 400.
