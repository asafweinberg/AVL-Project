package AVL;

import java.util.Arrays;
import java.util.Random;

import AVL.AVLTree.AVLNode;
import AVL.AVLTree.IAVLNode;
import printing.TreePrinter;

public class Tests {
	
	public static void main(String[] args) throws Exception {
//		customTest();
//		customTestDelete();
//		deletecheckcaseMax();
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
		
		int[] ins = new int[]{10,2,12,1,11,3,4,5,6,16,15,14,13,19,18,17,7,8,9};
		int[] del = new int[]{10,14,13,19,18,17,7,8,9};

		int[] ins2 = new int[] {10,9,8,7,6,5,4,3,2,1};
		
//		balancingTest(ins, del, 5);
		
//		joinTest();
		splitTest();
//		deletecheckcase1_2();
//		balancingTest(ins, del, 9);
		int []a= {10,9,8,7,6,5};
//		int swaps=ArraySelectionSort.SelectionSort(a);
//		System.out.println(swaps);
//		System.out.println(Arrays.toString(a));
//		seachInsertCheck(ins2,100);
		try_random();
//		try_down_sorted();
	}
	
	public static void try_down_sorted() {
		int i=4;
		int [] array = new int [10000*i];
		for (int k=0 ; k<array.length ;k++) {
			array[k]=array.length-k-1;
		}
		
		
		int valFromArray=ArraySelectionSort.SelectionSort(array);
		int valFromAVL=arrayToAVL(array);
		System.out.println("array: "+valFromArray);
		System.out.println("AVL: "+valFromAVL);

	}
	
	public static void try_random() {
		Random random = new Random();
		int i=4;
		int [] array = new int [10000*i];
		for (int k=0 ; k<array.length ;k++) {
			array[k]=random.nextInt();
		}
		
		
		int valFromArray=ArraySelectionSort.SelectionSort(array);
		int valFromAVL=arrayToAVL(array);
		System.out.println("array: "+valFromArray);
		System.out.println("AVL: "+valFromAVL);

	}
	
	
	public static int arrayToAVL(int [] array) {
		AVLTree tree = new AVLTree();
		int countSearchNum=0;
		for (int i=0 ; i<array.length ;i++) {
			countSearchNum+=tree.insertOtherSearch(array[i],"");
		}
		return countSearchNum;
	}
	
	public static void deletecheckcase1_2() { //
		AVLTree tree = new AVLTree();
		tree.insert(5, "number 4");
		tree.insert(2, "number 8");
		tree.insert(3, "number 5");
		tree.insert(1, "number 22");
		tree.insert(4, "number 1");
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(4);
 		System.out.println("check after "+x);
 		tree.printTree();
	}
	
	public static void deletecheckcase1_8() {
		AVLTree tree = new AVLTree();
		tree.insert(5, "number 4");
		tree.insert(2, "number 8");
		tree.insert(3, "number 5");
		tree.insert(1, "number 22");
		tree.insert(4, "number 1");
		tree.insert(10, "number 1");
		tree.insert(1, "number 1");
		tree.insert(6, "number 1");
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(4);
 		System.out.println("delete 4 check after "+x);
 		tree.printTree();
 		 x=tree.delete(3);
 		System.out.println("delete 3 check after "+x);
 		tree.printTree();
 		 x=tree.delete(1);
 		System.out.println("delete 1 check after "+x);
 		tree.printTree();

 		x=tree.delete(1);
 		System.out.println("delete 1 again check after "+x);
 		tree.printTree();

 		 x=tree.delete(6);
 		System.out.println("delete 6 check after "+x);
 		
 		tree.printTree();
	}
	public static void deletecheckcaseMax() {
		AVLTree tree = new AVLTree();
		tree.insert(5, "number 4");
		tree.insert(2, "number 8");
		tree.insert(3, "number 5");
		tree.insert(1, "number 22");
		tree.insert(4, "number 1");
		tree.insert(10, "number 1");
		tree.insert(1, "number 1");
		tree.insert(6, "number 1");
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(10);
 		System.out.println("delete 10 check after "+x);
 		tree.printTree();
 		
	}
	
	public static void deletecheckcase1_1() {
		AVLTree tree = new AVLTree();
		tree.insert(1, "number 4");
		tree.insert(2, "number 8");
		tree.insert(3, "number 5");
		tree.insert(4, "number 22");
		tree.insert(5, "number 1");
		tree.insert(6, "number 1");
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(1);
 		System.out.println("check after "+x);
 		tree.printTree();
	}
	
	public static void deletecheckcase1_4() {
		AVLTree tree = new AVLTree();
		tree.insert(1, "number 4");
		tree.insert(2, "number 8");
		tree.insert(4, "number 5");
		tree.insert(3, "number 5");
	
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(1);
 		System.out.println("check after "+x);
 		tree.printTree();
	}
	
	public static void deletecheckcase1_5() {
		AVLTree tree = new AVLTree();
		tree.insert(2, "number 4");
		tree.insert(3, "number 8");
		tree.insert(5, "number 5");
		tree.insert(6, "number 5");
		tree.insert(7, "number 5");
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(2);
 		System.out.println("check after "+x);
 		tree.printTree();
	}
	
	public static void deletecheckcase1_3() {
		AVLTree tree = new AVLTree();
		tree.insert(2, "number 4");
		tree.insert(1, "number 8");
		tree.insert(3, "number 5");
		tree.insert(4, "number 5");
	
		System.out.println("check before");
 		tree.printTree();
 		int x=tree.delete(1);
 		System.out.println("check after "+x);
 		tree.printTree();
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
	
	public static void customTestDelete() {
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
	}
	
	public static void insertOneTest(AVLTree tree, int key, int result) throws Exception {
		int actions = tree.insert(key,"0");
		if (actions != result) {
			System.out.println("Number of actions for " + key + " is incorrect - expeceted: " + result + ", result: " + actions);
			throw new Exception();
		}
		isValidTree(tree);
	}
	
	public static void deleteOneTest(AVLTree tree, int key, int result) throws Exception {
		int actions = tree.insert(key,"0");
		if (actions != result) {
			System.out.println("Number of actions for " + key + " is incorrect - expeceted: " + result + ", result: " + actions);
			throw new Exception();
		}
		isValidTree(tree);
	}
	
	public static void seachInsertCheck(int[] keys, int breakOnKey) {
		AVLTree tree = new AVLTree();
		for (int i = 0; i < keys.length; i++) {
			try {
				if ( keys[i] == breakOnKey) {
					boolean MARK_BREAK_POINT_FOR_DEBUG = true;
				}
//				IAVLNode x=tree.getInsertLocation(tree.getRoot(),keys[i]);
//				IAVLNode y=tree.getInsertLocationFinger(tree.getRoot(),keys[i]);
//				if (x!=y) {
//					System.out.println("round "+i+":not the same");
//				}
				int ret=tree.insertOtherSearch(keys[i],"i");
				isValidTree(tree);
				System.out.println("inserted "+keys[i]+"num searched:"+ ret);
				System.out.println("tree looks like:");
				tree.printTree();
			}
			catch (Exception e) {
				System.out.println("failed on Insert(" + keys[i] + ")  -  Error: " + e);
				tree.printTree();
				return;
			}
		}
	}
	
	public static void balancingTest(int[] keys, int[] toDelete, int breakOnKey) {
		AVLTree tree = new AVLTree();
		for (int i = 0; i < keys.length; i++) {
			try {
				if ( keys[i] == breakOnKey) {
					boolean MARK_BREAK_POINT_FOR_DEBUG = true;
				}
				tree.insert(keys[i],"i");
				isValidTree(tree);
			}
			catch (Exception e) {
				System.out.println("failed on Insert(" + keys[i] + ")  -  Error: " + e);
				tree.printTree();
				return;
			}
		}
		
		tree.printTree();
		
		for (int i = 0; i < toDelete.length; i++) {
			try {
				if ( toDelete[i] == breakOnKey) {
					boolean MARK_BREAK_POINT_FOR_DEBUG = true;
					tree.printTree();
				}
				tree.delete(toDelete[i]);
//				System.out.println("this is after "+toDelete[i]);
//				tree.printTree();
				isValidTree(tree);
			}
			catch (Exception e) {
				System.out.println("failed on Delete(" + toDelete[i] + ")  -  Error: " + e);
//				tree.printTree();
				return;
			}
		}
		tree.printTree();
	}
	
	public static void joinTest() throws Exception {
		AVLTree s = createTree(new int[]{10,2,12,1,11});
		AVLTree b = createTree(new int[]{23, 21, 30, 24, 39, 33, 34, 235});
		b.join(new AVLNode(20, "x"), s);
		try {
			isValidTree(b);
		}
		catch (Exception e) {
			System.out.println("join tree is not valid: " + e);
			return;
		}
		
		b.printTree();
	}
	
	public static void splitTest() throws Exception {
		AVLTree s = createTree(new int[]{10,2,12,1,11,4,5,13, 23, 21, 30, 24, 39, 33, 34, 235});
		AVLTree[] res =  s.split(11);
		try {
			isValidTree(res[0]);
		}
		catch (Exception e) {
			System.out.println("left split tree is not valid: " + e);
			return;
		}
		
		try {
			isValidTree(res[1]);
		}
		catch (Exception e) {
			System.out.println("left split tree is not valid: " + e);
			return;
		}
		
		res[0].printTree();
		System.out.println();
		res[1].printTree();
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
	
	public static AVLTree createTree(int[] keys ) {
		AVLTree tree = new AVLTree();
		for (int i = 0; i < keys.length; i++) {
			tree.insert(keys[i],"i");
		}
		return tree;
	}

}
