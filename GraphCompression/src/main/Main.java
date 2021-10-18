package main;


import java.io.IOException;

import graphComponents.Graph;
import graphUtils.General;

public class Main {
	public static void main(String[] args) throws IOException {
		String file1,     file2;
		Graph  G1,        G2,      G3;
		long   startTime, endTime;
		
		file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		file2 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph2.txt";
		
		G1 = General.fromFile(file1);
		G2 = General.fromFile(file1);
		G3 = General.fromFile(file1);
		
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
		
		System.out.println("Non-deterministic Vertex sparsification time: " + (endTime - startTime));
	}
}
