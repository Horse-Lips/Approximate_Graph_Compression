package graphComponents;


import java.util.ArrayList;


/*Vertex class used for representing Graph nodes*/
public class Vertex {
	private int ID;				//Index of the Vertex in the Graph Vertex list
	private ArrayList<AdjNode> adjList;	//List of Vertices adjacent to the Vertex
	private int value;				//Value stored in the Vertex
	private Vertex parent;			//Parent of Vertex (e.g. in shortest path)
	
	public Vertex(int id) {
		this.ID = id;
		this.adjList = new ArrayList<AdjNode>();
		this.value = 0;
		this.parent = null;
	}
	
	
	/*Returns the ID of the Vertex*/
	public int getID() {
		return this.ID;
	}
	
	
	/*Updates the ID of the Vertex*/
	public void setID(int ID) {
		this.ID = ID;
	}
	
	
	/*Returns the adjacency list of the Vertex*/
	public ArrayList<AdjNode> getAdj() {
		return this.adjList;
	}
	
	/*Adds a vertex to the adjacency list*/
	public void addToAdj(Vertex v, float weight) {
		this.adjList.add(new AdjNode(v, weight));
	}
	
	
	/*Removes a vertex from the adjacency list*/
	public void removeFromAdj(AdjNode adj) {
		this.adjList.remove(adj);
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
}
