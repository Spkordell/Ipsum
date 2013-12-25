package ipsum;
import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedList;
import java.util.List;


public class GI implements INode {
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
	
	@Override
	public boolean hasDendriteConnectedTo(INode node) {
		return false;
	}

	@Override
	public boolean isTwin(INode node) {
		return false;
	}

	@Override
	public boolean isTwinIfConnected(INode node, INode toNode) {
		return false;
	}

	@Override
	public LinkedList<INode> getDendrites() {
		return new LinkedList<INode>();
	}

}
