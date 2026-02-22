package umu.pds.parametrizado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogParametrizado {
	// Inicializacion del logger. Como parametro debe ser la clase sobre la que esta
	// haciendo log (LogParametrizado.class)
	private static final Logger log = LoggerFactory.getLogger(LogParametrizado.class);

	// Al ser una clase con solo metodos estaticos creamos un constructor privado
	// para que no se puedan crear instancias de la misma
	private LogParametrizado() {
	}

	public static void saluda() {
		String hola = "Hola";
		String mundo = "Mundo";
		log.info("Vamos a saludar 5 veces");

		for (int i = 0; i < 5; i++) {
			// Con las {} dentro del string del mensaje indico a la libreria de log que ahi
			// va un parametro
			// Al final del metodo meto las variables ordenadas para que cuadren con las {}
			// La libreria de log susituira las {} por la variable correspondiente
			log.debug("Saludo nº: {} {} {} .Fin del saludo", i, hola, mundo);
		}

	}

}
