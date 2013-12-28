package ipsum;

import ipsum.interfaces.INode;

import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedList;

public class GO implements INode{
	private double axon;
	private INode dendrite;
	private Network network;
	
	public GO(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		
		INode node = this;
		while(node instanceof GI || node instanceof GO || this.network.hasTwin(node)) {
			node = this.network.getRandomNode();
		}
		connectDendriteTo(node);
	}

	@Override
	public void step() {
		this.axon = dendrite.getAxon();
		System.out.println("GO: "+ axon);
	}

	@Override
	public double getAxon() {
		return this.axon;
	}

	@Override
	public boolean isReadyToConnect() {
		return false; //You can't connect a dendrite to a global output
	}

	public void connectDendriteTo(INode node) {
		this.dendrite = node;
		this.network.getGraph().addEdge(this.network.getEdgeCount(), node, this);
		this.network.incrementEdgeCount();
	}

	@Override
	public Paint getColor() {
		return Color.RED;
	}
	
	@Override
	public boolean hasDendriteConnectedTo(INode node) {
		return (dendrite == node);
	}

	@Override
	public boolean isTwin(INode node) {
		return hasDendriteConnectedTo(node);
	}

	@Override
	public boolean isTwinIfConnected(INode node, INode toNode) {
		return (toNode == node);
	}

	@Override
	public LinkedList<INode> getDendrites() {
		LinkedList<INode> dendrites = new LinkedList<INode>();
		dendrites.add(dendrite);
		return dendrites;
	}

	@Override
	public void optimize() {
		//do nothing
	}

	@Override
	public boolean isTwinIfDisconnected(INode node, INode toNode) {
		return false;
		//TODO double check this
	}

}
