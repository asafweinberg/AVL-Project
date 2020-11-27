package AVL;
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
	PROMOTE,ROTATE,DOUBLE;
}

public class AVLTree {
	private AVLNode root;
	private int size;
	
	public AVLTree() {
		this.root = new AVLNode();
		this.size = 0;
		int y=0;
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
		return ((AVLNode)r).getInfo();
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
		if (this.empty())
		{
			this.root = new AVLNode(k, i);
			this.size++;
			return 1;
		}
		
		AVLNode parent = (AVLNode)getInsertLocation(this.getRoot(), k);
		if (parent == null) {
			return -1;
		}
		
		boolean isParentLeaf=parent.isLeaf();
		
		AVLNode node = new AVLNode(k, i);
		if (parent.getKey() > k) {
			parent.setLeft(node);
		}
		else {
			parent.setRight(node);
		}
		this.size++;
		
		if (!isParentLeaf) {
			return 1;	//to check if 1 or 0 with GARIBOS
		}
		
		else {
			parent.promote();
			return insertToLeaf(parent);
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
		int balanceActions=1;
		EInsertCase which_case=getCaseOfInsert(parent);
		while (node!=this.root ) {
			switch(which_case) {
				case PROMOTE:
					node.promote();
					break;
				case ROTATE:
					node.rotate();
					balanceActions+=2;   //to check with GARIBI
					break;
				case DOUBLE:
					node.doubleRotate();
					balanceActions+=5; 
					break;
				default:
					return balanceActions;
			}
			node=(AVLNode)node.getParent();
			which_case=getCaseOfInsert(node);
		}		
		return balanceActions;		
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
		return 42;	// to be replaced by student code
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 */
	public String min()
	{
		IAVLNode n = this.root;
		while(n.getLeft().isRealNode()) {
			n = n.getLeft();
		}
		return ((AVLNode)n).getInfo();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max()
	{
		IAVLNode n = this.root;
		while(n.getRight().isRealNode()) {
			n = n.getRight();
		}
		return ((AVLNode)n).getInfo();
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray()
	{
		int[] arr = new int[this.size];
		return arr;
		
		//check if lists are allowed !!!!!!!
		
//		private int[] keysToArray_rec(IAVLNode node, int[] keys) {
//			if (node.isRealNode()) {
//				keysToArray_rec(node.getLeft(), keys);
//			}
//		}
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
		String[] arr = new String[this.size];
		return arr;
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
	public class AVLNode implements IAVLNode{
		private int key;
		private String info;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		private int height;
		private int rank;
		
		// This constructor creates a virtual leaf
		public AVLNode() {
			this.key = -1;
			this.height = -1;
			this.rank = -1;
		}
		
		public void doubleRotate() {
			// TODO Auto-generated method stub
			
		}

		public void rotate() {
			// TODO Auto-generated method stub
			
		}

		public void promote() {
			// TODO Auto-generated method stub
			
		}

		// Constructor to create a new tree-node with two virtual sons
		public AVLNode(int key, String info) {
			this.key = key;
			this.info = info;
			
			this.height = 0;
			this.rank = 0;
			
			this.left = new AVLNode();
			this.left.setParent(this);
			
			this.right = new AVLNode();
			this.right.setParent(this);
		}
		
		public int getKey()
		{
			return this.key; // to be replaced by student code
		}
		public String getInfo() {
			return this.info;
		}
		public String getValue()
		{
			return null; // to be replaced by student code
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node; 
			node.setParent(this);
		}
		public IAVLNode getLeft()
		{
			return this.left; // to be replaced by student code
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
			return this.parent; // to be replaced by student code
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return this.getHeight() != -1; // to be replaced by student code
		}
		public void setHeight(int height)
		{
			return; // to be replaced by student code
		}
		public int getHeight()
		{
			return this.height; // to be replaced by student code
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
		
		public String getText_print() {
			return this.getKey() + " (h:" + this.height + " r:" + this.rank + ")";
		}
	}
}


