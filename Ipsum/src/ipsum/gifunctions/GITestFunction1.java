package ipsum.gifunctions;

import ipsum.interfaces.GIFunction;

public class GITestFunction1 implements GIFunction {

	@Override
	public double initializeAxon() {
		return 0;
	}

	@Override
	public double step(double lastAxon) {
		return lastAxon+1;
	}

}
