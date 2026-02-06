package pds.duolingo.application.dto;

import java.util.List;

public record PreguntaTestPresentacionDTO(int id, String enunciado, List<String> opciones)
        implements PreguntaPresentacionDTO {
}
