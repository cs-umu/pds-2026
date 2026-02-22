package umu.pds.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogDebugClass {
	// Inicializacion del logger. Como parametro debe ser la clase sobre la que esta
	// haciendo log (LodDebugClass.class)
	private static final Logger log = LoggerFactory.getLogger(LogDebugClass.class);

	// Al ser una clase con solo metodos estaticos creamos un constructor privado
	// para que no se puedan crear instancias de la misma
	private LogDebugClass() {
	}

	public static void imprimeDebug() {
		log.debug("Imprimo mensaje de debug");
		log.trace("Imprimo trace desde el metodo de Debug");
		log.error("Error con codigo ", new Exception("Excepcion de error"));
	}

}
