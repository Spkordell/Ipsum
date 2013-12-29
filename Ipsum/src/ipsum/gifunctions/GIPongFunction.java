package ipsum.gifunctions;

import ipsum.interfaces.GIFunction;

public class GIPongFunction implements GIFunction {

	private int type;
	//1 for ball x
	//2 for ball y
	
	public static double ballx;
	public static double bally;
	public static double enemyPaddle;
	public static double playerPaddle;
	
	public GIPongFunction(int type) {
		this.type = type;
	}
	
	@Override
	public double giInitializeAxon() {
		return 0;
	}

	@Override
	public double giStep(double lastAxon) {
		if(this.type == 1) {
			return ballx;
		} else if(this.type == 2){
			return bally;
		} else if (this.type == 3){
			return enemyPaddle;
		} else {
			return playerPaddle;
		}
	}

}
