package graphComponents;


import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import graphUtils.SimpleTuple;


/*Vertex class used for representing Graph nodes*/
public class Vertex {
	/*Standard Vertex fields*/
	private int                index;	//Index of this Vertex in the Graph Vertex list
	private ArrayList<AdjNode> adjList;	//List of Vertices adjacent to the Vertex, or edges between this Vertex and vertices stored in AdjNodes in the list
	
	/*Fields used in shortest path algorithm*/
	private boolean visited;			//Used to check if Vertex visited in shortest path algorithm
	private boolean inQueue;			//Used to check if Vertex in priority queue (may need to update priority) during shortest path algorithm
	private double  currentPathLength;	//Length of shortest path from some other Vertex to this Vertex
	private Vertex  parent = null;		//Parent of Vertex (e.g. in shortest path)
	
	/*Fields used in sampling edges*/
	private double  totalEdgeWeight; 	//Cumulative sum of the edge weights of all edges containing this Vertex (Weights added in addToAdj method)
	private boolean partitioned;		//Used to check if the current Vertex has been partitioned
	private SimpleTuple<Double, AdjNode, AdjNode>[] partitions;	//Partitions used in Alias method for discrete sampling
	
	/*Fields used in sparsification*/
	private boolean terminal;	//Indicates whether or not this Vertex is a terminal
	
	public Vertex(int index) {
		this.index   = index;
		this.adjList = new ArrayList<AdjNode>();
		
		this.visited = false;
		this.inQueue = false;
		this.currentPathLength = 0;
		this.parent = null;
		
		this.partitioned = false;
		
		this.terminal = false;
	}
	
	
	/*Returns the index of the Vertex*/
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
	public void addToAdj(Vertex vert, double weight) {
		this.adjList.add(new AdjNode(vert, weight));
		this.totalEdgeWeight += weight;	//Add weight to the cumulative edge weight
		
		this.partitioned = false;		//The edges have changed so we must repartition
	}
	
	
	/*Removes a vertex from the adjacency list*/
	public void removeFromAdj(AdjNode adj) {
		if (adj != null) {	//This only occurs in certain circumstances, e.g. in Graph.Gauss when n1Adj == n2Adj
			this.adjList.remove(adj);
			this.totalEdgeWeight -= adj.getWeight();	//Remove weight from the cumulative edge weight
			
			this.partitioned = false;	//Edges have changed so we must repartition
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
	
	
	/*Returns the parent Vertex of the current Vertex*/
	public Vertex getParent() {
		return this.parent;
	}
	
	/*Updates the parent of the Vertex*/
	public void setParent(Vertex parent) {
		this.parent = parent;
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
	public void setPathLength(double length) {
		this.currentPathLength = length;
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
	@SuppressWarnings("unchecked")
	public void partition() {
		if (this.partitioned) { return; }	//Don't partition if it's not needed
		
		int pCount      = this.adjList.size();		//Number of partitions
		this.partitions = new SimpleTuple[pCount];	//Initialise partitions for this particular Vertex
		TreeMap<Double, AdjNode> probMap = new TreeMap<Double, AdjNode>(); //Create a new TreeMap to store probability-edge pairs
		
		for (AdjNode neighbour: this.adjList) {	//Iterate neighbours
			double currentEdgeProb = neighbour.getWeight() / this.totalEdgeWeight;	//Calculate probability of current edge
			
			while (probMap.containsKey(currentEdgeProb)) {
				currentEdgeProb += 0.000001;	//Account for duplicate keys with a slight change to a probability
			}
			
			probMap.put(currentEdgeProb, neighbour);	//Insert prob-AdjNode pair into the TreeMap
		}
		
		for (int i = 0; i < pCount; i++) {
			Entry<Double, AdjNode> smallest = probMap.pollFirstEntry();	//Pop smallest entry from probMap
			Entry<Double, AdjNode> largest  = probMap.pollLastEntry();	//Pop largest if it exists (It wont if there was only 1 entry as we removed smallest - the only entry)
			
			double leftOver = (1.0 / pCount) - smallest.getKey();	//Calculate the remaining space left in the partition (We fill this with some of the larger probability)
			SimpleTuple<Double, AdjNode, AdjNode> partition;		//Create a tuple representing a partition which will contain the probability (element 0) of selecting element 1
			
			/*Note: here we use 0.00001 to account for roundoff*/
			if (leftOver > 0.00001 && largest != null) {	//Fill any leftover space using largest prob
				probMap.put(largest.getKey() - leftOver, largest.getValue());	//Calculate remaining probability of largest and reinsert into the TreeMap
				
				partition = new SimpleTuple<Double, AdjNode, AdjNode>(smallest.getKey(), smallest.getValue(), largest.getValue());	//Fill the partition as stated above
				this.partitions[i] = partition;
				
			} else {
				partition = new SimpleTuple<Double, AdjNode, AdjNode>(smallest.getKey(), smallest.getValue(), smallest.getValue());	//In this case, "smallest" occupies the entire partition
				this.partitions[i] = partition;
				
			}
			
			if (largest != null) {
				probMap.put(largest.getKey(), largest.getValue());
			}
		}
		
		this.partitioned = true;
	}
	
	
	/*Sample from partition and return an edge to contract*/
	public AdjNode sample() {
		double randProb  = Math.random() / this.partitions.length;			//Generate a random number between 0 and 1 inclusive and divide by amount of partitions
		int randomPIndex = (int) (Math.random() * this.partitions.length);	//Choose a random partition
		SimpleTuple<Double, AdjNode, AdjNode> partition = partitions[randomPIndex];
		
		if (randProb <= partition.getFirst()) {	//If randProb lies within the probability then return the "smallest"
			return partition.getSecond();
			
		} else {							//Otherwise return the "largest"
			return partition.getThird();
			
		}
	}
	
	
	public String toString() {
		String retString = "Vertex Object: \nIndex: " + this.getIndex() + "\nDegree: " + this.adjList.size() + "\nParent Index: " + this.getParent().getIndex();
		
		return retString;
	}
	
}
