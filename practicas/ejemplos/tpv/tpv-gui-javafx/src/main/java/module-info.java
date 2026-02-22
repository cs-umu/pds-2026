module es.um.informatica.TpvFx {
    requires javafx.controls;
    requires javafx.fxml;
	requires jakarta.xml.bind;
	requires com.google.common;
	requires javafx.base;
	requires javafx.graphics;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires org.apache.logging.log4j;
	requires java.net.http;
	requires org.slf4j; 
 
    opens tds.tpv to javafx.fxml;
    opens tds.tpv.vista to javafx.fxml;
    opens tds.tpv.negocio.modelo to javafx.base, com.fasterxml.jackson.databind;
    opens tds.tpv.adapters.repository.impl to com.fasterxml.jackson.databind;

    exports tds.tpv;
}
