package graphComponents;


import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import graphUtils.SimpleTuple;


/*Vertex class used for representing Graph nodes*/
public class Vertex {
	private int index;				//Index of the Vertex in the Graph Vertex list
	private ArrayList<AdjNode> adjList;	//List of Vertices adjacent to the Vertex
	private String value;				//Value stored in the Vertex
	private Vertex parent;			//Parent of Vertex (e.g. in shortest path)
	
	private double totalEdgeWeight; //Sum of the edge weights of all edges containing this Vertex (Weights added in addToAdj method)
	
	private boolean visited;			//Used to check if Vertex visited in shortest path algorithm
	private boolean inQueue;			//Used to check if Vertex in priority queue (may need to update priority) during shortest path algorithm
	private double currentPathLength;	//Length of shortest path to this Vertex
	
	private boolean terminal;	//Indicates whether or not this Vertex is a terminal
	
	private SimpleTuple<Double, AdjNode, AdjNode>[] partition;	//Partitions used in Alias method for discrete sampling
	private boolean partitioned;	//Used to check if the current Vertex has been partitioned
	
	public Vertex(int index) {
		this.index = index;
		this.adjList = new ArrayList<AdjNode>();
		this.value = "";
		this.parent = null;
		
		this.totalEdgeWeight = 0;
		
		this.visited = false;
		this.inQueue = false;
		this.currentPathLength = 0;
		
		this.terminal = false;
		
		this.partitioned = false;
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
	public void addToAdj(Vertex v, double weight) {
		this.adjList.add(new AdjNode(v, weight));
		this.totalEdgeWeight += weight;	//Add weight to the cumulative edge weight
		
		this.partitioned = false;
	}
	
	
	/*Removes a vertex from the adjacency list*/
	public void removeFromAdj(AdjNode adj) {
		if (adj != null) {	//This only occurs in certain circumstances, e.g. in Graph.Gauss when n1Adj == n2Adj
			this.adjList.remove(adj);
			this.totalEdgeWeight -= adj.getWeight();	//Remove weight from the cumulative edge weight
			
			this.partitioned = false;
		}
	}
	
	
	/*Returns the edge or AdjNode to a specified Vertex*/
	public AdjNode getFromAdj(Vertex end) {
		for (AdjNode neighbour: this.adjList) {
			if (neighbour.getVert().getIndex() == end.getIndex()) {
				return neighbour;
			}
		}
		
		return null;
	}
	
	
	
	/*Returns the value of the Vertex*/
 	public String getVal() {
		return this.value;
	}
	
	/*Updates the value of the Vertex*/
	public void setVal(String val) {
		this.value = val;
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
	public double getPathLength() {
		return this.currentPathLength;
	}
	
	
	/*Updates current path length*/
	public void setPathLength(double l) {
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
	
	
	/*Returns the boolean value of partitioned, used to check if the current vertex has been partitioned for alias method discrete sampling*/
	public boolean getPartitioned() {
		return this.partitioned;
	}
	
	
	/*Updates the value of this.partitioned*/
	public void setPartitioned(boolean b) {
		this.partitioned = b;
	}
	
	
	/*Partitions edges and probabilities into boxes for Alias method*/
	public void partition() {
		if (this.partitioned) {	//Don't partition if it's not needed
			return;
		}
		
		int boxCount = this.adjList.size();
		
		this.partition = new SimpleTuple[boxCount];	//Initialise partitions for this particular Vertex
		TreeMap<Double, AdjNode> probMap = new TreeMap<Double, AdjNode>(); //Create a new AVL Tree for probabilities
		
		for (AdjNode neighbour: this.adjList) {
			double currentKey = neighbour.getWeight() / this.getTotWeight();
			
			while (probMap.containsKey(currentKey)) {
				currentKey += 0.000001;	//This accounts for duplicate keys with a slight change to a probability
			}
			
			probMap.put(currentKey, neighbour);	//Insert all prob-AdjNode pairs into probMap
		}
		
		for (int i = 0; i < boxCount; i++) {
			Entry<Double, AdjNode> smallest = probMap.pollFirstEntry();	//Pop smallest entry from probMap
			Entry<Double, AdjNode> largest  = probMap.pollLastEntry();	//Pop largest if it exists (It wont if there was only 1 entry as we removed smallest - the only entry)
			
			double leftOver = (1.0 / boxCount) - smallest.getKey();
			
			/*Note: here we use 0.0000000001 to account for roundoff*/
			if (leftOver > 0.00001 && largest != null) {	//Fill any leftover space using largest prob
				probMap.put(largest.getKey() - leftOver, largest.getValue());
				
				SimpleTuple<Double, AdjNode, AdjNode> box = new SimpleTuple<Double, AdjNode, AdjNode>(smallest.getKey(), smallest.getValue(), largest.getValue());	//"Fill" box
				this.partition[i] = box;
				
			} else {
				SimpleTuple<Double, AdjNode, AdjNode> box = new SimpleTuple<Double, AdjNode, AdjNode>(smallest.getKey(), smallest.getValue(), smallest.getValue());
				this.partition[i] = box;
				
			}
			
			if (largest != null) {
				probMap.put(largest.getKey(), largest.getValue());
			}
				
				
		}
		
		this.partitioned = true;
	}
	
	
	/*Sample from partition and return an edge to contract*/
	public AdjNode sample() {
		int randomBoxIndex = ThreadLocalRandom.current().nextInt(0, partition.length);	//Choose random box
		SimpleTuple<Double, AdjNode, AdjNode> box = partition[randomBoxIndex];
		
		double randProb = Math.random() / this.partition.length;	//Generate a random number between 0 and 1 inclusive and divide by box count
		
		if (randProb <= box.getFirst()) {
			return box.getSecond();
			
		} else {
			return box.getThird();
			
		}
	}
	
	
	public String toString() {
		String retString = "Vertex Object: \nIndex: " + this.getIndex() + "\nDegree: " + this.adjList.size() + "\nParent Index: " + this.getParent().getIndex();
		
		return retString;
	}
	
}
