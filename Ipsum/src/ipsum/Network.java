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

public class Network {
	LinkedList<Node> nodes;
	
	// Graph<V, E> where V is the type of the vertices and E is the type of the edges
    private Graph<Node, Integer> g;
    private int edgeCount = 0;
	public Network() {		
        g = new DirectedSparseGraph<Node, Integer>();
		nodes = new LinkedList<Node>();
        /*
	    //Make a PRM
        PRM prm = new PRM(this);
        //Make a global input
        GI gi1 = new GI(this,-1);
        GI gi2 = new GI(this,-1);
        //Make a global output
        GO go = new GO(this);
        //connect the PRM to the axon (for now, we will do this manually, but future implementations would likely have the PRM handle it on it's own as it feels the need to)
        prm.connectDendriteTo(gi1);
        prm.connectDendriteTo(gi2);
        go.connectDendriteTo(prm); 
        
        for (int i = 0; i < 10; i++) {
	        gi1.step();
	        gi2.step();
	        prm.step();
	        go.step();
        }
        prm.plotDendrites();  
		   
		   */		
	}
	
	public void buildNetwork(int GICount, int PRMCount, int GOCount) {
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
	    Layout<Node, Integer> layout = new SpringLayout<Node, Integer>(g);
	    layout.setSize(new Dimension(600,600)); // sets the initial size of the layout space
	    // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
	    BasicVisualizationServer<Node,Integer> vv = new BasicVisualizationServer<Node,Integer>(layout);
	    vv.setPreferredSize(new Dimension(600,600)); //Sets the viewing area size
	    
        // Setup up a new vertex to paint transformer...
        Transformer<Node,Paint> vertexPaint = new Transformer<Node,Paint>() {
            public Paint transform(Node i) {
            	return i.getColor();
            }
        };  
               
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);      
	    
	    Main.add(vv); 	
	}
	
	public Graph<Node, Integer> getGraph() {
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
		for(int i = 0; i < stepCount; i++) {
			for (Node n : nodes) {
				n.step();
			}
		}
	}

	public Node getRandomNode() {
		Random rand = new Random();
		return nodes.get(rand.nextInt(this.nodes.size()));
	}
	
	
}
