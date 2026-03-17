# Lenguaje ubicuo

Este documento recoge los principales conceptos del dominio utilizados en UMULingo.

El objetivo del lenguaje ubicuo es que desarrolladores y usuarios del sistema utilicen los mismos términos para referirse a los elementos del dominio.

| Concepto                          | Descripción                               |
|:----------------------------------|:------------------------------------------|
| **Curso**                         | Conjunto de preguntas organizadas para el aprendizaje que los usuarios pueden realizar para practicar un idioma. Define el contenido de aprendizaje. |
| **Pregunta**                      | Elemento evaluable que forma parte de un curso y que el usuario debe responder. Puede tener distintos formatos. |
| **Pregunta abierta**              | Tipo de pregunta en la que el usuario introduce una respuesta en texto. |
| **Pregunta de test**              | Tipo de pregunta en la que el usuario debe seleccionar una opción correcta entre varias posibles. |
| **Pregunta de huecos**            | Tipo de pregunta en la que el usuario debe completar una frase rellenando palabras que faltan. |
| **Usuario**                       | Persona que usa la plataforma para practicar cursos de idiomas. |
| **Inscripción**                   | Relación que indica que un usuario está inscrito en un curso. |
| **Curso en ejecución**            | Sesión en la que un usuario realiza un curso y responde sus preguntas. |
| **Respuesta del usuario**         | Respuesta proporcionada por un usuario al intentar resolver una pregunta durante la realización de un curso. |
| **Respuesta correcta**            | Respuesta definida en la configuración de una pregunta que se utiliza como referencia para evaluar si la respuesta del usuario es correcta. |
| **Pregunta respondida**           | Evento que se produce cuando un usuario responde a una pregunta. |
| **Progreso**                      | Estado de avance de un usuario dentro de un curso, incluyendo las preguntas respondidas y los resultados obtenidos. |
| **Organización de preguntas**     | Estrategia de ordenación de preguntas dentro de un curso (por ejemplo, secuencial o aleatoria). |
| **Ranking**                       | Clasificación de usuarios en función de su rendimiento al responder preguntas en la plataforma. |
| **Administrador**                 | Persona responsable de organizar y preparar los cursos, definiendo sus preguntas y su estructura dentro de la plataforma. |


### Notas sobre el lenguaje de dominio

1. **Tipos de preguntas**  
   Los términos *Pregunta abierta*, *Pregunta de test* y *Pregunta de huecos* representan **subtipos de preguntas** dentro del dominio. Todos ellos comparten el concepto base de *Pregunta*, pero se diferencian en la forma en que el usuario introduce la respuesta.

2. **Eventos del dominio**  
   El término *Pregunta respondida* se considera un **evento de dominio**, ya que representa algo que ocurre en el sistema cuando un usuario responde a una pregunta.

3. **Roles de usuario**  
   En el sistema se distinguen dos roles principales:  
   - *Usuario*, que utiliza la plataforma para practicar cursos.  
   - *Administrador*, responsable de definir y organizar los cursos y sus preguntas. No se implementará en esta demo, ya que las preguntas vendrán configuradas en la BBDD.

4. **Curso vs curso en ejecución**  
   Se distingue entre el *Curso* como el contenido de aprendizaje definido en la plataforma (sus preguntas y las respuestas correctas) y el *Curso en ejecución*, que representa la sesión concreta en la que un usuario está realizando ese curso y está monitorizando su progreso y sus respuestas.

5. **Respuesta correcta vs respuesta del usuario**  
   Se distingue entre la *Respuesta correcta*, definida como parte de la configuración de una pregunta dentro de un curso, y la *Respuesta del usuario*, que es la respuesta proporcionada por un usuario al intentar resolverla. La comparación entre ambas permite determinar si una pregunta ha sido respondida correctamente.


