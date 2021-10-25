package graphComponents;

import java.util.ArrayList;
import java.util.function.Consumer;

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
	
	
	/*Returns the Vertex with the given ID*/
	public Vertex getVertex(int index) {
		for (Vertex v: this.vertList) {
			if (index == v.getIndex()) {
				return v;
			}
		}
		
		return null;
	}
	
	
	/*Returns the size of the Graph represented by vertCount*/
	public int size() {
		return this.vertCount;
	}
	
	
	/*Removes the Vertex at index i and resizes the graph*/
	public void removeVertex(int vertIndex) {
		Vertex toRemove = this.getVertex(vertIndex);
		
		if (toRemove == null) { return; }
		
		Vertex[] newVertList = new Vertex[this.vertCount - 1];
		int currentIndex = 0;
		
		for (Vertex v: this.vertList) {
			if (v != toRemove) {
				newVertList[currentIndex] = v;
				v.setIndex(currentIndex);
				
				currentIndex++;
			}
		}
		
		this.vertList = newVertList;
		this.vertCount = this.size() - 1;
	}
	
	
	/*Removes a Vertex using the Gaussian Elimination method*/
	public void Gauss(int vertIndex) {
		Vertex toRemove = this.getVertex(vertIndex);	//Retrieve Vertex to remove using ID
		
		if (toRemove == null) { return; }	//Make sure Vertex to be removed is actually in the graph
		
		ArrayList<AdjNode> neighbours = toRemove.getAdj();	//Get neighbours of Vertex to be removed
		
		for (AdjNode n1Adj: neighbours) {
			Vertex n1Vertex = n1Adj.getVert();	//Get Vertex representation of neighbourOne
			AdjNode n1ToRemove = null;	//Edge containing toRemove to be deleted
			
			for (AdjNode n2Adj: neighbours) {
				Vertex n2Vertex = n2Adj.getVert();	//Get Vertex representation of neighbourTwo
				AdjNode n2ToRemove = null;	//Edge containing toRemove to be deleted
				
				if (n1Vertex != n2Vertex && n1Vertex != toRemove && n2Vertex != toRemove) {
					double newWeight = n1Adj.getWeight() + n2Adj.getWeight();	//Calculate the new weight of the path through the vertex to delete
					boolean noEdge  = true;		//Used to check if there was an existing edge between neighbourOne and neighbourTwo
					
					for (AdjNode n1Neighbour: n1Vertex.getAdj()) {	//Iterate over neighbourOne's neighbours
						if (n1Neighbour.getVert() == n2Vertex) {
							noEdge = false;
							
							if (n1Neighbour.getWeight() > newWeight) {
								n1Neighbour.setWeight(newWeight);	//Update weight of the edge between neighbourOne and neighbourTwo if it existed previously
							}
							
						} else if (n1Neighbour.getVert() == toRemove) {
							n1ToRemove = n1Neighbour;
							
						}
					}
					
					for (AdjNode n2Neighbour: n2Vertex.getAdj()) {	//Here we iterate over neighbourTwo's neighbours as we'd need to anyway to determine the edge conatining toRemove to delete
						if (n2Neighbour.getVert() == n1Vertex) {
							noEdge = false;
							
							if (n2Neighbour.getWeight() > newWeight) {
								n2Neighbour.setWeight(newWeight);
							}
							
						} else if (n2Neighbour.getVert() == toRemove) {
							n2ToRemove = n2Neighbour;
							
						}
					}
					
					if (noEdge) {	//If no edge existed between n1 and n2 previously we must create one
						n1Vertex.addToAdj(n2Vertex, newWeight);
						n2Vertex.addToAdj(n1Vertex, newWeight);
						
					}
				}
				
				n2Vertex.removeFromAdj(n2ToRemove);
			}
			
			n1Vertex.removeFromAdj(n1ToRemove);
		}
		
		this.removeVertex(vertIndex);	//Finally, remove the vertex to be removed from the graph
	}
	
	
	/*Carries out Vertex sparsification using minimum degree heuristic, contraction method defaults to Vertex sparsification method*/
	public void sparsify(String method) {
		SimpleQueuePrio<Vertex> nonTermQueue = new SimpleQueuePrio<Vertex>();	//Queue containing all nonterminals, priority is degree using lower priorities
		
		for (Vertex v: this.vertList) {
			if (!v.getTerminal()) {
				nonTermQueue.insert(v, v.getAdj().size());	//Degree calculated using size of adjacency list since undirected (or symmetric directed)
			}
		}
		
		Consumer<Integer> contractionMethod;
		
		if (method == "gauss") {
			contractionMethod = this::Gauss;	//Use Gauss if specified
			
		} else {
			contractionMethod = this::contract;	//Default to random edge contraction
			
		}
		
		Vertex currentVert;
		
		while ((currentVert = nonTermQueue.pop()) != null) {	//While there are non-terminals to contract, contract them
			contractionMethod.accept(currentVert.getIndex());
		}
	}
	
	
	/*Contracts the edge between the nodes with IDs "toRemoveID" and "superNodeID"*/
	public int contract(int toRemoveIndex) {
		Vertex toRemove = this.getVertex(toRemoveIndex);
		
		if (toRemove == null) { //Check that vertex to be removed is actually in the graph
			return -1; 
			
		} else if (!toRemove.getPartitioned()) {	//Also check if Vertex needs to be partitioned after adding/removing an edge
			toRemove.partition();
			
		}
		
		/*Using alias method*/
		AdjNode edgeToContract = toRemove.sample();	//Get edge to contract
		double removedEdgeWeight = edgeToContract.getWeight();	//Get weight of contracted edge for addition to existing edges
		toRemove.removeFromAdj(edgeToContract);	//Remove edge
		
		Vertex superNode = edgeToContract.getVert();	//Get new supernode (two merged vertices)
		
		
		/*The following removes the edge between superNode and toRemove*/
		AdjNode removeFromSuper = null;
		
		for (AdjNode superNeighbour: superNode.getAdj()) {
			if (superNeighbour.getVert().getIndex() == toRemove.getIndex()) {
				removeFromSuper = superNeighbour;
			}
		}
		
		superNode.removeFromAdj(removeFromSuper);
		
		
		ArrayList<AdjNode> toRemoveNeighbours = toRemove.getAdj();
		
		for (AdjNode toRemoveNeighbourAdj: toRemoveNeighbours) {	//Iterate over toRemove's neighbours
			Vertex toRemoveNeighbour = toRemoveNeighbourAdj.getVert();	//Get vertex representation of toRemove neighbour
			
			double newEdgeWeight = toRemoveNeighbourAdj.getWeight() + removedEdgeWeight;	//Calculate new edge weights to from neighbour to superNode through toRemove
			
			AdjNode edgeToToRemove = null;	//Edge from neighbour to toRemove
			AdjNode edgeToSuperNode = null;	//Edge from neighbour to superNode if it exists
			
			for (AdjNode neighbour: toRemoveNeighbour.getAdj()) {	//For loop retrieves the above two edges
				Vertex neighbourVert = neighbour.getVert();
				
				if (neighbourVert.getIndex() == superNode.getIndex()) {
					edgeToSuperNode = neighbour;
					
				} else if (neighbourVert.getIndex() == toRemove.getIndex()) {
					edgeToToRemove = neighbour;
					
				}
			}
			
			if (edgeToSuperNode != null) {	//If an edge to superNode already exists, set its weight to the minimum of existing edge or new edge weight
				edgeToSuperNode.setWeight(Math.min(edgeToSuperNode.getWeight(), newEdgeWeight));
				toRemoveNeighbour.removeFromAdj(edgeToToRemove);	//Also remove edge to toRemove
				
			} else {	//Otherwise update existing edge to point to superNode with the new edge weight
				edgeToToRemove.setVert(superNode);
				edgeToToRemove.setWeight(newEdgeWeight);
				
			}
		}
		
		this.removeVertex(toRemoveIndex); //Then we remove toRemove from the Graph
		
		return 1;
	}
	
	
	/*Implementation of Dijkstra's algorithm using a priority queue*/
	public void dijkstra(int startIndex, int endIndex) {
		for (Vertex v: this.vertList) {	//Set all vertices as unvisited, non-terminal, not in queue, with path length of 0
			v.setVisited(false);
			v.setTerminal(false);
			v.setQueueStatus(false);
			v.setPathLength(0);
		}
		
		Vertex startVert = this.getVertex(startIndex);
		Vertex endVert   = this.getVertex(endIndex);
		
		if (startVert == null || endVert == null) { return; }	//Return as neither start nor end are in the Graph
		
		
		SimpleQueuePrio<Vertex> prioQ = new SimpleQueuePrio<Vertex>();	//Priority queue
		
		prioQ.insert(startVert, 0);	//Add startVert to queue with 0 prio
		startVert.setQueueStatus(true);
		
		Vertex currentVertex;	//Vertex currently being processed
		
		while ((currentVertex = prioQ.pop()) != null) {
			currentVertex.setVisited(true);	//Set node as visited
			
			if (currentVertex.getIndex() == endIndex) {	//currentVertex is the end Vertex
				Vertex currentTerminal = currentVertex;
				
				while (currentTerminal != null) {	//Set all vertices in path as terminals
					currentTerminal.setTerminal(true);
					currentTerminal = currentTerminal.getParent();
				}
				
				return;
			}
			
			ArrayList<AdjNode> currentNeighbours = currentVertex.getAdj();	//Get current Node's neighbours
			
			for (AdjNode neighbour: currentNeighbours) {
				Vertex neighbourVertex = neighbour.getVert();	//Get vertex representation of neighbour
				double pathWeight = currentVertex.getPathLength() + neighbour.getWeight();	//Calculate total path weight through currentVertex to neighbourVertex
				
				if (!neighbourVertex.getVisited()) {	//Make sure neighbour hasn't been processed
					if (!neighbourVertex.getQueueStatus()) {	//If neighbour not in queue then add it
						neighbourVertex.setParent(currentVertex);	//Set currentNode as neighbour's parent
						prioQ.insert(neighbourVertex, pathWeight);
						
						neighbourVertex.setQueueStatus(true);	//Indicate that the Vertex is in the queue
						neighbourVertex.setPathLength(pathWeight);
						
					} else {
						int success = prioQ.update(neighbourVertex, pathWeight);
						
						if (success == 1) {	//If an update was made, update the parent as this was a shorter path
							neighbourVertex.setParent(currentVertex);
							neighbourVertex.setPathLength(pathWeight);
						}
						
					}
				}
			}
		}
	}
	
	
	public String toString() {
		String retString = this.size() + "\n";
		
		for (int vertIndex = 0; vertIndex < this.size(); vertIndex++) {
			Vertex currentVertex = this.getVertex(vertIndex);
			
			if (currentVertex != null) {
				String[] currentLine = new String[this.size()];
				
				for (int i = 0; i < this.size(); i++) {
					currentLine[i] = "0";
				}
				
				for (AdjNode currentAdj: currentVertex.getAdj()) {
					currentLine[currentAdj.getVert().getIndex()] = Double.toString(currentAdj.getWeight());
				}
				
				retString += String.join(" ", currentLine) + "\n";
			}
		}
		
		return retString;
	}
}
