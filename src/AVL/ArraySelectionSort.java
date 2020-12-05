package AVL;

public class ArraySelectionSort {
	//func sorts the array and returns number of swaps
	public static int SelectionSort(int [] array) {
		int key;
		int j;
		int count=0;
		for (int i=1 ;i<array.length ; i++) {
			key=array[i];
			j=i-1;
			while (j>=0 && array[j] >key) {
				array[j+1]=array[j];
				j--;
				count++;
			}
			array[j+1]=key;
		}
		return count;
	}
}
