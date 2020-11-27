package AVL;

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
	}
	
	public static void insertOneTest(AVLTree tree, int key, int result) throws Exception {
		int actions = tree.insert(key);
		if (actions != result) {
			System.out.println("Number of actions for " + key + " is incorrect - expeceted: " + result + ", result: " + actions);
			throw new Exception();
		}
	}

}
