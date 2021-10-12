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
	
	
	/*Returns the Vertex at the given index*/
	public Vertex getVertex(int i) {
		return vertList[i];
	}
	
	
	/*Returns the size of the Graph represented by vertCount*/
	public int size() {
		return this.vertCount;
	}
	
	
	/*Removes the Vertex at index i and resizes the graph*/
	public void removeVertex(int vertIndex) {
		Vertex[] newVertList = new Vertex[this.vertCount - 1];
		
		int currentIndex = 0;
		
		for (int i = 0; i < this.vertCount; i++) {
			if (i != vertIndex) {
				newVertList[currentIndex++] = this.getVertex(i);
			}
		}
		
		this.vertList = newVertList;
		this.vertCount -= 1;
	}
	
	
	/*Removes a Vertex using the Gaussian Elimination method*/
	public void Gauss(int vertIndex) {
		Vertex toRemove = this.getVertex(vertIndex);	//Retrieve Vertex to remove
		ArrayList<AdjNode> neighbours = toRemove.getAdj();
		
		for (AdjNode neighbourOne: neighbours) {
			Vertex n1Vertex = this.getVertex(neighbourOne.getIndex());	//Get Vertex representation of neighbourOne
			
			for (AdjNode neighbourTwo: neighbours) {
				Vertex n2Vertex = this.getVertex(neighbourTwo.getIndex());	//Get Vertex representation of neighbourTwo
				
				if (neighbourOne != neighbourTwo) {
					float newWeight = neighbourOne.getWeight() + neighbourTwo.getWeight();	//Calculate the new weight of the path through the vertex to delete
					
					boolean updated = false;	//Used to check if an update was made to the weight of the edge between neighbourOne and neighbourTwo
					boolean noEdge  = true;		//Used to check if there was an existing edge between neighbourOne and neighbourTwo
					
					for (AdjNode n1Neighbour: n1Vertex.getAdj()) {	//Iterate over neighbourOne's neighbours
						if (n1Neighbour.getIndex() == neighbourTwo.getIndex()) {
							
							if (n1Neighbour.getWeight() > newWeight) {
								n1Neighbour.setWeight(newWeight);	//Update weight of the edge between neighbourOne and neighbourTwo
								updated = true;
								noEdge = false;
								
							}
						}
					}
					
					if (updated) {	//If we updated the weight of {n1, n2} we must do the same for {n2, n1}
						for (AdjNode n2Neighbour: n2Vertex.getAdj()) {
							if (n2Neighbour.getIndex() == neighbourOne.getIndex()) {
								n2Neighbour.setWeight(newWeight);
								
							}
						}
						
					} else if (noEdge) {	//If no edge existed between n1 and n2 previously we must create one
						n1Vertex.addToAdj(this.getVertex(neighbourTwo.getIndex()), newWeight);
						n2Vertex.addToAdj(this.getVertex(neighbourOne.getIndex()), newWeight);
						
					}
				}
				
				/*Remove vertex to be removed from neighbourTwo's adjacency list*/
				AdjNode n2ToRemove = null;
				
				for (AdjNode n2Neighbour: n2Vertex.getAdj()) {
					if (n2Neighbour.getIndex() == vertIndex) {
						n2ToRemove = n2Neighbour;
					}
				}
				
				n2Vertex.removeFromAdj(n2ToRemove);
			}
			
			/*Remove vertex to be removed from neighbourOne's adjacency list*/
			AdjNode n1ToRemove = null;
			
			for (AdjNode n1Neighbour: n1Vertex.getAdj()) {
				if (n1Neighbour.getIndex() == vertIndex) {
					n1ToRemove = n1Neighbour;
				}
			}
			
			n1Vertex.removeFromAdj(n1ToRemove);
		}
		
		this.removeVertex(vertIndex);	//Finally, remove the vertex to be removed from the graph
	}
	
	
	public String toString() {
		String retString = this.vertCount + "\n";
		
		for (int vertIndex = 0; vertIndex < this.vertCount; vertIndex++) {
			String[] currentLine = {"0", "0", "0", "0", "0", "0", "0"};
			Vertex currentVertex = this.getVertex(vertIndex);
			
			for (AdjNode currentAdj: currentVertex.getAdj()) {
				currentLine[currentAdj.getIndex()] = Float.toString(currentAdj.getWeight());
			}
			
			retString += String.join(" ", currentLine) + "\n";
		}
		
		return retString;
	}
}
