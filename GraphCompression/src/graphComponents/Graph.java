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
		
		if (toRemove == null) {
			return;
		}
		
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
		if (toRemove == null) {
			return;
		}
		
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
