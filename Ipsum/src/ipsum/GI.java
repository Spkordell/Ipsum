package ipsum;
import ipsum.interfaces.GIFunction;
import ipsum.interfaces.INode;

import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedList;
import java.util.Random;


public class GI implements INode {
	private double axon;
	private Network network;
	private GIFunction f;
	Random rand;

	public GI(Network network, GIFunction f) {
		this.network = network;
		this.f = f;
		this.network.getGraph().addVertex(this);
		this.rand = new Random();
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
		return rand.nextBoolean();
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

	@Override
	public void optimize() {
		//Do nothing
	}

	@Override
	public boolean isTwinIfDisconnected(INode node, INode toNode) {
		return false;
		//TODO double check this
	}

}
