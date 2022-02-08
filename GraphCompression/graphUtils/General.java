package graphUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

import graphComponents.Graph;
import graphComponents.Vertex;

import graphUtils.SimpleTuple;


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
					currentVertex.addToAdj(neighbIndex, edgeWeight);
				}
			}
			
			vertIndex++;
		}
		
		lineScanner.close();
		
		return newGraph;
	}
	
	
	public static Graph fromSNAPFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		Scanner lineScanner = new Scanner(br);
				
		lineScanner.nextLine();	//Skip header
		lineScanner.nextLine();
		
		int graphSize = Integer.parseInt(lineScanner.nextLine().split("\t")[0].split(":")[1]);	//Get size of new graphs
		
		//System.out.println("Got graph size");
		
		Graph newGraph = new Graph(graphSize);	//Create list of Graphs
		
		//System.out.println("Graphs created");
		
		lineScanner.nextLine();
		
		HashMap<Integer, Integer> converter = new HashMap<Integer, Integer>();
		
		int graphIndex = 0;
		int currentIndex;
		
		while (lineScanner.hasNextLine()) {
			currentIndex = Integer.parseInt(lineScanner.nextLine().split("\t")[0]);
			
			if (!converter.containsKey(currentIndex)) {
				converter.put(currentIndex, graphIndex);
				
				graphIndex++;
			}
			
		}
		
		//System.out.println("Conversion complete");
		
		lineScanner.close();
		
		br = new BufferedReader(new FileReader(filename));
		lineScanner = new Scanner(br);
		
		lineScanner.nextLine();
		lineScanner.nextLine();
		lineScanner.nextLine();
		lineScanner.nextLine();
		
		while (lineScanner.hasNextLine()) {
			String[] splitLine = lineScanner.nextLine().split("\t");
			
			int from = converter.get(Integer.parseInt(splitLine[0]));
			int to   = converter.get(Integer.parseInt(splitLine[1]));
			
			int weight = 1;
			
			if (from != to) {
                newGraph.getVertex(from).addToAdj(to, weight);
			}
			
		}
		
        newGraph.removeLoners();
		
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
