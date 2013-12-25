package ipsum;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.swing.*;


/* TODO list
 * 
 * ensure no PRM can attach to the same set of nodes
 * Need to allow GIs to take a function
 * 
 * 
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
        
        Network network = new Network();
        try {
			network.buildNetwork(50,50,10);
		} catch (notEnoughPRMsException e) {
			e.printStackTrace();
			System.exit(1);
		}
        network.drawGraph();
     
        //Display the window.
        frame.setPreferredSize(new Dimension(700,700));
        frame.pack();
        frame.setVisible(true);

        (new Thread(network)).start();
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
}