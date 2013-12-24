package ipsum;

import java.awt.Color;
import java.awt.Paint;

public class GO implements INode{
	private double axon;
	private INode dendrite;
	private Network network;
	
	public GO(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		
		INode node = this;
		while(node instanceof GI || node instanceof GO || this.network.alreadyAttachedToGO(node)) {
			node = this.network.getRandomNode();
		}
		connectDendriteTo(node);
	}

	@Override
	public void step() {
		this.axon = dendrite.getAxon();
		//System.out.println("GO: "+ axon);
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

}
