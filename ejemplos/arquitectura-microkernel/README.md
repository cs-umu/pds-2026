
# Microkernel

El estilo arquitectónico de Microkernel o arquitectura de plug-ins, tiene como objetivo crear aplicaciones que sean extensibles ``externamente'', esto es, que se pueda agregar nueva funcionalidad a la aplicación desde módulos creados externamente.

Este ejemplo simula el núcleo de una arquitectura de microkernel.
 * El sistema tiene como objetivo saludar en diversos idiomas, pero los idiomas
 * se configuran externamente como plugins.

## Ejecución

Para probar el ejemplo hay que ejecutar la clase `microkernel.nucleo.Main`.

En la `Run configuration` de Eclipse hay que ir a la pestaña `Dependencias` y añadir una referencia al proyecto `microkernel.extensiones`. 
Esto es necesario para que el _CLASSPATH_ de Java incluya las clases de las extensiones y se puedan cargar de manera dinámica. 
