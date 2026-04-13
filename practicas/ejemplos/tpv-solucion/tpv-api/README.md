# TPV-API

### Descripcion de la API
Api para dar soporte a un frontend de punto de venta

Tiene dos grupos de endpoints:
- Stock: Endpoint privado para gestionar el stock de productos
- TPV: Endpoint público con todas las acciones que permiten realizar las compras

### Anotaciones
- Por simplicidad no se ha implementado la capa de autenticación de SpringBoot para este proyecto.
- Se define una base de datos en memoria h2 que se autoinicializa al arrancar con un conjunto de datos preexistentes.
- La persistencia se hará con JPA


