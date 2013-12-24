
public class GI implements Node {
	private double axon;
	
	@Override
	public void step() {
		//FIXME
		this.axon++;
	}
	
	public GI(double axon) {
		this.axon = axon;
	}

	@Override
	public double getAxon() {
		return this.axon;
	}

	@Override
	public boolean isReadyToConnect() {
		return true;
	}

}
