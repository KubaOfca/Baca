//Jakub Baran - 8

import java.util.Scanner;

public class Source {

    public static Scanner scn = new Scanner(System.in);
    public static String[] songsNames;
    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for (int i = 0; i < numberOfSets; i++) {
            int numberOfSongs = scn.nextInt();
            scn.nextLine(); //zabieg stosowany gdy po wykonaniu funkcji nextInt chcemy wykonwac funkcje nextLine.
                            //nextInt po wykowaniu zostawia kursor w tej samej lini. W zwiazku z tym nextline nic nie wczyta poniewaz pierwszy
                            //znak jaki dostanie to znak nowej lini ktory konczy wykonanie tej funckji. Musimy wiec wczytac ten znak \n i potem
                            //ponownie powtorzyc wykonanie nextline aby poprawnie wczytac dane
            songsNames = scn.nextLine().split(" ");

            shufflingAndFindingPrefix(0,numberOfSongs-1);
            displayStringArray(songsNames, numberOfSongs);

        }
    }

    public static void shufflingAndFindingPrefix( int start, int end) {
        int half = (int) Math.ceil((start + end + 1.0)/2.0); // 2

        if(half % 2 != 0){
            if((start + end + 1) % 2 == 0){
                for (int i = half-1; i < end-1; i++){
                    swap(i, i+1);
                }

            }else {
                for (int i = half-1; i < end; i++){
                    swap(i, i+1);
                }
            }
            end -= 2;
            half = (int) Math.ceil((start + end + 1.0)/2.0);
        }

        int half_half = (start+half) / 2;
        int j = 0;
        for (int i = half_half; i < half; i++, j++) {
            swap(i, half + j);
        }

        if((end - start) <= 3) {
            return;
        }

        shufflingAndFindingPrefix(start, half - 1);
        shufflingAndFindingPrefix(half, end);

    }

    public static void swap(int i, int j){
        String tmp = songsNames[i];
        songsNames[i] = songsNames[j];
        songsNames[j] = tmp;
    }

    //Pomocnicze Fukncje----
    public static void displayStringArray(String[] array, int size){
        for (int j = 0; j < size; j++) {
            System.out.print(array[j] + " ");
        }
        System.out.println();
    }
}
