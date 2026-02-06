package pds.duolingo.application;

import java.util.List;

import pds.duolingo.application.dto.CursoDTO;
import pds.duolingo.application.dto.ParteTextoDTO;
import pds.duolingo.application.dto.PreguntaAbiertaDTO;
import pds.duolingo.application.dto.PreguntaDTO;
import pds.duolingo.application.dto.PreguntaHuecosDTO;
import pds.duolingo.application.dto.PreguntaTestDTO;
import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.curso.model.Curso;
import pds.duolingo.domain.curso.model.PreguntaHuecos.ParteTexto;
import pds.duolingo.domain.curso.repository.CursoRepository;
import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;

public class CursoService {
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public CursoDTO crearCurso(String nombre) {
        Curso curso = new Curso(CursoId.nuevo(), nombre);
        cursoRepository.guardar(curso);
        return toDTO(curso);
    }

    public int agregarPregunta(String cursoId, PreguntaDTO dto) {
        CursoId cursoIdVO = new CursoId(cursoId);
        Curso curso = cursoRepository.findById(cursoIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + cursoId));
        PreguntaId id;

        switch(dto) {
			case PreguntaAbiertaDTO(String enunciado, String respuestaCorrecta) -> {
				id = curso.nuevaPreguntaAbierta(enunciado, respuestaCorrecta);
			}
			case PreguntaTestDTO(String enunciado, List<String> opciones, int opcionCorrecta) -> {
				id = curso.nuevaPreguntaTest(enunciado, opciones, opcionCorrecta);
			}
			case PreguntaHuecosDTO(String enunciado, List<ParteTextoDTO> partesDto) -> {
	            List<ParteTexto> partes = partesDto.stream()
	                    .map(p -> p.esHueco() ? ParteTexto.hueco(p.texto()) : ParteTexto.texto(p.texto()))
	                    .toList();
	            id = curso.nuevaPreguntaHuecos(enunciado, partes);
			}
        }

        cursoRepository.guardar(curso);
        return id.indice();
    }

    private CursoDTO toDTO(Curso curso) {
        return new CursoDTO(curso.getId().valor(), curso.getNombre());
    }

    /*
    public void cambiarOrganizacion(CursoId cursoId, OrganizacionCurso organizacion) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + cursoId));
        curso.setOrganizacion(organizacion);
        cursoRepository.guardar(curso);
    }

    public Optional<Curso> obtenerCurso(CursoId cursoId) {
        return cursoRepository.findById(cursoId);
    }

    public List<Curso> obtenerTodosLosCursos() {
        return cursoRepository.buscarTodos();
    }

    public void eliminarCurso(CursoId cursoId) {
        cursoRepository.eliminar(cursoId);
    }
    */
}
