package graphUtils;

public class SimpleQueuePrio<Item> {
	/*Simple queue data structure that can be used as either a FIFO or priority queue*/
	
	private static class Node<Item> {
		private Item value;	//Value stored in the Node
		private int prio;	//Priority of the Node in the queue
		
		private Node<Item> next;	//Next node in queue
		private Node<Item> prev;	//Prev node in queue
		
		public Node(Item value, int prio) {
			this.value = value;
			this.prio = prio;
			
			this.next = null;
			this.prev = null;
		}
	}
	
	
	private Node<Item> head;	//Node at the head of the queue
	
	public SimpleQueuePrio() {
		this.head = null;
	}
	
	
	/*Removes item at the head of the queue and returns it*/
	public Node<Item> pop() {
		Node<Item> temp = this.head;
		
		this.head = this.head.next;
		
		if (this.head != null) { 
			this.head.prev = null;
		}
		
		return temp;
	}
	
	
	/*Inserts an item into the queue with the given priority*/
	public void insert(Item value, int prio) {
		Node<Item> newNode = new Node<Item>(value, prio);
		
		if (this.head == null) {	//Update head if no items in queue
			this.head = newNode;
			
			return;
		}
		
		
		Node<Item> prevNode    = null;
		Node<Item> currentNode = this.head;
		
		while (currentNode != null && prio >= currentNode.prio) {
			prevNode = currentNode;
			currentNode = currentNode.next;
		}
		
		if (currentNode == null) {	//Add item to back of queue
			prevNode.next = newNode;
			newNode.prev = prevNode;
			
		} else {	//If not at back of queue then prio < currentNode.prio
			prevNode.next    = newNode;
			newNode.prev     = prevNode;
			
			newNode.next     = currentNode;
			currentNode.prev = newNode;
			
		}
	}
	
	
	/*Inserts an item at the back of the queue, for use as a FIFO queue*/
	public void insert(Item value) {
		Node<Item> newNode = new Node<Item>(value, 0);
		
		if (this.head == null) {
			this.head = newNode;
		}
		
		Node<Item> currentNode = this.head;
		
		while (currentNode.next != null) {
			currentNode = currentNode.next;
		}
		
		currentNode.next = newNode;
		newNode.prev = currentNode;
	}
}
