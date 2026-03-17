package pds.umulingo.infrastructure;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pds.umulingo.application.CursoServiceImpl;
import pds.umulingo.domain.ports.input.dto.ParteTextoDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaAbiertaDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaHuecosDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaTestDTO;

@Component
@Order(1)
public class DataInitializer implements ApplicationRunner {

    private final CursoServiceImpl cursoService;

    public DataInitializer(CursoServiceImpl cursoService) {
        this.cursoService = cursoService;
    }

    @Override
    public void run(ApplicationArguments args) {
        crearCursoIngles();
        crearCursoFrances();
    }

    private void crearCursoIngles() {
        var curso = cursoService.crearCurso("ingles-basico", "Inglés Básico");

        cursoService.agregarPregunta(curso.id(), new PreguntaTestDTO(
                "¿Cómo se dice 'gato' en inglés?",
                List.of("Dog", "Cat", "Bird", "Fish"),
                1));

        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO(
                "Traduce al inglés: 'Hola, ¿cómo estás?'",
                "Hello, how are you?"));

        cursoService.agregarPregunta(curso.id(), new PreguntaHuecosDTO(
                "Completa la oración: 'Soy un estudiante'",
                List.of(
                        new ParteTextoDTO("I ", false),
                        new ParteTextoDTO("am", true),
                        new ParteTextoDTO(" a ", false),
                        new ParteTextoDTO("student", true),
                        new ParteTextoDTO(".", false))));

        cursoService.agregarPregunta(curso.id(), new PreguntaTestDTO(
                "¿Cuál es el plural de 'child'?",
                List.of("Childs", "Children", "Childes", "Child"),
                1));
    }

    private void crearCursoFrances() {
        var curso = cursoService.crearCurso("frances-basico", "Français Básico");

        cursoService.agregarPregunta(curso.id(), new PreguntaTestDTO(
                "¿Cómo se dice 'gracias' en francés?",
                List.of("Bonjour", "Merci", "S'il vous plaît", "Au revoir"),
                1));

        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO(
                "Traduce al español: 'Bonjour, comment allez-vous?'",
                "Hola, ¿cómo está usted?"));

        cursoService.agregarPregunta(curso.id(), new PreguntaHuecosDTO(
                "Completa: 'Je ___ étudiant' (Soy estudiante)",
                List.of(
                        new ParteTextoDTO("Je ", false),
                        new ParteTextoDTO("suis", true),
                        new ParteTextoDTO(" étudiant", false))));
    }
}
