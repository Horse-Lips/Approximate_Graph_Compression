package graphUtils;


public class SimpleAVLTree<Item> {
	public static class Node<Item> {
		/** Key of this node */
		private double  key;
		
		/** Value stored in this node */
		private Item value;
		
		/** Height of this node */
		private int height;
		
		/** Left child of this node */
		private Node<Item> left;
		
		/** Right child of this node */
		private Node<Item> right;
		
		
		/** Parent of this node */
		private Node<Item> parent;
		
		
		public Node(double key, Item value) {
			this.key   = key;
			this.value = value;
			
			this.height = 1;
			
			this.left   = null;
			this.right  = null;
		}
		
		
		/**
		 * Return the key of this node
		 * @return the key of this node
		 */
		public double getKey() {
			return this.key;
		}
		
		
		/**
		 * Return the value of this node
		 * @return the value of this node
		 */
		public Item getValue() {
			return this.value;
		}
	}
	
	
	/** Root of tree*/
	private Node<Item> root;
	
	
	public SimpleAVLTree() {
		this.root = null;
	}
	
	
	/**
	 * Get the height of a given node, return -1 if node is null
	 * @param n, a node, preferrably one in the tree
	 * @return an int, the height of the node
	 */
	private int getHeight(Node<Item> n) {
		return (n != null) ? n.height : 0;
	}
	
	
	/**
	 * Update the height of a node in the tree
	 * @param n, a node in the tree
	 */
	private void setHeight(Node<Item> n) {
		n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
	}
	
	
	/**
	 * Return the balance of the children of a node in the tree
	 * @param n, a node in the tree
	 * @return an int, the difference in heights between the children of the node
	 */
	@SuppressWarnings("null")
	private int getBalance(Node<Item> n) {
		return (n != null) ? 0 : getHeight(n.left) - getHeight(n.right);
	}
	
	
	/**
	 * Rotates a node in the tree so that its left child becomes 
	 * its parent and it becomes the right child of its new parent
	 * @param subRoot, the root of the subtree to rotate right
	 */
	private Node<Item> rightRotation(Node<Item> subRoot) {
		Node<Item> leftChild = subRoot.left;
		
		leftChild.parent = subRoot.parent;
		subRoot.left     = leftChild.right;
		
		if (subRoot.left != null) {
			subRoot.left.parent = subRoot;
		}
		
		leftChild.right = subRoot;
		subRoot.parent  = leftChild;
		
		if (leftChild.parent != null) {
			if (leftChild.parent.right == subRoot) {
				leftChild.parent.right = leftChild;
				
			} else {
				leftChild.parent.left = leftChild;
				
			}
		}
		
		setHeight(subRoot);
		setHeight(leftChild);
		
		return leftChild;
	}
	
	
	/**
	 * Rotates a node in the tree so that its right child becomes
	 * its parent and it becomes the left child of its new parent
	 * @param subRoot
	 */
	private Node<Item> leftRotation(Node<Item> subRoot) {
		Node<Item> rightChild = subRoot.right;
		
		rightChild.parent = subRoot.parent;
		subRoot.right     = rightChild.left;
		
		if (subRoot.right != null) {
			subRoot.right.parent = subRoot;
		}
		
		rightChild.left = subRoot;
		subRoot.parent  = rightChild;
		
		if (rightChild.parent != null) {
			if (rightChild.parent.right == subRoot) {
				rightChild.parent.right = rightChild;
				
			} else {
				rightChild.parent.left = rightChild;
				
			}
		}
		
		setHeight(subRoot);
		setHeight(rightChild);
		
		return rightChild;
	}
	
	
	/**
	 * Rebalance a node so that the difference in heights between
	 * its children is no more than 1 and no less than -1
	 * @param n, the node to rebalance
	 */
	private void rebalance(Node<Item> n) {
		setHeight(n);
		
		int balance = getBalance(n);
		
		if (balance > 1) {
			if (getHeight(n.right.right) >= getHeight(n.right.left)) {
				n = leftRotation(n);
				
			} else {
				n.right = rightRotation(n.right);
				n = leftRotation(n);
				
			}
			
		} else if (balance < -1) {
			if (getHeight(n.left.left) >= getHeight(n.left.right)) {
				n = rightRotation(n);
				
			} else {
				n.left = leftRotation(n.left);
				n = rightRotation(n);
				
			}
			
		}
		
		if (n.parent != null) {
			rebalance(n.parent);
			
		} else {
			this.root = n;
			
		}
	}
	
	
	/**
	 * Insert a key-value pair into the tree
	 * @param key, the key of the node
	 * @param value, the value of the node
	 */
	public void insert(double key, Item value) {
		Node<Item> newNode = new Node<Item>(key, value);
		
		if (this.root == null) {
			this.root = newNode;
			
			return;
		}
		
		
		Node<Item> currentNode = this.root;
		Node<Item> trailingParent = null;
		
		while (currentNode != null) {
			trailingParent = currentNode;
			
			if (key < currentNode.key) {
				currentNode = currentNode.left;
				
			} else {
				currentNode = currentNode.right;
				
			}
			
			setHeight(trailingParent);
		}
		
		if (key < trailingParent.key) {
			trailingParent.left = newNode;
			
		} else {
			trailingParent.right = newNode;
			
		}
		
		newNode.parent = trailingParent;
		rebalance(trailingParent);
	}
	
	
	/**
	 * Return node with smallest key in the tree
	 * @return node with smallest key in the tree
	 */
	public Node<Item> popMin() {
		if (this.root == null) { return null; }
		
		Node<Item> current  = this.root;
		Node<Item> trailing = null;
		
		while (current.left != null) {
			trailing = current;
			current = current.left;
		}
		
		if (trailing != null) {
			trailing.left = current.right;
			rebalance(trailing);
		
		} else {
			this.root = this.root.right;
			
		}
		
		return current;
	}
	
	
	/**
	 * Return node with the largest key in the tree
	 * @return node with largest value in the tree
	 */
	public Node<Item> popMax() {
		if (this.root == null) { return null; }
		
		Node<Item> current  = this.root;
		Node<Item> trailing = null;
		
		while (current.right != null) {
			trailing = current;
			current = current.right;
		}
		
		if (trailing != null) {
			trailing.right = current.left;
			rebalance(trailing);
			
		} else {
			this.root = this.root.left;
			
		}
		
		return current;
	}
	
}











