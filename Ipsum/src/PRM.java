import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.OPTICS;
import jsat.distributions.multivariate.NormalM;
import jsat.graphing.CategoryPlot;
import jsat.graphing.Graph2D;
import jsat.graphing.ParallelCoordinatesPlot;
import jsat.graphing.ReachabilityPlot;
import jsat.graphing.ScatterPlot;
import jsat.graphing.ScatterplotMatrix;
import jsat.linear.DenseMatrix;
import jsat.linear.DenseVector;
import jsat.linear.Matrix;
import jsat.linear.RandomVector;
import jsat.linear.Vec;


/*
import java.awt.FileDialog;

import de.lmu.ifi.dbs.elki.algorithm.Algorithm;
import de.lmu.ifi.dbs.elki.algorithm.outlier.lof.LOF;
import de.lmu.ifi.dbs.elki.data.LabelList;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.FileBasedDatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancevalue.DoubleDistance;
import de.lmu.ifi.dbs.elki.result.Result;
import de.lmu.ifi.dbs.elki.result.outlier.OutlierResult;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;
*/

public class PRM {



	public PRM() {
		LinkedList<DataPoint> data = new LinkedList<DataPoint>();
		
		for (int i = 0; i<100; i++) {
			data.add(new DataPoint(DenseVector.toDenseVec(i,i),null,null));
		}
		DataSet dataSet = new SimpleDataSet(data);
	
        ScatterPlot plot = new ScatterPlot(dataSet.getNumericColumn(0),dataSet.getNumericColumn(1));
        Main.getFrame().add(plot);
		
//		OPTICS optics = new OPTICS();
//		List<List<DataPoint>> out = optics.cluster(dataSet);
//		System.out.println(out);
		
		//ScatterPlot plot = new ScatterPlot(optics.cluster(dataSet));
//		SimpleDataSet outDataSet = new SimpleDataSet(out.get(0));
		//ParallelCoordinatesPlot plot = new ParallelCoordinatesPlot(outDataSet);
		
		//CategoryPlot plot = new CategoryPlot(new ClassificationDataSet(outDataSet,1));
		
//		ReachabilityPlot rplot = new ReachabilityPlot(optics.getReachabilityArray());
//        Main.getFrame().add(rplot);
		
		//dataSet.addDataPoint(a, new int[0], 0);
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
	/*
	FileDialog fd;
	public PRM() {
		
		fd = new FileDialog(Main.getFrame(), "Select a File", FileDialog.LOAD);
	    fd.setVisible(true);
	    String filename = fd.getDirectory() + System.getProperty("file.separator") + fd.getFile();

		
		// Setup parameters:
		ListParameterization params = new ListParameterization();
		params.addParameter(FileBasedDatabaseConnection.INPUT_ID, filename);
		// Add other parameters for the database here!

		// Instantiate the database:
		Database db = ClassGenericsUtil.parameterizeOrAbort(StaticArrayDatabase.class, params);
		// Don't forget this, it will load the actual data...
		db.initialize();

		Relation<NumberVector<?>> vectors = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
		Relation<LabelList> labels = db.getRelation(TypeUtil.LABELLIST);
		
		System.out.println("");
		System.out.println("HERE");
		System.out.println(vectors.getLongName());
		System.out.println(labels.getLongName());		
		//////
		
		//ListParameterization params = new ListParameterization();
		//params.addParameter(LOF.K_ID, 20);

		Algorithm alg = ClassGenericsUtil.parameterizeOrAbort(LOF.class, params);
		Result result = alg.run(db); // will choose the relation automatically!

		LOF<NumberVector<?>, DoubleDistance> lof = ClassGenericsUtil.parameterizeOrAbort(LOF.class, params);
		//OutlierResult outliers = alg.run(rel); // Manually chosen relation - not general!
	}
	*/
}
