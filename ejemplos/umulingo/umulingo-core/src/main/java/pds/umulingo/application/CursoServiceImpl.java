package pds.umulingo.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;
import pds.umulingo.domain.model.curso.model.PreguntaHuecos.ParteTexto;
import pds.umulingo.domain.model.curso.repository.CursoRepository;
import pds.umulingo.domain.model.cursoenejecucion.id.PreguntaId;
import pds.umulingo.domain.ports.input.CursoService;
import pds.umulingo.domain.ports.input.dto.CursoDTO;
import pds.umulingo.domain.ports.input.dto.ParteTextoDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaAbiertaDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaHuecosDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaTestDTO;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public CursoDTO crearCurso(String cursoId, String nombre) {
    	CursoId id = new CursoId(cursoId);
    	Preconditions.checkArgument(cursoRepository.findById(id) == null);
    	Curso curso = new Curso(id, nombre);
        cursoRepository.save(curso);
        return toDTO(curso);
    }

    @Override
    public int agregarPregunta(String cursoId, PreguntaDTO dto) {
        CursoId cursoIdVO = new CursoId(cursoId);
        Curso curso = Preconditions.checkNotNull(cursoRepository.findById(cursoIdVO));
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

        cursoRepository.save(curso);
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
