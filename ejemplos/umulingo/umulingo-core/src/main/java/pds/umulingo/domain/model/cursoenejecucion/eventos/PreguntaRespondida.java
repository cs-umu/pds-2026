package pds.umulingo.domain.model.cursoenejecucion.eventos;

import pds.umulingo.common.events.EventoDominio;
import pds.umulingo.domain.model.cursoenejecucion.id.PreguntaId;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

public record PreguntaRespondida(UsuarioId usuarioId, PreguntaId pregunta, boolean correcta)
	implements EventoDominio {

}
