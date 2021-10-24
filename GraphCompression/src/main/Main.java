package main;


import java.io.IOException;

import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		String file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		
		Graph G1 = General.fromFile(file1);
		Graph G2 = General.fromFile(file1);
		
		G1.dijkstra(0, 6);
		G2.dijkstra(0, 6);
		
		long   startTime, endTime;
		
		System.out.println("Example Graph 1 (Small Graph)");
		
		startTime = System.nanoTime();
		G1.sparsify("gauss");
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + (endTime - startTime));
		
		startTime = System.nanoTime();
		G2.sparsify("");
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + (endTime - startTime));
		
		
		System.out.println("\n");
	}
}
