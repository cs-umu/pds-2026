package pds.duolingo.domain.curso.model;

import java.util.ArrayList;
import java.util.List;

import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;

public class PreguntaHuecos extends Pregunta {
    private final List<ParteTexto> partes;

    public PreguntaHuecos(PreguntaId id, String enunciado, List<ParteTexto> partes) {
        super(id, enunciado);
        this.partes = List.copyOf(partes);
    }

    public List<? extends ParteTexto> getPartes() {
        return partes;
    }

    private List<String> getRespuestasCorrectas() {
        List<String> respuestas = new ArrayList<>();
        for (ParteTexto parte : partes) {
            if (parte.esHueco()) {
                respuestas.add(parte.getTexto());
            }
        }
        return respuestas;
    }

    @Override
    public boolean verificarRespuesta(Respuesta respuesta) {
        if (!(respuesta instanceof RespuestaHuecos)) {
            return false;
        }
        RespuestaHuecos respuestaHuecos = (RespuestaHuecos) respuesta;
        List<String> huecosCompletados = respuestaHuecos.getHuecosCompletados();
        List<String> correctas = getRespuestasCorrectas();
        if (huecosCompletados.size() != correctas.size()) {
            return false;
        }
        for (int i = 0; i < correctas.size(); i++) {
            if (!correctas.get(i).equalsIgnoreCase(huecosCompletados.get(i).trim())) {
                return false;
            }
        }
        return true;
    }

    public static class ParteTexto {
        private final String texto;
        private final boolean esHueco;

        private ParteTexto(String texto, boolean esHueco) {
            this.texto = texto;
            this.esHueco = esHueco;
        }

        public static ParteTexto texto(String texto) {
            return new ParteTexto(texto, false);
        }

        public static ParteTexto hueco(String respuestaCorrecta) {
            return new ParteTexto(respuestaCorrecta, true);
        }

        public String getTexto() {
            return texto;
        }

        public boolean esHueco() {
            return esHueco;
        }
    }
}
