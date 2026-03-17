package pds.umulingo.adapters.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pds.umulingo.domain.model.curso.repository.CursoRepository;
import pds.umulingo.domain.ports.input.dto.CursoDTO;

@RestController
@RequestMapping("/cursos")
public class CursoEndpoint {

    private final CursoRepository cursoRepository;

    public CursoEndpoint(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        List<CursoDTO> cursos = cursoRepository.buscarTodos().stream()
                .map(c -> new CursoDTO(c.getId().valor(), c.getNombre()))
                .toList();
        return ResponseEntity.ok(cursos);
    }
}
