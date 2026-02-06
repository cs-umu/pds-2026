package pds.duolingo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import pds.duolingo.application.EjecucionDeCursoService;
import pds.duolingo.application.dto.ParteTextoPresentacionDTO;
import pds.duolingo.application.dto.PreguntaAbiertaPresentacionDTO;
import pds.duolingo.application.dto.PreguntaHuecosPresentacionDTO;
import pds.duolingo.application.dto.PreguntaPresentacionDTO;
import pds.duolingo.application.dto.PreguntaTestPresentacionDTO;
import pds.duolingo.application.dto.RespuestaDTO;
import pds.duolingo.application.dto.RespuestaHuecosDTO;
import pds.duolingo.application.dto.RespuestaOpcionDTO;
import pds.duolingo.application.dto.RespuestaTextoDTO;

public class ConsolaDuolingo {
    private final EjecucionDeCursoService progresoService;
    private final Scanner scanner;

    public ConsolaDuolingo(EjecucionDeCursoService progresoService) {
        this.progresoService = progresoService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar(String progresoId) {
        System.out.println("===========================================");
        System.out.println("       DUOLINGO - Curso Interactivo        ");
        System.out.println("===========================================");
        System.out.println();

        boolean continuar = true;
        while (continuar) {
            Optional<PreguntaPresentacionDTO> preguntaOpt = progresoService.presentarPregunta(progresoId);

            if (preguntaOpt.isEmpty()) {
                mostrarFinCurso();
                continuar = false;
            } else {
                PreguntaPresentacionDTO pregunta = preguntaOpt.get();
                mostrarPregunta(pregunta);
                RespuestaDTO respuesta = leerRespuesta(pregunta);
                boolean correcta = progresoService.responderPregunta(progresoId, respuesta);
                if (correcta) {
                	System.out.println("Es correcta!");
                } else {
                	System.out.println("No es correcta!");
                }
            }
        }
    }

   
    private void mostrarPregunta(PreguntaPresentacionDTO pregunta) {
        System.out.println("-------------------------------------------");

        switch(pregunta) {
			case PreguntaTestPresentacionDTO p    -> mostrarPreguntaTest(p);
			case PreguntaAbiertaPresentacionDTO p -> mostrarPreguntaAbierta(p);
			case PreguntaHuecosPresentacionDTO p  -> mostrarPreguntaHuecos(p);
        }
    }

    private void mostrarPreguntaTest(PreguntaTestPresentacionDTO pregunta) {
        System.out.println("[PREGUNTA TEST]");
        System.out.println();
        System.out.println(pregunta.enunciado());
        System.out.println();

        List<String> opciones = pregunta.opciones();
        for (int i = 0; i < opciones.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, opciones.get(i));
        }
        System.out.println();
    }

    private void mostrarPreguntaAbierta(PreguntaAbiertaPresentacionDTO pregunta) {
        System.out.println("[PREGUNTA ABIERTA]");
        System.out.println();
        System.out.println(pregunta.enunciado());
        System.out.println();
    }

    private void mostrarPreguntaHuecos(PreguntaHuecosPresentacionDTO pregunta) {
        System.out.println("[COMPLETA LOS HUECOS]");
        System.out.println();
        System.out.println(pregunta.enunciado());
        System.out.println();

        StringBuilder textoConHuecos = new StringBuilder();
        int numHueco = 1;
        for (ParteTextoPresentacionDTO parte : pregunta.partes()) {
            if (parte.esHueco()) {
                textoConHuecos.append("[___").append(numHueco++).append("___]");
            } else {
                textoConHuecos.append(parte.texto());
            }
        }
        System.out.println(textoConHuecos);
        System.out.println();
    }

    private RespuestaDTO leerRespuesta(PreguntaPresentacionDTO pregunta) {
        if (pregunta instanceof PreguntaTestPresentacionDTO pt) {
            return leerRespuestaTest(pt);
        } else if (pregunta instanceof PreguntaAbiertaPresentacionDTO) {
            return leerRespuestaAbierta();
        } else if (pregunta instanceof PreguntaHuecosPresentacionDTO ph) {
            return leerRespuestaHuecos(ph);
        }
        throw new IllegalArgumentException("Tipo de pregunta no soportado");
    }

    private RespuestaOpcionDTO leerRespuestaTest(PreguntaTestPresentacionDTO pregunta) {
        int numOpciones = pregunta.opciones().size();
        int opcion = -1;

        while (opcion < 1 || opcion > numOpciones) {
            System.out.printf("Selecciona una opcion (1-%d): ", numOpciones);
            String input = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
                if (opcion < 1 || opcion > numOpciones) {
                    System.out.println("Opcion no valida. Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un numero.");
            }
        }

        return new RespuestaOpcionDTO(opcion - 1); // Convertir a indice 0-based
    }

    private RespuestaTextoDTO leerRespuestaAbierta() {
        System.out.print("Tu respuesta: ");
        String respuesta = scanner.nextLine();
        return new RespuestaTextoDTO(respuesta);
    }

    private RespuestaHuecosDTO leerRespuestaHuecos(PreguntaHuecosPresentacionDTO pregunta) {
        int numHuecos = contarHuecos(pregunta);
        List<String> respuestas = new ArrayList<>();

        for (int i = 1; i <= numHuecos; i++) {
            System.out.printf("Hueco %d: ", i);
            String respuesta = scanner.nextLine();
            respuestas.add(respuesta);
        }

        return new RespuestaHuecosDTO(respuestas);
    }

    private int contarHuecos(PreguntaHuecosPresentacionDTO pregunta) {
        int count = 0;
        for (ParteTextoPresentacionDTO parte : pregunta.partes()) {
            if (parte.esHueco()) {
                count++;
            }
        }
        return count;
    }

    private void mostrarFinCurso() {
        System.out.println("===========================================");
        System.out.println("        CURSO COMPLETADO!                  ");
        System.out.println("===========================================");
        System.out.println();
        System.out.println("Has terminado todas las preguntas.");
        System.out.println("Gracias por usar Duolingo!");
        System.out.println();
    }
}
