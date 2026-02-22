package tds.tpv.vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import tds.tpv.Configuracion;
import tds.tpv.negocio.controladores.ControladorTPV;
import tds.tpv.negocio.modelo.Item;
import tds.tpv.negocio.modelo.Producto;

public class TiendaViewController {

	private static final Logger log = LogManager.getLogger();

	private static final DataFormat PRODUCTO_FORMAT = new DataFormat("application/x-java-producto");

	private ControladorTPV controlador;

	private Item productoSeleccionado;

	@FXML
	private TextField totalField;

	@FXML
	private TableView<Item> tablaProductos;

	@FXML
	private TableColumn<Item, String> colDescripcion;
	@FXML
	private TableColumn<Item, String> colCantidad;
	@FXML
	private TableColumn<Item, Double> colPrecio;

	@FXML
	private ScrollPane panelProductos;

	@FXML
	private TextField filtroProductos;

	@FXML
	public void initialize() {
		controlador = Configuracion.getInstancia().getControladorTPV();
		cargaProductos("");
		tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.productoSeleccionado = newSelection;
			}
		});

		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));

		// Cargo la lista de productos
		ObservableList<Item> lista = null;
		try {
			lista = FXCollections.observableArrayList(new ArrayList<Item>());
		} catch (Exception e) {
			log.error("Error inicializando TiendaViewController", e);
		}
		// Pasar lista a la tabla
		tablaProductos.setItems(lista);

		// Necesario para que el componente muestre el icono y acepte el drop
		tablaProductos.setOnDragOver(event -> {
			if (event.getDragboard().hasContent(PRODUCTO_FORMAT)) {
				event.acceptTransferModes(TransferMode.COPY);
				event.consume();
			}

		});

		// Quien ejecuta realmente la accion del drop
		tablaProductos.setOnDragDropped(event -> {
			if (event.getDragboard().hasContent(PRODUCTO_FORMAT)) {
				event.acceptTransferModes(TransferMode.COPY);
				Producto producto = (Producto) event.getDragboard().getContent(PRODUCTO_FORMAT);
				event.consume();
				aniadeListaCompra(producto);
			}
		});

	}

	private void cargaProductos(String filtro) {

		TilePane contenedor = new TilePane();
		contenedor.setPrefColumns(4); // máximo 3 productos por fila
		contenedor.setHgap(10); // separación horizontal
		contenedor.setVgap(10); // separación vertical
		contenedor.setStyle("-fx-padding: 10;");

		// Simulamos una lista de productos
		List<? extends Producto> listaProductos = controlador.getProductosPorFiltro(filtro);
		listaProductos.stream().forEach(producto -> {
			// Crear labels
			Label labelNombre = new Label(producto.getDescripcion());
			Label labelPrecio = new Label(Double.toString(producto.getPrecio()));

			// Contenedor vertical para centrar cada fila
			VBox vbox = new VBox(5, labelNombre, labelPrecio);
			vbox.setAlignment(Pos.CENTER);

			// Botón con VBox como "gráfico"
			Button btnProducto = new Button();
			btnProducto.setPrefSize(100, 60);
			btnProducto.setGraphic(vbox);
			btnProducto.setOnAction(e -> {
				aniadeListaCompra(producto);
			});

			btnProducto.setOnDragDetected(event -> {
				Dragboard db = btnProducto.startDragAndDrop(TransferMode.COPY);
				ClipboardContent content = new ClipboardContent();
				content.put(PRODUCTO_FORMAT, producto);
				db.setContent(content);
				event.consume();
			});

			contenedor.getChildren().add(btnProducto);

			return;
		});
		panelProductos.setContent(contenedor);

	}

	private void aniadeListaCompra(Producto producto) {
		controlador.getCarrito().addItem(producto, 1);
		
		Item itemEnLista = null;
		for(Item item: tablaProductos.getItems()) {
			if(item.getProducto().equals(producto)) {
				itemEnLista=item;
			}
		}
		if(itemEnLista==null) {
			tablaProductos.getItems().add(new Item(producto, 1));
		}else {
			tablaProductos.getItems().remove(itemEnLista);
			tablaProductos.getItems().add(new Item(producto, itemEnLista.getCantidad()+1));
		}
				

		// Es necesario reemplazar , por . ya que String.format usa el formato espanio
		// para los decimales
		double total = Double
				.parseDouble(totalField.getText().isEmpty() ? " 0" : totalField.getText().replace(",", "."));
		totalField.setText(String.format("%.2f", total + producto.getPrecio()));

		tablaProductos.refresh();
	}

	@FXML
	private void quitarDeListaCompra() {
		Preconditions.checkNotNull(productoSeleccionado);
		controlador.quitarFromCarrito(productoSeleccionado.getProducto());
		double total = Double
				.parseDouble(totalField.getText().isEmpty() ? " 0" : totalField.getText().replace(",", "."));
		totalField.setText(String.format("%.2f", total - productoSeleccionado.getPrecioTotal()));
		tablaProductos.getItems().remove(productoSeleccionado);
		tablaProductos.refresh();
	}

	@FXML
	private void buscarProducto() {
		if (filtroProductos != null && !filtroProductos.getText().isEmpty()) {
			cargaProductos(filtroProductos.getText());
		}else {
			cargaProductos("");
		}
	}

	@FXML
	private void limpiarFiltro() {
		cargaProductos("");
		filtroProductos.setText("");
	}

	@FXML
	private void irAStock() {
		Configuracion.getInstancia().getSceneManager().showTiendaStock();
	}

	@FXML
	private void confirmarCompra() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Resumen de la compra");
		dialog.setHeaderText("Confirme su compra");

		ButtonType confirmarCombraBtn = new ButtonType("Comprar", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirmarCombraBtn, ButtonType.CANCEL);

		// Se tiene que copiar la tabla inicial porque JavaFX traslada el elemento de
		// nodo y no lo copia
		// Para conservar la tabla en el otro panel se tiene que copiar
		TableView<Item> tablaDialog = new TableView<>();
		tablaDialog.getColumns().addAll(tablaProductos.getColumns());
		tablaDialog.setItems(tablaProductos.getItems()); // reutiliza los datos

		// Total
		double total = controlador.getCarrito().getTotal();
		Label lblTotal = new Label("Total: " + String.format("%.2f €", total));

		VBox content = new VBox(10, tablaDialog, lblTotal);
		content.setPrefSize(335, 400);
		dialog.getDialogPane().setContent(content);

		// Mostrar diálogo y comprobar respuesta
		Optional<ButtonType> response = dialog.showAndWait();
		if (response.isPresent() && response.get() == confirmarCombraBtn) {

			log.info("Compra confirmada");
			// El procesamiento de la compra lo obviamos, simplemente actualiza el STOCK
			// creamos un nuevo carrito.
			tablaProductos.getItems().stream()
					.forEach(item -> controlador.reducirStock(item.getProducto(), item.getCantidad()));

			controlador.nuevoCarrito();
			tablaProductos.getItems().clear();
			totalField.setText(String.format("%.2f", 0f));
		} else {
			log.info("Compra cancelada");
		}

	}

}
