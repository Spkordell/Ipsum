package ipsum;
import java.util.LinkedList;
import java.util.List;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.DBSCAN;
import jsat.graphing.CategoryPlot;
import jsat.graphing.ScatterPlot;
import jsat.linear.DenseVector;
import jsat.linear.RandomVector;
import jsat.linear.Vec;

public class ClusterTest {
	public ClusterTest() {
		LinkedList<DataPoint> data = new LinkedList<DataPoint>();
		for (int i = -400; i < 400; i++) {
			Vec v = RandomVector.random(2);
			
			if(v.get(0) > .3) {
				v.set(0,v.get(0)+.1);
			} else {
				v.set(0,v.get(0)-.1);
			}
			
			if(v.get(1) > .5) {
				v.set(1,v.get(1)+.1);
			} else {
				v.set(1,v.get(1)-.1);
			}
			data.add(new DataPoint(v,null,null));
			
			//data.add(new DataPoint(DenseVector.toDenseVec(i,i),null,null));
			//data.add(new DataPoint(DenseVector.toDenseVec(i,(2*i)+15),null,null));
			//data.add(new DataPoint(DenseVector.toDenseVec(i,i*i+1000),null,null));
			//data.add(new DataPoint(DenseVector.toDenseVec(i,i*i+5000),null,null));
			
		}
		DataSet dataSet = new SimpleDataSet(data);
	
        ScatterPlot plot = new ScatterPlot(dataSet.getNumericColumn(0),dataSet.getNumericColumn(1));
        Main.add(plot);
		
		DBSCAN dbscan = new DBSCAN();	
		List<List<DataPoint>> out = dbscan.cluster(dataSet,2);
		
		int[] outAsIntArray = dbscan.cluster(dataSet,(int[])null);
		
		for(int a: outAsIntArray) {
			System.out.println("[[  "+a+"  ]]");
		}
		
		System.out.println("Number of clusters: "+out.size());
		System.out.println("Dimensionality of points: "+out.get(0).get(0).numNumericalValues());

		//We create a new data set for the clusters. This data set will have 2 dimensions so we can visualize it, and out.size() target class values
        ClassificationDataSet clusterOut = new ClassificationDataSet(2, new CategoricalData[0], new CategoricalData(out.size()));
		
        for (int i = 0; i < out.size(); i++) {
        	for (int j = 0; j < out.get(i).size(); j++) {
        		clusterOut.addDataPoint(out.get(i).get(j).getNumericalValues(), new int[0], i);
        	}
        }
        
       CategoryPlot clusterPlot = new CategoryPlot(clusterOut);
       Main.add(clusterPlot);
        
       /*ReachabilityPlot rplot = new ReachabilityPlot(dbscan.getReachabilityArray());
       Main.add(rplot);*/
		
       
       //try to compute axon (vector drawn from center of largest cluster to last input)
       
       //find largest cluster
       int largestCluster = 0;
       for (int i = 0; i < out.size(); i++) {
		   System.out.println("Cluster "+(i+1)+" size: "+out.get(i).size());
    	   if (out.get(i).size() > out.get(largestCluster).size()) {
    		   largestCluster = i;
    	   }
       }
       System.out.println("Largest Cluster: "+(largestCluster+1)); //Only adding one because the graph starts options from 1 but indexes start from 0
       
       //find center of largest cluster (Find the mean of each dimension, only two in this case, but more in future implementations)
       Vec center = DenseVector.zeros(2);
       for (int d = 0; d < 2; d++) { //do for each dimension, currently 2
	       for (int i = 0; i < out.get(largestCluster).size(); i++) {
	    	   center.set(d, center.get(d)+out.get(largestCluster).get(i).getNumericalValues().get(d));
	       }
	       center.set(d, center.get(d)/out.get(largestCluster).size());
       }
       System.out.println("Largest Cluster Center: " + center);
       
       //draw vector from center to latest input and use as axon
       Vec last = dataSet.getDataPoint(dataSet.getNumNumericalVars()).getNumericalValues(); 
       Vec axon = last.subtract(center);
       System.out.println("Last: "+last);
       System.out.println("Axon: "+axon);
	}
	
		
	/*
	public PRM() {
		//We create a new data set. This data set will have 2 dimensions so we can visualize it, and 4 target class values
        ClassificationDataSet dataSet = new ClassificationDataSet(2, new CategoricalData[0], new CategoricalData(4));
                
        //We can generate data from a multivarete normal distribution. The 'M' at the end stands for Multivariate 
        NormalM normal;

        //The normal is specifed by a mean and covariance matrix. The covariance matrix must be symmetric. 
        //We use a simple covariance matrix for each data point for simplicity
        Matrix covariance = new DenseMatrix(new double[][]
        {
            {1.0, 0.0}, //Try altering these values to see the change!
            {0.0, 1.0} //Just make sure its still symetric! 
        });

        //And we create 4 different means
        Vec mean0 = DenseVector.toDenseVec(0.0, 0.0);
        Vec mean1 = DenseVector.toDenseVec(0.0, 4.0);
        Vec mean2 = DenseVector.toDenseVec(4.0, 0.0);
        Vec mean3 = DenseVector.toDenseVec(4.0, 4.0);

        System.out.println(mean1);
        
        Vec[] means = new Vec[] {mean0, mean1, mean2, mean3};

        //We now generate out data
        for(int i = 0; i < means.length; i++)
        {
            normal = new NormalM(means[i], covariance);
            for(Vec sample : normal.sample(300, new Random()))
                dataSet.addDataPoint(sample, new int[0], i);
        }
        
        CategoryPlot plot = new CategoryPlot(dataSet);
        Main.getFrame().add(plot);
	}
	
	
	*/
}
