
# Proyecto práctico de PDS

En este proyecto se pretende construir un sistema de gestión de trabajo colaborativo a través de tableros de tareas. Como referencia para identificar las características de este tipo de aplicaciones se pueden explorar aplicaciones como Trello (https://www.trello.com/). 

La aplicación deberá permitir:

- Crear y modificar _tableros_ donde hay _listas de tareas_. Una lista de tareas tiene _tarjetas_ de manera que las tarjetas sirven para asignar tareas o para anotar información relevante.
- Las tarjetas se pueden marcar como completadas, en cuyo caso pueden pasar a una lista especial del tablero de tarjetas completadas.
- Las tarjetas pueden tener etiquetas para clasificarlas. Una etiqueta además tiene un color.
- Hay dos tipos de tarjetas: tarjetas que tienen tareas y tarjetas que tienen checklists.
- Los tableros mantienen una historia de todas la acciones de los usuarios. Por ejemplo, si se mueve una tarjeta entre listas de tareas se debe registrar una traza de que este movimiento se ha realizado.
- Es posible bloquear temporalmente un tablero para que no se puedan añadir nuevas tarjetas, solo mover tarjetas entre sus listas (por ejemplo, un tablero con TODO, DOING, DONE en el que durante una semana no se pueden añadir nuevas tarjetas).
- Para simplificar la gestión de lo usuarios se utilizará una estrategia simple: cualquier persona con un correo electrónico podrá crear un tablero y obtendrá
una URL única (y privada a no ser que la comparta) para su tablero.
- Un tablero puede compartirse con otros usuarios de la aplicación, para ello se comparte la URL.

## Características opcionales

Se deberán implementar al menos **dos** de las siguientes características. Para obtener la máxima nota deberán implementarse al menos **cuatro**:

- Reglas a nivel de lista: una lista no puede tener más de N items (configurable) y una lista puede definir que una tarjeta tiene que haber pasado por otras listas antes de llegar a ella.
- Filtrado de tarjetas por etiquetas.
- Creación de plantillas. Una plantilla es un tablero con listas y tarjetas predeterminadas. Las plantillas se definen como un fichero YAML.
- Compactación automática de tableros. Se definirá alguna estrategia para el archivo automático de tarjetas (ej., aquellas tarjetas no completadas en más de una semana).
- Autenticación basada en código por correo. El sistema enviará a un usuario que quiera entrar en la aplicación un correo electrónico con un código. El usuario utilizará este código en todas sus peticiones y será válido durante, p.ej., 5 minutos (después de cada uso volverá a ser válido 5 minutos).
- Permisos para usuarios. Cada tablero tendrá un dueño que podrá decidir qué usuarios de aquellos con los que ha compartido el tablero pueden leer o escribir en ciertas tarjetas.
- Definición de reglas de automatización. Se propone definir reglas al estilo: "Si ocurre evento X -> hacer Y". Dependiendo de la libertad que se dé al usuario para definir X e Y la dificultad de esta característica se incrementa.  
- Alguna otra característica comentada con el profesor de prácticas

## Requisitos de implementación
La implementación deberá cumplir lo siguiente:

- Lenguaje de programación Java.
- Arquitectura Hexagonal siguiendo DDD
- Backend implementado en SpringBoot
- Interfaz gráfica de escritorio con JavaFX
- Maven como sistema de construcción.
- Persistencia con JPA.
- Pruebas de software.

## Realización de la práctica

La práctica se realizará en grupos de 3 estudiantes. Cada grupo deberá crear una repositorio en GitHub en el el que alojar el proyecto y trabajar activamente sobre el repositorio. Todos los artefactos del proyecto, incluyendo la documentación deben estar alojados en el proyecto de GitHub. Se deberá utilizar GitHub para la gestión del proyecto.

El fichero README.md del proyecto deberá incluir:
- Nombre de la aplicación
- Nombre y correo electrónico de los participantes
- Lista de características de la aplicación
- Enlace a un fichero CREDITOS.md que incluirá información sobre en qué ha participado cada componente del grupo, con referencias a ejemplos de _commits_, _PRs_, _issues y discusiones_ o cualquier otro elemento que sirva para valorar la participación. No tiene que ser una descripción exhaustiva pero debe quedar clara la participación de todos los miembros del equipo.

Se valorará:
- El diseño del sistema y su justificación. Las clases deberán tener comentarios relevantes explicando el diseño.
- Breve documentación donde se describa el modelo DDD obtenido y las decisiones de diseño.
- La corrección de la implementación y otros atributos de calidad (ej., mantenibilidad, legibilidad, extensibilidad, etc.)
- El buen uso del lenguaje Java
- El buen uso de las pruebas de software y la cobertura del código obtenida, en particular de la parte del modelo de dominio.

## Hitos sugeridos y fecha de entrega

- **23-marzo**: Proyecto con las características básicas, incluyendo interfaz gráfica inicial y pruebas del modelo de dominio. Sin persistencia en BD.
- **27-abril**: Proyecto incluyendo la persistencia, pruebas adicionales y al menos dos características simples.
- **8-mayo**: Entrega del proyecto

