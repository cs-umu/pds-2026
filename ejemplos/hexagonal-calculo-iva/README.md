
# Ejemplo de arquitectura hexagonal en Spring Boot

Esta implementación se basa en el ejemplo de las transparencias
de clase sobre Arquitectura Hexagonal.

Hay dos componentes:
- CalculoIVA-core que implementa la aplicación en sí siguiendo la arquitectura hexagonal.
- CalculoIVA-rest que implementa un adaptador REST y configura la aplicación con adaptadores para servicios externos.

## Configuración

Se ha creado `CalculoIVA-core` como un proyecto Maven independiente.
El objetivo es mostrar que efectivamente `CalculoIVA-core` está efectivamente desacoplado de la parte tecnológica implementada en Spring Boot.

Por tanto:
```
cd CalculoIVA-core
mvn install
```

Una vez instalado este módulo (en `~/.m2`) ya se puede usar 
para compilar y ejecutar el proyecto Spring Boot que proporciona
la interfaz REST del servicio.

```
cd CalculoIVA-rest
./mvnw spring-boot:run
```

## Probar el servicio

Para probar el servicio se puede usar cualquier programa que permita
realizar llamadas HTTP como Postman. Una alternativa simple de línea de comandos es `curl`:

```bash
curl "http://localhost:8080/api/calculo-iva?precio=10&producto=BASICO"
```

El resultado es:

```
{"precioSinIVA":10.0,"tipoProducto":"BASICO","importeIVA":40.0}
```
