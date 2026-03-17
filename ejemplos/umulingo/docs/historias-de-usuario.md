# Historias de usuario

Este documento recoge las principales historias de usuario identificadas en UMULingo a partir del análisis del dominio y de los casos de uso implementados.

## Índice

- [US-01. Inscribirse en un curso](#us01)
- [US-02. Iniciar un curso](#us02)
- [US-03. Responder preguntas de un curso](#us03)
- [US-04. Recibir feedback sobre una respuesta](#us04)
- [US-05. Registrar el progreso de un curso](#us05)
- [US-06. Reanudar un curso](#us06)
- [US-07. Consultar el ranking](#us07)
- [US-08. Calcular el ranking de usuarios](#us08)
- [US-09. Definir las preguntas de un curso](#us09)

---

<a id="us01"></a>
## US-01. Inscribirse en un curso

**Como** usuario  
**quiero** inscribirme en un curso  
**para** poder empezar a practicar ese curso en la plataforma.

### Criterios de aceptación

- Un usuario puede inscribirse en un curso disponible.
- Un usuario no puede inscribirse dos veces en el mismo curso.
- La inscripción permite posteriormente iniciar el curso.

---

<a id="us02"></a>
## US-02. Iniciar un curso

**Como** usuario  
**quiero** iniciar un curso en el que estoy inscrito  
**para** comenzar a responder sus preguntas.

### Criterios de aceptación

- Un curso solo puede iniciarse si el usuario está inscrito.
- Al iniciar un curso se crea un **curso en ejecución**.
- El sistema presenta la primera pregunta del curso.

---

<a id="us03"></a>
## US-03. Responder preguntas de un curso

**Como** usuario  
**quiero** responder preguntas de distintos tipos  
**para** practicar el idioma de diferentes formas.

### Criterios de aceptación

- El sistema soporta preguntas abiertas, de test y de huecos.
- El usuario puede introducir una respuesta para cada pregunta.
- El sistema registra la respuesta del usuario.

---

<a id="us04"></a>
## US-04. Recibir feedback sobre una respuesta

**Como** usuario  
**quiero** saber si mi respuesta es correcta o incorrecta  
**para** aprender de mis errores en el momento.

### Criterios de aceptación

- El sistema compara la respuesta del usuario con la respuesta correcta.
- El sistema informa al usuario si la respuesta es correcta o incorrecta.
- El resultado queda registrado en el progreso del curso.

---

<a id="us05"></a>
## US-05. Registrar el progreso de un curso

**Como** usuario  
**quiero** que se guarde mi progreso en un curso  
**para** poder reanudarlo más adelante desde el punto en el que lo dejé.

### Criterios de aceptación

- El sistema registra las preguntas respondidas por el usuario.
- El progreso incluye los resultados obtenidos.
- El progreso queda asociado al usuario y al curso.

---

<a id="us06"></a>
## US-06. Reanudar un curso

**Como** usuario  
**quiero** continuar un curso que ya he empezado  
**para** seguir respondiendo preguntas desde mi último progreso guardado.

### Criterios de aceptación

- El sistema recupera el progreso guardado del usuario.
- El usuario continúa el curso desde la última pregunta no respondida.
- El progreso se sigue actualizando durante la sesión.

---

<a id="us07"></a>
## US-07. Consultar el ranking

**Como** usuario  
**quiero** ver mi posición en el ranking  
**para** comparar mi rendimiento con el de otros usuarios.

### Criterios de aceptación

- El ranking muestra la clasificación de los usuarios.
- Cada usuario puede consultar su posición.
- El ranking refleja el rendimiento acumulado de los usuarios.

---

<a id="us08"></a>
## US-08. Calcular el ranking de usuarios

**Como** administrador  
**quiero** calcular el ranking de usuarios en función de las preguntas respondidas, su tipo y dificultad  
**para** reflejar de forma justa el rendimiento de cada usuario.

### Criterios de aceptación

- El ranking se calcula a partir de las preguntas respondidas por los usuarios.
- El cálculo tiene en cuenta el tipo de pregunta y su dificultad.
- El resultado actualiza la posición de cada usuario en el ranking.

---

<a id="us09"></a>
## US-09. Definir las preguntas de un curso

**Como** administrador  
**quiero** definir las preguntas que forman parte de un curso  
**para** crear ejercicios que los usuarios puedan resolver.

### Criterios de aceptación

- Un curso puede contener distintas preguntas.
- El sistema soporta preguntas abiertas, de test y de huecos.
- Cada pregunta pertenece a un curso.
- Cada pregunta tiene definida una respuesta correcta.
