package graphComponents;


import java.util.HashMap;
import java.util.Map.Entry;

import graphUtils.SimpleAVLTree;
import graphUtils.SimpleAVLTree.Node;
import graphUtils.SimpleTuple;


@SuppressWarnings("unchecked")


/**
 * Vertex class used for representing nodes in a Graph data structure.
 * Edges to other Vertices are represented by HashMap <Integer, Double>
 * entries, where the Integer represents the index of the other Vertex 
 * and the Double represents the weight of the edge.
 * 
 * @author Jake Haakanson. GUID: 2407682H
 *
 */
public class Vertex {
	/*Standard Vertex fields*/
	/** Index of this Vertex in the Graph Vertex list */
	private int index;
	
	/** HashMap of Vertices adjacent to this Vertex. Key represents the adjaceny Vertex's index and value represents the weight of the edge */
	HashMap<Integer, Double> adjList;
	
	/*Fields used in shortest path algorithm*/
	/** Used to check if Vertex visited (Shortest Path) */
	private boolean visited;
	
	/** Used to check if Vertex is in priority queue as priority may need updating (Shortest Path) */
	private boolean inQueue;
	
	/** Length of shortest path from some starting Vertex to this Vertex (Shortest Path) */
	private double currentPathLength;
	
	/** Parent of this Vertex (Shortest Path) */
	private Vertex parent;
	
	/*Fields used in sampling edges*/
	/** Cumulative sum of all edge weights of edges incident to this Vertex (Alias Method Discrete Sampling) */
	private double totalEdgeWeight;
	
	/** Used to check if this Vertex has been partitioned (Alias Method Discrete Sampling) */
	private boolean partitioned;
	
	/** Partitions of edge weight probabilities for sampling (Alias Method Discrete Sampling) */
	private SimpleTuple<Double, Integer, Integer>[] partitions;
	
	/*Fields used in sparsification*/
	/** Indicates whether or not this Vertex is a terminal (Sparsification) */
	private boolean terminal;
	
	/** Indicates whether or not this Vertex has been "removed" from the Graph (Sparsification) */
	private boolean deactivated;
	
	public Vertex(int index) {
		this.index   = index;
		this.adjList = new HashMap<Integer, Double>();
		
		this.visited = false;
		this.inQueue = false;
		this.currentPathLength = 0;
		this.parent = null;
		
		this.partitioned = false;
		
		this.terminal = false;
		this.deactivated = false;
	}
	
	
	/**
	 * Get the index of this Vertex
	 * @return the index of this Vertex
	 */
	public int getIndex() {
		return this.index;
	}

	
	
	/** 
	 * Get the entire adjacency list of this Vertex
	 * @return a Hashmap of Integer-Double key-value pairs where the Integer is some Vertex v's index and the Double is the weight of the edge between this Vertex and v
	 */
	public HashMap<Integer, Double> getAdj() {
		return this.adjList;
	}
	
	
	public boolean getAdjEmpty() {
		return this.adjList.isEmpty();
	}
	
	/**
	 * Adds an edge between this Vertex and the Vertex represented by vertIndex
	 * @param vertIndex, the index of the other Vertex in the edge
	 * @param weight, the weight of the edge
	 */
	public void addToAdj(int vertIndex, double weight) {
		this.adjList.put(vertIndex, weight);
		
		this.totalEdgeWeight += weight;	//Add weight to the cumulative edge weight
		this.partitioned = false;		//The edges have changed so we must repartition
	}
	
	
	/**
	 * Removes an edge from this Vertex's adjacency list
	 * @param vertIndex, the index of the Vertex to be removed
	 */
	public void removeFromAdj(int vertIndex) {
		this.totalEdgeWeight -= adjList.get(vertIndex);	//Remove weight from cumulative edge weight
		this.adjList.remove(vertIndex);
		
		this.partitioned = false;	//Edges have changed so we must repartition
	}
	
	
	/**
	 * Returns the weight of the edge from this Vertex to the vertex specified by vertIndex
	 * @param vertIndex, the other Vertex in the edge
	 * @return the weight between this Vertex and the Vertex represented by vertIndex
	 */
	public Double getFromAdj(int vertIndex) {
		return this.adjList.get(vertIndex);
	}
	
	
	/**
	 * Check if the specified Vertex is in this Vertex's adjacency list
	 * @param vertIndex, the index of the Vertex to check for
	 * @return true if the Vertex is in the adjacency list, false otherwise
	 */
	public boolean adjContains(int vertIndex) {
		return this.adjList.containsKey(vertIndex);
	}
	
	
	/**
	 * Updates the edge containing vertIndex to the weight specified by newWeight. Also updates totalEdgeWeight to reflect this change
	 * @param vertIndex, index of Vertex whose edge between this Vertex's weight should be updated
	 * @param newWeight, the new weight of the edge
	 */
	public void updateAdj(int vertIndex, double newWeight) {
		Double oldWeight = this.adjList.put(vertIndex, newWeight);
		
		this.totalEdgeWeight += newWeight;
		
		if (oldWeight != null) {
			this.totalEdgeWeight -= oldWeight;
		}
	}
	
	
	/**
	 * Sets the adjacency hashmap of this vertex to the one provided
	 * @param newAdj, a hashmap representing adjacent vertices and their edge weights
	 */
	public void setAdj(HashMap<Integer, Double> newAdj) {
		this.adjList = newAdj;
	}
	
	
	/**
	 * Get the parent of this Vertex (Shortest Path)
	 * @return the parent of this Vertex
	 */
	public Vertex getParent() {
		return this.parent;
	}
	
	/**
	 * Set the parent of this Vertex (Shortest Path)
	 * @param parent, the parent Vertex of this Vertex
	 */
	public void setParent(Vertex parent) {
		this.parent = parent;
	}
	
	
	/**
	 * Check if this Vertex has been visited (Shortest Path)
	 * @return true if this Vertex has been visited, false otherwise
	 */
	public boolean getVisited() {
		return this.visited;
	}
	
	
	/**
	 * Set the value of visited (Shortest Path)
	 * @param b, the boolean to set the value of visited to
	 */
	public void setVisited(boolean b) {
		this.visited = b;
	}
	
	
	/**
	 * Check the queue status of this Vertex (Shortest Path)
	 * @return true if the Vertex is queued, false otherwise
	 */
	public boolean getQueueStatus() {
		return this.inQueue;
	}
	
	
	/**
	 * Set the value of inQueue (Shortest Path)
	 * @param b, the boolean value to set inQueue to
	 */
	public void setQueueStatus(boolean b) {
		this.inQueue = b;
	}
	
	
	/**
	 * Get the length of the shortest path to this Vertex from some starting Vertex (Shortest Path)
	 * @return the length of shortest path from some Vertex to this Vertex
	 */
	public double getPathLength() {
		return this.currentPathLength;
	}
	
	
	/**
	 * Update shortest path length (Shortest Path)
	 * @param length, the new length of the shortest path from some starting Vertex to this Vertex
	 */
	public void setPathLength(double length) {
		this.currentPathLength = length;
	}
	
	
	/**
	 * Get the status of whether or not this Vertex is a terminal (Sparsification)
	 * @return true if this Vertex is a terminal, false otherwise
	 */
	public boolean getTerminal() {
		return this.terminal;
	}
	
	
	/**
	 * Update this Vertex's status as a terminal (Sparsification)
	 * @param b, the boolean value to set whether or not this Vertex is a terminal
	 */
	public void setTerminal(boolean b) {
		this.terminal = b;
	}
	
	
	/**
	 * Get the status of whether or not this Vertex has been deactivated (Sparsification)
	 * @return true if Vertex deactivated, false otherwise
	 */
	public boolean isDeactivated() {
		return this.deactivated;
	}
	
	
	/**
	 * Deactivates this Vertex (Sparsification)
	 */
	public void deactivate() {
		this.deactivated = true;
	}
	
	
	/**
	 * Check if this Vertex is partitioned (Alias Method Discrete Sampling)
	 * @return true if this Vertex is partitioned, false otherwise
	 */
	public boolean getPartitioned() {
		return this.partitioned;
	}
	
	
	/**
	 * Set this Vertex as partitioned or unpartitioned (Alias Method Discrete Sampling)
	 * @param b, the boolean value that is true if this Vertex is partitioned and false otherwise
	 */
	public void setPartitioned(boolean b) {
		this.partitioned = b;
	}
	
	
	/**
	 * Partition edges based on edge probability using alias method (discrete sampling)
	 */
	public void partition() {
		if (this.partitioned) { return; }	//Don't partition if we don't need to
		
		int partitionCount = this.adjList.size();				//Number of partitions to create
		this.partitions    = new SimpleTuple[partitionCount];	//Initialise partitions for the vertex
		
		SimpleAVLTree<Integer> probTree = new SimpleAVLTree<Integer>();	//Initialise AVL tree to store probability-edge pairs
		
		for (Entry<Integer, Double> adjNode: this.adjList.entrySet()) {
			double currentEdgeProb = adjNode.getValue() / this.totalEdgeWeight;	//Calculate current edge probability
			
			probTree.insert(currentEdgeProb, adjNode.getKey());	//Insert all probability-edge pair into AVL tree
		}
		
		for (int i = 0; i < partitionCount; i++) {
			Node<Integer> smallest = probTree.popMin();	//Pop entry in tree with smallest key
			
			double leftOver = (1.0 / partitionCount) - smallest.getKey();	//Fill partition with as much of smallest's probability as possible
			
			SimpleTuple<Double, Integer, Integer> partition;	//Create a new partition in the form of a tuple
			
			if (leftOver > 0.00001) {	//Fill leftover space with some of largest tree entry's probability
				Node<Integer> largest  = probTree.popMax();	//Pop entry in tree with largest key
				
				double largestValue = largest.getKey() - leftOver;
				
				if (largestValue > 0.00001) {
					probTree.insert(largest.getKey() - leftOver, largest.getValue());	//Reinsert into tree with reduced probability
				}
				
				partition = new SimpleTuple<Double, Integer, Integer>(smallest.getKey(), smallest.getValue(), largest.getValue());
				
			} else {
				partition = new SimpleTuple<Double, Integer, Integer>(smallest.getKey(), smallest.getValue(), smallest.getValue());
				
			}
			
			
			this.partitions[i] = partition;
		}
		
		this.partitioned = true;
	}
	
	
	/**
	 * Sample this Vertex's partitioned edges for an edge to contract (Alias Method Discrete Sampling)
	 * @return The vertex that there is an edge from this Vertex to
	 */
	public int sample() {
		this.partition();	//Partition Vertex if needed
		
		double randProb  = Math.random() / this.partitions.length;			//Generate a random number between 0 and 1 inclusive and divide by amount of partitions
		int randomPIndex = (int) (Math.random() * this.partitions.length);	//Choose a random partition
		SimpleTuple<Double, Integer, Integer> partition = partitions[randomPIndex];
		
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
