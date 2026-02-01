# Ejercicio - Vigilancias de examen

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

>```En la facultad se desea hacer una aplicación para gestionar profesores, asignaturas, exámenes y vigilancias realizadas por cada profesor en cada examen.```
>
>```Cada asignatura está asociada a una titulación y tiene un profesor responsable.```
>
>```En cada convocatoria (febrero, junio y julio) se realiza un examen de cada asignatura, cada uno en una fecha determinada. Con suficiente antelación, antes del periodo de exámenes, el secretario de la facultad asigna a cada examen un turno (mañana, tarde), una o varias aulas y el profesor responsable del mismo. Este profesor responsable es el encargado de determinar si es necesario algún profesor más para ayudar a vigilar el examen, y de solicitarlo, en su caso, como mínimo con 15 días de antelación a la celebración del examen.```
>
>```El secretario de la facultad recibe una notificación cada vez que se solicitan vigilantes para un examen. El secretario revisa periódicamente estas solicitudes y las valida asignando profesores vigilantes al mismo y avisando a los profesores implicados para que confirmen la vigilancia. Esta revisión de solicitudes se realiza de la siguiente manera: el secretario de la facultad inicia la validación de la solicitud, tras lo cual el sistema asignará las vigilancias solicitadas a los profesores con menos vigilancias hasta el momento y que no tengan un examen en el mismo día y turno. En caso de empate el sistema seleccionará al profesor vigilante de manera aleatoria. El secretario entonces validará esa asignación y el sistema avisará automáticamente a los profesores seleccionados para que verifiquen que pueden realizar la vigilancia y la confirmen o bien indiquen que no les es posible realizar dicha vigilancia.```
