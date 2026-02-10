package umu.pds.calculoiva.rest.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esto es necesario porque CalculoIvaRestApplication no está definido en
// el paquete raiz (umu.pds.calculoiva.rest) y por tanto hay que decirle
// a Spring en qué sitios debe buscar components. La razón es que Spring
// empieza a buscar en el paquete donde esté definida la clase principal.
@SpringBootApplication(scanBasePackages = "umu.pds.calculoiva.rest")
public class CalculoIvaRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculoIvaRestApplication.class, args);
	}

}
