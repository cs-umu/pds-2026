package pds.duolingo.domain.cursoenejecucion.eventos;

import pds.duolingo.common.events.EventoDominio;
import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;
import pds.duolingo.domain.usuario.id.UsuarioId;

public record PreguntaRespondida(UsuarioId usuarioId, PreguntaId pregunta, boolean correcta)
	implements EventoDominio {

}
