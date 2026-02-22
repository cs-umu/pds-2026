package umu.pds;

import umu.pds.debug.LogDebugClass;
import umu.pds.parametrizado.LogParametrizado;
import umu.pds.trace.LogTraceClass;

public class App {

	public static void main(String[] args) {
		//Lanzamos la clase que debe imprimir logs de nivel TRACE o superior
		LogTraceClass.imprimeTrace();
		//Lanzamos la clase que debe imprimir logs de nivel DEBUG o superior
		LogDebugClass.imprimeDebug();
		//Lanzamos la clase que imprime lineas de log parametrizadas
		LogParametrizado.saluda();
	}
}
