package main;


import java.io.IOException;

import graphComponents.Graph;
import graphUtils.General;

import sparsifiers.exact.DijkstraSPTree;
import sparsifiers.exact.GaussianElimination;


public class Main {
	public static void main(String[] args) throws IOException {
		String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		
		//Load file
		Graph G1 = General.fromFile(file);
		Graph G2 = General.fromFile(file);
		
		//Get random terminal set
		Integer[] terminals = sparsifiers.SparsifierUtils.randomTerminals(G1, 3);
		
		//Create a Gaussian elimination sparsifier and set its terminal list
		GaussianElimination gaussSparsifier = new GaussianElimination(G1);
		gaussSparsifier.setTerminals(terminals);
		
		//Create Dijkstra Shortest Path Tree sparsifier and set its terminal list
		DijkstraSPTree dijSparsifier = new DijkstraSPTree(G2);
		dijSparsifier.setTerminals(terminals);
		
		//Compress G1
		System.out.println(G1);
		gaussSparsifier.sparsify();
		System.out.println(G1);
		
		//Compress G2
		System.out.println(G2);
		dijSparsifier.sparsify();
		System.out.println(G2);
	}
}
