package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		String file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph3.txt";
		
		
		/*Create two graphs from the same file*/
		Graph G1 = General.fromFile(file1);
		Graph G2 = General.fromFile(file1);
		
		
		/*Create a sparsifier for each graph*/
		Sparsifier G1Sparsifier = new Sparsifier(G1);
		Sparsifier G2Sparsifier = new Sparsifier(G2);
		
		
		/*Create a random set of terminals and use it for both graphs*/
		G1Sparsifier.randomTerminals(0, 40);	//Set 40 random Vertices as terminals
		G2Sparsifier.setTerminals(G1Sparsifier.getTerminals());
		
		
		/*Set sparsifier 1 to use the Gaussian Elimination method*/
		G1Sparsifier.setMethod("gauss");
		
		
		/*Carry out sparsification of graph using both methods*/
		G1Sparsifier.sparsify(true);
		G2Sparsifier.sparsify(true);
		
	}
}
