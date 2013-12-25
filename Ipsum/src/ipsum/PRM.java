package ipsum;
import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedList;
import java.util.List;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.DBSCAN;
import jsat.graphing.ScatterPlot;
import jsat.linear.DenseVector;
import jsat.linear.Vec;


/*
 * This is a PRM which with dendrites in the form of scalers. It will group the dendrites into vectors (frames).
 * The frames will collect over time to form a matrix. The matrix will be clustered and a vector from the largest cluster to the last frame will be drawn.
 * The axon will be the dot product of this vector with itself.
 * 
 * A way to determine correlation still needs to be determined.
 * 
 * MDS or tensors are not needed with this implementation.
 */

public class PRM implements INode {
	private static final int minSteps = 4;
	private LinkedList<INode> dendrites;
	private LinkedList<DataPoint> frames;
	private double axon;
	private Network network;
	
	DBSCAN dbscan;
	
	public PRM(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		this.dendrites = new LinkedList<INode>();
		this.frames = new LinkedList<DataPoint>();
		this.dbscan = new DBSCAN();
		this.axon = -1;
	}

	@Override
	public void step() {
		LinkedList<Double> dendriteValues = new LinkedList<Double>();	
		DataSet data;
		
		//TODO: decide how long to keep frames and begin throwing away old ones after a certain point
		
		for(INode d: dendrites) {
			dendriteValues.add(d.getAxon());
		}
		Vec frame = new DenseVector(dendriteValues);
		frames.add(new DataPoint(frame,null,null));
		data = new SimpleDataSet(frames);
		
		if (frames.size() >= minSteps) {
			List<List<DataPoint>> cluster = dbscan.cluster(data);
	
			//find largest cluster
			int largestCluster = 0;
			for (int i = 0; i < cluster.size(); i++) {
				if (cluster.get(i).size() > cluster.get(largestCluster).size()) {
					largestCluster = i;
				}
			}
		       
	        //find center of largest cluster (Find the mean of each dimension)
	        Vec center = DenseVector.zeros(dendrites.size());
	        for (int d = 0; d < dendrites.size(); d++) { //do for each dimension, currently 2
		        for (int i = 0; i < cluster.get(largestCluster).size(); i++) {
		     	   center.set(d, center.get(d)+cluster.get(largestCluster).get(i).getNumericalValues().get(d));
		        }
		        center.set(d, center.get(d)/cluster.get(largestCluster).size());
	        }
		       
		    //draw vector from center of largest cluster to latest input
		    Vec axonVec = frame.subtract(center);
		    axon = axonVec.dot(axonVec);
		    
		    //System.out.println("Center: "+center+", Last: "+frame+", Axon: "+ axon);
		}
		decideMakeNewConnection();
	}

	private void decideMakeNewConnection() {
		if (dendrites.size() < 2) { //TODO: should be 1
			INode node = this;
			while(node == this || node.isReadyToConnect() == false || this.network.hasTwinIfConnected(this,node)) {
				
				//todo, add condition to exit the loop without makign a connection if there are no possible connections
				
				node = this.network.getRandomNode();
			}
			connectDendriteTo(node);
		}
	}

	@Override
	public double getAxon() {
		return this.axon;
	}

	@Override
	public boolean isReadyToConnect() {
		//TODO: future implementations will need to base this on the correlation as well. Answer this question, has this node found a pattern?
		return (frames.size() >= minSteps);
	}
	
	public void connectDendriteTo(INode node) {
		this.dendrites.add(node);
		this.network.getGraph().addEdge(this.network.getEdgeCount(), node, this);
		this.network.incrementEdgeCount();
		
		//Need to clear data every time dimensionality changes
		this.frames = new LinkedList<DataPoint>();
	}

	public void plotDendrites() {
		DataSet data = new SimpleDataSet(frames);
		ScatterPlot plot = new ScatterPlot(data.getNumericColumn(0),data.getNumericColumn(1));
		Main.add(plot);
	}

	@Override
	public Paint getColor() {
		return Color.BLUE;
	}

	
	@Override
	public boolean hasDendriteConnectedTo(INode node) {
		for (INode n: dendrites) {
			if(n == node) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isTwin(INode node) {
		if (dendrites.size() == 0) {
			return false;
		}
		for (INode n: dendrites) {
			if (!node.hasDendriteConnectedTo(n)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isTwinIfConnected(INode node, INode toNode) {
		if (dendrites.size() == 0) {
			return false;
		}
		@SuppressWarnings("unchecked")
		LinkedList<INode> temp = (LinkedList<INode>) node.getDendrites().clone();
		temp.add(toNode);
		return (dendrites.containsAll(temp));
	}

	@Override
	public LinkedList<INode> getDendrites() {
		return dendrites;
	}
}
