package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		String file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		Graph G1 = General.fromFile(file1);
		Graph G2 = General.fromFile(file1);
		
		G1.dijkstra(0);
		G2.dijkstra(0);
		
		System.out.println("G1 shortest paths from Vertex 0 before contraction");
		for (int i = 0; i < G1.size(); i++) {
			System.out.println("Vertex " + i + ": " + G1.getVertex(i).getPathLength());
		}
		System.out.println("\n");
		
		G1.getVertex(0).setTerminal(true);
		G1.getVertex(2).setTerminal(true);
		G1.getVertex(4).setTerminal(true);
		G1.getVertex(6).setTerminal(true);
		
		Sparsifier G1Sparsifier = new Sparsifier(G1);
		G1Sparsifier.setMethod("gauss");
		
		G1Sparsifier.sparsify();
		
		System.out.println("G1 shortest paths from Vertex 0 after contraction");
		for (int i = 0; i < G1.size(); i++) {
			if (!G1.getVertex(i).isDeactivated()) {
				System.out.println("Vertex " + i + ": " + G1.getVertex(i).getPathLength());
			}
		}
		System.out.println("\n");
		
		
		System.out.println("G2 shortest paths from Vertex 0 before contraction");
		for (int i = 0; i < G2.size(); i++) {
			System.out.println("Vertex " + i + ": " + G2.getVertex(i).getPathLength());
		}
		System.out.println("\n");
		
		G2.getVertex(0).setTerminal(true);
		G2.getVertex(2).setTerminal(true);
		G2.getVertex(4).setTerminal(true);
		G2.getVertex(6).setTerminal(true);
		
		Sparsifier G2Sparsifier = new Sparsifier(G2);
		
		G2Sparsifier.sparsify();
		
		System.out.println("G2 shortest paths from Vertex 0 after contraction");
		for (int i = 0; i < G2.size(); i++) {
			if (!G2.getVertex(i).isDeactivated()) {
				System.out.println("Vertex " + i + ": " + G2.getVertex(i).getPathLength());
			}
		}
		System.out.println("\n");
		
	}
}
