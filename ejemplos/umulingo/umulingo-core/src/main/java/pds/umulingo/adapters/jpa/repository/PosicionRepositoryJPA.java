package pds.umulingo.adapters.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pds.umulingo.adapters.jpa.entity.PosicionUsuarioEntity;

@Repository
public interface PosicionRepositoryJPA extends JpaRepository<PosicionUsuarioEntity, String> {

	@Query("select p from PosicionUsuarioEntity p order by p.puntos")
	List<PosicionUsuarioEntity> findAllOrdered();
}
