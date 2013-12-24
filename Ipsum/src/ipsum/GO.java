package ipsum;

import java.awt.Color;
import java.awt.Paint;

public class GO implements Node{
	private double axon;
	private Node dendrite;
	private Network network;
	
	public GO(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		
		Node node = this;
		while(node instanceof GI || node instanceof GO) {
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

	public void connectDendriteTo(Node node) {
		this.dendrite = node;
		this.network.getGraph().addEdge(this.network.getEdgeCount(), node, this);
		this.network.incrementEdgeCount();
	}

	@Override
	public Paint getColor() {
		return Color.RED;
	}

}
