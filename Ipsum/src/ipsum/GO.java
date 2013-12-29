package ipsum;

import ipsum.interfaces.GOFunction;
import ipsum.interfaces.INode;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.LinkedList;

public class GO implements INode{
	private static final int dendritesToKeep = 500;
	private double axon;
	private INode dendrite;
	private Network network;
	private GOFunction f;
	
	LinkedList<Double> pastDendrites = new LinkedList<Double>();
	double maxDendrite;
	double minDendrite;
	
	public GO(Network network, GOFunction f) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		this.f = f;
		
		INode node = this;
		while(node instanceof GI || node instanceof GO || this.network.hasTwin(node)) {
			node = this.network.getRandomNode();
		}
		connectDendriteTo(node);
	}

	@Override
	public void step() {
		pastDendrites.add(dendrite.getAxon());
		normalize();
		this.axon = map(dendrite.getAxon(),minDendrite,maxDendrite,0,1);
		if (f != null) {
			f.goStep(this.axon);
		}
	}

	private void normalize() {
		maxDendrite = Double.NEGATIVE_INFINITY;
		minDendrite = Double.POSITIVE_INFINITY;
		for(double d:pastDendrites) {
			if(d > maxDendrite) {
				maxDendrite = d;
			}
			if(d < minDendrite) {
				minDendrite = d;
			}
		}
		while (pastDendrites.size() > dendritesToKeep) {
			pastDendrites.removeFirst();
		}
	}
	
	double map(double x, double in_min, double in_max, double out_min, double out_max) {
	  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
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
