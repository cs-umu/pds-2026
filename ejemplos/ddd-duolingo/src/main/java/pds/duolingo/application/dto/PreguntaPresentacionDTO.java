package pds.duolingo.application.dto;

public sealed interface PreguntaPresentacionDTO
        permits PreguntaTestPresentacionDTO, PreguntaAbiertaPresentacionDTO, PreguntaHuecosPresentacionDTO {
    int id();
    String enunciado();
}
