package graphComponents;


import java.util.List;


/*Vertex class used for representing Graph nodes*/
public class Vertex {
	private int index;				//Index of the Vertex in the Graph Vertex list
	private List<AdjNode> adjList;	//List of Vertices adjacent to the Vertex
	private int value;				//Value stored in the Vertex
	private Vertex parent;			//Parent of Vertex (e.g. in shortest path)
	
	public Vertex(int index) {
		this.index = index;
		
	}
	
	
	/*Returns the index of the Vertex in the Graph Vertex list*/
	public int getIndex() {
		return this.index;
	}
	
	
	/*Returns the adjacency list of the Vertex*/
	public List<AdjNode> getAdj() {
		return this.adjList;
	}
	
	/*Adds a vertex to the adjacency list*/
	public void addToAdj(Vertex v) {
		//this.adjList.add(v);
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
