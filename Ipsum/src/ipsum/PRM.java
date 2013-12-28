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
 * The frames will collect over time to form a matrix. The matrix will be clustered and the axon will be determined as by (number of points in cluster last frame was grouped into)/total number of saved frames.
 *  
 * MDS or tensors are not needed with this implementation.
 * 
 */

public class PRM implements INode {
	private static final int minSteps = 4;
	private static final int frameCleaningMultiple = 200;
	private static final float connectionProbabilityDivider = 20;
	private static final int minPoints = 3;
	private LinkedList<INode> dendrites;
	private LinkedList<DataPoint> frames;
	private double axon;
	private Network network;
	private float correlation;
	
	DBSCAN dbscan;
	Random rand;
	
	public PRM(Network network) {
		this.network = network;
		this.network.getGraph().addVertex(this);
		this.dendrites = new LinkedList<INode>();
		this.frames = new LinkedList<DataPoint>();
		this.dbscan = new DBSCAN();
		this.axon = -1;
		this.rand = new Random();
		this.correlation = 0;
	}

	@Override
	public void step() {
		if (dendrites.size() > 0) {
			
			//prepare the incoming data
			LinkedList<Double> dendriteValues = new LinkedList<Double>();	
			DataSet data;					
			for(INode d: dendrites) {
				dendriteValues.add(d.getAxon());
			}
			Vec frame = new DenseVector(dendriteValues);
			frames.add(new DataPoint(frame,null,null));
			data = new SimpleDataSet(frames);

			//process the data
			if (frames.size() >= minSteps) {
				try {
					//cluster
					int[] designations = dbscan.cluster(data,minPoints,(int[])null);			
					List<List<DataPoint>> cluster = DBSCAN.createClusterListFromAssignmentArray(designations, data);
					//find largest cluster
					int largestCluster = 0;
					for (int i = 0; i < cluster.size(); i++) {
						if (cluster.get(i).size() > cluster.get(largestCluster).size()) {
							largestCluster = i;
						}
					}		
					//set axon and correlation
					axon = (float)cluster.get(designations[designations.length-1]).size()/frames.size();
					correlation = (float)cluster.get(largestCluster).size()/frames.size();
				} catch (RuntimeException e) {						
					axon = -1;
					//e.printStackTrace();
				}	
			} else {
				axon = -1;
			}
		}
		growDendrites();
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
		if (dendrites.size() < 1 || rand.nextFloat() < ((1-correlation)/connectionProbabilityDivider)   ) {
			INode node = this;
			int attemptsRemaining = 6;
			while((node == this || node.isReadyToConnect() == false || this.network.hasTwinIfConnected(this,node) ) && attemptsRemaining-- > 0) {
				node = this.network.getRandomNode();
			}
			if(attemptsRemaining > 0) {
				connectDendriteTo(node);
			}
		}
	}
	
	@Override
	public void optimize() {
		for(int k = 0; k < dendrites.size()/2; k++) {
			if (frames.size() >= minSteps && dendrites.size() >= 2) {
				try {
					//calculate current correlation
					DataSet data = new SimpleDataSet(frames);
					List<List<DataPoint>> cluster = dbscan.cluster(data,minPoints);			
					int largestCluster = 0;
					for (int i = 0; i < cluster.size(); i++) {
						if (cluster.get(i).size() > cluster.get(largestCluster).size()) {
							largestCluster = i;
						}
					}		
					float currentCorrelation = (float)cluster.get(largestCluster).size()/frames.size(); //TODO the correlations really should be calculated by a helper function to make alterations easier
		
					//remove a dendrite (random for now, probably want to iterate later)
					int dendriteToRemove = rand.nextInt(dendrites.size());
					LinkedList<DataPoint> alteredFrames = new LinkedList<DataPoint>();
					for (DataPoint p: frames) {
						LinkedList<Double> alteredDendriteValues = new LinkedList<Double>();	
						for (int i = 0; i < p.numNumericalValues(); i++) {
							if (i != dendriteToRemove) {
								alteredDendriteValues.add(p.getNumericalValues().get(i));
							}
						}
						alteredFrames.add(new DataPoint(new DenseVector(alteredDendriteValues),null,null));
					}
					
					//calculate correlation without dendrite
					data = new SimpleDataSet(alteredFrames);
					cluster = dbscan.cluster(data,minPoints);			
					largestCluster = 0;
					for (int i = 0; i < cluster.size(); i++) {
						if (cluster.get(i).size() > cluster.get(largestCluster).size()) {
							largestCluster = i;
						}
					}		
					float futureCorrelation = (float)cluster.get(largestCluster).size()/alteredFrames.size();  //TODO: here too, and many other placed in this class
					
					//if correlation rises, remove the dendrite for real, else, leave things as they are
					if (futureCorrelation > currentCorrelation) {
						//TODO: Will need to check for twin condition here too.
						System.out.println("removed dendrite");
						this.network.getGraph().removeEdge(this.network.getGraph().findEdge(dendrites.get(dendriteToRemove),this)); //TODO, check order or "this" and removed node, probably right	
						dendrites.remove(dendriteToRemove);
						this.frames = new LinkedList<DataPoint>();
					} else {
						System.out.println("kept dendrite");
					}
				} catch (RuntimeException e) {
					
				}
			}
		}
	}

	@Override
	public double getAxon() {
		return this.axon;
	}

	@Override
	public boolean isReadyToConnect() {
		return (frames.size() >= minSteps && dendrites.size() > 1 && rand.nextFloat() < (correlation/connectionProbabilityDivider));
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
		if (correlation > .75) {
			return Color.BLUE; 
		} else if (correlation >.5){
			return Color.ORANGE;
		} else if (correlation > .25){
			return Color.GRAY;
		} else {
			return Color.WHITE;
		}
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
