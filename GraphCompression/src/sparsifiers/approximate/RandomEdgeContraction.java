package sparsifiers.approximate;

import graphComponents.Graph;

public class RandomEdgeContraction {
	/** Graph object to carry out edge contractions on */
	private Graph G;
	
	/** List of indexes of terminal Vertices */
	private Integer[] terminalList;
	
	public RandomEdgeContraction(Graph G) {
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
}
