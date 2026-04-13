package tds.tpv;

import tds.tpv.adapters.repository.ProductoRepository;
import tds.tpv.adapters.repository.impl.ProductoRepositoryJSONImpl;
import tds.tpv.adapters.repository.impl.ProductoRestRepositoryImpl;
import tds.tpv.negocio.controladores.ControladorTPV;

public class ConfiguracionImpl extends Configuracion {

	private ControladorTPV controlador;

	public ConfiguracionImpl() {
		//Por defecto cargo el repositorio que carga desde fichero JSON
		this.controlador = new ControladorTPV(new ProductoRepositoryJSONImpl());		
	}
	
	@Override
	public ControladorTPV getControladorTPV() {
		return controlador;
	}
	
	//Metodo para cambiar el repositorio
	public void setProductoRepository(ProductoRepository repositorio) {
		this.controlador.setProductoRepository(repositorio);
	}
	
	@Override
	public String getRutaAlmacen() {
		return "/data/almacen.json";
	}
	
}