package pds.umulingo.adapters.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pds.umulingo.adapters.rest.requests.RegistrarUsuarioRequest;
import pds.umulingo.domain.ports.input.InscripcionService;
import pds.umulingo.domain.ports.input.UsuarioService;
import pds.umulingo.domain.ports.input.dto.InscripcionDTO;
import pds.umulingo.domain.ports.input.dto.InscripcionesUsuarioDTO;
import pds.umulingo.domain.ports.input.dto.UsuarioDTO;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioEndpoint {

    private static final Logger log = LoggerFactory.getLogger(UsuarioEndpoint.class);

    private final UsuarioService usuarioService;
    private final InscripcionService inscripcionService;

    public UsuarioEndpoint(UsuarioService usuarioService, InscripcionService inscripcionService) {
        this.usuarioService = usuarioService;
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistrarUsuarioRequest request) {
        try {
            UsuarioDTO usuario = usuarioService.registrarUsuario(request.email(), request.nombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> obtener(@PathVariable String usuarioId) {
        return usuarioService.obtenerUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{usuarioId}/inscripcion")
    public ResponseEntity<InscripcionDTO> inscribir(@PathVariable String usuarioId,
                                                     @RequestParam String cursoId) {
        InscripcionDTO inscripcion = inscripcionService.inscribirUsuario(usuarioId, cursoId);
        return ResponseEntity.status(HttpStatus.OK).body(inscripcion);
    }
    
    @GetMapping("/{usuarioId}/inscripcion")
    public ResponseEntity<InscripcionesUsuarioDTO> listarInscripciones(@PathVariable String usuarioId) {
    	InscripcionesUsuarioDTO resultado = inscripcionService.cursosInscrito(usuarioId);
    	return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
}
