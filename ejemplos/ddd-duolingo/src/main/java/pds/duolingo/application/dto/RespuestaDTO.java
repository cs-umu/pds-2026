package pds.duolingo.application.dto;

public sealed interface RespuestaDTO
        permits RespuestaOpcionDTO, RespuestaTextoDTO, RespuestaHuecosDTO {
}
