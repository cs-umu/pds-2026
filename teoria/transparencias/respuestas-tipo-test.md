# Respuestas tipo test
A continuación, se muestran las respuestas del tipo test de repaso que hicimos durante las clases de repaso.

| Slide | Respuesta | Justificación |
|--:|--:|---|
| 2 | 3 | El software es un producto lógico, no físico. A diferencia del hardware, el mayor coste del software está en el desarrollo y mantenimiento, no en la fabricación de copias. |
| 3 | 4 | El software es abstracto e intangible, lo que dificulta medirlo, visualizarlo y validarlo en comparación con productos físicos. |
| 4 | 3 | La complejidad inherente del problema es una causa esencial del desarrollo. Esta complejidad no puede eliminarse simplemente cambiando herramientas o metodologías. Estamos resolviendo problemas complejos. |
| 5 | 2 | Los cambios constantes de requisitos son un problema accidental relacionado con el proceso de gestión y comunicación con el cliente. |
| 6 | 4 | _No hay bala de plata_ significa que no existe una única técnica o herramienta capaz de resolver todos los problemas del desarrollo de software. |
| 7 | 1 | _Serializador_ es un concepto técnico de infraestructura, no un concepto del dominio del negocio. El lenguaje ubicuo debe centrarse en términos del dominio. |
| 8 | 2 | En un modelo anémico las entidades solo almacenan datos y la lógica de negocio queda fuera, normalmente en servicios. |
| 9 | 3 | Las entidades se identifican por su identidad única, mientras que los Value Objects se comparan por el valor de sus atributos. |
| 10 | 1 | La primitive obsession consiste en usar tipos primitivos para representar conceptos complejos del dominio en lugar de encapsularlos en objetos específicos. |
| 11 | 2 | La lógica de negocio compleja debe estar en el dominio (entidades, agregados o servicios de dominio), no en los servicios de aplicación. |
| 12 | 2 | Pedido actúa como raíz del agregado y mantiene consistencia sobre sus líneas de pedido. |
| 13 | 3 | Los agregados se diseñan para mantener invariantes de negocio consistentes dentro de sus límites transaccionales. |
| 14 | 2 | La consistencia eventual se usa cuando la sincronización inmediata no es necesaria y puede alcanzarse de manera asíncrona. |
| 15 | 4 | La arquitectura hexagonal desacopla la lógica de negocio mediante puertos y adaptadores, aislándola de sistemas externos. |
| 16 | 3 | La Inyección de Dependencias es una forma concreta de implementar Inversión de Control delegando la creación de dependencias a un componente externo. |
| 17 | 2 | En IoC el control sobre la creación y gestión de dependencias se delega en un servicio o contenedor externo. |
| 18 | 3 | Una provided interface define los servicios que la aplicación ofrece al exterior. |
| 19 | 2 | HATEOAS aporta navegabilidad a una API REST mediante enlaces entre recursos. |
| 20 | 3 | En una API stateless cada petición debe contener toda la información necesaria para procesarse independientemente. |
| 21 | 3 | En filtros y tuberías puede aparecer sobrecarga y latencia debido al paso de datos entre múltiples filtros. |
| 22 | 2 | En pub/sub varios suscriptores pueden reaccionar a un evento, mientras que en colas normalmente un mensaje es consumido por un único consumidor. |
| 23 | 1 | El objetivo principal del testing es encontrar comportamientos incorrectos y detectar defectos. |
| 24 | 1 | Un buen test puede fallar si detecta correctamente un error en el sistema. |
| 25 | 3 | Error → acción humana incorrecta. Defecto → problema en el código. Fallo → manifestación observable durante la ejecución. |
| 26 | 4 | El testing es una técnica dinámica porque ejecuta el programa. |
| 27 | 3 | El defect clustering indica que una pequeña parte de los módulos suele concentrar la mayoría de defectos. |
| 28 | 4 | La paradoja del pesticida indica que repetir siempre los mismos tests hace que pierdan efectividad. |
| 29 | 1 | Para clases de equivalencia interesa probar justo dentro y fuera de los límites válidos. Es mejor que la 3 para probar clases de equivalencia porque tiene menos casos. Sin embargo, la 3 tiene mejores casos de prueba porque explora los valores límite. |
| 30 | 1 | La impedancia objeto-relacional describe las diferencias conceptuales entre objetos y tablas relacionales. |
| 31 | 2 | Los tipos complejos generan problemas de granularidad al mapear objetos a columnas simples. |
| 32 | 2 | PreparedStatement mejora la seguridad evitando inyecciones SQL mediante consultas parametrizadas. La respuesta 1 también es parcialmente correcta, pero no es su objetivo principal. |
| 33 | 2 | Una clave subrogada es una clave artificial generada automáticamente y sin significado de negocio. |
| 34 | 1 | Un objeto recién creado en JPA está en estado transient porque todavía no está gestionado por el contexto de persistencia. |
| 35 | 2 | El análisis estático no ejecuta el programa; el testing sí lo hace. |
| 36 | 1 | Una limitación típica del análisis estático es generar falsos positivos: avisos sobre problemas que realmente no ocurren en ejecución. |
| 37 | 3 | Los linters detectan problemas simples relacionados con estilo, convenciones y patrones básicos de código. |
| 38 | 2 | En integración continua se ejecutan automáticamente tests y análisis estático al integrar cambios en el repositorio. |
