package graphComponents;


import java.util.Map.Entry;

import graphUtils.SimpleQueuePrio;


/*Graph class, maintains a list of Vertices (See Vertex class)*/
public class Graph {
	private Vertex[] vertList;
	private int      vertCount;
	
	public Graph(int vertCount) {
		this.vertCount = vertCount;				//Size of Graph as count of Vertices
		this.vertList  = new Vertex[vertCount];	//List of all Vertices in the Graph
		
		for (int i = 0; i < vertCount; i++) {
			vertList[i] = new Vertex(i);
		}
	}
	
	
	/**
	 * Returns vertex at given index
	 * @param i, the index of the Vertex in the Graph's vertList
	 * @return the Vertex at index i
	 */
	public Vertex getVertex(int i) {
		for (Vertex v: this.vertList) {
			if (i == v.getIndex()) {
				return v;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Get the size of the Graph
	 * @return size of the graph represented by the length of the Graph's vertList
	 */
	public int size() {
		return this.vertCount;
	}

	
	
	/**
	 * Carries out Dijkstra's shortest path algorithm using a priority queue
	 * @param startIndex, the index of the Vertex to start the search from
	 */
	public void dijkstra(int startIndex) {
		for (Vertex v: this.vertList) {	//Set all vertices as...
			v.setVisited(false);		//Unvisited
			v.setQueueStatus(false);	//Un-queued
			v.setPathLength(0);			//No path leading to this Vertex
		}
		
		Vertex startVert = this.getVertex(startIndex);
		
		if (startVert == null) { return; }	//Check that start is in the Graph
		
		
		SimpleQueuePrio<Vertex> prioQ = new SimpleQueuePrio<Vertex>();	//Priority queue
		
		prioQ.insert(startVert, 0);	//Add startVert to queue with 0 (highest) priority
		startVert.setQueueStatus(true);
		
		Vertex currentVertex;	//Vertex currently being processed
		
		while ((currentVertex = prioQ.pop()) != null) {
			currentVertex.setVisited(true);	//Set node as visited
			
			//HashMap<Integer, Double> currentNeighbours = currentVertex.getAdj();	//Get current Node's neighbours
			
			for (Entry<Integer, Double> neighbour: currentVertex.getAdj().entrySet()) {		//Iterate over current Node's neighbours
				Vertex neighbourVertex = this.getVertex(neighbour.getKey());				//Get vertex representation of neighbour
				double pathWeight = currentVertex.getPathLength() + neighbour.getValue();	//Calculate total path weight through currentVertex to neighbourVertex
				
				if (neighbourVertex.getVisited()) { continue; }	//Don't re-process visited Vertices
				
				if (!neighbourVertex.getQueueStatus()) {		//Neighbour not in queue
					neighbourVertex.setParent(currentVertex);	//Set currentNode as neighbour's parent
					prioQ.insert(neighbourVertex, pathWeight);	//Add neighbour to queue
					
					neighbourVertex.setQueueStatus(true);		//Indicate that the Vertex is now in the queue
					neighbourVertex.setPathLength(pathWeight);	//Update total length of path used to reach this Vertex
					
				} else {	//Neighbour already in queue
					int success = prioQ.update(neighbourVertex, pathWeight);	//Check if priority needs updating
					
					if (success == 1) {	//If an update was made, update the parent as this was a shorter path
						neighbourVertex.setParent(currentVertex);
						neighbourVertex.setPathLength(pathWeight);
					}
				}
			}
		}
	}
	
	
	/*Set n random vertices as terminals, mustHave specifies vertices that MUST be terminals*/
	public void randomTerminals(int n, int[] mustHave) {
		for (int i: mustHave) {
			this.getVertex(i).setTerminal(true);	//Set all mustHaves as terminals
			n--;
		}
		
		while (n > 0) {
			int randIndex = (int) (Math.random() * this.vertCount);	//Select random terminal index
			Vertex currentVert = this.getVertex(randIndex);
			
			if (!currentVert.getTerminal()) {	//If Vertex is not already a terminal, make it a terminal
				currentVert.setTerminal(true);
				n--;
			}
		}
	}
	
	
	public String toString() {
		String retString = "";
		
		for (Vertex v: this.vertList) {
			if (v.isDeactivated()) { continue; }
			
			retString += "Index: " + v.getIndex() + " AdjList: ";
			
			for (Entry<Integer, Double> adjNode: v.getAdj().entrySet()) {
				retString += "Edge to Vertex " + adjNode.getKey() + " with weight " + adjNode.getValue() + ". ";
			}
			
			retString += "\n";
		}
		
		return retString;
	}
}
