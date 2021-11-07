package algorithms;


import java.util.Map.Entry;
import java.util.function.Consumer;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.SimpleQueuePrio;


public class Sparsifier {
	/** Graph object to carry out edge contractions on */
	private Graph  G;
	
	/** Contraction method, initially blank, can be set to "gauss" for Gaussian elimination */
	private String method;
	
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
	
	
	/*Carries out Vertex sparsification using minimum degree heuristic, contraction method defaults to Vertex sparsification method*/
	public void sparsify() {
		SimpleQueuePrio<Vertex> nonTermQueue = new SimpleQueuePrio<Vertex>();	//Queue containing all nonterminals, priority is degree using lower priorities
		
		for (int i = 0; i < G.size(); i++) {
			Vertex v = G.getVertex(i);
			
			if (!v.getTerminal()) {
				nonTermQueue.insert(v, v.getAdj().size());	//Degree calculated using size of adjacency list since undirected (or symmetric directed)
			}
		}
		
		Consumer<Integer> contractionMethod;
		
		if (this.method == "gauss") {
			contractionMethod = this::Gauss;	//Use Gauss if specified
			
		} else {
			contractionMethod = this::contract;	//Default to random edge contraction
			
		}
		
		Vertex currentVert;
		
		while ((currentVert = nonTermQueue.pop()) != null) {	//While there are non-terminals to contract, contract them
			contractionMethod.accept(currentVert.getIndex());
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
