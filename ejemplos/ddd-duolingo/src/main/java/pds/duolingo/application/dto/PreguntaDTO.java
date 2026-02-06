package pds.duolingo.application.dto;

public sealed interface PreguntaDTO
        permits PreguntaAbiertaDTO, PreguntaTestDTO, PreguntaHuecosDTO {
    String enunciado();
}
