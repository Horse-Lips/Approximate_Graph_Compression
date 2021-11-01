package main;


import java.io.IOException;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.General;


public class Main {
	public static void main(String[] args) throws IOException {
		String file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		String file2 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph2.txt";
		String file3 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph3.txt";
		
		long startTime, endTime;
		
		Graph G;
		
		
		double percentTerminals = 60;	//Set % of Vertices as terminals
		double divTerminals;
		int    totalTerminals;			//Total amount of terminals in Graph
		int[] mustHave = new int[2];					//Vertex indexes that must be terminals (Just start and end here)
		mustHave[0] = 0;
		
		
		int    endIndex;	//Goal index in shortest path
		Vertex endVert;		//Vertex representation of goal
		double originalPathLength;
		
		/*Compression of small graph (file1)*/
		System.out.println("Compressing small graph");
		
		G = General.fromFile(file1);
		endIndex = 6;
		endVert  = G.getVertex(endIndex);
		
		G.dijkstra(0, endIndex);
		originalPathLength = endVert.getPathLength();
		System.out.println("Shortest path of length: " + originalPathLength);
		
		divTerminals = 100 / percentTerminals;
		totalTerminals   = ((int) (G.size() / divTerminals)) - mustHave.length;
		mustHave[1]      = endIndex;
		
		G.randomTerminals(totalTerminals, mustHave);	//Set totalTerminals Vertices as terminals at random
		
		startTime = System.nanoTime();
		G.sparsify("");								//Sparsify graph using random edge contractions
		endTime = System.nanoTime();
		
		G.dijkstra(0, endVert.getIndex());				//Re-calculate shortest path
		System.out.println("Removed " + (100 - percentTerminals) + "% of vertices with quality " + (endVert.getPathLength() / originalPathLength) + " in " + (endTime - startTime) / 1000000000.0 + "s");
		System.out.println("New shortest path lenth: " + endVert.getPathLength());
		
		System.out.println("\n");
		
		
		/*Compression of medium graph (file2)*/
		System.out.println("Compressing medium graph");
		
		G = General.fromFile(file2);
		
		endIndex = 21;
		endVert  = G.getVertex(endIndex);
		
		G.dijkstra(0, endIndex);
		originalPathLength = endVert.getPathLength();
		System.out.println("Shortest path of length: " + originalPathLength);
		
		divTerminals = 100 / percentTerminals;
		totalTerminals   = ((int) ((G.size() / divTerminals) + 1)) - mustHave.length;
		mustHave[1]      = endIndex;
		
		G.randomTerminals(totalTerminals, mustHave);	//Set totalTerminals Vertices as terminals at random
		
		startTime = System.nanoTime();
		G.sparsify("");								//Sparsify graph using random edge contractions
		endTime = System.nanoTime();
		
		G.dijkstra(0, endVert.getIndex());				//Re-calculate shortest path
		System.out.println("Removed " + (100 - percentTerminals) + "% of vertices with quality " + (endVert.getPathLength() / originalPathLength) + " in " + (endTime - startTime) / 1000000000.0 + "s");
		System.out.println("New shortest path lenth: " + endVert.getPathLength());
		
		
		System.out.println("\n");
		
		
		/*Compression of large graph (file3)*/
		System.out.println("Compressing medium graph");
		
		G = General.fromFile(file3);
		
		endIndex = 65;
		endVert  = G.getVertex(endIndex);
		
		G.dijkstra(0, endIndex);
		originalPathLength = endVert.getPathLength();
		System.out.println("Shortest path of length: " + originalPathLength);
		
		divTerminals = 100 / percentTerminals;
		totalTerminals   = ((int) ((G.size() / divTerminals) + 1)) - mustHave.length;
		mustHave[1]      = endIndex;
		
		G.randomTerminals(totalTerminals, mustHave);	//Set totalTerminals Vertices as terminals at random
		
		startTime = System.nanoTime();
		G.sparsify("");								//Sparsify graph using random edge contractions
		endTime = System.nanoTime();
		
		G.dijkstra(0, endVert.getIndex());				//Re-calculate shortest path
		System.out.println("Removed " + (100 - percentTerminals) + "% of vertices with quality " + (endVert.getPathLength() / originalPathLength) + " in " + (endTime - startTime) / 1000000000.0 + "s");
		System.out.println("New shortest path lenth: " + endVert.getPathLength());
		
		
		/*String file1 = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph1.txt";
		
		Graph G1 = General.fromFile(file1);
		Graph G2 = General.fromFile(file1);
		
		G1.dijkstra(0, 6);
		G2.dijkstra(0, 6);
		
		long   startTime, endTime;
		
		System.out.println("Example Graph 1 (Small Graph)");
		
		startTime = System.nanoTime();
		G1.sparsify("gauss");
		endTime = System.nanoTime();
		
		System.out.println("Gaussian time: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		startTime = System.nanoTime();
		G2.sparsify("");
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		
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
		
		System.out.println("Gaussian time: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		startTime = System.nanoTime();
		G2.sparsify("");
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		
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
		
		System.out.println("Gaussian time: " + ((endTime - startTime) / 1000000000.0) + "s");
		
		startTime = System.nanoTime();
		G2.sparsify("");
		endTime = System.nanoTime();
		
		System.out.println("Vertex sparsification time: " + ((endTime - startTime) / 1000000000.0) + "s");*/
	}
}
