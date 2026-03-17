package pds.umulingo.adapters.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Nullable;
import pds.umulingo.adapters.rest.requests.IniciarCursoRequest;
import pds.umulingo.adapters.rest.requests.ResponderPreguntaRequest;
import pds.umulingo.domain.ports.input.EjecucionDeCursoService;
import pds.umulingo.domain.ports.input.dto.PreguntaPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.ProgresoDTO;
import pds.umulingo.domain.ports.input.dto.RespuestaDTO;

@RestController
@RequestMapping("/ejecucion")
@Validated
public class EjecucionCursoEndpoint {

	private static final Logger log = LoggerFactory.getLogger(EjecucionCursoEndpoint.class);

	private EjecucionDeCursoService ejecucionService;
	
	public EjecucionCursoEndpoint(EjecucionDeCursoService ejecucionService) {
		this.ejecucionService = ejecucionService;
	}

	@PostMapping
	public ResponseEntity<ProgresoDTO> iniciarCurso(@RequestBody IniciarCursoRequest request) {
		log.info("Creando nueva instancia de curso para {}", request.cursoId());
		ProgresoDTO progreso = this.ejecucionService.iniciarCurso(request.usuarioId(), request.cursoId());
		return ResponseEntity.status(HttpStatus.OK).body(progreso);
	}

	@GetMapping("/{progresoId}")
	public ResponseEntity<SiguientePreguntaResponse> presentarPregunta(@PathVariable String progresoId) {
		log.info("Presentar pregunta para progreso {}", progresoId);
		SiguientePreguntaResponse respuesta = getSiguientePregunta(progresoId);
		return ResponseEntity.status(HttpStatus.OK).body(respuesta);
	}
	
	@PutMapping("/{progresoId}")
	public ResponseEntity<PreguntaRespondidaResponse> responderPregunta(@PathVariable String progresoId, @RequestBody ResponderPreguntaRequest request) {
		RespuestaDTO respuesta;
		switch(request.tipo()) {
		case HUECOS:
			respuesta = new RespuestaDTO.RespuestaHuecosDTO(request.respuestas().stream().map(r -> (String) r).toList());
			break;
		case OPCION_SIMPLE:
			respuesta = new RespuestaDTO.RespuestaOpcionDTO((int) request.respuestas().get(0));
			break;
		case TEXTO:
			respuesta = new RespuestaDTO.RespuestaTextoDTO((String) request.respuestas().get(0));
			break;
		default:
			throw new IllegalStateException();
		}
		
		boolean esCorrecta = this.ejecucionService.responderPregunta(progresoId, respuesta);
		SiguientePreguntaResponse siguientePregunta = getSiguientePregunta(progresoId);
		
		return ResponseEntity.status(HttpStatus.OK).body(new PreguntaRespondidaResponse(esCorrecta, siguientePregunta));
	}
	
	private SiguientePreguntaResponse getSiguientePregunta(String progresoId) {
		Optional<PreguntaPresentacionDTO> progreso = this.ejecucionService.presentarPregunta(progresoId);
		
		SiguientePreguntaResponse respuesta = progreso.map(p -> new SiguientePreguntaResponse(true, p)).
				orElse(SiguientePreguntaResponse.NO_PREGUNTA);
		return respuesta;
	}
	
	private static record SiguientePreguntaResponse(boolean hayMas, @Nullable PreguntaPresentacionDTO pregunta) {
		private static final SiguientePreguntaResponse NO_PREGUNTA = new SiguientePreguntaResponse(false, null);
	}
	
	private static record PreguntaRespondidaResponse(boolean esCorrecta, SiguientePreguntaResponse siguiente) {
	}

}
