package umu.pds.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInfoClass {
	private static final Logger log = LoggerFactory.getLogger(LogInfoClass.class);

	public static void imprimeInfo() {
		log.info("Imprimo mensaje de info");
	}
}
