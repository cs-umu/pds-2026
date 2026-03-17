package pds.umulingo.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.fusesource.jansi.AnsiConsole;

import pds.umulingo.application.dto.CursoDTO;
import pds.umulingo.application.dto.ParteTextoPresentacionDTO;
import pds.umulingo.application.dto.PreguntaAbiertaPresentacionDTO;
import pds.umulingo.application.dto.PreguntaHuecosPresentacionDTO;
import pds.umulingo.application.dto.PreguntaPresentacionDTO;
import pds.umulingo.application.dto.PreguntaTestPresentacionDTO;
import pds.umulingo.application.dto.UsuarioDTO;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name = "umulingo", mixinStandardHelpOptions = true, version = "1.0",
         description = "Aprendizaje de idiomas")
public class ConsolaApiClient implements Callable<Integer> {

    private static final int ANCHO = 56;

    private final Scanner sc = new Scanner(System.in);
    private ControladorJuego controlador;

    @Option(names = {"-h", "--host"}, description = "Host")
    private String baseURL = "http://localhost:8080";

    @Override
    public Integer call() throws Exception {
        controlador = new ControladorJuego(baseURL);
        ejecutar();
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new ConsolaApiClient()).execute(args);
        System.exit(exitCode);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Flujo principal
    // ════════════════════════════════════════════════════════════════════════

    // ── Resultado del bucle de juego ─────────────────────────────────────────
    sealed interface ResultadoJuego {
        record Completado(int correctas, int total) implements ResultadoJuego {}
        record Abandonado()                         implements ResultadoJuego {}
    }
    
    public void ejecutar() {
        AnsiConsole.systemInstall();
        try {
            mostrarBienvenida();
            Optional<UsuarioDTO> usuarioOpt = menuAutenticacion();
            if (usuarioOpt.isEmpty()) return;
            UsuarioDTO usuario = usuarioOpt.get();

            boolean salir = false;
            while (!salir) {
                int opcion = mostrarMenuPrincipal();
                switch (opcion) {
                    case 1 -> {
                        CursoDTO curso = seleccionarCurso();
                        controlador.inscribir(curso.id());
                        controlador.iniciarEjecucion(curso.id());
                        manejarResultado(jugar(), curso.id());
                    }
                    case 2 -> {
                        String cursoId = seleccionarCursoInscrito(usuario.id());
                        if (cursoId != null) {
                            controlador.iniciarEjecucion(cursoId);
                            manejarResultado(jugar(), cursoId);
                        }
                    }
                    case 3 -> mostrarRanking(usuario.id());
                    case 4 -> salir = true;
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println(ansi().fg(RED).bold().a("\n  Error inesperado: ").a(e.getMessage()).reset());
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    private void manejarResultado(ResultadoJuego resultado, String cursoId) {
        switch (resultado) {
            case ResultadoJuego.Completado c -> {
                mostrarResumen(c.correctas(), c.total());
            }
            case ResultadoJuego.Abandonado a -> {
                System.out.println(ansi().fg(YELLOW).a("  ↩  Curso pausado. Puedes reanudarlo desde el menú.").reset());
                System.out.println();
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Autenticación / registro de usuario
    // ════════════════════════════════════════════════════════════════════════

    private Optional<UsuarioDTO> menuAutenticacion() throws IOException, InterruptedException {
        while (true) {
            System.out.println(ansi().fgBright(CYAN).bold().a("  Acceso").reset());
            System.out.println(ansi().fg(BLUE).a("  " + "─".repeat(ANCHO)).reset());
            System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[1] ").reset()
                    .fg(WHITE).a("Iniciar sesión").reset());
            System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[2] ").reset()
                    .fg(WHITE).a("Registrarse").reset());
            System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[3] ").reset()
                    .fg(WHITE).a("Salir").reset());
            System.out.println();

            int opcion = leerEntero("  Selecciona una opción", 1, 3);
            switch (opcion) {
                case 1 -> {
                    Optional<UsuarioDTO> usuario = iniciarSesion();
                    if (usuario.isPresent()) return usuario;
                }
                case 2 -> { return Optional.of(registrarse()); }
                case 3 -> { return Optional.empty(); }
            }
        }
    }

    private Optional<UsuarioDTO> iniciarSesion() throws IOException, InterruptedException {
        System.out.print(ansi().fg(CYAN).a("  ✉  Email: ").reset());
        String email = sc.nextLine().trim();

        Optional<UsuarioDTO> existente = controlador.iniciarSesión(email);
        if (existente.isPresent()) {
            System.out.println(ansi().fg(GREEN).a("  👋 ¡Hola de nuevo, ")
                    .bold().a(existente.get().nombre()).reset().fg(GREEN).a("!").reset());
            System.out.println();
            return existente;
        }

        System.out.println(ansi().fg(RED).a("  ✘  Usuario no encontrado.").reset());
        System.out.println();
        return Optional.empty();
    }

    private UsuarioDTO registrarse() throws IOException, InterruptedException {
        System.out.print(ansi().fg(CYAN).a("  ✉  Email: ").reset());
        String email = sc.nextLine().trim();
        System.out.print(ansi().fg(CYAN).a("  👤 Nombre: ").reset());
        String nombre = sc.nextLine().trim();

        UsuarioDTO nuevo = controlador.registrarUsuario(email, nombre);
        System.out.println(ansi().fg(GREEN).a("  ✅ ¡Bienvenido, ")
                .bold().a(nuevo.nombre()).reset().fg(GREEN).a("!").reset());
        System.out.println();
        return nuevo;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Menú principal
    // ════════════════════════════════════════════════════════════════════════

    private int mostrarMenuPrincipal() {
        System.out.println(ansi().fgBright(CYAN).bold().a("  ¿Qué quieres hacer?").reset());
        System.out.println(ansi().fg(BLUE).a("  " + "─".repeat(ANCHO)).reset());
        System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[1] ").reset()
                .fg(WHITE).a("Empezar un nuevo curso").reset());
        System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[2] ").reset()
                .fg(WHITE).a("Retomar un curso inscrito").reset());
        System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[3] ").reset()
                .fg(WHITE).a("Ver ranking").reset());
        System.out.println(ansi().fg(WHITE).a("    ").fgBright(YELLOW).bold().a("[4] ").reset()
                .fg(WHITE).a("Salir").reset());
        System.out.println();
        return leerEntero("  Selecciona una opción", 1, 4);
    }

    private String seleccionarCursoInscrito(String usuarioId) throws IOException, InterruptedException {
        List<ControladorJuego.CursoInscritoInfo> lista = controlador.obtenerInscripciones();

        if (lista.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("  No tienes ningún curso inscrito.").reset());
            System.out.println();
            return null;
        }

        System.out.println(ansi().fgBright(CYAN).bold().a("  📚 Tus cursos inscritos").reset());
        System.out.println(ansi().fg(BLUE).a("  " + "─".repeat(ANCHO)).reset());
        for (int i = 0; i < lista.size(); i++) {
            String nombre = lista.get(i).nombre();
            // TODO: Los cursos no están pausados, simplemente estás inscrito o no
            boolean pausado = false;
            String etiqueta = pausado ? nombre + ansi().fg(YELLOW).a(" [pausado]").reset() : nombre;
            System.out.println(ansi().fg(WHITE).a("    ")
                    .fgBright(YELLOW).bold().a("[" + (i + 1) + "] ").reset()
                    .fg(WHITE).a(etiqueta).reset());
        }
        System.out.println();

        int sel = leerEntero("  Selecciona un curso", 1, lista.size());
        ControladorJuego.CursoInscritoInfo elegido = lista.get(sel - 1);
        System.out.println(ansi().fg(GREEN).a("  ✔  ").bold().a(elegido.nombre()).reset());
        System.out.println();
        return elegido.cursoId();
    }

    private CursoDTO seleccionarCurso() throws IOException, InterruptedException {
        List<CursoDTO> cursos = controlador.obtenerCursos();

        System.out.println(ansi().fgBright(CYAN).bold().a("  📚 Cursos disponibles").reset());
        System.out.println(ansi().fg(BLUE).a("  " + "─".repeat(ANCHO)).reset());
        for (int i = 0; i < cursos.size(); i++) {
            System.out.println(ansi().fg(WHITE).a("    ")
                    .fgBright(YELLOW).bold().a("[" + (i + 1) + "] ").reset()
                    .fg(WHITE).a(cursos.get(i).nombre()).reset());
        }
        System.out.println();

        int sel = leerEntero("  Selecciona un curso", 1, cursos.size());
        CursoDTO elegido = cursos.get(sel - 1);
        System.out.println(ansi().fg(GREEN).a("  ✔  ").bold().a(elegido.nombre()).reset());
        System.out.println();
        return elegido;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Bucle principal de preguntas y respuestas
    // ════════════════════════════════════════════════════════════════════════

    private ResultadoJuego jugar() throws IOException, InterruptedException {
        ControladorJuego.SiguientePreguntaInfo actual = controlador.obtenerPreguntaActual();

        int correctas   = 0;
        int incorrectas = 0;
        int numPregunta = 1;

        while (actual != null && actual.hayMas()) {
            mostrarCabeceraPregunta(numPregunta);
            mostrarPregunta(actual.pregunta());

            Optional<ControladorJuego.ResponderRequest> peticionOpt = leerRespuesta(actual.pregunta());
            if (peticionOpt.isEmpty()) {
                return new ResultadoJuego.Abandonado();
            }

            ControladorJuego.PreguntaRespondidaInfo resultado =
                    controlador.responder(peticionOpt.get());

            System.out.println();
            if (resultado.esCorrecta()) {
                correctas++;
                System.out.println(ansi().fgBright(GREEN).bold()
                        .a("  ✔  ¡Correcto!").reset());
            } else {
                incorrectas++;
                System.out.println(ansi().fgBright(RED).bold()
                        .a("  ✘  Incorrecto").reset());
            }
            System.out.println();

            numPregunta++;
            actual = resultado.siguiente();
        }

        return new ResultadoJuego.Completado(correctas, correctas + incorrectas);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Banner de bienvenida
    // ════════════════════════════════════════════════════════════════════════

    private void mostrarBienvenida() {
        String linea  = "═".repeat(ANCHO);
        String titulo = "UMULingo  —  Aprende idiomas";
        System.out.println();
        System.out.println(ansi().fgBright(YELLOW).bold().a("  ╔").a(linea).a("╗").reset());
        System.out.print(ansi().fgBright(YELLOW).bold().a("  ║  ").reset());
        System.out.print(ansi().fgBright(GREEN).bold().a(centrar(titulo, ANCHO - 2)).reset());
        System.out.println(ansi().fgBright(YELLOW).bold().a("║").reset());
        System.out.println(ansi().fgBright(YELLOW).bold().a("  ╚").a(linea).a("╝").reset());
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Cabecera de cada pregunta
    // ════════════════════════════════════════════════════════════════════════

    private void mostrarCabeceraPregunta(int num) {
        String sep = "─".repeat(ANCHO + 2);
        System.out.println(ansi().fg(BLUE).a("  " + sep).reset());
        System.out.println(ansi().fgBright(WHITE).bold().a("  Pregunta " + num).reset()
                + "   " + ansi().fg(BLUE).a("(escribe /salir para volver al menú)").reset());
        System.out.println(ansi().fg(BLUE).a("  " + sep).reset());
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Visualización de preguntas (switch sobre el tipo sellado)
    // ════════════════════════════════════════════════════════════════════════

    private void mostrarPregunta(PreguntaPresentacionDTO pregunta) {
        switch (pregunta) {
            case PreguntaTestPresentacionDTO   p -> mostrarTest(p);
            case PreguntaAbiertaPresentacionDTO p -> mostrarAbierta(p);
            case PreguntaHuecosPresentacionDTO  p -> mostrarHuecos(p);
        }
    }

    private void mostrarTest(PreguntaTestPresentacionDTO p) {
        System.out.println(ansi().fg(MAGENTA).bold().a("  [ OPCIÓN MÚLTIPLE ]").reset());
        System.out.println();
        System.out.println(ansi().fgBright(WHITE).a("  " + p.enunciado()).reset());
        System.out.println();
        List<String> opciones = p.opciones();
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println(ansi().fgBright(YELLOW).bold().a("    " + (i + 1) + ". ").reset()
                    .fg(WHITE).a(opciones.get(i)).reset());
        }
        System.out.println();
    }

    private void mostrarAbierta(PreguntaAbiertaPresentacionDTO p) {
        System.out.println(ansi().fg(MAGENTA).bold().a("  [ RESPUESTA LIBRE ]").reset());
        System.out.println();
        System.out.println(ansi().fgBright(WHITE).a("  " + p.enunciado()).reset());
        System.out.println();
    }

    private void mostrarHuecos(PreguntaHuecosPresentacionDTO p) {
        System.out.println(ansi().fg(MAGENTA).bold().a("  [ COMPLETA LOS HUECOS ]").reset());
        System.out.println();
        System.out.println(ansi().fgBright(WHITE).a("  " + p.enunciado()).reset());
        System.out.println();
        System.out.print("  ");
        int n = 1;
        for (ParteTextoPresentacionDTO parte : p.partes()) {
            if (parte.esHueco()) {
                System.out.print(ansi().fgBright(YELLOW).bold().a("[___" + n++ + "___]").reset());
            } else {
                System.out.print(ansi().fg(WHITE).a(parte.texto()).reset());
            }
        }
        System.out.println();
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Lectura de respuestas del usuario
    // ════════════════════════════════════════════════════════════════════════

    private Optional<ControladorJuego.ResponderRequest> leerRespuesta(PreguntaPresentacionDTO pregunta) {
        return switch (pregunta) {
            case PreguntaTestPresentacionDTO p -> {
                OptionalInt n = leerEnteroOSalir("  Tu opción", 1, p.opciones().size());
                yield n.isPresent()
                        ? Optional.of(new ControladorJuego.ResponderRequest(List.of(n.getAsInt() - 1), "OPCION_SIMPLE"))
                        : Optional.empty();
            }
            case PreguntaAbiertaPresentacionDTO ignored -> {
                System.out.print(ansi().fg(CYAN).a("  ✏  Tu respuesta: ").reset());
                String texto = sc.nextLine().trim();
                yield texto.equals("/salir")
                        ? Optional.empty()
                        : Optional.of(new ControladorJuego.ResponderRequest(List.of(texto), "TEXTO"));
            }
            case PreguntaHuecosPresentacionDTO p -> {
                long numHuecos = p.partes().stream()
                        .filter(ParteTextoPresentacionDTO::esHueco).count();
                List<Object> respuestas = new ArrayList<>();
                for (int i = 1; i <= numHuecos; i++) {
                    System.out.print(ansi().fg(CYAN).a("  ✏  Hueco " + i + ": ").reset());
                    String r = sc.nextLine().trim();
                    if (r.equals("/salir")) yield Optional.empty();
                    respuestas.add(r);
                }
                yield Optional.of(new ControladorJuego.ResponderRequest(respuestas, "HUECOS"));
            }
        };
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Ranking global
    // ════════════════════════════════════════════════════════════════════════

    private void mostrarRanking(String usuarioId) throws IOException, InterruptedException {
        ControladorJuego.RankingInfo ranking = controlador.obtenerRanking();
        List<ControladorJuego.PosicionInfo> posiciones = ranking.posiciones();

        String linea = "═".repeat(ANCHO);
        System.out.println(ansi().fgBright(YELLOW).bold().a("  ╔").a(linea).a("╗").reset());
        System.out.print(ansi().fgBright(YELLOW).bold().a("  ║  ").reset());
        System.out.print(ansi().fgBright(GREEN).bold().a(centrar("🏆  Ranking global", ANCHO - 2)).reset());
        System.out.println(ansi().fgBright(YELLOW).bold().a("║").reset());
        System.out.println(ansi().fgBright(YELLOW).bold().a("  ╚").a(linea).a("╝").reset());
        System.out.println();

        if (posiciones == null || posiciones.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("  Aún no hay puntuaciones registradas.").reset());
            System.out.println();
            return;
        }

        String[] medallas = {"🥇", "🥈", "🥉"};
        for (int i = 0; i < posiciones.size(); i++) {
            ControladorJuego.PosicionInfo pos = posiciones.get(i);
            boolean esYo = pos.usuarioId().equals(usuarioId);
            String prefijo = i < medallas.length ? medallas[i] : "   ";
            String posStr  = String.format("%2d.", i + 1);
            String puntos  = pos.puntos() + " pts";

            if (esYo) {
                System.out.println(
                    ansi().fgBright(GREEN).bold()
                        .a("  " + prefijo + " " + posStr + "  ")
                        .a(String.format("%-24s", pos.nombre()))
                        .a("  " + puntos + "  ◀ tú")
                        .reset());
            } else {
                System.out.println(
                    ansi().fg(WHITE)
                        .a("  " + prefijo + " " + posStr + "  ")
                        .a(String.format("%-24s", pos.nombre()))
                        .a("  ")
                        .fg(CYAN).a(puntos)
                        .reset());
            }
        }
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Resumen final
    // ════════════════════════════════════════════════════════════════════════

    private void mostrarResumen(int correctas, int total) {
        int incorrectas = total - correctas;
        double pct = total > 0 ? (100.0 * correctas / total) : 0.0;
        String barraVerde = "█".repeat(correctas);
        String barraRoja  = "░".repeat(incorrectas);

        String linea = "═".repeat(ANCHO);
        System.out.println(ansi().fgBright(YELLOW).bold().a("  ╔").a(linea).a("╗").reset());
        System.out.print(ansi().fgBright(YELLOW).bold().a("  ║  ").reset());
        System.out.print(ansi().fgBright(GREEN).bold().a(centrar("🏆  ¡Curso completado!", ANCHO - 2)).reset());
        System.out.println(ansi().fgBright(YELLOW).bold().a("║").reset());
        System.out.println(ansi().fgBright(YELLOW).bold().a("  ╚").a(linea).a("╝").reset());
        System.out.println();

        System.out.println(ansi().fg(GREEN).a("  ✔  Correctas:    ")
                .fgBright(GREEN).bold().a(correctas + " / " + total).reset());
        System.out.println(ansi().fg(RED).a("  ✘  Incorrectas:  ")
                .fgBright(RED).bold().a(incorrectas + " / " + total).reset());
        System.out.printf(ansi().fg(CYAN).a("  ★  Puntuación:   ")
                .fgBright(CYAN).bold().a("%.0f%%").reset().toString() + "%n", pct);

        System.out.println();
        System.out.print("     ");
        System.out.print(ansi().fgBright(GREEN).a(barraVerde).reset());
        System.out.println(ansi().fg(RED).a(barraRoja).reset());
        System.out.println();
        System.out.println(ansi().fg(YELLOW).a("  El servidor sigue activo en ").bold().a(baseURL).reset());
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Utilidades
    // ════════════════════════════════════════════════════════════════════════

    private int leerEntero(String prompt, int min, int max) {
        while (true) {
            System.out.print(ansi().fg(CYAN).a(prompt + " [" + min + "-" + max + "]: ").reset());
            String linea = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(linea);
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println(ansi().fg(RED).a("  ⚠  Valor inválido, intenta de nuevo.").reset());
        }
    }

    /** Como leerEntero pero acepta /salir, devolviendo OptionalInt vacío en ese caso. */
    private OptionalInt leerEnteroOSalir(String prompt, int min, int max) {
        while (true) {
            System.out.print(ansi().fg(CYAN).a(prompt + " [" + min + "-" + max + "]: ").reset());
            String linea = sc.nextLine().trim();
            if (linea.equals("/salir")) return OptionalInt.empty();
            try {
                int n = Integer.parseInt(linea);
                if (n >= min && n <= max) return OptionalInt.of(n);
            } catch (NumberFormatException ignored) {}
            System.out.println(ansi().fg(RED).a("  ⚠  Valor inválido, intenta de nuevo.").reset());
        }
    }

    private String centrar(String texto, int ancho) {
        int pad = Math.max(0, (ancho - texto.length()) / 2);
        return " ".repeat(pad) + texto;
    }
}
