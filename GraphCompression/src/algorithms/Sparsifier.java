package algorithms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;

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
	
	/** The percentage of non-terminals to contract before stopping. Values may be 25, 50, and 75. */
	private int earlyStopping;
	
	/** List of indexes of terminal Vertices */
	private Integer[] terminalList;
	
	/** Whether or not to use the independent set for the REC algorithm */
	private boolean useIndSet = false;
	
	/** Stores shortest path lengths from each terminal to each other terminal */
	private double[][] pathLengths = null;
	
	
	/** The time that sparsification started */
	private long startTime;
	
	/** The time that sparsification ended */
	private long endTime;
	
	
	public Sparsifier(Graph G) {
		this.G      = G;
		this.method = "";
		this.earlyStopping = 0;
	}
	
	
	/**
	 * Sets the contraction method to the given method
	 * @param method, a string representing the contraction method to use
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	
	/**
	 * Returns the method that this sparsifier has been set to
	 * @return a string of the name of the sparsification method being used
	 */
	public String getMethod() {
		if (this.method == "gauss") {
			return "Gaussian Elimination";
			
		} else if (this.method == "sptree") {
			return "Shortest Path Tree";
		
		} else if (this.method == "random") {
			return "Random Selection";
			
		} else {
			if (this.useIndSet) {
				return "Random Edge Contractions with Independent Set";
				
			} else if (this.earlyStopping != 0) {
				return "Random Edge Contractions with Early Stopping After " + this.earlyStopping + "%";
				
			} else {
				return "Random Edge Contractions";
				
			}
		}
	}
	
	
	/**
	 * Use the early stopping version of REC with the given percentage of non-terminals being contracted
	 * @param i, the percentage of non-terminals to contract
	 */
	public void setEarlyStopping(int i) {
		this.earlyStopping = i;
	}
	
	
	/**
	 * Enable or disable the use of independent set non-terminal contractions in the REC algorithm
	 * @param b, true if REC should use the independent set, false otherwise
	 */
	public void setUseIndSet(boolean b) {
		this.useIndSet = b;
	}
	
	
	/**
	 * Sets the terminal list of the sparsifier object to the provided list of terminals
	 * @param terminalList, a list of indexes of terminal Vertices
	 */
	public void setTerminals(Integer[] terminalList) {
		this.terminalList = terminalList;
		
		for (int termIndex: terminalList) {
			G.getVertex(termIndex).setTerminal(true);
		}
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
	public void randomTerminals(int numTerminals) {
		Set<Integer> terminalSet = new HashSet<Integer>();	//Create a new set containing the startIndex or must have Vertex index (Usually start of shortest path)
		
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
	 * Determines the (approximate) maximum independent set of all non-deactivated non-terminals.
	 * @return S, the independent set of non-terminals
	 */
	private ArrayList<Integer> indSet() {
		ArrayList<Integer> S = new ArrayList<Integer>();			//Stores the independent set
		
		SimpleQueuePrio<Integer> nonTerms = this.getNonTermQueue();	//Priority queue of non-terminals
		Integer currentVert;
		
		while ((currentVert = nonTerms.pop()) != null) {	//Get each vertex from min to max degree
			S.add(currentVert);							//Add vertex to set
			this.G.getVertex(currentVert).deactivate();	//Deactivate the vertex in G
			
			for (int i: this.G.getVertex(currentVert).getAdj().keySet()) {
				//System.out.print("Removing: " + i + " from " + currentVert + "\n");
				nonTerms.remove(i);	//Remove each of the neighbours of the vertex from the queue
			}
		}
		
		return S;
	}
	
	
	/**
	 * Creates a priority queue containing all non-terminal vertices
	 * @return a priority queue containing all non terminals
	 */
	public SimpleQueuePrio<Integer> getNonTermQueue() {
		SimpleQueuePrio<Integer> nonTermQueue = new SimpleQueuePrio<Integer>();	//Queue containing all nonterminals, priority is degree using lower priorities
		
		Set<Integer> terminalSet = new HashSet<Integer>(Arrays.asList(terminalList));
		for (int i = 0; i < G.size(); i++) {
			Vertex currentVert = this.G.getVertex(i);
			
			if (!terminalSet.contains(i) && !currentVert.isDeactivated()) {	//Add all active nonterminals to the queue
				nonTermQueue.insert(i, currentVert.getAdj().size());	//Priority is degree of Vertex, which is size of adjacency list
			}
		}
		
		return nonTermQueue;
	}
	
	
	/**
	 * Performs a quality check of the compressed graph. If initial
	 * is set to true then an initial path lengths matrix consisting
	 * of shortest path lengths between terminals is constructed. If
	 * initial is set to false then a quality matrix is constructed
	 * and printed showing the quality of the compression.
	 * 
	 * @param initial, true if the graph has not been compressed, false otherwise
	 */
	private void checkQuality(boolean initial) {
		if (initial) {
			this.pathLengths = new double[terminalList.length][terminalList.length];	//Initialise 2d array of matrix of path lengths
			
			for (int i = 0; i < terminalList.length; i++) {		//Iterate over "start positions" in shortest path
				int currentTermIndex = this.terminalList[i];	//Index of current "start position" (index of a Vertex)
				
				this.G.dijkstra(currentTermIndex);	//Compute shortest path from terminal to each Vertex
				
				for (int j = 0; j < terminalList.length; j++) {
					int pathTermIndex = this.terminalList[j];	//Terminal at the "other end" of a path from the starting terminal
					
					pathLengths[i][j] = this.G.getVertex(pathTermIndex).getPathLength();	//Store shortest paths to each terminal Vertex
				}
			}
		
		} else {
				double[][] qualityMatrix = new double[terminalList.length][terminalList.length];	//Initialise 2d array of matrix of qualities
				
				for (int i = 0; i < terminalList.length; i++) {		//Iterate over "start positions" in shortest path
					int currentTermIndex = this.terminalList[i];	//Index of current "start position" (index of a Vertex)
					
					this.G.dijkstra(currentTermIndex);	//Compute shortest path from terminal to each Vertex
					
					for (int j = 0; j < terminalList.length; j++) {
						int pathTermIndex = this.terminalList[j];	//Terminal at the "other end" of a path from the starting terminal
						
						qualityMatrix[i][j] = (i != j) ? this.G.getVertex(pathTermIndex).getPathLength() / pathLengths[i][j] : 0;	//Add new shortest path length divided by original shortest path length, 0 if diagonal
					}
				}
				
				System.out.println(this.getMethod());
				System.out.println("Time taken: " + ((this.endTime - this.startTime) / 1000000000.0) + "s");
				
				for (int termIndex = 0; termIndex < terminalList.length; termIndex++) {
					String currentLine = "Terminal " + termIndex + " (Vertex " + terminalList[termIndex] + ") ";
					
					for (double quality: qualityMatrix[termIndex]) {
						currentLine += (quality + " ");
					}
					
					System.out.println(currentLine);
				}
				
				System.out.println("\n");
		}

	}
	
	
	/**
	 * Carries out Vertex sparsification based on the method field. Defaults to random edge contractions based on edge weight probabilities.
	 * Setting this.method to "gauss" will cause this function to use Gaussian elimination to eliminate Vertices from the Graph.
	 * Setting qualityCheck to true will cause the algorithm to assess the quality of the sparsifier.
	 * @param qualityCheck
	 */
	public void sparsify(boolean qualityCheck) {
		
		if (qualityCheck) { checkQuality(true); }	//If qualityCheck is true perform initial step locating shortest paths between terminals
		
		SimpleQueuePrio<Integer> nonTermQueue = this.getNonTermQueue();	//Get all non-terminals as a priority queue ordered by degree of vertex
		Integer currentVertIndex;										//Index of the vertex currently being removed from the graph
		
		if (this.method == "gauss") {	//Carry out removal of vertices using Gaussian elimination method
			this.startTime = System.nanoTime();
			
			while ((currentVertIndex = nonTermQueue.pop()) != null) { this.eliminate(currentVertIndex); }
			
			this.endTime = System.nanoTime();
		
		} else if (this.method == "sptree") {	//Cary out elimination of vertices using shortest path tree method
			this.startTime = System.nanoTime();
			
			this.SPTree();
			
			this.endTime = System.nanoTime();
		
		} else if (this.method == "random") {
			this.startTime = System.nanoTime();
			
			while ((currentVertIndex = nonTermQueue.pop()) != null) {
				if (Math.random() < 0.5) {
					this.eliminate(currentVertIndex);	//If random less than 0.5 choose gaussian elimination method
					
				} else {
					this.REC(currentVertIndex);			//Otherwise choose REC method
					
				}
			}
			
			this.endTime = System.nanoTime();
			
		} else {								//Default to REC method
			this.startTime = System.nanoTime();
			
			if (this.earlyStopping != 0) {
				int maxContractions = (int) (this.G.size() / (100 / this.earlyStopping));	//Calculate how many non-terminals to contract before early stopping
				int i = 0;
				
				while (i < maxContractions && (currentVertIndex = nonTermQueue.pop()) != null) { this.REC(currentVertIndex); i++;}
				
				
			} else { 
				if (this.useIndSet) {
					ArrayList<Integer> currentSet;
					
					while (!(currentSet = this.indSet()).isEmpty()) {
						for (int i: currentSet) {
							this.REC(i);
						}
					}
					
				} else {
					while ((currentVertIndex = nonTermQueue.pop()) != null) { this.REC(currentVertIndex); }
				}
				
			}
			
			this.endTime = System.nanoTime();
		}
		
		if (qualityCheck) { checkQuality(false); }	//Perform final step of quality check, calculating and displaying time taken and quality of compression
	}
	
	
	/**
	 * Removes a Vertex v from the Graph G using the Gaussian elimination method.
	 * Creates edges between pairs of neighbours of v and selects minimum edge where duplicates exist.
	 * @param vertIndex, the index of the Vertex to be removed
	 */
	private void eliminate(int toRemoveIndex) {
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
	private void REC(int toRemoveIndex) {
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
				superNode.updateAdj(currentNeighbour.getIndex(), Math.min(currentNeighbour.getFromAdj(superNodeIndex), newWeight));
				
			} else {	//No edge exists so create a new edge to supernode with weight equal to the path weigt from neighbour to superNode through toRemove
				currentNeighbour.addToAdj(superNodeIndex, newWeight);
				superNode.addToAdj(currentNeighbour.getIndex(), newWeight);
				
			}
			currentNeighbour.removeFromAdj(toRemoveIndex);	//Remove edge from currentNeighbour to toRemove
		}
		toRemove.deactivate();	//deactivate toRemove
	}
	
	
	/**
	 * Converts this graph to a shortest-path tree using dijkstra's algorithm and a given set of terminals
	 */
	@SuppressWarnings("unchecked")
	public void SPTree() {
		HashMap<Integer, Double>[] newAdjLists = new HashMap[G.size()];
		
		for (int i = 0; i < G.size(); i++) {
			newAdjLists[i] = new HashMap<Integer, Double>();
		}
		
		for (Integer sourceTerm: this.terminalList) {
			G.dijkstra(sourceTerm);
			
			for (Integer terminal: this.terminalList) {
				if (sourceTerm != terminal) {
					Vertex currentVert = G.getVertex(terminal);
					Vertex parent      = currentVert.getParent();
					
					while (parent != null) {
						int currentIndex = currentVert.getIndex();
						int parentIndex  = parent.getIndex();
						
						double newWeight = (newAdjLists[currentIndex].containsKey(parentIndex)) ?
								Math.min(currentVert.getFromAdj(parentIndex), newAdjLists[currentIndex].get(parentIndex)) :
								currentVert.getFromAdj(parentIndex);
						
						newAdjLists[currentIndex].put(parentIndex, newWeight);
						newAdjLists[parentIndex].put(currentIndex, newWeight);
						
						currentVert = currentVert.getParent();
						parent      = parent.getParent();
					}
				}
			}
		}
		
		for (int i = 0; i < G.size(); i++) {
			HashMap<Integer, Double> newAdj = newAdjLists[i];
			
			if (newAdj.isEmpty()) {
				G.getVertex(i).deactivate();
				
			} else if (newAdj.size() == 2 && !G.getVertex(i).getTerminal()) {
				double newWeight = 0;
				
				for (double edgeWeight: newAdj.values()) {
					newWeight += edgeWeight;
				}
				
				for (Integer key1: newAdj.keySet()) {
					newAdjLists[key1].remove(i);
					
					for (Integer key2: newAdj.keySet()) {
						if (key1 != key2) {
							newAdjLists[key1].put(key2, newWeight);
						}
					}
				}
				
				G.getVertex(i).deactivate();
				
			} else {
				G.getVertex(i).setAdj(newAdj);
				
			}
		}
		
	}
}
