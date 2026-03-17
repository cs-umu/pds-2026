package pds.umulingo.cli;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pds.umulingo.application.dto.CursoDTO;
import pds.umulingo.application.dto.PreguntaPresentacionDTO;
import pds.umulingo.application.dto.ProgresoDTO;
import pds.umulingo.application.dto.UsuarioDTO;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class ControladorJuego {

    // ── Tipos espejo para deserializar respuestas del servidor ───────────────
    record SiguientePreguntaInfo(boolean hayMas, PreguntaPresentacionDTO pregunta) {}
    record PreguntaRespondidaInfo(boolean esCorrecta, SiguientePreguntaInfo siguiente) {}
    record CursoInscritoInfo(String cursoId, String nombre) {}
    record InscripcionesInfo(List<CursoInscritoInfo> inscripciones) {}
    record PosicionInfo(String usuarioId, String nombre, int puntos) {}
    record RankingInfo(List<PosicionInfo> posiciones) {}

    // ── Tipos para las peticiones ────────────────────────────────────────────
    record RegistrarRequest(String email, String nombre) {}
    record IniciarRequest(String usuarioId, String cursoId) {}
    record ResponderRequest(List<Object> respuestas, String tipo) {}

    // ── Infraestructura HTTP ─────────────────────────────────────────────────

    private final String baseURL;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    private UsuarioDTO usuarioActual;
    private ProgresoDTO cursoEnProgreso;
    
    public ControladorJuego(String baseURL) {
        this.baseURL = baseURL;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API: Usuarios
    // ════════════════════════════════════════════════════════════════════════

    public Optional<UsuarioDTO> iniciarSesión(String email) throws IOException, InterruptedException {
        HttpResponse<String> resp = get("/usuarios/" + enc(email));
        if (resp.statusCode() == 200) {
        	UsuarioDTO usuario = json.readValue(resp.body(), UsuarioDTO.class);
            this.usuarioActual = usuario;
        	return Optional.of(usuario);
        }
        return Optional.empty();
    }

    public UsuarioDTO registrarUsuario(String email, String nombre) throws IOException, InterruptedException {
        this.usuarioActual = post("/usuarios", new RegistrarRequest(email, nombre), UsuarioDTO.class);
        return this.usuarioActual;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API: Cursos
    // ════════════════════════════════════════════════════════════════════════

    public List<CursoDTO> obtenerCursos() throws IOException, InterruptedException {
        HttpResponse<String> resp = get("/cursos");
        return json.readValue(resp.body(), new TypeReference<>() {});
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API: Ranking
    // ════════════════════════════════════════════════════════════════════════

    public RankingInfo obtenerRanking() throws IOException, InterruptedException {
        return get("/ranking", RankingInfo.class);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API: Inscripciones
    // ════════════════════════════════════════════════════════════════════════

    public List<CursoInscritoInfo> obtenerInscripciones() throws IOException, InterruptedException {
        String usuarioId = asegurarUsuarioLogeado();
    	InscripcionesInfo info = get("/usuarios/" + enc(usuarioId) + "/inscripcion", InscripcionesInfo.class);
        List<CursoInscritoInfo> lista = info.inscripciones();
        return lista != null ? lista : List.of();
    }

    public void inscribir(String cursoId) {
        try {
            String usuarioId = asegurarUsuarioLogeado();
        	post("/usuarios/" + enc(usuarioId) + "/inscripcion?cursoId=" + enc(cursoId), "");
        } catch (Exception e) {
            // Ya inscrito u otro error: continuamos igualmente
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API: Ejecución (juego)
    // ════════════════════════════════════════════════════════════════════════

    public ProgresoDTO iniciarEjecucion(String cursoId) throws IOException, InterruptedException {
        String usuarioId = asegurarUsuarioLogeado();
    	ProgresoDTO progreso = post("/ejecucion", new IniciarRequest(usuarioId, cursoId), ProgresoDTO.class);
    	this.cursoEnProgreso = progreso;
    	return progreso;
    }

    public SiguientePreguntaInfo obtenerPreguntaActual() throws IOException, InterruptedException {
        String progresoId = asegurarCursoEnEjecucion();
    	return get("/ejecucion/" + enc(progresoId), SiguientePreguntaInfo.class);
    }

    public PreguntaRespondidaInfo responder(ResponderRequest peticion) throws IOException, InterruptedException {
    	String progresoId = asegurarCursoEnEjecucion();
    	return put("/ejecucion/" + enc(progresoId), peticion, PreguntaRespondidaInfo.class);
    }

	private String asegurarUsuarioLogeado() {
		if (usuarioActual == null) 
			throw new IllegalStateException();
        return usuarioActual.id();
	}
	
	private String asegurarCursoEnEjecucion() {
		if (cursoEnProgreso == null) 
			throw new IllegalStateException();
        return cursoEnProgreso.id();
	}
    // ════════════════════════════════════════════════════════════════════════
    //  HTTP (privado)
    // ════════════════════════════════════════════════════════════════════════

    private HttpResponse<String> get(String uri) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + uri))
                .GET()
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private <T> T get(String uri, Class<T> clazz) throws IOException, InterruptedException {
        return json.readValue(get(uri).body(), clazz);
    }

    private HttpResponse<String> post(String uri, Object body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)))
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private <T> T post(String uri, Object body, Class<T> clazz) throws IOException, InterruptedException {
        return json.readValue(post(uri, body).body(), clazz);
    }

    private HttpResponse<String> put(String uri, Object body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + uri))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)))
                .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private <T> T put(String uri, Object body, Class<T> clazz) throws IOException, InterruptedException {
        return json.readValue(put(uri, body).body(), clazz);
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
