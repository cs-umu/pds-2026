package pds.duolingo.application.dto;

import java.util.List;

public record PreguntaHuecosDTO(String enunciado, List<ParteTextoDTO> partes) implements PreguntaDTO {
}
