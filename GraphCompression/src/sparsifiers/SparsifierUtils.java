package sparsifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import graphComponents.Graph;
import graphComponents.Vertex;
import graphUtils.SimpleQueuePrio;

public class SparsifierUtils {
	/**
	 * Creates a set of randomly selected terminals of length numTerminals
	 * @param numTerminals, number of Vertices to set as terminals
	 * @return A list containing indices of graph terminals
	 */
	public static Integer[] randomTerminals(Graph G, int numTerminals) {
		Set<Integer> terminalSet = new HashSet<Integer>();	//Create a new set containing the startIndex or must have Vertex index (Usually start of shortest path)
		
		for (int terminalCount = 0; terminalCount < numTerminals;) {
			int rand = (int) (Math.random() * G.size());
			
			if (!terminalSet.contains(rand)) {	//Add random terminal to set if it is not already in the set
				terminalSet.add(rand);
				terminalCount++;
			}
		}
		
		Integer[] newTerminalList = new Integer[numTerminals];
		return terminalSet.toArray(newTerminalList);
	}
	
	
	/**
	 * Creates a priority queue containing all non-terminal vertices
	 * @return a priority queue containing all non terminals
	 */
	public static SimpleQueuePrio<Integer> getNonTermQueue(Integer[] terminalList, Graph G) {
		SimpleQueuePrio<Integer> nonTermQueue = new SimpleQueuePrio<Integer>();			//Queue containing all nonterminals, priority is degree where lower degree is higher priority
		Set<Integer> terminalSet = new HashSet<Integer>(Arrays.asList(terminalList));
		
		for (int i = 0; i < G.size(); i++) {
			Vertex currentVert = G.getVertex(i);
			
			if (!terminalSet.contains(i) && !currentVert.isDeactivated()) {	//Add all active nonterminals to the queue
				nonTermQueue.insert(i, currentVert.getAdj().size());	//Priority is degree of Vertex, which is size of adjacency list
			}
		}
		
		return nonTermQueue;
	}
	
	
	/**
	 * Determines the (approximate) maximum independent set of all non-deactivated non-terminals.
	 * @return S, the independent set of non-terminals
	 */
	public static ArrayList<Integer> indSet(Integer[] terminalList, Graph G) {
		ArrayList<Integer> S = new ArrayList<Integer>();
		
		SimpleQueuePrio<Integer> nonTerms = getNonTermQueue(terminalList, G);
		Integer currentVert;
		
		while ((currentVert = nonTerms.pop()) != null) {
			S.add(currentVert);
			
			for (int i: G.getVertex(currentVert).getAdj().keySet()) {
				nonTerms.remove(i);
			}
		}
		
		return S;
	}
}
