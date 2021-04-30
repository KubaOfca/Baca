//Jakub Baran - 8

import java.util.Scanner;

public class Source {

    public static Scanner scn = new Scanner(System.in);
    public static String[] songsNames;
    public static String commonPrefix;
    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for (int i = 0; i < numberOfSets; i++) {
            int numberOfSongs = scn.nextInt();
            scn.nextLine(); //zabieg stosowany gdy po wykonaniu funkcji nextInt chcemy wykonwac funkcje nextLine.
                            //nextInt po wykowaniu zostawia kursor w tej samej lini. W zwiazku z tym nextline nic nie wczyta poniewaz pierwszy
                            //znak jaki dostanie to znak nowej lini ktory konczy wykonanie tej funckji. Musimy wiec wczytac ten znak \n i potem
                            //ponownie powtorzyc wykonanie nextline aby poprawnie wczytac dane
            songsNames = scn.nextLine().split(" ");
            commonPrefix = songsNames[0];
            shufflingAndFindingPrefix(0,numberOfSongs-1);
            displayStringArray(songsNames, numberOfSongs);
            System.out.println(commonPrefix);

        }
    }

    public static void shufflingAndFindingPrefix( int start, int end) {
        int half = (int) Math.ceil((start + end + 1.0)/2.0); // 2

        if(half % 2 != 0 && (start + end + 1) > 2){
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

        if((start + end + 1) > 2){
            int half_half = (start+half) / 2;
            int j = 0;
            for (int i = half_half; i < half; i++, j++) {
                swap(i, half + j);
            }
        }

        if((end - start) <= 3) {
            for(int i = start; i < end+1; i++){
                while (songsNames[i].indexOf(commonPrefix) != 0){
                    commonPrefix = commonPrefix.substring(0, commonPrefix.length() - 1);
                    if (commonPrefix.isEmpty()){
                        commonPrefix = "";
                        return;
                    }
                }
            }
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

/*
Testy input
5
1
a1
3
a1 a2 b1
5
a1 a2 a3 b1 b2
6
a1 a2 a3 b1 b2 b3
7
a1 a2 a3 a4 b1 b2 b3

Testy output
a1
a1
a1 b1 a2

a1 b1 a2 b2 a3

a1 b1 a2 b2 a3 b3

a1 b1 a2 b2 a3 b3 a4

 */