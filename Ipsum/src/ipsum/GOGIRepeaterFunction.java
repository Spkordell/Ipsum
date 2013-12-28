package ipsum;

import ipsum.interfaces.GIFunction;
import ipsum.interfaces.GOFunction;

public class GOGIRepeaterFunction implements GOFunction,GIFunction{

	double signal;
	
	@Override
	public double giInitializeAxon() {
		signal = .5;
		return .25;
	}

	@Override
	public double giStep(double lastAxon) {
		return signal;
	}

	@Override
	public void goStep(double dendrite) {
		this.signal = dendrite;
		System.out.println("Repeater: "+ signal);
	}

}
