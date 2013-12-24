import java.awt.Paint;


/*
 * An interface to represent any type of node in the network, whether it is a PRM (Pattern Recognition Module), GI (Global Input), or GO (Global Output). 
 */

public interface Node {
	public void step(); //Tells a node to process its inputs
	public double getAxon(); //Tells the node to return it's output
	//TODO need methods for returning corralation
	public boolean isReadyToConnect();
	public Paint getColor();
}
