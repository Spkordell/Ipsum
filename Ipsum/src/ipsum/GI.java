package ipsum;
import java.awt.Color;
import java.awt.Paint;


public class GI implements Node {
	private double axon;
	private Network network;

	public GI(Network network, double axon) {
		this.network = network;
		this.axon = axon;
		this.network.getGraph().addVertex(this);
	}
	
	@Override
	public void step() {
		//FIXME
		this.axon++;
	}

	@Override
	public double getAxon() {
		return this.axon;
	}

	@Override
	public boolean isReadyToConnect() {
		return true;
	}

	@Override
	public Paint getColor() {
		return Color.GREEN;
	}

}
