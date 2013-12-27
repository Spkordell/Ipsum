package ipsum.interfaces;
import java.awt.Paint;
import java.util.LinkedList;

/*
 * An interface to represent any type of node in the network, whether it is a PRM (Pattern Recognition Module), GI (Global Input), or GO (Global Output). 
 */

public interface INode {
	public void step(); //Tells a node to process its inputs
	public double getAxon(); //Tells the node to return it's output
	//TODO need methods for returning correlation
	public boolean isReadyToConnect();
	public Paint getColor();
	public boolean hasDendriteConnectedTo(INode node);
	public boolean isTwin(INode node);
	public boolean isTwinIfConnected(INode node, INode toNode);
	public LinkedList<INode> getDendrites();
}