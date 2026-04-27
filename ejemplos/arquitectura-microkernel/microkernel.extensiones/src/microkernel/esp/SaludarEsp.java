package microkernel.esp;

import microkernel.ExtensionSaludo;

public class SaludarEsp implements ExtensionSaludo {

	@Override
	public void saludar() {
		System.out.println("Hola");
	}

}
