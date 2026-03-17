package pds.umulingo.application.dto;

import java.util.List;

public sealed interface RespuestaDTO permits 
		RespuestaDTO.RespuestaOpcionDTO, 
        RespuestaDTO.RespuestaTextoDTO, 
        RespuestaDTO.RespuestaHuecosDTO {

	public record RespuestaHuecosDTO(List<String> huecosCompletados) implements RespuestaDTO {
	}
	
	public record RespuestaOpcionDTO(int opcionSeleccionada) implements RespuestaDTO {
	}

	public record RespuestaTextoDTO(String texto) implements RespuestaDTO {
	}
}
