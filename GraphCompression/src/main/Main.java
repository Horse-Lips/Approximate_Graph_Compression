package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		
		
		//Load file
		Graph G1 = General.fromFile(file);
		Graph G2 = General.fromFile(file);
		Graph G3 = General.fromFile(file);
		
		
		//Create a sparsifier for each Graph
		Sparsifier REC = new Sparsifier(G1);
		
		Sparsifier Gauss = new Sparsifier(G2);
		Gauss.setMethod("gauss");
		
		Sparsifier SP = new Sparsifier(G3);
		SP.setMethod("sptree");
		
		
		//Generate a random terminal set
		REC.randomTerminals(3);
		
		
		//Set all sparsifiers to use the same terminal set
		Integer[] terminals = REC.getTerminals();
		
		Gauss.setTerminals(terminals);
		SP.setTerminals(terminals);
		
		
		//Sparsify each graph using the chosen method
		REC.sparsify(true);
		Gauss.sparsify(true);
		SP.sparsify(true);
	}
}
