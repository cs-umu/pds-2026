package inf.pds.tpv.adapters.jpa.repository;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import inf.pds.tpv.adapters.jpa.entity.ProductoEntity;

//Anotacion de SpringBoot para que sepa que es un repositorio
//Permite hacer operaciones en base de datos directamente
//Ya incluye por defecto las operaciones basicas, para las especificas incluimos aqui los metodos (sin implementacion)
//https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa
@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {
	
	//Spring puede componer consultas SQL a partir del nombre de los metodos
	//https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html
    List<ProductoEntity> findByDescripcionContaining(String descripcion);


}
