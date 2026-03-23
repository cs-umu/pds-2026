package pds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pds.ejemplos.PersistenciaSimple;

@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private PersistenciaSimple simple;

    //Para el ejercicio 3
    //@Autowired
    //private PersistenciaConsultas consultas;

    //Para el ejercicio 4 y 5
    //@Autowired
    //private PersistenciaConRepositorios jpaRepos;
 
    @Override
    public void run(String... args) {
    	simple.ejecutar();
    }
}
