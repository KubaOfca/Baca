//Jakub Baran - 8
import java.util.Scanner;
class Source {
    public static Scanner scn = new Scanner(System.in);

    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for(int i=0; i< numberOfSets; i++){
            int sizeOfArray = scn.nextInt();
            int[] array = new int[sizeOfArray];
            for(int j=0; j<sizeOfArray; j++){
                array[j] = scn.nextInt();
            }

            int numberOfCommand = scn.nextInt();
            int[] keyToFind = new int[numberOfCommand];
            for(int k=0; k < numberOfCommand; k++){
                keyToFind[k] = scn.nextInt();
            }

        }
    }

    //Pomocnicze Fukncje
    public static void displayArray(int[] array){
        for(int i=0; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

}
