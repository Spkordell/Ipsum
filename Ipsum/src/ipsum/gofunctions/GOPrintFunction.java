package ipsum.gofunctions;

import ipsum.interfaces.GOFunction;

public class GOPrintFunction implements GOFunction {
	String name;
	
	public GOPrintFunction() {
		this.name = null;
	}
	
	public GOPrintFunction(String name) {
		this.name = name;
	}
	
	@Override
	public void goStep(double dendrite) {
		if (name != null) {
			System.out.println(name+": "+dendrite);
		} else {
			System.out.println("GO: "+dendrite);
		}
	}

}
