package pds.umulingo.adapters.jpa;

import java.util.List;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;
import pds.umulingo.domain.model.curso.repository.CursoRepository;

// Por el momento usamos la versión en memoria
public class CursoRepositoryImpl implements CursoRepository {

	@Override
	public void save(Curso curso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Curso findById(CursoId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Curso> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(CursoId id) {
		// TODO Auto-generated method stub
		
	}

}
