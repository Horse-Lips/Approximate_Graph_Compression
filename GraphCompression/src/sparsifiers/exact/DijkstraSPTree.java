package sparsifiers.exact;

import java.util.HashMap;

import graphComponents.Graph;
import graphComponents.Vertex;

public class DijkstraSPTree {
	/** Graph object to carry out edge contractions on */
	private Graph G;
	
	/** List of indexes of terminal Vertices */
	private Integer[] terminalList;
	
	public DijkstraSPTree(Graph G) {
		this.G = G;
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
	
	
	public void sparsify() {
		long startTime = System.nanoTime();
		long endTime   = System.nanoTime();
		
		startTime = System.nanoTime();
		
		this.SPTree();
		
		endTime = System.nanoTime();
		
		System.out.println("Dijkstra Shortest Path Tree\nTime Taken: " + ((endTime - startTime) / 1000000000.0) + "s");
	}
	
	/**
	 * Converts this graph to a shortest-path tree using dijkstra's algorithm and a given set of terminals
	 */
	@SuppressWarnings("unchecked")
	private void SPTree() {
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
