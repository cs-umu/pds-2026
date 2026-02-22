package tds.tpv.vista;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import tds.tpv.Configuracion;
import tds.tpv.negocio.controladores.ControladorTPV;
import tds.tpv.negocio.modelo.Producto;

public class StockViewController {

	private static final Logger log = LogManager.getLogger();

	@FXML
	private TableView<Producto> tablaProductos;

	@FXML
	private TableColumn<Producto, String> colCodigo;
	@FXML
	private TableColumn<Producto, String> colDescripcion;
	@FXML
	private TableColumn<Producto, String> colCantidad;
	@FXML
	private TableColumn<Producto, Double> colPrecio;

	private Alert confirmarEliminacion;

	@FXML
	private TextField aumentarCantidadTextField;

	@FXML
	private TextField reducirCantidadTextField;

	private Producto productoSeleccionado;

	private ControladorTPV controlador;

	// Este metodo se ejecuta al cargar la pantalla correspondiente
	@FXML
	public void initialize() {
		controlador = Configuracion.getInstancia().getControladorTPV();

		// Creo los manejadores para los campos de las columnas
		colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

		// Cargo la lista de productos
		ObservableList<Producto> lista = null;
		try {
			lista = FXCollections.observableArrayList(controlador.getProductos());
		} catch (Exception e) {
			log.error("Error inicializando StockViewController",e);
		}

		// Listener para las filas seleccionadas
		tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.productoSeleccionado = newSelection;
			}
		});
		inicializaConfirmacion();
		// Pasar lista a la tabla
		tablaProductos.setItems(lista);
		
		//Aniadimos formateadores para evitar que se metan letras o numeros negativos
		aumentarCantidadTextField.setTextFormatter(new TextFormatter<>(cambio -> {
            String nuevo = cambio.getControlNewText();
            return nuevo.matches("\\d{0,5}") ? cambio : null;
        }));
		
		reducirCantidadTextField.setTextFormatter(new TextFormatter<>(cambio -> {
            String nuevo = cambio.getControlNewText();
            return nuevo.matches("\\d{0,5}") ? cambio : null;
        }));

	}

	private void inicializaConfirmacion() {
		confirmarEliminacion = new Alert(AlertType.CONFIRMATION);
		confirmarEliminacion.setTitle("Eliminar producto de stock");
		confirmarEliminacion.setHeaderText("Va a eliminar un producto de su stock, esta tarea no se puede deshacer");
	}

	@FXML
	private void aniadirStockDialog() {
		Dialog<Producto> dialog = new Dialog<>();
		dialog.setTitle("Alta de nuevo Producto");
		dialog.setHeaderText("Introduce los datos del nuevo producto");

		ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

		// --- Contenido del formulario ---
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField descripcionArea = new TextField();
		Spinner<Integer> cantidadSpinner = new Spinner<>(1, 1000, 1);
		TextField precioField = new TextField();

		grid.add(new Label("Descripción:"), 0, 1);
		grid.add(descripcionArea, 1, 1);
		grid.add(new Label("Cantidad:"), 0, 2);
		grid.add(cantidadSpinner, 1, 2);
		grid.add(new Label("Precio (€):"), 0, 3);
		grid.add(precioField, 1, 3);

		dialog.getDialogPane().setContent(grid);

		// --- Convertir el resultado en un objeto Product ---
		dialog.setResultConverter(new Callback<ButtonType, Producto>() {
			@Override
			public Producto call(ButtonType button) {
				if (button == guardarButtonType) {
					try {
						// FIXME: Violación del MVC
						Producto producto = tablaProductos.getItems().get(tablaProductos.getItems().size() - 1);
						double precio = Double.parseDouble(precioField.getText());
						Producto productoImpl = new Producto(
								String.format("%03d", Integer.parseInt(producto.getCodigo()) + 1),
								descripcionArea.getText(), cantidadSpinner.getValue(), precio);
						tablaProductos.getItems().add(productoImpl);
						controlador.nuevoProductoStock(productoImpl);
						return productoImpl;
					} catch (NumberFormatException ex) {
						Alert error = new Alert(Alert.AlertType.ERROR, "Precio inválido", ButtonType.OK);
						error.showAndWait();
						return null;
					}
				}
				return null;
			}
		});

		// --- Mostrar el dialogo y obtener el resultado ---
		dialog.showAndWait().ifPresent(producto -> {
			log.info("Producto creado: {} {}", producto.getCodigo(), producto.getDescripcion());
		});
	}

	@FXML
	private void irATienda() throws IOException {
		Configuracion.getInstancia().getSceneManager().showVentanaPrincipal();
	}

	@FXML
	private void eliminarDeStock() {
		if (this.productoSeleccionado != null) {
			confirmarEliminacion.setContentText(
					"¿Está seguro que quiere eliminar " + this.productoSeleccionado.getDescripcion() + "?");
			Optional<ButtonType> result = confirmarEliminacion.showAndWait();
			if (result.get() == ButtonType.OK) {
				tablaProductos.getItems().remove(this.productoSeleccionado);
				this.productoSeleccionado = null;
				tablaProductos.refresh();
				tablaProductos.getSelectionModel().clearSelection();

			}
		}

	}

	@FXML
	private void aumentarStock() {
		if (this.productoSeleccionado != null && this.aumentarCantidadTextField.getText() != null
				&& ! this.aumentarCantidadTextField.getText().isEmpty()) {
			try {
				int incremento = Integer.parseInt(aumentarCantidadTextField.getText());
				controlador.aumentarStock(productoSeleccionado, incremento);
				aumentarCantidadTextField.setText("");
				tablaProductos.refresh();
			} catch (NumberFormatException e) {
				log.error("Error aumentando stock",e);
			}
		}
	}

	@FXML
	private void reducirStock() {
		if (this.productoSeleccionado != null && this.reducirCantidadTextField.getText() != null
				&& !this.reducirCantidadTextField.getText().isEmpty()) {
			try {
				int decremento = Integer.parseInt(reducirCantidadTextField.getText());
				if (decremento > this.productoSeleccionado.getCantidad()) {
					controlador.reducirStock(productoSeleccionado, this.productoSeleccionado.getCantidad());
				} else {
					controlador.reducirStock(productoSeleccionado, decremento);
				}
				reducirCantidadTextField.setText("");
				tablaProductos.refresh();
			} catch (NumberFormatException e) {
				log.error("Error reduciendo stock",e);
			}
		}
	}

}
