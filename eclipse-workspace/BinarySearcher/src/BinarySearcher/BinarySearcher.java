package BinarySearcher;

public class BinarySearcher {
    public static void main(String[] args) {
        int[] numbers = {324, 768, 1097, 5647, 9876};
        BinarySearcher bSearcher = new BinarySearcher();
        System.out.println(bSearcher.search(numbers, 9876));   
    }
 
    int search(int[] numbers, int searchNumber) {
        int low=0, mid;
        int high = numbers.length-1;
 
        while (low <= high) {
            mid = (low + high) / 2;
            if (searchNumber < numbers[mid] ) high = mid-1;
            else if (searchNumber > numbers[mid]) low = mid + 1;
            else return mid;
        }
        return -1;
    }   
}



