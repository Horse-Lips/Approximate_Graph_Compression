package sparsifiers.approximate;

import java.util.ArrayList;
import java.util.Map.Entry;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.SimpleQueuePrio;

public class RandomEdgeContraction {
	/** Graph object to carry out edge contractions on */
	private Graph G;
	
	/** List of indexes of terminal Vertices */
	private Integer[] terminalList;
	
	/** Whether or not to contract nonterminals in an independent set */
	private boolean indSet;
	
	public RandomEdgeContraction(Graph G) {
		this.G = G;
		this.indSet = false;
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
	 * Update the value of indSet field variable
	 * @param b, a boolean: true if sparsifier should use independent set during sparsification, false if not
	 */
	public void useIndSet(boolean b) {
		this.indSet = b;
	}
	
	
	public void sparsify() {
		if (this.indSet) {
			this.sparsIndSet();
			
		} else {
			this.sparsStandard();
			
		}
	}
	
	
	private void sparsStandard() {
		SimpleQueuePrio<Integer> nonTermQueue = sparsifiers.SparsifierUtils.getNonTermQueue(this.terminalList, this.G);
		
		long startTime = System.nanoTime();
		long endTime   = System.nanoTime();
		Integer currentVertIndex;
		
		startTime = System.nanoTime();
		
		while ((currentVertIndex = nonTermQueue.pop()) != null) {
			this.REC(currentVertIndex);
		}
		
		endTime = System.nanoTime();
		
		System.out.println("Random Edge Contractions\nTime Taken: " + ((endTime - startTime) / 1000000000.0) + "s");
	}
	
	
	private void sparsIndSet() {
		long startTime = System.nanoTime();
		long endTime   = System.nanoTime();
		
		ArrayList<Integer> independentSet = sparsifiers.SparsifierUtils.indSet(this.terminalList, this.G);
		
		startTime = System.nanoTime();
		
		while (!(independentSet.isEmpty())) {
			for (int currentVert: independentSet) {
				this.REC(currentVert);
			}
			
			independentSet = sparsifiers.SparsifierUtils.indSet(this.terminalList, this.G);
		}
		
		endTime = System.nanoTime();
		
		System.out.println("Random Edge Contractions with Independent Set\nTime Taken: " + ((endTime - startTime) / 1000000000.0) + "s");
	}
	
	
	/**
	 * Contracts an edge of the Graph using random edge contractions based on edge probability.
	 * The Vertex to be removed will be "merged" with some Vertex based on edge probability
	 * @param toRemoveIndex
	 */
	private void REC(int toRemoveIndex) {
		Vertex toRemove;	//Vertex to be removed
		
		if (toRemoveIndex < 0 || toRemoveIndex >= this.G.size() || (toRemove = this.G.getVertex(toRemoveIndex)).isDeactivated()) {
			return;	//Exit if Vertex not in Graph
		}

		int superNodeIndex = toRemove.sample();					//Sample an edge to contract
		Vertex superNode = this.G.getVertex(superNodeIndex);	//Get superNode (This will be the Vertex that toRemove is "merged" into)
		
		double removedEdgeWeight = superNode.getFromAdj(toRemoveIndex);	//Weight of removed edge (below)
		
		superNode.removeFromAdj(toRemoveIndex);		//Remove edge between superNode and toRemove
		toRemove.removeFromAdj(superNodeIndex);
		
		for (Entry<Integer, Double> toRemoveNeighbour: toRemove.getAdj().entrySet()) {	//Iterate toRemove's neighbours
			Vertex currentNeighbour = this.G.getVertex(toRemoveNeighbour.getKey());		//Get Vertex of neighbour
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
	
}
