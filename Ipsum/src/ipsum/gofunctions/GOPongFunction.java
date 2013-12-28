package ipsum.gofunctions;

import ipsum.interfaces.GOFunction;

public class GOPongFunction implements GOFunction {

	public static double paddle1 = .5;
	public static double paddle2 = .5;
	private int type;
	
	public GOPongFunction(int type) {
		this.type = type;
	}

	@Override
	public void goStep(double dendrite) {
		if (this.type == 1) {
			paddle1 = dendrite;
		} else {
			paddle2 = dendrite;
		}
		System.out.println(dendrite);
	}

}
