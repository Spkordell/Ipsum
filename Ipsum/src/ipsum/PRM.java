package ipsum;
import ipsum.interfaces.INode;

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

public class PRM implements INode {
	private static final int minSteps = 4;
	private static final int frameCleaningMultiple = 200;
	private LinkedList<INode> dendrites;
	private LinkedList<DataPoint> frames;
	private double axon;
	private Network network;
	private int clusterCount;
	
	DBSCAN dbscan;
	Random rand;
	
	public PRM(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		this.dendrites = new LinkedList<INode>();
		this.frames = new LinkedList<DataPoint>();
		this.dbscan = new DBSCAN();
		this.axon = -1;
		this.clusterCount = 0;
		this.rand = new Random();
	}

	@Override
	public void step() {
		if (dendrites.size() > 0) {
			LinkedList<Double> dendriteValues = new LinkedList<Double>();	
			DataSet data;			
			
			for(INode d: dendrites) {
				dendriteValues.add(d.getAxon());
			}
			Vec frame = new DenseVector(dendriteValues);
			frames.add(new DataPoint(frame,null,null));
			data = new SimpleDataSet(frames);

			if (frames.size() >= minSteps) {
				try {
					List<List<DataPoint>> cluster = dbscan.cluster(data,3);
					
					//find largest cluster
					int largestCluster = 0;
					for (int i = 0; i < cluster.size(); i++) {
						if (cluster.get(i).size() > cluster.get(largestCluster).size()) {
							largestCluster = i;
						}
					}
				       
					/*
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
				    */
					
					axon = cluster.get(largestCluster).size();
				    //System.out.println("Center: "+center+", Last: "+frame+", Axon: "+ axon);
					
					clusterCount = cluster.size();
				    
				    
				    //System.out.println(cluster.get(largestCluster).size()+"/"+frames.size()+"="+(float)cluster.get(largestCluster).size()/frames.size());
				} catch (RuntimeException e) {
					//System.out.println(frames.size()+"__"+dendrites.size()+"____"+dendriteValues.size()+"______"+data);	
					axon = 0;
				}	
			}
		}
		growDendrites();
		//trimDendrites();
		cleanOldFrames();
		
	}

	private void cleanOldFrames() {
		int removeCount = 0;
		while (frames.size() > (Main.getStepsPerSecond() * frameCleaningMultiple)) {
			frames.removeFirst();
			removeCount++;
		}
		if (removeCount > 0) {
			System.out.println(removeCount+" frames removed. "+frames.size() + " remaining.");
		}
	}

	private void growDendrites() {
		//System.out.println(axon/frames.size());
		//if (dendrites.size() < 1 || rand.nextInt(101) < 8.5-(1.5*clusterCount)) {
		
		
		if (dendrites.size() < 1 || rand.nextFloat()<(1-(axon/frames.size()))) {
			INode node = this;
			int attemptsRemaining = 6;
			while((node == this || node.isReadyToConnect() == false || this.network.hasTwinIfConnected(this,node)) && attemptsRemaining-- > 0) {
				node = this.network.getRandomNode();
			}
			if(attemptsRemaining > 0) {
				connectDendriteTo(node);
			}
		}
	}
	
	public void trimDendrites() {
		if (clusterCount > 7) {
			dendrites.remove(rand.nextInt(dendrites.size()));
			clusterCount = 0;
			this.frames = new LinkedList<DataPoint>();
		}
		
	}

	@Override
	public double getAxon() {
		return this.axon;
	}

	@Override
	public boolean isReadyToConnect() {
		//return (frames.size() >= minSteps && dendrites.size() > 1 &&  rand.nextInt(101) < (1.5*clusterCount));
		
		return (frames.size() >= minSteps && dendrites.size() > 1 && rand.nextFloat()<(axon/frames.size()));
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
