package umu.pds.calculoiva.rest.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.ObjectMapper;
import umu.pds.calculoimpuestos.impl.CalculadorImpuestos;
import umu.pds.calculoimpuestos.port.ObtencionIVA;
import umu.pds.calculoiva.rest.adapters.IvaEuRatesAdapter;

@Configuration
public class CalculoIvaConfig {

	@Bean
	ObtencionIVA obtencionIVA(ObjectMapper objectMapper) {
		return new IvaEuRatesAdapter(objectMapper);
	}

	@Bean
	CalculadorImpuestos calculadorImpuestos(ObtencionIVA obtencionIVA) {
		return new CalculadorImpuestos(obtencionIVA);
	}
}
