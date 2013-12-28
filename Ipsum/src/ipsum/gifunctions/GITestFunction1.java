package ipsum.gifunctions;

import ipsum.interfaces.GIFunction;

public class GITestFunction1 implements GIFunction {

	@Override
	public double giInitializeAxon() {
		return 0;
	}

	@Override
	public double giStep(double lastAxon) {
		return lastAxon+1;
	}

}
