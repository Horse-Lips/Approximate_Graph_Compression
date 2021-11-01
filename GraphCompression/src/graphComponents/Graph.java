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

	
	/*Contracts the edge between the nodes with IDs "toRemoveID" and "superNodeID"*/
	public int contract(int toRemoveIndex) {
		Vertex toRemove = this.getVertex(toRemoveIndex);
		
		if (toRemove == null) { return -1; }	//Check that vertex to be removed is actually in the graph
		
		
		/*Using alias method*/
		toRemove.partition();	//Partition Vertex edges for efficient sampling
		
		AdjNode toRemoveToSuper = toRemove.sample();	//Get edge to contract
		
		Vertex superNode = toRemoveToSuper.getVert();	//Get new supernode (two merged vertices)
		AdjNode superNodeToToRemove = superNode.getFromAdj(toRemove);	//Edge between superNode and toRemove
		
		superNode.removeFromAdj(superNodeToToRemove);	//Remove contracted edge from respective adj lists
		toRemove.removeFromAdj(toRemoveToSuper);
		
		
		ArrayList<AdjNode> toRemoveNeighbours = toRemove.getAdj();
		
		for (AdjNode toRemoveNeighbourAdj: toRemoveNeighbours) {	//Iterate over toRemove's remaining neighbours
			Vertex toRemoveNeighbour = toRemoveNeighbourAdj.getVert();	//Get vertex representation of toRemove neighbour
			double newEdgeWeight = toRemoveNeighbourAdj.getWeight() + toRemoveToSuper.getWeight();	//Calculate new edge weights to from neighbour to superNode through toRemove
			
			AdjNode neighbourToToRemove  = toRemoveNeighbour.getFromAdj(toRemove);	//Edge from neighbour to toRemove
			AdjNode neighbourToSuperNode = toRemoveNeighbour.getFromAdj(superNode);	//Edge from neighbour to superNode if it exists
			
			if (neighbourToSuperNode != null)  {	//An edge between toRemove's neighbour and superNode already exists
				AdjNode superNodeToNeighbour = superNode.getFromAdj(toRemoveNeighbour);	//Get same edge from the "other end"
				
				neighbourToSuperNode.setWeight(Math.min(neighbourToSuperNode.getWeight(), toRemoveToSuper.getWeight()));	//set its weight to the minimum of existing edge or new edge weight through toRemove
				superNodeToNeighbour.setWeight(Math.min(neighbourToSuperNode.getWeight(), toRemoveToSuper.getWeight()));
				
				toRemoveNeighbour.removeFromAdj(neighbourToToRemove);	//Remove edge between toRemove and neighbour
				
			} else {	//Otherwise create a new edge
				neighbourToToRemove.setVert(superNode);			//Update vertex and weight of existing edge for efficiency
				neighbourToToRemove.setWeight(newEdgeWeight);
				
				superNode.addToAdj(toRemoveNeighbour, newEdgeWeight);	//Create a new edge from superNode to neighbour
			}
			
		}
		
		this.removeVertex(toRemoveIndex); //Then we remove toRemove from the Graph
		
		return 1;
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
	
	
	/*Implementation of Dijkstra's algorithm using a priority queue*/
	public void dijkstra(int startIndex, int endIndex) {
		for (Vertex v: this.vertList) {	//Set all vertices as...
			v.setVisited(false);		//Unvisited
			v.setQueueStatus(false);	//Un-queued
			v.setPathLength(0);			//No path leading to this Vertex
		}
		
		Vertex startVert = this.getVertex(startIndex);
		Vertex endVert   = this.getVertex(endIndex);
		
		if (startVert == null || endVert == null) { return; }	//Check that both start and end are in the Graph
		
		
		SimpleQueuePrio<Vertex> prioQ = new SimpleQueuePrio<Vertex>();	//Priority queue
		
		prioQ.insert(startVert, 0);	//Add startVert to queue with 0 (highest) priority
		startVert.setQueueStatus(true);
		
		Vertex currentVertex;	//Vertex currently being processed
		
		while ((currentVertex = prioQ.pop()) != null) {
			currentVertex.setVisited(true);	//Set node as visited
			
			if (currentVertex.getIndex() == endIndex) {
				return;	//A path has been found
			}
			
			ArrayList<AdjNode> currentNeighbours = currentVertex.getAdj();	//Get current Node's neighbours
			
			for (AdjNode neighbour: currentNeighbours) {
				Vertex neighbourVertex = neighbour.getVert();	//Get vertex representation of neighbour
				double pathWeight = currentVertex.getPathLength() + neighbour.getWeight();	//Calculate total path weight through currentVertex to neighbourVertex
				
				if (neighbourVertex.getVisited()) {
					continue;	//Don't re-process visited Vertices
				}
				
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
