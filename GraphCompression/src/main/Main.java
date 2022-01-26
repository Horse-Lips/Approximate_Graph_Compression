package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		//String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph3.txt";
		//String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/as19971108.txt";		//3000 Node graph
		//String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/as20000102.txt";		//6500 Node graph
		String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/SNAP graphs/Autonomous Systems/CAIDA Relationships/as-caida20071112.txt";	//26000 Node graph
		//String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/SNAP graphs/Road Networks/Texas/roadNet-TX.txt";	//1.3M node graph
		
		
		long startTime, endTime;
		
		//Load file
		startTime = System.nanoTime();
		Graph G1 = General.fromSNAPFile(file);
		Graph G2 = General.fromSNAPFile(file);
		Graph G3 = General.fromSNAPFile(file);
		Graph G4 = General.fromSNAPFile(file);
		Graph G5 = General.fromSNAPFile(file);
		Graph G6 = General.fromSNAPFile(file);
		Graph G7 = General.fromSNAPFile(file);
		Graph G8 = General.fromSNAPFile(file);
		endTime = System.nanoTime();
		
		System.out.println("Graphs created in: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		
		//Create a sparsifier for each Graph
		startTime = System.nanoTime();
		Sparsifier REC = new Sparsifier(G1);
		
		Sparsifier RECIndSet = new Sparsifier(G2);
		RECIndSet.setUseIndSet(true);
		
		Sparsifier REC25 = new Sparsifier(G3);
		REC25.setEarlyStopping(25);
		
		Sparsifier REC50 = new Sparsifier(G4);
		REC50.setEarlyStopping(50);
		
		Sparsifier REC75 = new Sparsifier(G5);
		REC75.setEarlyStopping(75);
		
		Sparsifier Gauss = new Sparsifier(G6);
		Gauss.setMethod("gauss");
		
		Sparsifier SP = new Sparsifier(G7);
		SP.setMethod("sptree");
		
		Sparsifier random = new Sparsifier(G8);
		random.setMethod("random");
		endTime = System.nanoTime();
		
		System.out.println("Sparsifiers created in: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		
		//Generate a random terminal set
		REC.randomTerminals(10);
		
		
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
