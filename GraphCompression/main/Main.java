package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;
import graphUtils.SimpleTuple;


public class Main {
	public static void main(String[] args) throws IOException {
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/as19971108.txt";		//3K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/as20000102.txt";		//6.5K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/BrightKite/Brightkite_edges.txt";	//60K Node graph
		String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Gowalla/Gowalla_edges.txt";	//200K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Road Networks/Texas/roadNet-TX.txt";	//1.3M node graph

		int numTerminals = 10;
		
		long startTime, endTime;
		
        
        System.out.println("------ Gaussian Elimination ------");
        Graph g = General.fromSNAPFile(file);       //Load file

		Sparsifier spars = new Sparsifier(g);       //Create sparsifier and set graph
        
        spars.randomTerminals(numTerminals);        //Generate some random terminals
		Integer[] terminals = spars.getTerminals(); //And retrieve them for later use

        
		//spars.setMethod("gauss");

        //Returns a typle of (time, average quality, worst quality)
        //SimpleTuple results = spars.sparsify();
        
        //System.out.println("Time taken: " + results.getFirst());
        //System.out.println("Average quality: " + results.getSecond());
        //System.out.println("Worst quality: " + results.getThird());

        //double[][] initialPathLengths = spars.getPathLengths();

        
        /*System.out.println("\n------ SPT ------");
        g = General.fromSNAPFile(file);

        spars = new Sparsifier(g);
        spars.setTerminals(terminals);
        spars.setMethod("");
        //spars.setPathLengths(initialPathLengths);

        SimpleTuple results = spars.sparsify();
        
        System.out.println("Time taken: " + results.getFirst());
        System.out.println("Average quality: " + results.getSecond());
        System.out.println("Worst quality: " + results.getThird());

        double[][] initialPathLengths = spars.getPathLengths();
        
        System.out.println("\n------ Standard REC ------");*/

        //double avgAvg   = 0;
        //double avgWorst = 0;
        //double avgTime  = 0;

        /*for (int i = 0; i < 10; i++) {
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("");
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  += (double) results.getFirst();            
            avgAvg   += (double) results.getSecond();
            avgWorst += (double) results.getThird();

            
        }

        System.out.println("Average of average qualities: " + (avgAvg / 10));
        System.out.println("Average of worst qualities: "   + (avgWorst / 10));
        System.out.println("Average Execution Time: "       + (avgTime / 10));
        

        System.out.println("\n------ REC with Independent Set------");

        avgAvg   = 0;
        avgWorst = 0;
        avgTime  = 0;

        for (int i = 0; i < 10; i++) {
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("");
            spars.setUseIndSet(true);
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  += (double) results.getFirst();            
            avgAvg   += (double) results.getSecond();
            avgWorst += (double) results.getThird();
        }

        System.out.println("Average of average qualities: " + (avgAvg / 10));
        System.out.println("Average of worst qualities: "   + (avgWorst / 10));
        System.out.println("Average Execution Time: "       + (avgTime / 10));


        System.out.println("\n------ REC With Early Stopping After 25% of Non-Terminals Contracted ------");
        
        */


        double avgAvg   = 0;
        double avgWorst = 0;
        double avgTime  = 0;

        g = General.fromSNAPFile(file);

        spars = new Sparsifier(g);
        spars.setTerminals(terminals);
        spars.setMethod("");
        spars.setEarlyStopping(70);

        SimpleTuple results = spars.sparsify();
        
        System.out.println("Time taken: " + results.getFirst());
        System.out.println("Average quality: " + results.getSecond());
        System.out.println("Worst quality: " + results.getThird());

        double[][] initialPathLengths = spars.getPathLengths();


        /*
        avgAvg   = 0;
        avgWorst = 0;
        avgTime  = 0;

        for (int i = 0; i < 10; i++) {
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("");
            spars.setEarlyStopping(25);
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  += (double) results.getFirst();            
            avgAvg   += (double) results.getSecond();
            avgWorst += (double) results.getThird();
        }

        System.out.println("Average of average qualities: " + (avgAvg   / 10));
        System.out.println("Average of worst qualities: "   + (avgWorst / 10));
        System.out.println("Average Execution Time: "       + (avgTime  / 10));


        System.out.println("\n------ REC With Early Stopping After 50% of Non-Terminals Contracted ------");

        avgAvg   = 0;
        avgWorst = 0;
        avgTime  = 0;

        for (int i = 0; i < 10; i++) {
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("");
            spars.setEarlyStopping(50);
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  += (double) results.getFirst();            
            avgAvg   += (double) results.getSecond();
            avgWorst += (double) results.getThird();
        }

        System.out.println("Average of average qualities: " + (avgAvg / 10));
        System.out.println("Average of worst qualities: "   + (avgWorst / 10));
        System.out.println("Average Execution Time: "       + (avgTime / 10));


        System.out.println("\n------ REC With Early Stopping After 75% of Non-Terminals Contracted ------");

        avgAvg   = 0;
        avgWorst = 0;
        avgTime  = 0;

        for (int i = 0; i < 10; i++) {
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("");
            spars.setEarlyStopping(75);
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  += (double) results.getFirst();            
            avgAvg   += (double) results.getSecond();
            avgWorst += (double) results.getThird();
        }

        System.out.println("Average of average qualities: " + (avgAvg / 10));
        System.out.println("Average of worst qualities: "   + (avgWorst / 10));
        System.out.println("Average Execution Time: "       + (avgTime / 10));


        /*System.out.println("\n------ Random Selection Algorithm ------");

        avgAvg   = 0;
        avgWorst = 0;
        avgTime  = 0;

        for (int i = 0; i < 10; i++) {
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("random");
            spars.setEarlyStopping(75);
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  += (double) results.getFirst();            
            avgAvg   += (double) results.getSecond();
            avgWorst += (double) results.getThird();
        }

        System.out.println("Average of average qualities: " + (avgAvg / 10));
        System.out.println("Average of worst qualities: "   + (avgWorst / 10));
        System.out.println("Average Execution Time: "       + (avgTime / 10));*/


        System.out.println("\n------ REC Every Percent ------");

        double[] runTimes   = new double[100];
        double[] avgQuals   = new double[100];
        double[] worstQuals = new double[100];
        
        for (int percent = 71; percent < 100; percent++) {
            avgAvg   = 0;
            avgWorst = 0;
            avgTime  = 0;

            
            g = General.fromSNAPFile(file);
            
            spars = new Sparsifier(g);
            spars.setTerminals(terminals);
            spars.setMethod("");
            spars.setEarlyStopping(percent);
            spars.setPathLengths(initialPathLengths);

            results = spars.sparsify();
            
            avgTime  = (double) results.getFirst();            
            avgAvg   = (double) results.getSecond();
            avgWorst = (double) results.getThird();
            
            
            runTimes[percent] = avgTime;
            avgQuals[percent] = avgAvg;
            worstQuals[percent] = avgWorst;

            System.out.println(percent + 1 + " Done!");

        }

        String timeString  = "Times: [";
        String avgString   = "Average Qualities: [";
        String worstString = "Worst Qualities: [";
        
        for (int i = 0; i < 99; i++) {
            timeString  = timeString  + Double.toString(runTimes[i])   + ", ";
            avgString   = avgString   + Double.toString(avgQuals[i])   + ", ";
            worstString = worstString + Double.toString(worstQuals[i]) + ", ";
        }

        timeString  = timeString  + Double.toString(runTimes[99])   + "]";
        avgString   = avgString   + Double.toString(avgQuals[99])   + "]";
        worstString = worstString + Double.toString(worstQuals[99]) + "]";

        System.out.println(timeString);
        System.out.println(avgString);
        System.out.println(worstString);
	}
}
