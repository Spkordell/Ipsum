package ipsum.gofunctions;

import ipsum.interfaces.GOFunction;

public class GOPrintFunction implements GOFunction {
	@Override
	public void goStep(double dendrite) {
		System.out.println("GO: "+dendrite);
	}

}
