package algorithms;


import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.function.Consumer;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.SimpleQueuePrio;


/**
 * Sparsifier class. Maintains methods for sparsifying a given Graph G and utilities for checking the quality of sparsification.
 * 
 * @author Jake Haakanson. GUID: 2407682H
 *
 */
public class Sparsifier {
	/** Graph object to carry out edge contractions on */
	private Graph  G;
	
	/** Contraction method, initially blank, can be set to "gauss" for Gaussian elimination */
	private String method;
	
	/** List of indexes of terminal Vertices */
	private Integer[] terminalList;
	
	public Sparsifier(Graph G) {
		this.G      = G;
		this.method = "";
	}
	
	
	/**
	 * Sets the contraction method to the given method
	 * @param method, a string representing the contraction method to use
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	
	/**
	 * Sets the terminal list of the sparsifier object to the provided list of terminals
	 * @param terminalList, a list of indexes of terminal Vertices
	 */
	public void setTerminals(Integer[] terminalList) {
		this.terminalList = terminalList;
	}
	
	
	/**
	 * Get the terminal set
	 * @return the terminal set used by this sparsifier
	 */
	public Integer[] getTerminals() {
		return this.terminalList;
	}
	
	
	/**
	 * Creates a set of randomly selected terminals of length (numTerminals + 1)
	 * @param startIndex, Vertex that must be a terminal (Usually start Vertex in shortest path
	 * @param numTerminals, number of Vertices to set as terminals
	 */
	public void randomTerminals(int startIndex, int numTerminals) {
		Set<Integer> terminalSet = new HashSet<Integer>();	//Create a new set containing the startIndex or must have Vertex index (Usually start of shortest path)
		terminalSet.add(startIndex);
		
		for (int terminalCount = 0; terminalCount < numTerminals;) {
			int rand = (int) (Math.random() * this.G.size());
			
			if (!terminalSet.contains(rand)) {	//Add random terminal to set if it is not already in the set
				terminalSet.add(rand);
				terminalCount++;
			}
		}
		
		Integer[] newTerminalList = new Integer[numTerminals];
		this.terminalList = terminalSet.toArray(newTerminalList);	//Update terminal set
	}
	
	
	/**
	 * Carries out Vertex sparsification based on the method field. Defaults to random edge contractions based on edge weight probabilities.
	 * Setting this.method to "gauss" will cause this function to use Gaussian elimination to eliminate Vertices from the Graph.
	 * Setting qualityCheck to true will cause the algorithm to assess the quality of the sparsifier.
	 * @param qualityCheck
	 */
	public void sparsify(boolean qualityCheck) {
		double[][] pathLengths = null;	//Stores shortest path lengths from each terminal to each terminal
		
		if (qualityCheck) {
			pathLengths = new double[terminalList.length][terminalList.length];	//Initialise 2d array of matrix of path lengths
			
			for (int i = 0; i < terminalList.length; i++) {		//Iterate over "start positions" in shortest path
				int currentTermIndex = this.terminalList[i];	//Index of current "start position" (index of a Vertex)
				
				this.G.dijkstra(currentTermIndex);	//Compute shortest path from terminal to each Vertex
				
				for (int j = 0; j < terminalList.length; j++) {
					int pathTermIndex = this.terminalList[j];	//Terminal at the "other end" of a path from the starting terminal
					
					pathLengths[i][j] = this.G.getVertex(pathTermIndex).getPathLength();	//Store shortest paths to each terminal Vertex
				}
			}
		}
		
		
		SimpleQueuePrio<Integer> nonTermQueue = new SimpleQueuePrio<Integer>();	//Queue containing all nonterminals, priority is degree using lower priorities
		
		Set<Integer> terminalSet = new HashSet<Integer>(Arrays.asList(terminalList));
		for (int i = 0; i < G.size(); i++) {
			if (!terminalSet.contains(i)) {	//Add all nonterminals to the queue
				nonTermQueue.insert(i, this.G.getVertex(i).getAdj().size());	//Priority is degree of Vertex, which is size of adjacency list
			}
		}
		
		Consumer<Integer> contractionMethod;
		
		if (this.method == "gauss") {
			contractionMethod = this::Gauss;	//Use Gauss if specified
			
		} else {
			contractionMethod = this::contract;	//Default to random edge contraction
			
		}
		
		
		Integer currentVertIndex;
		
		long startTime = System.nanoTime();	//Start time
		
		while ((currentVertIndex = nonTermQueue.pop()) != null) {	//While there are non-terminals to contract, contract them
			contractionMethod.accept(currentVertIndex);
		}
		
		long endTime = System.nanoTime();	//End time
		
		
		if (qualityCheck) {
			
			double[][] qualityMatrix = new double[terminalList.length][terminalList.length];	//Initialise 2d array of matrix of qualities
			
			for (int i = 0; i < terminalList.length; i++) {		//Iterate over "start positions" in shortest path
				int currentTermIndex = this.terminalList[i];	//Index of current "start position" (index of a Vertex)
				
				this.G.dijkstra(currentTermIndex);	//Compute shortest path from terminal to each Vertex
				
				for (int j = 0; j < terminalList.length; j++) {
					int pathTermIndex = this.terminalList[j];	//Terminal at the "other end" of a path from the starting terminal
					
					qualityMatrix[i][j] = (i != j) ? this.G.getVertex(pathTermIndex).getPathLength() / pathLengths[i][j] : 0;	//Add new shortest path length divided by original shortest path length, 0 if diagonal
				}
			}
			
			if (this.method == "gauss") {
				System.out.println("-- Gaussian Elimination --");
				
			} else {
				System.out.println("-- Random Edge Contractions --");
			}
			
			System.out.println("Time taken: " + ((endTime - startTime) / 1000000000.0) + "s");
			
			for (int termIndex = 0; termIndex < terminalList.length; termIndex++) {
				String currentLine = "Terminal " + termIndex + " (Vertex " + terminalList[termIndex] + ") ";
				
				for (double quality: qualityMatrix[termIndex]) {
					currentLine += (quality + " ");
				}
				
				System.out.println(currentLine);
			}
		}
	}
	
	
	/**
	 * Removes a Vertex v from the Graph G using the Gaussian elimination method.
	 * Creates edges between pairs of neighbours of v and selects minimum edge where duplicates exist.
	 * @param vertIndex, the index of the Vertex to be removed
	 */
	private void Gauss(int toRemoveIndex) {
		Vertex toRemove;	//Vertex to be removed
		
		if (toRemoveIndex < 0 || toRemoveIndex >= G.size() || (toRemove = G.getVertex(toRemoveIndex)).isDeactivated()) {
			return;	//Exit if Vertex not in Graph
		}
		
		for (Entry<Integer, Double> neighbourOneAdj: toRemove.getAdj().entrySet()) {		//Iterate over neighbours of toRemove
			Vertex neighbourOne = G.getVertex(neighbourOneAdj.getKey());	//Get Vertex representation of neighbourOne
			
			for (Entry<Integer, Double> neighbourTwoAdj: toRemove.getAdj().entrySet()) {	//Iterate again to make comparisons
				if (neighbourOneAdj != neighbourTwoAdj) {					//Ensure we are not comparing the same edge
					double newWeight = neighbourOneAdj.getValue() + neighbourTwoAdj.getValue();	//Calculate weight of path between neighbourOne and neighbourTwo through toRemove
					
					if (neighbourOne.adjContains(neighbourTwoAdj.getKey())) {	//If an edge already exists between neighbourOne and neighbourTwo, set its weight to be the minimum of the old weight and the new weight
						neighbourOne.updateAdj(neighbourTwoAdj.getKey(), Math.min(neighbourOne.getFromAdj(neighbourTwoAdj.getKey()), newWeight));
						
					} else {	//No previously existing edge between neighbourOne and neighbourTwo, so create one with the new weight (path through toRemove)
						neighbourOne.addToAdj(neighbourTwoAdj.getKey(), newWeight);
						
					}
				}
			}
			neighbourOne.removeFromAdj(toRemoveIndex);	//Remove edge from neighbourOne to toRemove
		}
		toRemove.deactivate();	//Deactivate toRemove
	}
	
	
	/**
	 * Contracts an edge of the Graph using random edge contractions based on edge probability.
	 * The Vertex to be removed will be "merged" with some Vertex based on edge probability
	 * @param toRemoveIndex
	 */
	private void contract(int toRemoveIndex) {
		Vertex toRemove;	//Vertex to be removed
		
		if (toRemoveIndex < 0 || toRemoveIndex >= G.size() || (toRemove = G.getVertex(toRemoveIndex)).isDeactivated()) {
			return;	//Exit if Vertex not in Graph
		}
		
		int superNodeIndex = toRemove.sample();				//Partition edges of the Vertex for efficient sampling
		Vertex superNode = G.getVertex(superNodeIndex);		//Get superNode (This will be the Vertex that toRemove is "merged" into)
		
		double removedEdgeWeight = superNode.getFromAdj(toRemoveIndex);
		
		superNode.removeFromAdj(toRemoveIndex);				//Remove edge between superNode and toRemove
		toRemove.removeFromAdj(superNodeIndex);
		
		for (Entry<Integer, Double> toRemoveNeighbour: toRemove.getAdj().entrySet()) {	//Iterate toRemove's neighbours
			Vertex currentNeighbour = G.getVertex(toRemoveNeighbour.getKey());						//Get Vertex of neighbour
			double newWeight = removedEdgeWeight + toRemoveNeighbour.getValue();	//Calculate weight of path from neighbour to superNode through toRemove
			
			if (currentNeighbour.adjContains(superNodeIndex)) {	//If an edge from neighbour to supernode already exists, set the weight to the minimum of the old and new weights
				currentNeighbour.updateAdj(superNodeIndex, Math.min(currentNeighbour.getFromAdj(superNodeIndex), newWeight));
				
			} else {	//No edge exists so create a new edge to supernode with weight equal to the path weigt from neighbour to superNode through toRemove
				currentNeighbour.addToAdj(superNodeIndex, newWeight);
				superNode.addToAdj(currentNeighbour.getIndex(), newWeight);
				
			}
			currentNeighbour.removeFromAdj(toRemoveIndex);	//Remove edge from currentNeighbour to toRemove
		}
		toRemove.deactivate();	//deactivate toRemove
	}
}
