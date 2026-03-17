package pds.umulingo.application.dto;

public sealed interface PreguntaDTO
        permits PreguntaAbiertaDTO, PreguntaTestDTO, PreguntaHuecosDTO {
    String enunciado();
}
