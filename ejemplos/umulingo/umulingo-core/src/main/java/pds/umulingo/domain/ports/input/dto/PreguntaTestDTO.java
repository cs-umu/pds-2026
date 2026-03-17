package pds.umulingo.domain.ports.input.dto;

import java.util.List;

public record PreguntaTestDTO(String enunciado, List<String> opciones, int opcionCorrecta) implements PreguntaDTO {
}
