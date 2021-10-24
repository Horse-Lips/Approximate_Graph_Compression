package graphComponents;


import java.util.ArrayList;


/*Vertex class used for representing Graph nodes*/
public class Vertex {
	private int index;				//Index of the Vertex in the Graph Vertex list
	private ArrayList<AdjNode> adjList;	//List of Vertices adjacent to the Vertex
	private int value;				//Value stored in the Vertex
	private Vertex parent;			//Parent of Vertex (e.g. in shortest path)
	
	private double totalEdgeWeight; //Sum of the edge weights of all edges containing this Vertex (Weights added in addToAdj method)
	
	private boolean visited;			//Used to check if Vertex visited in shortest path algorithm
	private boolean inQueue;			//Used to check if Vertex in priority queue (may need to update priority) during shortest path algorithm
	private float currentPathLength;	//Length of shortest path to this Vertex
	
	private boolean terminal;	//Indicates whether or not this Vertex is a terminal
	
	public Vertex(int index) {
		this.index = index;
		this.adjList = new ArrayList<AdjNode>();
		this.value = 0;
		this.parent = null;
		
		this.totalEdgeWeight = 0;
		
		this.visited = false;
		this.inQueue = false;
		this.currentPathLength = 0;
		
		this.terminal = false;
	}
	
	
	/*Returns the ID of the Vertex*/
	public int getIndex() {
		return this.index;
	}
	
	
	/*Updates the ID of the Vertex*/
	public void setIndex(int index) {
		this.index = index;
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
	
	
	/*Returns the value of visited*/
	public boolean getVisited() {
		return this.visited;
	}
	
	
	/*Updates the value of visited*/
	public void setVisited(boolean b) {
		this.visited = b;
	}
	
	
	/*Returns the value of inQueue*/
	public boolean getQueueStatus() {
		return this.inQueue;
	}
	
	
	/*Updates the value of inQueue*/
	public void setQueueStatus(boolean b) {
		this.inQueue = b;
	}
	
	
	/*Returns current shortest path length*/
	public float getPathLength() {
		return this.currentPathLength;
	}
	
	
	/*Updates current path length*/
	public void setPathLength(float l) {
		this.currentPathLength = l;
	}
	
	
	/*Returns whether or not this Vertex is a terminal*/
	public boolean getTerminal() {
		return this.terminal;
	}
	
	
	/*Updates the value of this.terminal*/
	public void setTerminal(boolean b) {
		this.terminal = b;
	}
	
	
	public String toString() {
		String retString = "Vertex Object: \nIndex: " + this.getIndex() + "\nDegree: " + this.adjList.size() + "\nParent Index: " + this.getParent().getIndex();
		
		return retString;
	}
	
}
