package umu.pds.calculoiva.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import umu.pds.calculoIva.domain.model.TipoProducto;
import umu.pds.calculoimpuestos.impl.CalculadorImpuestos;

@RestController
public class CalculoIvaController {

	private final CalculadorImpuestos calculadorImpuestos;

	public CalculoIvaController(CalculadorImpuestos calculadorImpuestos) {
		this.calculadorImpuestos = calculadorImpuestos;
	}

	@GetMapping("/api/calculo-iva")
	public CalculoIvaResponse calcularIva(
			@RequestParam double precio,
			@RequestParam TipoProducto producto) {

		double importeIVA = calculadorImpuestos.calcular(precio, producto);
		return new CalculoIvaResponse(precio, producto.name(), importeIVA);
	}
}
