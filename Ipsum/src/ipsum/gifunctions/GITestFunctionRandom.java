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
		double a = rand.nextDouble();
		/*if (a > .5) {
			a += .1;
		} else {
			a -= .1;
		}*/
		return a;
	}

}
