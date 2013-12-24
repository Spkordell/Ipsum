import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

public class PRM implements Node {
	private static final int minSteps = 4;
	private LinkedList<Node> dendrites;
	private LinkedList<DataPoint> frames;
	private double axon;
	private Network network;
	
	DBSCAN dbscan;
	
	public PRM(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		this.dendrites = new LinkedList<Node>();
		this.frames = new LinkedList<DataPoint>();
		this.dbscan = new DBSCAN();
		this.axon = -1;
	}

	@Override
	public void step() {
		LinkedList<Double> dendriteValues = new LinkedList<Double>();	
		DataSet data;
		
		//TODO: decide how long to keep frames and begin throwing away old ones after a certain point
		
		for(Node d: dendrites) {
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
	
	public void connectDendriteTo(Node node) {
		if (node.isReadyToConnect()) {
			this.dendrites.add(node);
			
			Random random = new Random();
			this.network.getGraph().addEdge(random.nextInt(), node, this);
		}
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
}
