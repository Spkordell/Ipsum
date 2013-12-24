import java.awt.Color;
import java.awt.Paint;
import java.util.Random;


public class GO implements Node{
	private double axon;
	private Node dendrite;
	private Network network;
	
	public GO(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
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

	public void connectDendriteTo(Node node) {
		this.dendrite = node;
		
		Random random = new Random();
		this.network.getGraph().addEdge(random.nextInt(), node, this);
	}

	@Override
	public Paint getColor() {
		return Color.RED;
	}

}
