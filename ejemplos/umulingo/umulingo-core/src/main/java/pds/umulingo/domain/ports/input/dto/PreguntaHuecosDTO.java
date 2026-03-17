package pds.umulingo.domain.ports.input.dto;

import java.util.List;

public record PreguntaHuecosDTO(String enunciado, List<ParteTextoDTO> partes) implements PreguntaDTO {
}
