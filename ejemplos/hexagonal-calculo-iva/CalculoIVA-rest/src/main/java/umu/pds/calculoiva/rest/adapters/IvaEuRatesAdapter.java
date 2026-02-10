package umu.pds.calculoiva.rest.adapters;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import umu.pds.calculoimpuestos.port.ObtencionIVA;

/**
 * Adaptador para obtener el IVA basado en https://vatlookup.eu/
 */
public class IvaEuRatesAdapter implements ObtencionIVA {
    private static final String COUNTRY_CODE = "ES";

    private ObjectMapper mapper;
    
    public IvaEuRatesAdapter(@Autowired ObjectMapper mapper) {
    	this.mapper = mapper;
    }
    
	@Override
	public double obtenerPara(TipoIVA tipo) {
		
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.vatlookup.eu/rates/" + COUNTRY_CODE;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
            	String body = response.body();
                
            	System.out.println("IVA para España:");
                System.out.println(body);
            	/*
            	 {"rates": [
            	 	{"name":"Super Reduced","rates":[4]},
            	 	{"name":"Reduced","rates":[10]},
            	 	{"name":"Standard","rates":[21]},
            	 	{"name":"Increased","rates":[]},
            	 	{"name":"Parking","rates":[]}],
            	 	"disclaimer":"Rates data is based on information published by the European Commission, updated 1st January 2025."}
            	 */

                Map<TipoIVA, Double> iva = processTiposIva(body);
            	
            	return iva.getOrDefault(tipo, 0.0);
            } else {
                System.err.println("Error HTTP: " + response.statusCode());
                System.err.println(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
	}

	private Map<TipoIVA, Double> processTiposIva(String body) {
		Map<TipoIVA, Double> iva = new HashMap<>();
		
		JsonNode root = mapper.reader().readTree(body);
		ArrayNode listaRates = (ArrayNode) root.get("rates");
		for (JsonNode jsonNode : listaRates) {
			String nombre = jsonNode.get("name").asString();
			switch(nombre) {
			case "Super Reduced" -> iva.put(TipoIVA.REDUCIDO, ((ArrayNode) jsonNode.get("rates")).get(0).asDouble());
			case "Reduced" -> iva.put(TipoIVA.BASICO, ((ArrayNode) jsonNode.get("rates")).get(0).asDouble());
			case "Standard" -> iva.put(TipoIVA.GENERAL, ((ArrayNode) jsonNode.get("rates")).get(0).asDouble());					
			default -> {}
			}
		}
		return iva;
	}
	
	public static void main(String[] args) {
		double ivaBasico = new IvaEuRatesAdapter(new ObjectMapper()).obtenerPara(TipoIVA.BASICO);
		System.out.println(ivaBasico);
	}
}
