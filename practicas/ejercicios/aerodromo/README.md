# Ejercicio - Aeródromo

Dado el enunciado que se muestra a continuación describiendo un dominio para el cual se desea construir una sistema
tu tarea es diseñar el sistema utilizando las técnicas de Domain-Driven Design. 
Para ello, en este ejercicio debes:

1. Identificar los elementos del lenguaje ubicuo. Incluir un glosario de los elementos más importantes y cualquier diagrama útil.
2. Identificar historias de usuario.
3. Crear un modelo de dominio "ligero" para entender los conceptos. Identificar las reglas de negocio.
4. Realizar el diseño táctico del sistema según DDD. 
   4.1. Realizar un modelo de dominio identificando entidades, value objects y servicios de dominio. Expresalo como un diagrama de clases.
   4.2. Identifica los agregados. 
   4.3. Asignar reglas de negocio
5. Identificar los servicios de aplicación a partir de las historias de usuario. ¿Qué elementos del modelo de dominio se coordinan en estos servicios?

Los puntos 1, 2 y 3 sirven para entender el dominio del problema. Este análisis hay que trasladarlo a un diseño software en el punto 4.
Se recomienda hacer los puntos 1, 2 y 3 de manera conjunta, entonces refinarlo para hacer el punto 4.1. A partir de ahí se pueden hacer 
los puntos 4.2, 4.3 y 5. En todo caso, se debe abordar como un proceso iterativo.

## Enunciado

> Un aeródromo quiere actualizar su sistema de gestión para la asignación de mecánicos a posibles averías reportadas en los aviones.

> Cada avión del aeródromo tiene información como número de registro, matrícula, antigüedad, fecha de registro y última revisión. Además, cada avión es de un tipo determinado, recogiéndose de cada tipo su marca y modelo, capacidad y peso.

> Cada avión está asignado a un propietario, que será el encargado de gestionar y actualizar su información y la de sus aviones.

> En el aeródromo hay dos tipos de empleados: mecánicos y pilotos. Mientras que los pilotos pueden pilotar cualquier tipo de avión, cada mecánico está cualificado para reparar sólo determinados tipos de aviones.

> Según el protocolo de aviación internacional, el piloto comprobará que todo funciona correctamente y en caso de detectar un fallo en el avión lo reportará para que un mecánico pueda repararlo. En el mismo momento en que se reporta la avería, el sistema seleccionará el mecánico libre más adecuado al tipo de avión y de avería. Este mecánico será el encargado de la reparación de dicho avión inmediatamente. Si no hay ningún mecánico libre, la avería se asigna a uno de los mecánicos cualificados para resolverla, quedando en cola hasta su resolución. (La avería se asigna al mecánico cualificado para resolverla que tenga menos averías pendientes de resolución, y en caso de empate, a uno cualquiera.)