package AVL;

import java.util.Arrays;

import AVL.AVLTree.AVLNode;
import AVL.AVLTree.IAVLNode;
import printing.TreePrinter;

public class Tests {

	public static void main(String[] args) throws Exception {
		customTest();
		
//		AVLTree tree = new AVLTree();
//		tree.insert(4, "number 4");
//		tree.insert(8, "number 8");
//		tree.insert(5, "number 5");
//		tree.insert(22, "number 22");
//		tree.insert(1, "number 1");
// 		tree.printTree();
//		System.out.println(tree.search(5));
//		System.out.println(tree.search(4));
//		System.out.println(tree.min());
//		tree.insert(13, "number 13");
//		System.out.println(tree.min());
//		System.out.println(tree.max());
	}
	
	public static void customTest() {
		AVLTree tree = new AVLTree();
		try {
			insertOneTest(tree, 150, 1);
			insertOneTest(tree, 70, 2);
			insertOneTest(tree, 80, 7);
			insertOneTest(tree, 120, 3);
			insertOneTest(tree, 200, 1);
			insertOneTest(tree, 100, 8);
			insertOneTest(tree, 250, 4);
			tree.printTree();
			System.out.println("success");
		}
		catch (Exception e) {
			tree.printTree();
			System.out.print(e);
		}
		System.out.println(Arrays.toString(tree.keysToArray()));
		System.out.println(Arrays.toString(tree.infoToArray()));
		System.out.println("success");
	}
	
	public static void insertOneTest(AVLTree tree, int key, int result) throws Exception {
		int actions = tree.insert(key);
		if (actions != result) {
			System.out.println("Number of actions for " + key + " is incorrect - expeceted: " + result + ", result: " + actions);
			throw new Exception();
		}
		isValidTree(tree);
	}
	
	public static void isValidTree(AVLTree tree) throws Exception {
		if (tree.empty()) {
			System.out.println("tree is empty");
			return;
		}
		IAVLNode r = tree.getRoot();
		if (r.getParent() != null) {
			throw new Exception("root has a parent with key: " + r.getParent().getKey());
		}
		checkHeightDiffs_Parents(r);
	}
	
	public static int checkHeightDiffs_Parents(IAVLNode node) throws Exception {
		if (!node.isRealNode()) {
			return -1;
		}
		int rh = -1;
		int lh = -1;
		if (node.getLeft().isRealNode()) {
			IAVLNode l = node.getLeft();
			if (l.getParent() != node) {
				if (l.getParent() == null)
					throw new Exception("node " + l.getKey() + " has a null parent");
				else
					throw new Exception("node " + l.getKey() + " is left son of: " + node.getKey() + " but his parent is: " + l.getParent().getKey());
			}
			lh = checkHeightDiffs_Parents(l);
		}
		if (node.getRight().isRealNode()) {
			IAVLNode r = node.getRight();
			if (r.getParent() != node) {
				if (r.getParent() == null)
					throw new Exception("node " + r.getKey() + " has a null parent");
				else
					throw new Exception("node " + r.getKey() + " is left son of: " + node.getKey() + " but his parent is: " + r.getParent().getKey());
			}
			rh = checkHeightDiffs_Parents(r);
		}
		int rankDiff = Math.abs(rh - lh);
		if (rankDiff < 0 || rankDiff > 1) {
			TreePrinter.print((AVLNode)node);
			throw new Exception("node " + node.getKey() + " has a rank difference of: " + rankDiff);
		}
		int h = Math.max(rh, lh) + 1;
		if (h - rh > 2 || h-rh < 1) {
			TreePrinter.print((AVLNode)node);
			throw new Exception("node height is: " + h + " right-son height is:"+rh);
		}
		if ( h- lh > 2 || h- lh < 1 ) {
			TreePrinter.print((AVLNode)node);
			throw new Exception("node height is: " + h + " left-son height is:"+lh);
		}
		if (h != node.getHeight()) {
			TreePrinter.print((AVLNode)node);
			throw new Exception("calculated height of node " + node.getKey() + " is: " + h + " but in the tree is:" + node.getHeight());
		}
		return h;
	}

}
