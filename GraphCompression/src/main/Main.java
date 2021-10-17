package main;


import java.io.IOException;

import graphComponents.Graph;
import graphUtils.General;

public class Main {
	public static void main(String[] args) throws IOException {
		String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph.txt";
		
		Graph G1 = General.fromFile(file);
		Graph G2 = General.fromFile(file);
		
		long startTime, endTime;
		
		startTime = System.nanoTime();
		System.out.println(G1);
		G1.Gauss(0);
		System.out.println(G1);
		G1.Gauss(0);
		System.out.println(G1);
		G1.Gauss(0);
		System.out.println(G1);
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + (endTime - startTime));
		
		startTime = System.nanoTime();
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + (endTime - startTime));
	}
}
