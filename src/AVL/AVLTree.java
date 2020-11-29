package AVL;
import java.util.Stack;

import printing.TreePrinter;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */
enum EInsertCase {
	PROMOTE,ROTATE,DOUBLE, OK, ERROR;
}

enum EDeleteCase {
	DEMOTE, ROTATE_DEMOTE, ROTATE_DOUBLE_DEMOTE, DOUBLE, OK, ERROR;
}

public class AVLTree {
	private AVLNode root;
	private int size;
	private IAVLNode min;
	private IAVLNode max;
	
	public AVLTree() {
		this.root = new AVLNode();
		this.size = 0;
		this.max = new AVLNode(Integer.MIN_VALUE, "");
		this.min = new AVLNode(Integer.MIN_VALUE, "");
	}
	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() {
		return this.size == 0;
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k)
	{
		return this.search_rec(this.getRoot(), k);
	}
	
	private String search_rec(IAVLNode r, int k) {
		if (!r.isRealNode()) {
			return null;
		}
		if (r.getKey() > k) {
			return this.search_rec(r.getLeft(), k);
		}
		if (r.getKey() < k) {
			return this.search_rec(r.getRight(), k);
		}
		return ((AVLNode)r).getValue();
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		AVLNode nodeToInsert = new AVLNode(k, i);
		
		if (k < this.min.getKey())
			this.min = nodeToInsert;
		if (k > this.max.getKey())
			this.max = nodeToInsert;
		
		if (this.empty())
		{
			this.root = nodeToInsert;
			this.size++;
			return 1;
		}
		
		AVLNode parent = (AVLNode)getInsertLocation(this.getRoot(), k);
		if (parent == null) {
			return -1;
		}
		
		boolean isParentLeaf = parent.isLeaf();
		
		if (parent.getKey() > k) {
			parent.setLeft(nodeToInsert);
		}
		else {
			parent.setRight(nodeToInsert);
		}
		
		this.size++;
		
		if (!isParentLeaf) {
			return 1;	//to check if 1 or 0 with GARIBOS
		}
		else {
			parent.promote();
			return this.insertToLeaf(parent);
		}
		
	}
	
	// The method gets the root and the key needed to be inserted into the tree.
	// returns the node which the new node will be under him.
	private IAVLNode getInsertLocation(IAVLNode r, int k) {
		if (!r.isRealNode()) {
			return r.getParent();
		}
		if (r.getKey() > k) {
			return getInsertLocation(r.getLeft(), k);
		}
		if (r.getKey() < k) {
			return getInsertLocation(r.getRight(), k);
		}
		return null;
	}
	
	private int insertToLeaf(AVLNode parent) {
		AVLNode node = parent;
		int balanceActions = 1;
		EInsertCase which_case = getCaseOfInsert(parent);
		while (node!=this.root ) {
			switch(which_case) {
				case PROMOTE:
					node.promote();
					break;
				case ROTATE:
					node.rotateInsert();
					balanceActions+=2;
					break;
				case DOUBLE:
					node.doubleRotateInsert();
					balanceActions+=5; 
					break;
				case ERROR:
					System.out.println("not working - case Insert not found :(");
				default:
					return balanceActions;
			}
			node = (AVLNode)node.getParent();
			which_case = getCaseOfInsert(node);
		}		
		return balanceActions;		
	}
	
	private EInsertCase getCaseOfInsert(AVLNode node) {
		int rightDifference = AVLNode.getRankDifference(node.getRight());
		int leftDifference = AVLNode.getRankDifference(node.getLeft());
		if (leftDifference == 1 && rightDifference == 1) {
			return EInsertCase.OK;
		}
		if (leftDifference == 0 || rightDifference == 0) {
			if (leftDifference == 1 || rightDifference == 1) {
				return EInsertCase.PROMOTE;
			}
			if (leftDifference == 2 || rightDifference == 2) {
				if (leftDifference == 0) {
					AVLNode l = (AVLNode)node.getLeft();
					if(AVLNode.getRankDifference(l.getLeft()) == 1 && AVLNode.getRankDifference(l.getRight()) == 2) {
						return EInsertCase.ROTATE;
					}
					else {
						return EInsertCase.DOUBLE;
					}
				}
				else {
					return EInsertCase.ERROR;
				}
			}
		}
		return EInsertCase.ERROR;
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k)
	{
		if (this.empty()) {
			return 0;
		}
		AVLNode nodeToDelete = (AVLNode)this.getDeleteLocation(k);
		if (nodeToDelete==null)
			return -1;
		
		if(nodeToDelete.isLeaf() ) {
			AVLNode replacement = (AVLNode) this.getNodeToReplace(nodeToDelete);
			this.replace(nodeToDelete,replacement);
		}
		
		AVLNode parent = (AVLNode)nodeToDelete.getParent();
//		boolean deletingLeaf = nodeToDelete.isLeaf();
		
		this.deleteNode(nodeToDelete);
		
		int balanceActions;
		
//		if(deletingLeaf) {
			balanceActions = this.deleteRebalance(parent);
//		}
//		else {
//			balanceActions = this.deleteRebalance(parent);
//		}
		
		if (k == this.min) {
			this.min = this.minNode().getKey();
		}
		if (k == this.max) {
			this.max = this.minNode().getKey();
		}
		
		return balanceActions;
	}
	
	private int deleteRebalance(AVLNode parent) {
		AVLNode node = parent;
		int balanceActions = 1;
		EDeleteCase which_case = getCaseOfDelete(parent);
		while (node != this.root ) {
			switch(which_case) {
				case DEMOTE:
					node.demote();
					balanceActions++;
					break;
				case ROTATE_DEMOTE:
					node.rotateDemote();
					balanceActions+=3;
					break;
				case ROTATE_DOUBLE_DEMOTE:
					node.rotateDoubleDemote();
					balanceActions+=3;
					break;
				case DOUBLE:
					node.doubleRotateDelete();
					balanceActions+=6; 
					break;
				case ERROR:
					System.out.println("not working - case Insert not found :(");
				default:
					return balanceActions;
			}
			node = (AVLNode)node.getParent();
			which_case = getCaseOfDelete(node);
		}		
		return balanceActions;		
	}
	
	private EDeleteCase getCaseOfDelete(AVLNode node) {
		int rightDifference = AVLNode.getRankDifference(node.getRight());
		int leftDifference = AVLNode.getRankDifference(node.getLeft());
		if (rightDifference == 2 || leftDifference == 2) {
			if (rightDifference - leftDifference == 0) {
				return EDeleteCase.DEMOTE;
			}
			else {
				return EDeleteCase.OK;
			}
		}
		if (rightDifference == 3 || leftDifference == 3) {
			AVLNode son = null;
			if (rightDifference == 1) {
				son = (AVLNode)node.getRight();
			}
			else {
				if (leftDifference == 1) {
					son = (AVLNode)node.getLeft();
				}
				else {
					return EDeleteCase.ERROR;
				}
			}
			

			int rankDiff = AVLNode.getRankDifference(node.getRight()) - AVLNode.getRankDifference(node.getLeft());
			if (rankDiff == 0) {
				return EDeleteCase.ROTATE_DEMOTE;
			}
			else {
				if (son.isLeftSon()) {
					rankDiff *=-1;
				}
				
				if (rankDiff == 1) {
					return EDeleteCase.DOUBLE;
				}
				if (rankDiff == -1) {
					return EDeleteCase.ROTATE_DOUBLE_DEMOTE;
				}
				else {
					return EDeleteCase.ERROR;
				}
			}
		}
		else {
			return EDeleteCase.ERROR;
		}
	}

	//return a node with key k to delete (assumption is that k exist)
 	private IAVLNode getDeleteLocation(int k) {
 		
 			IAVLNode iteratorNode=(this.getRoot());
 			while(iteratorNode.isRealNode()) {
 				if (iteratorNode.getKey() == k)
 					return iteratorNode;
 				if (iteratorNode.getKey() > k) {
 					iteratorNode=iteratorNode.getLeft();
 	 			}
 				if (iteratorNode.getKey() < k) {
 					iteratorNode=iteratorNode.getRight();
 	 			}
 			}
 			return null;		
 	}
 	private IAVLNode getNodeToReplace(IAVLNode toDelete) {
 		AVLNode successor= (AVLNode)this.SuccessorOf(toDelete);

 		if (successor==null)
 			return toDelete;
 		return successor;
 	}
 	
	private void replace(AVLNode deleted, AVLNode replacement) { //returns the one which needs to be deleted, in the original replacement place with 
		int heightDeleted =deleted.getHeight();
		int heightRep=replacement.getHeight();
		//rep parameters
		AVLNode repParent = (AVLNode) replacement.getParent();
		AVLNode repRight = (AVLNode) replacement.getRight();
		AVLNode repLeft = (AVLNode) replacement.getLeft();
		
		replacement.setParent(deleted.getParent());
		replacement.setRight(deleted.getRight());
		replacement.setLeft(deleted.getLeft());
		replacement.setHeight(heightDeleted);
		deleted.setHeight(heightRep);
		deleted.setRight(repRight);
		deleted.setLeft(repLeft);
		deleted.setParent(repParent);
	}
	
	private void deleteNode(AVLNode nodeToDelete) { //delete when it is not a binary node!
		if(nodeToDelete.getRight().isRealNode() && nodeToDelete.getLeft().isRealNode() ) { // if it has 2 children
			System.out.println("error it is a binary tree that you try to delete");
			return;
		}
		AVLNode parent=(AVLNode) nodeToDelete.getParent();
		AVLNode rest;
		
		if (nodeToDelete.getRight().isRealNode()) { //has right son
			rest=(AVLNode) nodeToDelete.getRight();
			}
		else {
			rest=(AVLNode) nodeToDelete.getLeft();
		}
					
		if (nodeToDelete.isLeftSon()) {
			parent.setLeft(rest);
		}
		else {
			parent.setRight(rest);
		}
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 */
	public String min()
	{
		return this.min.getValue();
	}
	
	private AVLNode getMinNode() {
		IAVLNode n = this.root;
		while(n.getLeft().isRealNode()) {
			n = n.getLeft();
		}
		return (AVLNode)n;
	}
	
	private AVLNode minNodeFromNode(IAVLNode node) {
		
		while(node.getLeft().isRealNode()) {
			node = node.getLeft();
		}
		return (AVLNode)node;
	}
	
	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max()
	{
		return this.max.getValue();
	}
	
	private AVLNode getMaxNode() {
		IAVLNode n = this.root;
		while(n.getRight().isRealNode()) {
			n = n.getRight();
		}
		return (AVLNode)n;
	}
	
	private AVLNode SuccessorOf(IAVLNode node) {
		
		if (node.getRight().isRealNode()) {
			return this.minNodeFromNode(node);
		}
		IAVLNode parent=node.getParent();
		while (parent!=null && parent.getRight()==node) {
			node=parent;
			parent=node.getParent();
		}
		return (AVLNode)parent;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray()
	{
		int[] keys = new int[this.size];
		IAVLNode[] nodesArray = this.getSortedNodesArray();
		for (int i = 0; i < nodesArray.length; i++) {
			keys[i] = nodesArray[i].getKey();
		}
		
		return keys;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray()
	{
		String[] infos = new String[this.size];
		IAVLNode[] nodesArray = this.getSortedNodesArray();
		for (int i = 0; i < nodesArray.length; i++) {
			infos[i] = ((AVLNode)nodesArray[i]).getValue();
		}
		
		return infos;
	}
	
	private IAVLNode[] getSortedNodesArray() {
		IAVLNode[] nodesArray = new IAVLNode[this.size];
		Stack<IAVLNode> s = new Stack<IAVLNode>();
		this.getSortedNodesArray_rec(this.root, s);
		for (int i = nodesArray.length - 1; i >= 0; i--) {
			nodesArray[i] = s.pop();
		}
		return nodesArray;
	}
	
	private void getSortedNodesArray_rec(IAVLNode node, Stack<IAVLNode> s) {
		if(!node.isRealNode()) {
			return;
		}
		getSortedNodesArray_rec(node.getLeft(), s);
		s.push(node);
		getSortedNodesArray_rec(node.getRight(), s);
	}
	

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none
	 * postcondition: none
	 */
	public int size()
	{
		return this.size;
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none
	 * postcondition: none
	 */
	public IAVLNode getRoot()
	{
		return this.root;
	}
	/**
	 * public string split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. 
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	 * postcondition: none
	 */   
	public AVLTree[] split(int x)
	{
		return null; 
	}
	/**
	 * public join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. 	
	 * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
	 * postcondition: none
	 */   
	public int join(IAVLNode x, AVLTree t)
	{
		return 0; 
	}
	
	public void printTree() {
		TreePrinter.print(this.root);
	}

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in 
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public static class AVLNode implements IAVLNode{
		private int key;
		private String info;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		private int height;
		
		// This constructor creates a virtual leaf
		public AVLNode() {
			this.key = -1;
			this.height = -1;
		}

		

		// Constructor to create a new tree-node with two virtual sons
		public AVLNode(int key, String info) {
			this.key = key;
			this.info = info;
			
			this.height = 0;
			
			this.left = new AVLNode();
			this.left.setParent(this);
			
			this.right = new AVLNode();
			this.right.setParent(this);
		}
		
		public int getKey()
		{
			return this.key;
		}
		public String getValue()
		{
			return this.info;
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node; 
			node.setParent(this);
		}
		public IAVLNode getLeft()
		{
			return this.left;
		}
		public void setRight(IAVLNode node)
		{
			this.right = node;
			node.setParent(this);
		}
		public IAVLNode getRight()
		{
			return this.right; 
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return this.getHeight() != -1;
		}
		public void setHeight(int height)
		{
			this.height = height;
		}
		public int getHeight()
		{
			return this.height;
		}
		
		public boolean isLeaf() {
			return !this.getLeft().isRealNode() && !this.getRight().isRealNode();
		}
		
		public boolean isRightSon() {
			if (this.parent == null) {
				return false;
			}
			return this.parent.getRight().getKey() ==  this.getKey();
		}
		
		public boolean isLeftSon() {
			if (this.parent == null) {
				return false;
			}
			return this.parent.getLeft().getKey() ==  this.getKey();
		}

		public void promote() {
			this.height++;
		}
		
		public void demote() {
			this.height--;
		}
		
		public static int getRankDifference(IAVLNode node) {
			if (node.getParent() != null) {
				return node.getParent().getHeight() - node.getHeight();
			}
			return -1;
		}
		
		private void RotateRight() {
			IAVLNode newRoot = this.getLeft();
			this.setLeft(newRoot.getRight());
			newRoot.setRight(this);
		}
		
		private void RotateLeft() {
			IAVLNode newRoot = this.getRight();
			this.setRight(newRoot.getLeft());
			newRoot.setLeft(this);
		}
		
		private void DoubleRotateRight() {
			IAVLNode newRoot = this.getLeft().getRight();
			IAVLNode rootPrevLeft = this.getLeft();
			
			this.getLeft().setRight(newRoot.getLeft());
			this.setLeft(newRoot.getRight());
			newRoot.setRight(this);
			newRoot.setLeft(rootPrevLeft);
		}
		
		private void DoubleRotateLeft() {
			IAVLNode newRoot = this.getRight().getLeft();
			IAVLNode rootPrevRight = this.getRight();
			
			this.getRight().setLeft(newRoot.getRight());
			this.setRight(newRoot.getLeft());
			newRoot.setLeft(this);
			newRoot.setRight(rootPrevRight);
		}
		
		public void doubleRotateInsert() {
			this.demote();
			if(getRankDifference(this.getLeft()) == 0) {
				((AVLNode)this.getLeft()).demote();
				((AVLNode)this.getLeft().getRight()).promote();
				this.DoubleRotateRight();
			}
			else {
				((AVLNode)this.getRight()).demote();
				((AVLNode)this.getRight().getLeft()).promote();
				this.DoubleRotateLeft();
			}
		}
		
		public void rotateDemote() {
			this.demote();
			if(getRankDifference(this.getRight()) == 1) {
				((AVLNode)this.getRight()).demote();
				this.RotateLeft();
			}
			else {
				((AVLNode)this.getLeft()).demote();
				this.RotateRight();
			}
		}
		
	
		public String getText_print() {
			return this.getKey() + " (h:" + this.height;
		}
	}
}


