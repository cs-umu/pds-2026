package tds.tpv.vista;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import tds.tpv.Configuracion;
import tds.tpv.adapters.repository.impl.ProductoRepositoryJSONImpl;
import tds.tpv.adapters.repository.impl.ProductoRestRepositoryImpl;

public class BarraMenuControllerView {
	
	@FXML
	private Menu menuJson;

	@FXML
	private void irAStock() throws IOException {
		Configuracion.getInstancia().getSceneManager().showTiendaStock();
	}

	@FXML
	public void salir(Event e) {
		Platform.exit();
	}

	@FXML
	void abrirAcerca(Event e) {
		Configuracion.getInstancia().getSceneManager().showAcerca();
	}

	@FXML
	void activarRepositorioJson(Event e) {
		Configuracion.getInstancia().setProductoRepository(new ProductoRepositoryJSONImpl());
		menuJson.setText("Usando repositorio JSON");
		menuJson.getStyleClass().remove("menu-verde");
		menuJson.getStyleClass().add("menu-azul");
	}

	@FXML
	void activarRepositorioRest(Event e) {
		Configuracion.getInstancia().setProductoRepository(new ProductoRestRepositoryImpl());
		menuJson.setText("Usando repositorio REST");
		menuJson.getStyleClass().remove("menu-azul");
		menuJson.getStyleClass().add("menu-verde");
	}

}
