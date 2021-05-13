//Jakub Baran - 8
/*Idea dzialania programu
    Program ma za zadanie perfekcyjnie potasowac miedzy soba piosenki z dwoch roznych plyt, wyznaczyc
    najdluzszy mozliwy prefix nazw piosenek, jezeli takowy istnieje. Rozklad piosenek z dwoch plyt
    jest sobie rowny, natomaist jezeli z gory wiemy ze w suma piosenek z pierwszej plyty i z drugiej plyty
    jest liczba nieparzysta to zawsze jedna nadmiarowa piosenka znajduje sie na pierwszej plycie. Termin
    perfekcyjne tasowanie oznacza to ze piosenki sa grance naprzemian raz z jednej plyty raz z drugiej.
 */

import java.util.Scanner;

public class Source {

    public static Scanner scn = new Scanner(System.in);
    public static String[] songsNames;
    public static String commonPrefix;
    public static boolean prefixDoesNotExist;
    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for (int i = 0; i < numberOfSets; i++) {
            int numberOfSongs = scn.nextInt();
            scn.nextLine(); //zabieg stosowany gdy po wykonaniu funkcji nextInt chcemy wykonwac funkcje nextLine.
                            //nextInt po wykowaniu zostawia kursor w tej samej lini. W zwiazku z tym nextline nic nie wczyta poniewaz pierwszy
                            //znak jaki dostanie to znak nowej lini ktory konczy wykonanie tej funckji. Musimy wiec wczytac ten znak \n i potem
                            //ponownie powtorzyc wykonanie nextline aby poprawnie wczytac dane
            songsNames = scn.nextLine().split(" ");
            commonPrefix = songsNames[0]; // przypisanie pierwszej piosenki do prefixu
            prefixDoesNotExist = false;
            shufflingAndFindingPrefix(0,numberOfSongs-1); //potasowanie piosenek i znalezenie prefixu
            displayStringArray(songsNames, numberOfSongs); // wyswieletenie wynikowej tablicy z potasowanymi piosenkami
            System.out.println(commonPrefix); // wypisanie na ekran prefixu

        }
    }
    /* Opis dzialania ponizszej funkcji
        Tasowanie odbywa sie za wykorzystywaniem metody dziel i zwycierzaj. Dana tablice dzielmy na 2 podtablice
        az do osiagniecia tablicy wielkosci 2. Przed podzialem tablic wykonywany jest swap elementow na lewo i na prawo
        od srodka w zakresie 1/4 wielkosci tablicy tzn a1 a2 a3 a4 b1 b2 b3 b4 -> a1 a2 b1 b2 a3 a4 b3 b4. Dopiero
        na tak zmieniowej tablicy wykonywany jest jej podzial na "2 podtablice".
        Oprocz tego musimy rozwazyc przypadki gdy wielkosc tablicy nie daje podzielic sie na 1/2 badz 1/4 bez reszty.
        Gdy tablica:
        1)ma nieparzysta liczbe elementow przy podziale na 1/2 i sufit tego podzialu daje liczbe nieparzysta,
          przsuwany element ostatni 1 plyty na koniec tablicy. Wiemy iz 2 ostatnie elementy sa na swoich poprawnych
          pozycjach wiec zmiejszamy wielkosc tablicy o te pozycje, i kolejne instrukcje wykonujemy jak dla tablicy
          w ktorej takowy konflik nie wystepuje.
        2)ma parzysta liczbe elementow przy podziale na 1/2 ale nieparzysta przy podzialne na 1/4 wtedy sytuacja
          jest taka sama jak w wyzej wymieniowym przypadku lecz z ta roznica ze przesuwamy ostatni element 1 plyty
          na przedostatnie miejsce.

        Szukanie prefixu odbywa sie na tablicach 2 elementowych. Prownujemy ich elementu i szukamy najwieszego wspolnego
        prefixu. Zapisujemy wynik do zmiennej i porownujemy z kolejemymi tablica o wielkosci 2.

     */
    public static void shufflingAndFindingPrefix(int start, int end) {
        int half = (int) Math.ceil((start + end + 1.0) / 2.0); // sufit z polowy wielkosci tablicy

        if ((end - start + 1) <= 2) {// jesli tablica sklada sie z 2 elementow to zakoncz podzial i znajd prefix
            if(!prefixDoesNotExist){ // szukamy prefixu jesli wczesniejsze poszukiwania nie zakonczyly sie niepoowdzeniem
                for (int i = start; i < end + 1; i++) {
                    if(i != 0){ // na poczatku dzialania programu do zmiennej commonPrefix zapiywany jest 1 element z tablicy
                        //wiec nie ma sensu porownywac go z samym soba, dlatego nie wykonujemy ponizszych funkcji gdy sprawdzamym
                        //elementem jest element 1 z tablicy songsNames
                        while (songsNames[i].indexOf(commonPrefix) != 0) {// szukamy dopoki nie nie znajdziemy wspolnego prefixu
                            commonPrefix = commonPrefix.substring(0, commonPrefix.length() - 1); // jesli prefixy nie sa sobie rowne to zmniejszamy istniejacy o 1 element i szukamy dalej
                            if(commonPrefix.isEmpty()){//jezeli zmienan commonPrefix bedzie pusta to wiemy ze nie uda sie juz znalesc prefixu i dalesze proby jego szukania sa bez sensu.
                                prefixDoesNotExist = true;
                            }
                        }
                    }
                }
            }
            return;
        }

        if (half % 2 != 0) { // jezeli 1/2 z sufitu polowy wielkosci tablicy jest liczba nie parzysta
            if ((end - start + 1) % 2 == 0) { // to jesli jej poczatkowa ilosc elementow byla patrzysta
                for (int i = half - 1; i < end - 1; i++) {
                    swap(i, i + 1); // przesuwamy ostatni element 1 plyty na przed ostatnie miejsce
                }

            } else {
                for (int i = half - 1; i < end; i++) {
                    swap(i, i + 1);// przesuwamy ostatni element 1 plyty na ostatnie miejsce
                }
            }
            end -= 2; // zmiejszamy wielkosc tablic o 2
            half = (int) Math.ceil((start + end + 1.0) / 2.0); // ponownie wyliczamy polowe z nowej wielkosci tablicy
        }

        int half_half = (start + half) / 2; // wyliczamy 1/4 z wielksoci tablicy
        int j = 0;
        for (int i = half_half; i < half; i++, j++) {
            swap(i, half + j); // robimy swap elementow o half_half na lewo i na prawo od srodka tablicy
        }
        // wykonujmey odowlnie rekurnecyjne na 2 podtablicach
        shufflingAndFindingPrefix(start, half - 1);
        shufflingAndFindingPrefix(half, end);

    }

    //Pomocnicze Fukncje----
    //funkcja swap do zamiany poszeczegolnych elementow miedzy soba
    public static void swap(int i, int j){
        String tmp = songsNames[i];
        songsNames[i] = songsNames[j];
        songsNames[j] = tmp;
    }
    //wyswietlenei tablicy wynikowej
    public static void displayStringArray(String[] array, int size){
        for (int j = 0; j < size; j++) {
            System.out.print(array[j] + " ");
        }
        System.out.println();
    }
}

/*
Uzasadnienei zlozonosci:
Przestawienie elementow w tablicy odbywa sie w sposob liniowy poniewaz wykonujemy fukncje swap. Nastepnie
dzielimy tablice na 2 podtablice, za kazdym razem rownej dlugosci. Taka operacja jest wielkosc logn zlozonosci czasowej, jest to
typowy przyklad algorytmu dziel i zwycierzaj.Zatem w ostateczenym rozrachunku zlozonosc wynosi n * log n. Wyszukiwanie prefixu
nie zwieszka zlozonosci poniewaz jest ono liniowe. Wynika to z faktu ze tytyl piosenki co najwyzej wynosi 100 elemtnow.
Wiec zloznosc szukania prefiksu wynosi 100*n. Ostatniecznie 100*n + n*logn = nlogn.

 */


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