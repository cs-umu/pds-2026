package umu.pds.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTraceClass {
	//Inicializacion del logger. Como parametro debe ser la clase sobre la que esta haciendo log (LogTraceClass.class)
	private static final Logger log = LoggerFactory.getLogger(LogTraceClass.class);

	// Al ser una clase con solo metodos estaticos creamos un constructor privado
	// para que no se puedan crear instancias de la misma
	private LogTraceClass() {
	}
	
	public static void imprimeTrace() {
		log.trace("Imprimo mensaje de trace");
		log.debug("Imprimo debug desde el metodo de Trace");
	}
}
