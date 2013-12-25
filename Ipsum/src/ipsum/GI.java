package ipsum;
import ipsum.interfaces.GIFunction;
import ipsum.interfaces.INode;

import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedList;


public class GI implements INode {
	private double axon;
	private Network network;
	private GIFunction f;

	public GI(Network network, GIFunction f) {
		this.network = network;
		this.f = f;
		this.network.getGraph().addVertex(this);
		this.axon = f.initializeAxon();;
	}
	
	@Override
	public void step() {
		this.axon = f.step(this.axon);
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
