package graphComponents;

import java.util.ArrayList;

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
	public Vertex getVertex(int ID) {
		for (Vertex v: this.vertList) {
			if (ID == v.getID()) {
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
	public void removeVertex(int vertID) {
		Vertex toRemove = this.getVertex(vertID);
		
		if (toRemove == null) { return; }
		
		Vertex[] newVertList = new Vertex[this.vertCount - 1];
		int currentIndex = 0;
		
		for (Vertex v: this.vertList) {
			if (v != toRemove) {
				newVertList[currentIndex] = v;
				v.setID(currentIndex);
				
				currentIndex++;
			}
		}
		
		this.vertList = newVertList;
		this.vertCount = this.size() - 1;
	}
	
	
	/*Removes a Vertex using the Gaussian Elimination method*/
	public void Gauss(int vertID) {
		Vertex toRemove = this.getVertex(vertID);	//Retrieve Vertex to remove
		
		/*Make sure Vertex to be removed is actually in the graph*/
		if (toRemove == null) { return; }
		
		ArrayList<AdjNode> neighbours = toRemove.getAdj();
		
		for (AdjNode neighbourOne: neighbours) {
			Vertex n1Vertex = neighbourOne.getVert();	//Get Vertex representation of neighbourOne
			
			for (AdjNode neighbourTwo: neighbours) {
				Vertex n2Vertex = neighbourTwo.getVert();	//Get Vertex representation of neighbourTwo
				
				if (neighbourOne != neighbourTwo) {
					float newWeight = neighbourOne.getWeight() + neighbourTwo.getWeight();	//Calculate the new weight of the path through the vertex to delete
					
					boolean updated = false;	//Used to check if an update was made to the weight of the edge between neighbourOne and neighbourTwo
					boolean noEdge  = true;		//Used to check if there was an existing edge between neighbourOne and neighbourTwo
					
					for (AdjNode n1Neighbour: n1Vertex.getAdj()) {	//Iterate over neighbourOne's neighbours
						if (n1Neighbour.getVert() == neighbourTwo.getVert()) {
							
							if (n1Neighbour.getWeight() > newWeight) {
								n1Neighbour.setWeight(newWeight);	//Update weight of the edge between neighbourOne and neighbourTwo
								updated = true;
								noEdge = false;
								
							}
						}
					}
					
					if (updated) {	//If we updated the weight of {n1, n2} we must do the same for {n2, n1}
						for (AdjNode n2Neighbour: n2Vertex.getAdj()) {
							if (n2Neighbour.getVert() == neighbourOne.getVert()) {
								n2Neighbour.setWeight(newWeight);
								
							}
						}
						
					} else if (noEdge) {	//If no edge existed between n1 and n2 previously we must create one
						n1Vertex.addToAdj(n2Vertex, newWeight);
						n2Vertex.addToAdj(n1Vertex, newWeight);
						
					}
				}
				
				/*Remove vertex to be removed from neighbourTwo's adjacency list*/
				AdjNode n2ToRemove = null;
				
				for (AdjNode n2Neighbour: n2Vertex.getAdj()) {
					if (n2Neighbour.getVert() == toRemove) {
						n2ToRemove = n2Neighbour;
					}
				}
				
				n2Vertex.removeFromAdj(n2ToRemove);
			}
			
			/*Remove vertex to be removed from neighbourOne's adjacency list*/
			AdjNode n1ToRemove = null;
			
			for (AdjNode n1Neighbour: n1Vertex.getAdj()) {
				if (n1Neighbour.getVert() == toRemove) {
					n1ToRemove = n1Neighbour;
				}
			}
			
			n1Vertex.removeFromAdj(n1ToRemove);
		}
		
		this.removeVertex(vertID);	//Finally, remove the vertex to be removed from the graph
	}
	
	
	/*Contracts the edge between the nodes with IDs "toRemoveID" and "superNodeID"*/
	public void contract(int toRemoveID) {
		Vertex toRemove  = this.getVertex(toRemoveID);
		
		/*
		 * The following selects an edge to contract, or alternatively selects the Vertex that will absorb "toRemove" using the probability of the edge
		 * The probability of the edge is the weight of the edge divided by the sum of all edge weights containing the Vertex
		 * The sum of all edge weights is calculated when adding edges to a Vertex in order to save time and is retrieved using v.getTotWeight() for some Vertex v
		*/
		double randProb  = Math.random();
		double cumSum    = 0;
		
		float removedEdgeWeight = 0;	//The weight of the edge being contracted
		
		AdjNode edgeToContract = null;
		
		for (AdjNode adj: toRemove.getAdj()) {
			double currentProb = adj.getWeight() / toRemove.getTotWeight();
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
		
		
		
		
		/*TESTING*/
		System.out.println("Merging with Vertex with ID " + edgeToContract.getVert().getID());
		
		
		
		Vertex superNode = edgeToContract.getVert();	//The supernode after edge contraction has been performed
		
		/*The following removes the edge between superNode and toRemove*/
		AdjNode removeFromSuper = null;
		
		for (AdjNode adjSuper: superNode.getAdj()) {
			if (adjSuper.getVert() == toRemove) {
				removeFromSuper = adjSuper;
			}
		}
		
		superNode.removeFromAdj(removeFromSuper);
		
		
		for (AdjNode adjToRemove: toRemove.getAdj()) {	//Iterate over toRemove's neighbours
			if (adjToRemove != edgeToContract) {
				Vertex removeNeighbourVert = adjToRemove.getVert();					//Get Vertex representation of neighbour
				float newEdgeWeight = adjToRemove.getWeight() + removedEdgeWeight;	//Weight of path from neighbour to superNode through toRemove
				
				boolean updated = false;		//Used to check if an edge to superNode already exists
				AdjNode edgeToToRemove = null;	//Edge containing toRemove
				
				for (AdjNode adjNeighbour: removeNeighbourVert.getAdj()) {	//Iterate over the neighbour's neighbours
					if (adjNeighbour.getVert() == superNode) {	//An edge already exists to the super node
						updated = true;	//Indicate we have made an update
						
						if (adjNeighbour.getWeight() > newEdgeWeight) {	//Make an update if necessary
							adjNeighbour.setWeight(newEdgeWeight);
						}
						
					} else if (adjNeighbour.getVert() == toRemove) {
						edgeToToRemove = adjNeighbour;	//Store edge containing toRemove
						
					}
				}
				
				if (!updated) {	//If no update was made, there is no edge containing superNode so we update the existing edge to take us to superNode "through" toRemove
					edgeToToRemove.setVert(superNode);
					edgeToToRemove.setWeight(newEdgeWeight);
					
					superNode.addToAdj(adjToRemove.getVert(), newEdgeWeight);	//Since no edge exists, add an edge from superNode to the neighbour of toRemove
					
				} else {	//If an update was made then we can simply remove the edge containing toRemove
					removeNeighbourVert.removeFromAdj(edgeToToRemove);
					
				}
			}
		}
		
		this.removeVertex(toRemoveID); //Then we remove toRemove from the Graph
	}
	
	
	public String toString() {
		String retString = this.size() + "\n";
		
		for (int vertID = 0; vertID < this.size(); vertID++) {
			Vertex currentVertex = this.getVertex(vertID);
			
			if (currentVertex != null) {
				String[] currentLine = new String[this.size()];
				
				for (int i = 0; i < this.size(); i++) {
					currentLine[i] = "0";
				}
				
				for (AdjNode currentAdj: currentVertex.getAdj()) {
					currentLine[currentAdj.getVert().getID()] = Float.toString(currentAdj.getWeight());
				}
				
				retString += String.join(" ", currentLine) + "\n";
			}
		}
		
		return retString;
	}
}
