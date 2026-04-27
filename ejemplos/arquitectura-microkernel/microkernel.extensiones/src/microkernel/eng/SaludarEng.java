package microkernel.eng;

import microkernel.ExtensionSaludo;

public class SaludarEng implements ExtensionSaludo {

	@Override
	public void saludar() {
		System.out.println("Hello");
	}

}
