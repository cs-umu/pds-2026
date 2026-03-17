package pds.umulingo.domain.ports.input.dto;

import java.util.List;

public record RankingInfoDTO(List<PosicionDTO> posiciones) {

	public static record PosicionDTO(String usuarioId, String nombre, int puntos) { }
}
