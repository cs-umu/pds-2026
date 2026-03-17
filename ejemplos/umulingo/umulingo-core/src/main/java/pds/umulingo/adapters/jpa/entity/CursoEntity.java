package pds.umulingo.adapters.jpa.entity;

import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.PreguntaHuecos.ParteTexto;
import pds.umulingo.domain.model.cursoenejecucion.id.PreguntaId;

@Entity
public class CursoEntity {
	@Id
	@Embedded
	private CursoId id;
	private String nombre;
	//private List<PreguntaEntity> preguntas;
	
	public CursoEntity() { }
	
	@Entity
	public static abstract class PreguntaEntity {
		@Id
		@Embedded
		private PreguntaId id;
		private String enunciado;
	}
	
	public class PreguntaAbiertaEntity extends PreguntaEntity {
	    private String respuestaCorrecta;
	}
	
	public class PreguntaHuecosEntity extends PreguntaEntity {
	    private List<ParteTexto> partes;
	}
	
}
