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
		
		
		String file2 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph2.txt";
		
		G1 = General.fromFile(file2);
		G2 = General.fromFile(file2);
		
		G1.dijkstra(0, 19);
		G2.dijkstra(0, 19);
		
		System.out.println("Example Graph 2 (Medium Graph)");
		
		startTime = System.nanoTime();
		G1.sparsify("gauss");
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + (endTime - startTime));
		
		startTime = System.nanoTime();
		G2.sparsify("");
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + (endTime - startTime));
		
		
		System.out.println("\n");
		
		
		String file3 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph3.txt";
		
		G1 = General.fromFile(file3);
		G2 = General.fromFile(file3);
		
		G1.dijkstra(0, 63);
		G2.dijkstra(0, 63);
		
		System.out.println("Example Graph 3 (Large Graph)");
		
		startTime = System.nanoTime();
		G1.sparsify("gauss");
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + (endTime - startTime));
		
		startTime = System.nanoTime();
		G2.sparsify("");
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + (endTime - startTime));
	}
}
