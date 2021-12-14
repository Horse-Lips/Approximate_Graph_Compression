package sparsifiers.exact;

import java.util.Map.Entry;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.SimpleQueuePrio;


public class GaussianElimination {
	/** Graph object to carry out edge contractions on */
	private Graph G;
	
	/** List of indexes of terminal Vertices */
	private Integer[] terminalList;
	
	public GaussianElimination(Graph G) {
		this.G = G;
	}
	
	
	/**
	 * Sets the terminal list of the sparsifier object to the provided list of terminals
	 * @param terminalList, a list of indexes of terminal Vertices
	 */
	public void setTerminals(Integer[] terminalList) {
		this.terminalList = terminalList;
		
		for (int termIndex: terminalList) {
			this.G.getVertex(termIndex).setTerminal(true);
		}
	}
	
	
	/**
	 * Get the terminal set
	 * @return the terminal set used by this sparsifier
	 */
	public Integer[] getTerminals() {
		return this.terminalList;
	}
	
	
	public void sparsify() {
		SimpleQueuePrio<Integer> nonTermQueue = sparsifiers.SparsifierUtils.getNonTermQueue(this.terminalList, this.G);
		
		long startTime = System.nanoTime();
		long endTime   = System.nanoTime();
		
		Integer currentVertIndex;
		
		startTime = System.nanoTime();
		
		while ((currentVertIndex = nonTermQueue.pop()) != null) {
			this.Gauss(currentVertIndex);
		}
		
		endTime = System.nanoTime();
		
		
		System.out.println("Gaussian Elimination\nTime Taken: " + ((endTime - startTime) / 1000000000.0) + "s");
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
}
