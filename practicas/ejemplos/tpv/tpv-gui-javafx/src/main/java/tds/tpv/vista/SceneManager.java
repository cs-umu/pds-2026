package tds.tpv.vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tds.tpv.App;

/**
 * Se encarga de coordinar la navegación entre ventanas.
 */
public class SceneManager {

	private Stage stage;
    private Scene scenaActual;

	public void inicializar(Stage stage) {
		this.stage = stage;
	}

	public void showVentanaPrincipal() {
    	cargarYMostar("TiendaMain");
    }

	public void showTiendaStock() {
		cargarYMostar("TiendaStock");
	}
	
	public void showAcerca() {
		cargarYMostarDialogo("DialogoAyuda", "Acerca de ..");
	}

	private void cargarYMostarDialogo(String fxml, String titulo) {
        try {
        	DialogPane pane = (DialogPane) loadFXML(fxml);
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle(titulo);
            dialog.initStyle(StageStyle.UTILITY);

            // Muestra el diálogo y espera que el usuario presione Aceptar
            dialog.showAndWait();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

	private void cargarYMostar(String fxml) {
        try {
        	Parent root = loadFXML(fxml);
        	if (scenaActual == null) {
    			scenaActual = new Scene(root);
    	        stage.setScene(scenaActual);
    	        stage.show();
        	} else {
        		scenaActual.setRoot(root);        		
        	}
        	
        	
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
