package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph3.txt";
		
		/*Graph G = General.fromFile(file1);
		
		Sparsifier sparsifier = new Sparsifier(G);
		
		Integer[] termList = {0, 2, 6};
		
		sparsifier.setTerminals(termList);
		
		sparsifier.SPTree();
		
		System.out.println(G);*/
		
		/*Create two graphs from the same file*/
		Graph G1 = General.fromFile(file);
		Graph G2 = General.fromFile(file);
		Graph G3 = General.fromFile(file);
		
		
		/*Create a sparsifier for each graph*/
		Sparsifier G1Sparsifier = new Sparsifier(G1);
		Sparsifier G2Sparsifier = new Sparsifier(G2);
		Sparsifier G3Sparsifier = new Sparsifier(G3);
		
		
		/*Create a random set of terminals and use it for both graphs*/
		G1Sparsifier.randomTerminals(0, 10);	//Set 40 random Vertices as terminals
		G2Sparsifier.setTerminals(G1Sparsifier.getTerminals());
		G3Sparsifier.setTerminals(G1Sparsifier.getTerminals());
		
		
		/*Set sparsifier 1 to use the Gaussian Elimination method*/
		G1Sparsifier.setMethod("gauss");
		G3Sparsifier.setMethod("sptree");
		
		
		/*Carry out sparsification of graph using both methods*/
		G1Sparsifier.sparsify(true);
		G2Sparsifier.sparsify(true);
		G3Sparsifier.sparsify(true);
		
	}
}
