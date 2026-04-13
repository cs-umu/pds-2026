package tds.tpv;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	// Inicialización
    	Configuracion configuracion = new ConfiguracionImpl();
        Configuracion.setInstancia(configuracion);
        configuracion.getSceneManager().inicializar(stage);
        
        // Inicio de la aplicación
        configuracion.getSceneManager().showVentanaPrincipal();
    }
    
    public static void main(String[] args) {
        launch();
    }

}