import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;

public class Main {
	private static JFrame frame;
	private static LinkedList<JComponent> cList;

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	cList = new LinkedList<JComponent>();
    	
        //Create and set up the window.
        frame = new JFrame("Ipsum");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1));

/*        
        //Make a PRM
        PRM prm = new PRM();
        //Make a global input
        GI gi1 = new GI(-1);
        GI gi2 = new GI(-1);
        //Make a global output
        GO go = new GO();
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
        
        Network network = new Network();
        
        //Display the window.
        frame.setPreferredSize(new Dimension(1000,600));
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	public static Frame getFrame() {
		return frame;
	}
	
	public static void add(JComponent component) {
		cList.add(component);
		getFrame().setLayout(new GridLayout(cList.size(), 1));
		for(JComponent comp : cList) {
			getFrame().add(comp);
		}
	}
}