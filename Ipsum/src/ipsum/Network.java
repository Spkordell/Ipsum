package ipsum;

import java.awt.Dimension;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Network {
	// Graph<V, E> where V is the type of the vertices and E is the type of the edges
    Graph<Node, Integer> g;
	public Network() {		
        g = new DirectedSparseGraph<Node, Integer>();
		
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
		
		//TODO step network (see main for missing pieces)      
		   
	    // The Layout<V, E> is parameterized by the vertex and edge types
	    Layout<Node, Integer> layout = new CircleLayout<Node, Integer>(g);
	    layout.setSize(new Dimension(200,200)); // sets the initial size of the layout space
	    // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
	    BasicVisualizationServer<Node,Integer> vv = new BasicVisualizationServer<Node,Integer>(layout);
	    vv.setPreferredSize(new Dimension(300,300)); //Sets the viewing area size
	    
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

	public void run() {
		// TODO Auto-generated method stub	
	}
}
