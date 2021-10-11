package graphComponents;


/*Represents an adjacent Vertex in the adjacency list of a Vertex*/
public class AdjNode {
	private int index;	//Index of the Vertex represented by this node in the Graph's Vertex list
	private int weight;	//Represents the weight of the edge to get from some Vertex to this adjacent Vertex
	
	public AdjNode(int index, int weight) {
		this.index  = index;
		this.weight = weight;
	}
	
	
	/*Returns the index of the Vertex represented by this node in the Graph's Vertex list*/
	public int getIndex() {
		return this.index;
	}
	
	
	/*Returns weight of the edge to reach this Vertex from some adjacent Vertex*/
	public int getWeight() {
		return this.weight;
	}
}
