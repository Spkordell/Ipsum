
public class GO implements Node{
	private double axon;
	private Node dendrite;
	
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
	}

}
