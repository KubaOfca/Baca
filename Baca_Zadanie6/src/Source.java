//Jakub Baran - 8

import java.util.Scanner;

public class Source {

    public static Scanner scn = new Scanner(System.in);

    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for (int i = 0; i < numberOfSets; i++) {
            int numberOfSongs = scn.nextInt();
            scn.nextLine(); // zabieg stosowany gdy po wykonaniu funkcji nextInt chcemy wykonwac funkcje nextLine.
                            //nextInt po wykowaniu zostawia kursor w tej samej lini. W zwiazku z tym nextline nic nie wczyta poniewaz pierwszy
                            //znak jaki dostanie to znak nowej lini ktory konczy wykonanie tej funckji. Musimy wiec wczytac ten znak \n i potem
                            //ponownie powtorzyc wykonanie nextline aby poprawnie wczytac dane
            String[] songsNames = scn.nextLine().split(" ");
            displayStringArray(songsNames, numberOfSongs);

        }
    }

    public static void displayStringArray(String[] array, int size){
        for (int j = 0; j < size; j++) {
            System.out.print(array[j] + " ");
        }
        System.out.println();
    }
}
