package graphComponents;

import java.util.ArrayList;
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
					float newWeight = n1Adj.getWeight() + n2Adj.getWeight();	//Calculate the new weight of the path through the vertex to delete
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
	
	
	/*Contracts the edge between the nodes with IDs "toRemoveID" and "superNodeID"*/
	public void contract(int toRemoveIndex) {
		Vertex toRemove  = this.getVertex(toRemoveIndex);
		
		if (toRemove == null) { return; } //Check that vertex to be removed is actually in the graph
		/*
		 * The following selects an edge to contract, or alternatively selects the Vertex that will absorb "toRemove" using the probability of the edge
		 * The probability of the edge is the weight of the edge divided by the sum of all edge weights containing the Vertex
		 * The sum of all edge weights is calculated when adding edges to a Vertex in order to save time and is retrieved using v.getTotWeight() for some Vertex v
		*/
		double randProb  = Math.random();
		double cumSum    = 0;
		
		float removedEdgeWeight = 0;	//The weight of the edge being contracted, used for addition to existing edges later
		
		AdjNode edgeToContract = null;
		
		for (AdjNode adj: toRemove.getAdj()) {
			double currentProb = adj.getWeight() / toRemove.getTotWeight();	//Calculate current edge's probability
			cumSum += currentProb;
			
			if (cumSum >= randProb) {	//If the cumsum exceeds the probability then we have selected an edge
				edgeToContract = adj;
				removedEdgeWeight = adj.getWeight();
				
				break;
			}
		}
		
		if (edgeToContract == null) {	//Something has failed and we were unable to select an edge to contract
			return;
		}
		
		
		Vertex superNode = edgeToContract.getVert();	//The supernode after edge contraction has been performed
		
		/*The following removes the edge between superNode and toRemove*/
		AdjNode removeFromSuper = null;
		
		for (AdjNode adjSuper: superNode.getAdj()) {
			if (adjSuper.getVert() == toRemove) {
				removeFromSuper = adjSuper;
			}
		}
		
		superNode.removeFromAdj(removeFromSuper);
		
		
		for (AdjNode removeNeighbourAdj: toRemove.getAdj()) {	//Iterate over toRemove's neighbours
			if (removeNeighbourAdj != edgeToContract) {
				Vertex removeNeighbourVert = removeNeighbourAdj.getVert();					//Get Vertex representation of neighbour
				float newEdgeWeight = removeNeighbourAdj.getWeight() + removedEdgeWeight;	//Weight of path from neighbour to superNode through toRemove
				
				boolean updated = false;		//Used to check if an edge to superNode already exists
				AdjNode edgeToToRemove = null;	//Edge containing toRemove
				
				for (AdjNode neighbourAdj: removeNeighbourVert.getAdj()) {	//Iterate over the neighbour's neighbours
					if (neighbourAdj.getVert() == superNode) {	//An edge already exists to the super node
						updated = true;	//Indicate we have made an update
						
						if (neighbourAdj.getWeight() > newEdgeWeight) {	//Make an update if necessary
							neighbourAdj.setWeight(newEdgeWeight);
						}
						
					} else if (neighbourAdj.getVert() == toRemove) {
						edgeToToRemove = neighbourAdj;	//Store edge containing toRemove
						
					}
				}
				
				if (!updated) {	//If no update was made, there is no edge containing superNode so we update the existing edge to take us to superNode "through" toRemove
					edgeToToRemove.setVert(superNode);
					edgeToToRemove.setWeight(newEdgeWeight);
					
					superNode.addToAdj(removeNeighbourVert, newEdgeWeight);	//Since no edge exists, add an edge from superNode to the neighbour of toRemove
					
				} else {	//If an update was made then we can simply remove the edge containing toRemove
					removeNeighbourVert.removeFromAdj(edgeToToRemove);
					
				}
			}
		}
		
		this.removeVertex(toRemoveIndex); //Then we remove toRemove from the Graph
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
				float pathWeight = currentVertex.getPathLength() + neighbour.getWeight();	//Calculate total path weight through currentVertex to neighbourVertex
				
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
					currentLine[currentAdj.getVert().getIndex()] = Float.toString(currentAdj.getWeight());
				}
				
				retString += String.join(" ", currentLine) + "\n";
			}
		}
		
		return retString;
	}
}
