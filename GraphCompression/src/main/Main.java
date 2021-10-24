package main;


import java.io.IOException;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.General;

public class Main {
	public static void main(String[] args) throws IOException {
		String file1,     file2;
		
		file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		file2 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph2.txt";
		
		Graph G = General.fromFile(file1);
		
		/*FOR TESTING - set value of Vertices to A, B, C, etc...*/
		String[] names = {"A", "B", "C", "D", "E", "F", "G"};
		
		for (int i = 0; i < G.size(); i++) {
			G.getVertex(i).setVal(names[i]);
		}
		
		int end = 6;
		G.dijkstra(0, end);
		
		General.printPath(G.getVertex(end));
		
		System.out.println(G);
		G.sparsify();
		
		/*FOR TESTING*/
		
		
		/*Graph G1 = General.fromFile(file1);
		Graph G2 = General.fromFile(file1);
		Graph G3 = General.fromFile(file1);
		
		long   startTime, endTime;
		
		System.out.println("Example Graph 1 (Small Graph)");
		
		startTime = System.nanoTime();
		G1.Gauss(0);
		G1.Gauss(0);
		G1.Gauss(0);
		G1.Gauss(0);
		G1.Gauss(0);
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + (endTime - startTime));
		
		startTime = System.nanoTime();
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		endTime = System.nanoTime();
		
		System.out.println("Deterministic Vertex sparsification time: " + (endTime - startTime));
		
		
		startTime = System.nanoTime();
		int max = 4;
		
		while (max >= 0) {
			int rand = (int) (Math.random() * (max + 1));
			G3.contract(rand);
			max--;
		}
		endTime = System.nanoTime();
		
		System.out.println("Non-deterministic Vertex sparsification time: " + (endTime - startTime));
		
		
		System.out.println("\n");
		
		
		G1 = General.fromFile(file2);
		G2 = General.fromFile(file2);
		G3 = General.fromFile(file2);
		
		System.out.println("Example Graph 2 (Large Fully Connected Graph)");
		
		startTime = System.nanoTime();
		G1.Gauss(0);
		G1.Gauss(0);
		G1.Gauss(0);
		G1.Gauss(0);
		G1.Gauss(0);
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + (endTime - startTime));
		
		startTime = System.nanoTime();
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		G2.contract(0);
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + (endTime - startTime));
		
		
		startTime = System.nanoTime();
		max = 4;
		
		while (max >= 0) {
			int rand = (int) (Math.random() * (max + 1));
			G3.contract(rand);
			max--;
		}
		endTime = System.nanoTime();
		
		System.out.println("Non-deterministic Vertex sparsification time: " + (endTime - startTime));*/
	}
}
