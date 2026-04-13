# TPV — Punto de Venta

En este proyecto se implementa un **punto de venta** (TPV) con una **API REST** para gestionar el stock de productos y una **interfaz gráfica** en JavaFX para realizar ventas y mostrar el catálogo. El objetivo es demostrar cómo crear una API REST con Spring Boot y una aplicación cliente en JavaFX que se comunique con la API para obtener los datos de los productos. 

En el proyecto de la asignatura se deberá seguir una arquitectura similar.

## API REST

La API REST se implementa en el módulo `tpv-api` usando **Spring Boot**. 

### Arrancar la API

Hay dos formas, desde el IDE ejecutando la clase `TpvApiApplication` o desde terminal:

```bash
cd tpv-api
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### Base de datos

El proyecto usa **H2 en memoria**. Se inicializa automáticamente con 30 productos de ejemplo (frutas) al arrancar. 
Los datos se pierden al detener la aplicación.

Consola H2 (solo en desarrollo): `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bbdd`
- Usuario: `sa` / Contraseña: `password`

### Endpoints de stock

La API expone algunos endpoints para gestionar el stock de productos en la ruta `/tpv/private/v1.0/stock/producto`.
Estos endpoints están definidos en `StockEndpoint.java`.

## TpvFx — Interfaz gráfica

La interfaz gráfica se implementa en el proyecto `TpvFx` usando **JavaFX**.

### Arrancar

Desde el IDE ejecutar la clase `App` o desde terminal:

```bash
cd TpvFx
./mvnw javafx:run
```

### Fuente de datos

Por defecto la aplicación carga los productos desde el fichero local `src/main/resources/data/almacen.json`.

Para conectarla a la API REST, se puede usar la opción del menú "Tienda → Repository REST". Esto requiere tener la API en ejecución y que el endpoint de stock esté disponible, es decir, hay que arrancar antes la API (ver arriba).

Una alternativa que es la que se utilizará en el proyecto de la asignatura es editar `ConfiguracionImpl.java` y sustituir el repositorio en el constructor:

```java
// Fuente local (por defecto)
this.controlador = new ControladorTPV(new ProductoRepositoryJSONImpl());

// Fuente REST (requiere tpv-api en ejecución)
this.controlador = new ControladorTPV(new ProductoRestRepositoryImpl());
```

Con la fuente REST activa, la aplicación consume `http://localhost:8080/tpv/private/v1.0/stock/producto`.


### Drag and drop

La aplicación permite arrastrar productos desde el catálogo (panel izquierdo) al carrito de la compra (panel derecho). Esto se implementa 
en la clase `TiendaViewController`.

El protocolo que se sigue es el siguiente:
- En `cargaProductos` se asigna un manejador de eventos `setOnDragDetected` a cada celda de la tabla del catálogo, de manera que al iniciar el arrastre se guarda el producto seleccionado en el `Dragboard` usando un formato personalizado (`DataFormat`).
```java
      btnProducto.setOnDragDetected(event -> {
         Dragboard db = btnProducto.startDragAndDrop(TransferMode.COPY);
         ClipboardContent content = new ClipboardContent();
         content.put(PRODUCTO_FORMAT, producto);
         db.setContent(content);
         event.consume();
      });
```
- Cuando el ratón se mueve sobre el carrito, se detecta con `setOnDragOver` y se acepta el arrastre si el formato es correcto.
```java
		tablaProductos.setOnDragOver(event -> {
			if (event.getDragboard().hasContent(PRODUCTO_FORMAT)) {
				event.acceptTransferModes(TransferMode.COPY);
				event.consume();
			}

		});
```
- Al soltar el producto sobre el carrito, se detecta con `setOnDragDropped`, se obtiene el producto del `Dragboard` y se añade al carrito.
```java
		tablaProductos.setOnDragDropped(event -> {
			if (event.getDragboard().hasContent(PRODUCTO_FORMAT)) {
				event.acceptTransferModes(TransferMode.COPY);
				Producto producto = (Producto) event.getDragboard().getContent(PRODUCTO_FORMAT);
				event.consume();
				aniadeListaCompra(producto);
			}
		});
```
