package pds.umulingo.domain.ports.input.dto;

public sealed interface PreguntaDTO
        permits PreguntaAbiertaDTO, PreguntaTestDTO, PreguntaHuecosDTO {
    String enunciado();
}
