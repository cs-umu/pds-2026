package microkernel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Este ejemplo simula el núcleo de una arquitectura de microkernel.
 * El sistema tiene como objetivo saludar en diversos idiomas, pero los idiomas
 * se configuran externamente como plugins.
 * 
 * Funcionamiento del núcleo:
 * 
 * 	1. Leer extensiones (vienen dadas como argumento)
 * 	2. Para cada extensión
 *  	2.1 Cargar extensión
 *  	2.2 Pedir que salude en su idioma
 */
public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		System.out.println("Saludador v1.0");
	
		// Los plug-ins se especifica como carpetas. Vienen dadas como argumento.
		for (String carpeta : args) {
			
			// Cargar la configuración del plug-in
			Path p = Path.of(carpeta, "manifest.properties");
			Properties prop = new Properties();
			prop.load(new FileInputStream(p.toFile()));
			
			// Obtener la información del plug-in de la configuración
			String idioma = (String) prop.get("idioma");
			String clase = (String) prop.get("clase");
			
			// Cargar la clase que implementa la funcionalidad del plugin
			Class<?> clazz = Class.forName(clase);
			Object obj = clazz.getDeclaredConstructor().newInstance();
			ExtensionSaludo s = (ExtensionSaludo) obj;
			
			// Invocar al plugin
			System.out.println(idioma);
			s.saludar();
		}
		
		
	}
}
