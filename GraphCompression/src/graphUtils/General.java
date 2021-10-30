package graphUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import graphComponents.Graph;
import graphComponents.Vertex;


public class General {
	/*Loads a graph from an adjacency matrix text file*/
	public static Graph fromFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		Scanner lineScanner = new Scanner(br);
		
		int vertCount = Integer.valueOf(lineScanner.nextLine());	//Get size of graph
		Graph newGraph = new Graph(vertCount);						//Create new graph of given size
		
		int vertIndex = 0;	//Index of the current vertex
		
		while (lineScanner.hasNextLine()) {
			String[] chars = lineScanner.nextLine().split(" ");	//Split each line of adj matrix into array
			
			for (int neighbIndex = 0; neighbIndex < chars.length; neighbIndex++) {	//Iterate over neighbouring vertices
				Vertex currentVertex = newGraph.getVertex(vertIndex);
				double edgeWeight = Double.valueOf(chars[neighbIndex]);
				
				if (edgeWeight > 0) {
					currentVertex.addToAdj(newGraph.getVertex(neighbIndex), edgeWeight);
				}
			}
			
			vertIndex++;
		}
		
		lineScanner.close();
		
		return newGraph;
	}
	
	
	/*Prints a path in Graph G*/
	public static void printPath(Vertex end) {
		String retString = "";
		int vertCount = 0;
		Vertex currentVert = end;
		
		while (currentVert != null) {
			retString = "Vertex with index " + currentVert.getIndex() + "\n" + retString;
			vertCount++;
			currentVert = currentVert.getParent();
		}
		
		retString = "Path of length " + end.getPathLength() + " found containing " + vertCount + " vertices\nPath:\n" + retString;
		
		System.out.println(retString);
	}
}
