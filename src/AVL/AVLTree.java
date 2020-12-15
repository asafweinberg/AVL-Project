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
	PROMOTE,ROTATE,DOUBLE,JOIN_ROTATE, OK, ERROR;
}

enum EDeleteCase {
	DEMOTE, ROTATE_DEMOTE, ROTATE_DOUBLE_DEMOTE, DOUBLE, OK, ERROR;
}



public class AVLTree {
	//property: the node that represents the tree's root
	private AVLNode root;
	//property: the size of the tree
	private int size;
	//property: the node that contains the minimum key in the tree
	private IAVLNode min;
	//property: the node that contains the maximum key in the tree
	private IAVLNode max;
	
	
	
	private static int searchNumber=0;
	
	/**
	 * private AVLTree()
	 *
	 * creates an empty tree.
	 *
	 */
	public AVLTree() {
		setNullTree();
	}
	
	/**
	 * private AVLTree(IAVLNode root)
	 *
	 * gets IAVLNode and creates a tree with the node as the root.
	 *
	 */
	private AVLTree(IAVLNode root) {
		if (root.isRealNode()) {
			this.root = (AVLNode)root;
			this.size = this.root.getSize();
			this.max = this.getMaxNode();
			this.min = this.getMinNode();
		}
		else {
			setNullTree();
		}
	}
	
	
	/**
	 * private void setNullTree()
	 *
	 * handles an empty tree construction.
	 *
	 */
	private void setNullTree() {
		this.root = new AVLNode();
		this.size = 0;
		this.max = new AVLNode(Integer.MIN_VALUE, "");
		this.min = new AVLNode(Integer.MAX_VALUE, "");
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
	
	/**
	 * private String search_rec(IAVLNode r, int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * this function is a recursive helper function for search(int k).
	 */
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
		AVLNode nodeToInsert = new AVLNode(k, i); // creating a node to insert from the given variables
		
		//handling min\ max changes according to the given key
		if (k < this.min.getKey())
			this.min = nodeToInsert;
		if (k > this.max.getKey())
			this.max = nodeToInsert;
		
		// if the tree is empty, just making the node we created his root and return 1- the number of operations we made
		if (this.empty())
		{
			this.root = nodeToInsert;
			this.size++;
			return 1;
		}
		
		// finding the node to insert the new key as his son
		AVLNode parent = (AVLNode)getInsertLocation(this.getRoot(), k);
		if (parent == null) {
			return -1;
		}
		
		boolean isParentLeaf = parent.isLeaf();
		
		// insert on the correct son of the parent that was found
		if (parent.getKey() > k) {
			parent.setLeft(nodeToInsert);
		}
		else {
			parent.setRight(nodeToInsert);
		}
		
		this.size++; // after insertion adding 1 to the tree size
		
		// if the parent was unary node then insert does not require any rebalance action.
		// if the parent was a leaf the we perform the rebalance algorithm.
		if (!isParentLeaf) {
			return 1;	
		}
		else {
			return this.insertRebalance(parent);
		}
		
	}
	
	/**
	 * public int insert(int k)
	 *
	 * calls insert(int k, String i) with a constant i parameter by k.
	 */
	public int insert(int k) {
		return this.insert(k, "number "+k);
	}
	
	/**
	 * private IAVLNode getInsertLocation(IAVLNode r, int k)
	 *
	 * The method gets the root and the key needed to be inserted into the tree.
	 * returns the node which will be his parent.
	 */
	private IAVLNode getInsertLocation(IAVLNode r, int k) {
		if (!r.isRealNode()) {
			return r.getParent();
		}
		if (r.getKey() > k) {
			this.searchNumber++;
			return getInsertLocation(r.getLeft(), k);
		}
		if (r.getKey() < k) {
			this.searchNumber++;
			return getInsertLocation(r.getRight(), k);
		}
		return null;
	}
	
	
	public int insertOtherSearch(int k, String i) {
		AVLNode nodeToInsert = new AVLNode(k, i);
		
		if (k < this.min.getKey())
			this.min = nodeToInsert;
		if (k > this.max.getKey())
			this.max = nodeToInsert;
		
		if (this.empty())
		{
			this.root = nodeToInsert;
			this.size++;
			return 0; //check
		}
		
		AVLNode parent = (AVLNode)getInsertLocationFinger(this.getRoot(), k);
		int searchLen=this.searchNumber;
		
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
			return searchLen;	//to check if 1 or 0 with GARIBOS
		}
		else {
			this.insertRebalance(parent);
			return searchLen;
		}
		
	}
	
	private IAVLNode getInsertLocationFinger(IAVLNode r, int k) {
		AVLNode node= this.getMaxNode();
		this.searchNumber=0;
		
		while(node!=this.root && k<node.getParent().getKey() ) {  //climb the tree
			node=(AVLNode) node.getParent();
			this.searchNumber++;
		}
		if (node==this.root) { //we climbed to root
			this.searchNumber++;
			return getInsertLocation(node.getLeft(),k);
		}
		return getInsertLocation(node,k);
		
	}
	

	/**
	 * private int insertRebalance(AVLNode parent)
	 *
	 * The method gets a node from which the tree - rebalancing actions should begin
	 * it goes upto the root if needed and perform the required action according to the possible cases
	 */
	private int insertRebalance(AVLNode parent) {
		AVLNode node = parent;
		IAVLNode rootAfterRotate = null;
		int balanceActions = 1;
		EInsertCase which_case = null;
		while (node != null ) { // breaks after reaching the root and checking for the right case
			which_case = getCaseOfInsert(node); // get rebalance case
			switch(which_case) {
				// perform promotion
				case PROMOTE:
					node.promote(); // promoting the nodes height
					node.updateSize(); // updating the node's subtree size
					balanceActions++; // adding one rebalance action to the total actions
					break;
				// perform rotation
				case ROTATE:
					rootAfterRotate = node.rotateInsert(); //handle insert-rotation case
					balanceActions+=2; // adding 2 rebalance actions to the total actions
					// if we reached the root and performed a rotation then we need to change the root's ...
					// property accordingly to the new root after the rotation
					if (this.root == node)
						this.setRoot(rootAfterRotate);
					break;
				// perform rotation
				case DOUBLE:
					rootAfterRotate = node.doubleRotateInsert(); //handle insert-double-rotation case
					// if we reached the root and performed a rotation then we need to change the root's ...
					// property accordingly to the new root after the rotation
					if (this.root == node)
						this.setRoot(rootAfterRotate);
					balanceActions+=5; // adding 5 rebalance actions to the total actions
					break;
				// perform rotation after joining trees (can't happen after insertion)
				case JOIN_ROTATE:
					rootAfterRotate = node.joinRotate(); //handle join-rotation case
					// if we reached the root and performed a rotation then we need to change the root's ...
					// property accordingly to the new root after the rotation
					if (this.root == node)
						this.setRoot(rootAfterRotate);
					balanceActions+=2; // adding 2 rebalance actions to the total actions
					node = (AVLNode) rootAfterRotate;
					break;
				// A case for testing, in a correctly built tree we can't get to another case
				case ERROR:
					System.out.println("not working - case Insert not found :(");
				// default is OK
				default:
					this.updateSizeFrom(node); // updating the nodes's size property all the way to the root
					return balanceActions; // returning the number of balance actions performed.
			}
			node = (AVLNode)node.getParent(); // moving up to the parent and continue rebalncing the tree
		}
		return balanceActions;		
	}
	
	/**
	 * private EInsertCase getCaseOfInsert(AVLNode node)
	 *
	 * gets a node and checking his rank-differences.
	 * returning the rebalance action needed in order to rebalnce the tree according to his rank-defferences.
	 */
	private EInsertCase getCaseOfInsert(AVLNode node) {
		// get both rank-defferences 
		int rightDifference = ((AVLNode)node.getRight()).getRankDifference();
		int leftDifference = ((AVLNode)node.getLeft()).getRankDifference();
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
					int lDiff = ((AVLNode)l.getLeft()).getRankDifference();
					int rDiff = ((AVLNode)l.getRight()).getRankDifference();
					if(lDiff == 1 && rDiff == 2) {
						return EInsertCase.ROTATE;
					}
					if (lDiff == 2 && rDiff == 1) {
						return EInsertCase.DOUBLE;
					}
					if (lDiff == 1 && rDiff == 1) {
						return EInsertCase.JOIN_ROTATE;
					}
					return EInsertCase.ERROR;
				}
				if (rightDifference == 0) {
					AVLNode r = (AVLNode)node.getRight();
					int lDiff = ((AVLNode)r.getLeft()).getRankDifference();
					int rDiff = ((AVLNode)r.getRight()).getRankDifference();
					if(rDiff == 1 && lDiff == 2) {
						return EInsertCase.ROTATE;
					}
					if(rDiff == 2 && lDiff == 1) {
						return EInsertCase.DOUBLE;
					}
					if(rDiff == 1 && lDiff == 1) {
						return EInsertCase.JOIN_ROTATE;
					}
					return EInsertCase.ERROR;
				}
				else {
					return EInsertCase.ERROR;
				}
			}
		}
		return EInsertCase.ERROR;
	}
	
	/**
	 * private void updateSizeFrom(AVLNode node)
	 *
	 * update nodes's size property up to the root of the tree.
	 */
	private void updateSizeFrom(AVLNode node){
		while (node != null ) {
			node.updateSize();
			node = (AVLNode)node.getParent();
		}
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
		
		AVLNode replacement=null;
		if(nodeToDelete.isUnaryNode() ) {  
			replacement = (AVLNode) this.getNodeToReplace(nodeToDelete);
			this.replace(nodeToDelete,replacement);
			nodeToDelete=replacement;
		}
		
		AVLNode parent = (AVLNode)nodeToDelete.getParent();
//		boolean deletingLeaf = nodeToDelete.isLeaf();
		
		this.deleteNode(nodeToDelete);
//		System.out.println("after one delete:");
//		this.printTree();
		int balanceActions;
		
//		if(deletingLeaf) {
			balanceActions = this.deleteRebalance(parent);
//		}
//		else {
//			balanceActions = this.deleteRebalance(parent);
//		}
		
		if (k == this.min.getKey()) {
			this.min = this.getMinNode();
		}
		if (k == this.max.getKey()) {
			this.max = this.getMaxNode();
		}
		this.size--;
		return balanceActions;
	}
	
	private int deleteRebalance(AVLNode parent) {
		AVLNode node = parent;
		IAVLNode rootAfterRotate = null;
		int balanceActions = 1;
		while (node != null ) {
			EDeleteCase which_case = getCaseOfDelete(node);
			switch(which_case) {
				case DEMOTE:
					node.demote();
					balanceActions++;
					break;
				case ROTATE_DEMOTE:		
					rootAfterRotate= node.rotateDemote();
					balanceActions+=3;
					if (this.root == node)
						this.setRoot(rootAfterRotate);
					node = (AVLNode) rootAfterRotate; //not sure
					break;
				case ROTATE_DOUBLE_DEMOTE:	//CASE 3 after it the rank differences are 1 1 - we need to go one more parent up
					rootAfterRotate= node.rotateDoubleDemote();
					balanceActions+=3;
					if (this.root == node)
						this.setRoot(rootAfterRotate);
					node = (AVLNode) rootAfterRotate; //not sure
					break;
				case DOUBLE:
					rootAfterRotate= node.doubleRotateDelete();
					balanceActions+=6;
					if (this.root == node)
						this.setRoot(rootAfterRotate);
					node = (AVLNode) rootAfterRotate; //not sure
					break;
				case ERROR:
					System.out.println("not working - case Delete not found :(");
					break;
				default:
					return balanceActions;	//OK
			}
			node = (AVLNode)node.getParent();
		}		
		return balanceActions;		
	}
	
	private EDeleteCase getCaseOfDelete(AVLNode node) {
		int rightDifference = ((AVLNode)node.getRight()).getRankDifference();
		int leftDifference = ((AVLNode)node.getLeft()).getRankDifference();
		if (rightDifference == 2 || leftDifference == 2) {
			if (rightDifference - leftDifference == 0) {
				return EDeleteCase.DEMOTE;			//CASE 1 - 2 2
			}
			else {
				return EDeleteCase.OK;				//CASE 1 - 1 2
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
			

			int rankDiff = ((AVLNode)son.getRight()).getRankDifference() - ((AVLNode)son.getLeft()).getRankDifference();
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
		else if (rightDifference == 1 && leftDifference == 1) {
			return EDeleteCase.OK;
		}
		return EDeleteCase.ERROR;
	}

	//return a node with key k to delete (assumption is that k exist)
 	private IAVLNode getDeleteLocation(int k) {
 		
 			IAVLNode iteratorNode=(this.getRoot());
 			while(iteratorNode.isRealNode()) {
 				if (iteratorNode.getKey() == k)
 					return iteratorNode;
 				if (iteratorNode.getKey() > k) {
 					iteratorNode=iteratorNode.getLeft();
 					continue;
 	 			}
 				if (iteratorNode.getKey() < k) {
 					iteratorNode=iteratorNode.getRight();
 					continue;
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
 		int delKey=deleted.getKey();
 		String delInfo=deleted.getValue();
 		deleted.setKey(replacement.getKey());
 		deleted.setInfo(replacement.getValue());
 		replacement.setKey(delKey);
 		replacement.setInfo(delInfo);
 	}
 	
//		
//	private void replace(AVLNode deleted, AVLNode replacement) { //returns the one which needs to be deleted, in the original replacement place with 
//		int heightDeleted =deleted.getHeight();
//		int heightRep=replacement.getHeight();
//		AVLNode repRight;
//		AVLNode repParent;
//		AVLNode repLeft;
//				
//		replacement.setHeight(heightDeleted);
//		deleted.setHeight(heightRep);
//		
//		if(replacement==deleted.getRight()) { //close case 1
//			
//			AVLNode deletedOtherSon=(AVLNode) deleted.getLeft();
//			repRight = (AVLNode) replacement.getRight();
//			repLeft = (AVLNode) replacement.getLeft();
//			
//			replacement.setParent(deleted.getParent());
//			replacement.setRightForRep(deleted); //new
//			replacement.setLeftForRep(deleted.getLeft());
//			
//			deleted.setRightForRep(repRight);
//			deleted.setLeftForRep(repLeft);
//			deleted.setParent(replacement);	//new
////			if (deletedOtherSon.isRealNode())
//				deletedOtherSon.setParent(replacement);
////			if (repRight.isRealNode())
//				repRight.setParent(deleted);
////			if (repLeft.isRealNode())
//				repLeft.setParent(deleted);
//			System.out.println("inside replace this is how it looks");
//			this.printTree();
//			return;
//		}
//		
//		if(deleted==replacement.getLeft()) {
//			repRight = (AVLNode) replacement.getRight();
//			repParent = (AVLNode) replacement.getParent();
//			AVLNode repOtherSon=(AVLNode) replacement.getRight();
//			AVLNode delRight= (AVLNode) deleted.getRight();
//			AVLNode delLeft= (AVLNode) deleted.getLeft();
//			
//			replacement.setParent(deleted); //new
//			replacement.setRightForRep(deleted.getRight()); 
//			replacement.setLeftForRep(deleted.getLeft());
//			
//			deleted.setRightForRep(repRight);
//			deleted.setLeftForRep(replacement); //new
//			deleted.setParent(repParent);	
//			
//			
//			repOtherSon.setParent(deleted);
//			delRight.setParent(replacement);
//			delLeft.setParent(replacement);
//			System.out.println("inside replace this is how it looks");
//			this.printTree();
//			return;
//		}
//		
//		//rep parameters
//		repParent = (AVLNode) replacement.getParent();
//		repRight = (AVLNode) replacement.getRight();
//		repLeft = (AVLNode) replacement.getLeft();
//		
//		replacement.setParent(deleted.getParent());
//		replacement.setRightForRep(deleted.getRight());
//		replacement.setLeftForRep(deleted.getLeft());
//		
//		deleted.setRightForRep(repRight);
//		deleted.setLeftForRep(repLeft);
//		deleted.setParent(repParent);
//		System.out.println("inside replace this is how it looks");
//		this.printTree();	
//	}
	
	private void deleteNode(AVLNode nodeToDelete) { //delete when it is not a binary node!
		if(nodeToDelete.isUnaryNode() ) { // if it has 2 children
			System.out.println("error it is a binary node that you try to delete");
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
	
	/**
	 * public AVLNode getMinNode()
	 *
	 * Returns the node with the lowest key in the tree
	 */
	private AVLNode getMinNode() {
		return minNodeFromNode(this.root);
	}
	
	/**
	 * public AVLNode minNodeFromNode()
	 *
	 * Returns the node with the lowest key in the sub-tree of the given node
	 */
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
	
	/**
	 * public AVLNode getMaxNode()
	 *
	 * Returns the node with the highest key in the tree
	 */
	private AVLNode getMaxNode() {
		IAVLNode n = this.root;
		while(n.getRight().isRealNode()) {
			n = n.getRight();
		}
		return (AVLNode)n;
	}
	
	private AVLNode SuccessorOf(IAVLNode node) {
		
		if (node.getRight().isRealNode()) {
			return this.minNodeFromNode(node.getRight());
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
	
	/**
	 * public IAVLNode[] getSortedNodesArray()
	 *
	 * Returns a sorted array of all the nodes in the tree,
	 * or an empty array if the tree is empty.
	 */
	private IAVLNode[] getSortedNodesArray() {
		IAVLNode[] nodesArray = new IAVLNode[this.size];
		Stack<IAVLNode> s = new Stack<IAVLNode>();
		this.getSortedNodesArray_rec(this.root, s); // get a stack containing nodes by their keys, highest to lowest
		// moving the nodes from the stack to the array, opposite to the order of their insertion.
		for (int i = nodesArray.length - 1; i >= 0; i--) {
			nodesArray[i] = s.pop();
		}
		return nodesArray;
	}
	
	/**
	 * private void getSortedNodesArray_rec(IAVLNode node, Stack<IAVLNode> s)
	 *
	 * Returns a stack containing the nodes of the tree which "node" is his root.
	 * adding the elements to the stack is by in-order for their sorted order.
	 */
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
	 * private void setRoot(IAVLNode r)
	 *
	 * setting a new root to the tree
	 */
	private void setRoot(IAVLNode r) {
		this.root = (AVLNode)r;
		r.setParent(null); // setting the root's parent to null
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
		AVLTree[] result = new AVLTree[2];
		IAVLNode node = this.getDeleteLocation(x);
		result[0] = new AVLTree(node.getLeft());
		result[1] = new AVLTree(node.getRight());
		
		int maxJoin = 0; int sumJoin = 0; int countJoin = 0;
		int joinComp;
		
		boolean isRightSon = ((AVLNode)node).isRightSon();
		node = node.getParent();
		while(node != null) {
			AVLNode connectionNode = new AVLNode(node.getKey(), node.getValue());
			if (isRightSon) {
				joinComp = result[0].join(connectionNode, new AVLTree(node.getLeft()));
			}
			else {
				joinComp = result[1].join(connectionNode, new AVLTree(node.getRight()));
			}
			
			maxJoin = Math.max(maxJoin, joinComp);
			sumJoin += joinComp;
			countJoin++;
			
			isRightSon = ((AVLNode)node).isRightSon();
			node = node.getParent();
		}
		
		return result;
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
		AVLTree bigger, smaller;
		int complexity;
		if (this.getTreeHeight() > t.getTreeHeight()) {
			bigger = this;
			smaller = t;
			complexity = this.getTreeHeight() - t.getTreeHeight() + 1;
		}
		else {
				bigger = t;
				smaller = this;
				complexity = t.getTreeHeight() - this.getTreeHeight() + 1;
		}
		
		IAVLNode xParent;
		if(bigger.getRoot().getKey() > x.getKey()) {
			IAVLNode connectionNode = this.getLeftJoiningNode(bigger, smaller.getTreeHeight());
			xParent = connectionNode.getParent();
			
			x.setLeft(smaller.getRoot());
			x.setRight(connectionNode);
			x.setHeight(smaller.getTreeHeight() + 1);
			if(xParent != null) {
				xParent.setLeft(x);
				this.setRoot(bigger.getRoot());
			}
			else {
				this.setRoot(x);
			}
		}
		else {
			IAVLNode connectionNode = this.getRightJoiningNode(bigger, smaller.getTreeHeight());
			xParent = connectionNode.getParent();
			
			x.setRight(smaller.getRoot());
			x.setLeft(connectionNode);
			x.setHeight(smaller.getTreeHeight() + 1);
			if(xParent != null) {
				xParent.setRight(x);
				this.setRoot(bigger.getRoot());
			}
			else {
				this.setRoot(x);
			}
		}
		
		
		this.insertRebalance((AVLNode)xParent);
		this.size = ((AVLNode)this.getRoot()).getSize();
		this.min = this.getMinNode();
		this.max = this.getMaxNode();
		
		return complexity;
	}
	
	
	private int getTreeHeight() {
		if(this.root != null) {
			return this.root.getHeight();
		}
		else {
			return -1;
		}
	}
	
	private IAVLNode getLeftJoiningNode(AVLTree t, int height) {
		IAVLNode node = t.getRoot();
		while(node.getHeight() > height) {
			node = node.getLeft();
		}
		return node;
	}
	
	private IAVLNode getRightJoiningNode(AVLTree t, int height) {
		IAVLNode node = t.getRoot();
		while(node.getHeight() > height) {
			node = node.getRight();
		}
		return node;
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
		private int size;
		
		/** public AVLNode()
		 * empty constructor. creates a virtual leaf
		 */   
		public AVLNode() {
			this.key = -1;
			this.height = -1;
			this.size = 0;
		}

		/** public AVLNode(int key, String info)
		 * creates a new node with two virtual nodes below him.
		 */  
		public AVLNode(int key, String info) {
			this.key = key;
			this.info = info;
			
			this.height = 0;
			this.size = 1;
			
			this.left = new AVLNode();
			this.left.setParent(this);
			
			this.right = new AVLNode();
			this.right.setParent(this);
		}
		
		/** public int getKey()
		 * returns the node's key.
		 */
		public int getKey()
		{
			return this.key;
		}
		
		/** public void setKey(int key)
		 * sets the node's key to the given value
		 */
		public void setKey(int key)
		{
			this.key = key; 
		}
		
		/** public int getValue()
		 * returns the node's value.
		 */
		public String getValue()
		{
			return this.info;
		}
		
		/** public void setKey(int key)
		 * sets the node's key to the given value
		 */
		public void setInfo(String val)
		{
			this.info = val; 
		}
		
		/** public void setLeft(IAVLNode node)
		 * sets the node's left son to the given node
		 */
		public void setLeft(IAVLNode node)
		{
			this.left = node; 
			node.setParent(this); // updating the parent
			this.updateSize(); // updating the size
		}
		
		/** public IAVLNode getLeft()
		 * returns the node's left son.
		 */
		public IAVLNode getLeft()
		{
			return this.left;
		}
		
		/** public void setRight(IAVLNode node)
		 * sets the node's right son to the given node
		 */
		public void setRight(IAVLNode node)
		{
			this.right = node;
			node.setParent(this); // updating the parent
			this.updateSize(); // updating the size
		}
		
		/** public IAVLNode getRight()
		 * returns the node's right son.
		 */
		public IAVLNode getRight()
		{
			return this.right; 
		}
		
		/** public void setParent(IAVLNode node)
		 * sets the node's parent to the given node
		 */
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		
		/** public IAVLNode getParent()
		 * returns the node's parent.
		 */
		public IAVLNode getParent()
		{
			return this.parent;
		}
		
		/** public boolean isRealNode()
		 * returns true if the node is a real node, false if the node is a virtual leaf
		 */
		public boolean isRealNode()
		{
			return this.getHeight() != -1;
		}
		
		/** public void setHeight(int height)
		 * sets the node's height to the given value
		 */
		public void setHeight(int height)
		{
			this.height = height;
		}
		
		/** public int getHeight()
		 * returns the node's height.
		 */
		public int getHeight()
		{
			return this.height;
		}
		
		/** public void setSize(int size)
		 * sets the node's size to the given value
		 */
		public void setSize(int size)
		{
			this.size = size;
		}
		
		/** public int getSize()
		 * returns the node's size.
		 */
		public int getSize()
		{
			return this.size;
		}
		
		/** private void updateSize()
		 * calculates the node's size by the size of his sons.
		 */
		private void updateSize() {
			this.setSize(((AVLNode)this.getRight()).getSize() + ((AVLNode)this.getLeft()).getSize() + 1);
		}
		
		/** private void updateSize()
		 * calculates the node's height by the height of his sons.
		 */
		public void updateHeight() {
			this.setHeight(((AVLNode)this.getRight()).getHeight() + ((AVLNode)this.getLeft()).getHeight() + 1);
		}
		
		/** public boolean isLeaf()
		 * returns true if the node has no sons, else, returns false.
		 */
		public boolean isLeaf() {
			return !this.getLeft().isRealNode() && !this.getRight().isRealNode();
		}
		
		/** public boolean isUnaryNode()
		 * returns true if the node has exactly one son, else, returns false.
		 */
		public boolean isUnaryNode() {
			return this.getRight().isRealNode() && this.getLeft().isRealNode();
		}
		
		/** public boolean isRightSon()
		 * returns true if the node is the right son of his parent, else, returns false.
		 */
		public boolean isRightSon() {
			if (this.parent == null) {
				return false;
			}
			return this.parent.getRight().getKey() ==  this.getKey();
		}
		
		/** public boolean isLeftSon()
		 * returns true if the node is the left son of his parent, else, returns false.
		 */
		public boolean isLeftSon() {
			if (this.parent == null) {
				return false;
			}
			return this.parent.getLeft().getKey() ==  this.getKey();
		}

		/** public void promote()
		 * performs a promotion to the node (increasing his height by 1)
		 */
		public void promote() {
			this.height++;
		}
		
		/** public void demote()
		 * performs a demotion to the node (decreasing his height by 1)
		 */
		public void demote() {
			this.height--;
		}
		
		/** public int getRankDifference()
		 * returns the height difference between a node and his parent
		 */
		public int getRankDifference() {
			if (this.getParent() != null) {
				return this.getParent().getHeight() - this.getHeight();
			}
			return -1;
		}
		
		/** private void handleRotationParent(IAVLNode newSon)
		 * After rotations: connects the new sub-tree's root after the rotation to his parent
		 */
		private void handleRotationParent(IAVLNode newSon) {
			if (this.getParent() != null) {
				if(this.isLeftSon()) {
					this.getParent().setLeft(newSon);
				}
				else {
					this.getParent().setRight(newSon);
				}
			}
		}
		
		/** private IAVLNode RotateRight()
		 * performs an action of right rotation with the node a the previous root
		 * returns the new root
		 */
		private IAVLNode RotateRight() {
			IAVLNode newRoot = this.getLeft();
			handleRotationParent(newRoot);
			
			this.setLeft(newRoot.getRight());
			newRoot.setRight(this);
			
			return newRoot;
		}
		
		/** private IAVLNode RotateLeft()
		 * performs an action of left rotation with the node a the previous root
		 * returns the new root
		 */
		private IAVLNode RotateLeft() {
			IAVLNode newRoot = this.getRight();
			handleRotationParent(newRoot);
			
			this.setRight(newRoot.getLeft());
			newRoot.setLeft(this);
			
			return newRoot;
		}
		
		/** private IAVLNode DoubleRotateRight()
		 * performs an action of double rotation to the right with the node a the previous root
		 * returns the new root
		 */
		private IAVLNode DoubleRotateRight() {
			IAVLNode newRoot = this.getLeft().getRight();
			IAVLNode rootPrevLeft = this.getLeft();
			handleRotationParent(newRoot);
			
			this.getLeft().setRight(newRoot.getLeft());
			this.setLeft(newRoot.getRight());
			newRoot.setRight(this);
			newRoot.setLeft(rootPrevLeft);
			
			return newRoot;
		}
		
		/** private IAVLNode DoubleRotateLeft()
		 * performs an action of double rotation to the left with the node a the previous root
		 * returns the new root
		 */
		private IAVLNode DoubleRotateLeft() {
			IAVLNode newRoot = this.getRight().getLeft();
			IAVLNode rootPrevRight = this.getRight();
			handleRotationParent(newRoot);
			
			this.getRight().setLeft(newRoot.getRight());
			this.setRight(newRoot.getLeft());
			newRoot.setLeft(this);
			newRoot.setRight(rootPrevRight);
			
			return newRoot;
		}
		
		/** public IAVLNode rotateInsert()
		 * handles rebalance case of single rotation after Insert
		 * returns the new root
		 */
		public IAVLNode rotateInsert() {
			if(((AVLNode)this.getLeft()).getRankDifference() == 0) {
				this.demote();
				return this.RotateRight();
			}
			else {
				this.demote();
				return this.RotateLeft();
			}
		}
		
		/** public IAVLNode doubleRotateInsert()
		 * handles rebalance case of double rotation after Insert
		 * returns the new root
		 */
		public IAVLNode doubleRotateInsert() {
			if(((AVLNode)this.getLeft()).getRankDifference() == 0) {
				this.demote();
				((AVLNode)this.getLeft()).demote();
				((AVLNode)this.getLeft().getRight()).promote();
				return this.DoubleRotateRight();
			}
			else {
				this.demote();
				((AVLNode)this.getRight()).demote();
				((AVLNode)this.getRight().getLeft()).promote();
				return this.DoubleRotateLeft();
			}
		}
		
		/** public IAVLNode joinRotate()
		 * handles rebalance case of single rotation after join
		 * returns the new root
		 */
		public IAVLNode joinRotate() {
			if(((AVLNode)this.getLeft()).getRankDifference() == 0) {
				((AVLNode)(this.getLeft())).promote();
				return this.RotateRight();
			}
			else {
				((AVLNode)(this.getRight())).promote();
				return this.RotateLeft();
			}
		}
		
		/** public IAVLNode rotateDemote()
		 * handles rebalance case of single rotation and one demotion after Delete
		 * returns the new root
		 */
		public IAVLNode rotateDemote() {
			
			if(((AVLNode)this.getRight()).getRankDifference() == 1) {
				((AVLNode)this.getRight()).promote();
				this.demote();
				return this.RotateLeft();
			}
			else {
				this.demote();
				((AVLNode)this.getLeft()).promote();
				return this.RotateRight();
			}
		}
		
		/** public IAVLNode rotateDoubleDemote()
		 * handles rebalance case of single rotation and two demotions after Delete
		 * returns the new root
		 */
		public IAVLNode	rotateDoubleDemote() {
			
			if(((AVLNode)this.getRight()).getRankDifference() == 1) {
				this.demote();
				this.demote();
				return this.RotateLeft();
			}
			else {
				this.demote();
				this.demote();
				return this.RotateRight();
			}
//			AVLNode temp =(AVLNode) this.rotateDemote();
//			this.demote();
//			return temp;
		}
		
		/** public IAVLNode doubleRotateDelete()
		 * handles rebalance case of double rotation after Delete
		 * returns the new root
		 */
		public IAVLNode doubleRotateDelete() {
			if(((AVLNode)this.getRight()).getRankDifference() == 1) {
				AVLNode y= (AVLNode) this.getRight();
				this.demote();
				this.demote();			//z -2
				y.demote();				 //y -1
				((AVLNode)y.getLeft()).promote(); //a +1
				y.RotateRight();
				return this.RotateLeft();
			}
			else {
				AVLNode y= (AVLNode) this.getLeft();
				this.demote();
				this.demote();			//z -2
				y.demote();				 //y -1
				((AVLNode)y.getRight()).promote(); //a +1
				y.RotateLeft();
				return this.RotateRight();
			}
		}
		
		/** public String getText_print()
		 * returns a string containing the node's data for printing.
		 */
		public String getText_print() {
			return this.getKey() + " (h:" + this.height +" - s:" + this.getSize()+")";
		}
	}
}


