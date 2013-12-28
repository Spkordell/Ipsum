package ipsum;
import ipsum.exceptions.notEnoughPRMsException;
import ipsum.gifunctions.GITestFunction1;
import ipsum.gifunctions.GITestFunction2;
import ipsum.gifunctions.GITestFunctionRandom;
import ipsum.interfaces.GIFunction;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.swing.*;


/*TODO list
 * 
 * Fix Trimming (use sleeping)
 * 
 * Something is still does not seem right with the way to-be-twins are calculated. White cells should be able to connect. 
 * 
 * train towards a directive
 * Make simulators for testing
 * 
 * Fix exceptions in PRM (uncomment the stack-trace print in the catch to see what I mean)
 */

public class Main {
	private static JFrame frame;
	private static JPanel mainPanel;
	private static LinkedList<JComponent> cList;
	private static JLabel stepsPerSecondLabel;
	private static double stepsPerSecond;
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	cList = new LinkedList<JComponent>();
    	mainPanel = new JPanel(new GridLayout(1, 1));    	
        //Create and set up the window.
        frame = new JFrame("Ipsum");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel,BorderLayout.CENTER);      
        stepsPerSecondLabel = new JLabel("sps: "+stepsPerSecond);
        stepsPerSecondLabel.setSize(new Dimension(100,10));        
        frame.add(stepsPerSecondLabel,BorderLayout.SOUTH);
        
        
        LinkedList<GIFunction> giFunctions = new LinkedList<GIFunction>();
        giFunctions.add(new GITestFunction1());
        giFunctions.add(new GITestFunction2());  
        Network network = new Network();
        try {
			network.buildNetwork(20,60,1,new GITestFunctionRandom());
        	//network.buildNetwork(giFunctions,1,0);
		} catch (notEnoughPRMsException e) {
			e.printStackTrace();
			System.exit(1);
		}
        network.drawGraph();
        (new Thread(network)).start();
        
        //ClusterTest clusterTest = new ClusterTest();
        
        //Display the window.
        frame.setPreferredSize(new Dimension(700,700));
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

	public static JPanel getMainPanel() {
		return mainPanel;
	}
	
	public static void add(JComponent component) {
		cList.add(component);
		getMainPanel().setLayout(new GridLayout(cList.size(), 1));
		for(JComponent comp : cList) {
			getMainPanel().add(comp);
		}
	}
	
	public static void updateStepsPerSecond(double sps) {
		stepsPerSecond = sps;
		try {
			stepsPerSecondLabel.setText("sps: "+Double.parseDouble(new DecimalFormat("#.#").format(stepsPerSecond)));
		} catch(NumberFormatException e) {
		}
	}

	public static double getStepsPerSecond() {
		return stepsPerSecond;
	}
}