package AVL;

public class Tests {

	public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		tree.insert(4, "number 4");
		tree.insert(8, "number 8");
		tree.insert(5, "number 5");
		tree.insert(22, "number 22");
		tree.insert(1, "number 1");
 		tree.printTree();
//		System.out.println(tree.search(5));
//		System.out.println(tree.search(4));
		System.out.println(tree.min());
		tree.insert(13, "number 13");
//		System.out.println(tree.min());
		System.out.println(tree.max());
	}

}
