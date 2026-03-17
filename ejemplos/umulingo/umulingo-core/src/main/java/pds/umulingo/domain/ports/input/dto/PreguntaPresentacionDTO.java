package pds.umulingo.domain.ports.input.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PreguntaTestPresentacionDTO.class,   name = "TEST"),
    @JsonSubTypes.Type(value = PreguntaAbiertaPresentacionDTO.class, name = "ABIERTA"),
    @JsonSubTypes.Type(value = PreguntaHuecosPresentacionDTO.class,  name = "HUECOS")
})
public sealed interface PreguntaPresentacionDTO
        permits PreguntaTestPresentacionDTO, PreguntaAbiertaPresentacionDTO, PreguntaHuecosPresentacionDTO {
    int id();
    String enunciado();
}
