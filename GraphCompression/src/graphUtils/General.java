package graphUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import graphComponents.Graph;
import graphComponents.Vertex;


public class General {
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
				Float edgeWeight = Float.valueOf(chars[neighbIndex]);
				
				if (edgeWeight > 0.0) {
					currentVertex.addToAdj(newGraph.getVertex(neighbIndex), edgeWeight);
				}
			}
			
			vertIndex++;
		}
		
		lineScanner.close();
		
		return newGraph;
	}
}
