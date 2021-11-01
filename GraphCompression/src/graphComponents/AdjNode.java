package graphComponents;


/*Represents an adjacent Vertex in the adjacency list of a Vertex*/
public class AdjNode {
	private Vertex v;		//Index of the Vertex represented by this node in the Graph's Vertex list
	private double weight;	//Represents the weight of the edge to get from some Vertex to this adjacent Vertex
	
	public AdjNode(Vertex v, double weight) {
		this.v      = v;
		this.weight = weight;
	}
	
	
	/*Returns the index of the Vertex represented by this node in the Graph's Vertex list*/
	public Vertex getVert() {
		return this.v;
	}
	
	
	/*Updates the Vertex on the "other end" of the edge*/
	public void setVert(Vertex newVert) {
		this.v = newVert;
	}
	
	
	/*Returns weight of the edge to reach this Vertex from some adjacent Vertex*/
	public double getWeight() {
		return this.weight;
	}
	
	
	/*Updates the value of weight*/
	public void setWeight(double newWeight) {
		this.weight = newWeight;
	}
}
