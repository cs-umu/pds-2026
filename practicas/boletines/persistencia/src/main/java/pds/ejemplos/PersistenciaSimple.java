package pds.ejemplos;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pds.modelo.Admin;
import pds.modelo.Autor;
import pds.modelo.Categoria;
import pds.modelo.Cliente;
import pds.modelo.Libro;

@Component
public class PersistenciaSimple {
    @PersistenceContext
    private EntityManager em;

    // Las transacciones del EntityManager son gestionadas automáticamente por Spring
    // Es equivalente a em.getTransaction().begin() - código - em.getTransaction().commit();
    @Transactional
    public void ejecutar() {    	
    	// Crear datos de prueba
        Autor author = new Autor("George Orwell");
        em.persist(author);

        Libro book = new Libro("1984", author);
        em.persist(book);

        Categoria category = new Categoria("Distópico");
        em.persist(category);

        book.addCategory(category);
        em.persist(book);

        Admin admin = new Admin("root", "ALL");
        em.persist(admin);

        Cliente customer = new Cliente("Juan", "C/Espinardo");
        em.persist(customer);
    }
}
