package graphComponents;


import java.util.ArrayList;


/*Vertex class used for representing Graph nodes*/
public class Vertex {
	private int index;				//Index of the Vertex in the Graph Vertex list
	private ArrayList<AdjNode> adjList;	//List of Vertices adjacent to the Vertex
	private int value;				//Value stored in the Vertex
	private Vertex parent;			//Parent of Vertex (e.g. in shortest path)
	
	private double totalEdgeWeight; //Sum of the edge weights of all edges containing this Vertex (Weights added in addToAdj method)
	
	public Vertex(int id) {
		this.index = id;
		this.adjList = new ArrayList<AdjNode>();
		this.value = 0;
		this.parent = null;
		
		this.totalEdgeWeight = 0;
	}
	
	
	/*Returns the ID of the Vertex*/
	public int getIndex() {
		return this.index;
	}
	
	
	/*Updates the ID of the Vertex*/
	public void setIndex(int ID) {
		this.index = ID;
	}
	
	
	/*Returns the adjacency list of the Vertex*/
	public ArrayList<AdjNode> getAdj() {
		return this.adjList;
	}
	
	/*Adds a vertex to the adjacency list*/
	public void addToAdj(Vertex v, float weight) {
		this.adjList.add(new AdjNode(v, weight));
		
		this.totalEdgeWeight += weight;	//Add weight to the cumulative edge weight
	}
	
	
	/*Removes a vertex from the adjacency list*/
	public void removeFromAdj(AdjNode adj) {
		if (adj != null) {	//This only occurs in certain circumstances, e.g. in Graph.Gauss when n1Adj == n2Adj
			this.adjList.remove(adj);
			this.totalEdgeWeight -= adj.getWeight();	//Remove weight from the cumulative edge weight
		}
	}
	
	
	/*Returns the value of the Vertex*/
	public int getVal() {
		return this.value;
	}
	
	/*Updates the value of the Vertex*/
	public void setVal(int value) {
		this.value = value;
	}
	
	
	/*Returns the parent Vertex of the current Vertex*/
	public Vertex getParent() {
		return this.parent;
	}
	
	/*Updates the parent of the Vertex*/
	public void setParent(Vertex parent) {
		this.parent = parent;
	}
	
	
	/*Returns the cumulative edge weight of all edges containing this Vertex*/
	public double getTotWeight() {
		return this.totalEdgeWeight;
	}
}
