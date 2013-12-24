package ipsum;

import java.awt.Dimension;
import java.awt.Paint;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Network implements Runnable {
	LinkedList<INode> nodes;
	
	// Graph<V, E> where V is the type of the vertices and E is the type of the edges
    private Graph<INode, Integer> g;
    private int edgeCount = 0;
	public Network() {		
        g = new DirectedSparseGraph<INode, Integer>();
		nodes = new LinkedList<INode>();	
	}
	
	public void buildNetwork(int GICount, int PRMCount, int GOCount) throws notEnoughPRMsException {
		if (GOCount > PRMCount) {
			throw(new notEnoughPRMsException());			
		}
		
		
		for (int i = 0; i < GICount; i++) {
			nodes.add(new GI(this,-1));
		}
		for (int i = 0; i < PRMCount; i++) {
			nodes.add(new PRM(this));
		}		
		for (int i = 0; i < GOCount; i++) {
			nodes.add(new GO(this));
		}				
	}
	
	public void drawGraph() {
	    // The Layout<V, E> is parameterized by the vertex and edge types
	    Layout<INode, Integer> layout = new SpringLayout<INode, Integer>(g);
	    layout.setSize(new Dimension(600,600)); // sets the initial size of the layout space
	    // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
	    BasicVisualizationServer<INode,Integer> vv = new BasicVisualizationServer<INode,Integer>(layout);
	    vv.setPreferredSize(new Dimension(600,600)); //Sets the viewing area size
	    
        // Setup up a new vertex to paint transformer...
        Transformer<INode,Paint> vertexPaint = new Transformer<INode,Paint>() {
            public Paint transform(INode i) {
            	return i.getColor();
            }
        };  
               
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);      
	    
	    Main.add(vv); 	
	}
	
	public Graph<INode, Integer> getGraph() {
		return this.g;
	}
	
	public void incrementEdgeCount() {
		this.edgeCount++;
	}
	
	public void decrementEdgeCount() {
		this.edgeCount--;
	}
	
	public int getEdgeCount() {
		return this.edgeCount;
	}

	public void step(int stepCount) {
		double oldTime;
		
		for(int i = 0; i < stepCount; i++) {
			oldTime = System.currentTimeMillis();
			for (INode n : nodes) {
				n.step();
			}
			Main.updateStepsPerSecond(1/((System.currentTimeMillis() - oldTime)/1000));
		}
	}

	public INode getRandomNode() {
		Random rand = new Random();
		return nodes.get(rand.nextInt(this.nodes.size()));
	}

	@Override
	public void run() {
		double oldTime;
		int stepCount = 0;
		while(true) {
			oldTime = System.currentTimeMillis();
			for (INode n : nodes) {
				n.step();
			}
			stepCount++;
			Main.updateStepsPerSecond(1/((System.currentTimeMillis() - oldTime)/1000));
			System.out.println(stepCount);
		}
	}

	public boolean alreadyAttachedToGO(INode node) {
		for (INode n : nodes) {
			if (n instanceof GO) {
				if (n.hasDendriteConnectedTo(node)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
}
