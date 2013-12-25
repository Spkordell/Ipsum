package ipsum.gifunctions;

import ipsum.interfaces.GIFunction;

import java.util.Random;

public class GITestFunctionRandom implements GIFunction {
	Random rand;
	
	@Override
	public double initializeAxon() {
		rand = new Random();
		return 0;
	}

	@Override
	public double step(double lastAxon) {
		return rand.nextDouble();
	}

}
