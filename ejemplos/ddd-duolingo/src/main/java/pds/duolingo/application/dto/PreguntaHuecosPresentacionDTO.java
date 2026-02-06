package pds.duolingo.application.dto;

import java.util.List;

public record PreguntaHuecosPresentacionDTO(int id, String enunciado, List<ParteTextoPresentacionDTO> partes)
        implements PreguntaPresentacionDTO {
}
