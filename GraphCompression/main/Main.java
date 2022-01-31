package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/Code/exampleGraph3.txt";
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/as19971108.txt";		//3K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/as20000102.txt";		//6.5K Node graph
		String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/CAIDA Relationships/as-caida20071112.txt";	//26K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/BrightKite/Brightkite_edges.txt";	//60K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Gowalla/Gowalla_edges.txt";	//200K Node graph
		//String file = "/home/callm/Life/UniGlasgow/Year4/Project/SNAP graphs/Road Networks/Texas/roadNet-TX.txt";	//1.3M node graph

		int numTerminals = 25;
		
		long startTime, endTime;
		
		//Load file
		startTime = System.nanoTime();
		Graph[] Graphs = General.fromSNAPFile(file, 8);
		endTime = System.nanoTime();
		
		System.out.println("Graphs created in: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		
		//Create a sparsifier for each Graph
		startTime = System.nanoTime();
		Sparsifier REC = new Sparsifier(Graphs[0]);
		
		Sparsifier RECIndSet = new Sparsifier(Graphs[1]);
		RECIndSet.setUseIndSet(true);
		
		Sparsifier REC25 = new Sparsifier(Graphs[2]);
		REC25.setEarlyStopping(25);
		
		Sparsifier REC50 = new Sparsifier(Graphs[3]);
		REC50.setEarlyStopping(50);
		
		Sparsifier REC75 = new Sparsifier(Graphs[4]);
		REC75.setEarlyStopping(75);
		
		Sparsifier Gauss = new Sparsifier(Graphs[5]);
		Gauss.setMethod("gauss");
		
		Sparsifier SP = new Sparsifier(Graphs[6]);
		SP.setMethod("sptree");
		
		Sparsifier random = new Sparsifier(Graphs[7]);
		random.setMethod("random");
		endTime = System.nanoTime();
		
		System.out.println("Sparsifiers created in: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		
		//Generate a random terminal set
		REC.randomTerminals(numTerminals);
		
		//Set all sparsifiers to use the same terminal set
		Integer[] terminals = REC.getTerminals();
		
		RECIndSet.setTerminals(terminals);
		REC25.setTerminals(terminals);
		REC50.setTerminals(terminals);
		REC75.setTerminals(terminals);
		Gauss.setTerminals(terminals);
		SP.setTerminals(terminals);
		random.setTerminals(terminals);
		
		//Sparsify each graph using the chosen method
		System.out.println("Beginning Compression...\n");
		REC.sparsify();
		
		RECIndSet.setPathLengths(REC.getPathLengths());
		RECIndSet.sparsify();
		
		REC25.setPathLengths(REC.getPathLengths());
		REC25.sparsify();
		
		REC50.setPathLengths(REC.getPathLengths());
		REC50.sparsify();
		
		REC75.setPathLengths(REC.getPathLengths());
		REC75.sparsify();
		
		Gauss.setPathLengths(REC.getPathLengths());
		Gauss.sparsify();
		
		SP.setPathLengths(REC.getPathLengths());
		SP.sparsify();
		
		random.setPathLengths(REC.getPathLengths());
		random.sparsify();
	}
}
