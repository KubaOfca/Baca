//Jakub Baran - 8

/*Idea dzialania programu
Program ma za zadanie znalesc k-ata piosenke w kolejnosci rosnacej wedlug ilosci otrzymanych glosow w plebiscycie piosenki.
Wyszukiwanie opiera sie o metode sortowania quick sort, zmodyfikowana w taki sposob aby zlozonosc tej operacji byla liniowa.
Wyszukanie opiera sie rowniez o tzw mediany z median ktore sluza do wyznaczania "pivota" do algorytmu quick sort. Te mediany
wybierane sa z potablic 5 elementowych. Takich tablic jest n/5.
*/


import java.util.Scanner;
class Source {
    public static Scanner scn = new Scanner(System.in);

    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();
        for(int i=0; i< numberOfSets; i++){
            int sizeOfArray = scn.nextInt();
            int[] array = new int[sizeOfArray]; // tablica z punktami piosenek zdobytymi w rankignu
            for(int j=0; j<sizeOfArray; j++){
                array[j] = scn.nextInt();
            }

            int numberOfCommand = scn.nextInt(); // zmiena przechowywujaca ilosc zapytac o ilosc glosow uzyskane przez k-ata piosenke od konca
            int[] rank = new int[numberOfCommand]; // tablic ktora przechowuje informacje o ktore miejsce w rankingu chcemy zapytac
            for(int k=0; k < numberOfCommand; k++){
                rank[k] = scn.nextInt();
            }
            for(int l : rank){
                int result = howManyVotesDidElementK(array, 0, array.length-1, l);
                if(result >= 0){
                    System.out.println(l + " " + result);
                }else { // jezeli funkcja zwroci -1 to znaczy sie ze podano bledne dane i takie miejsce w rankingu nie istnieje
                    System.out.println(l + " " + "brak");
                }
            }

        }
    }
    //k - ilosc glosow na k pozycji liczac od konca rankingu
    public static int howManyVotesDidElementK(int[] array, int left, int right, int k){
        if (k > 0 && k <= right - left + 1){ // jesli k-aty element jest mniejszy od 0 lub przekracza zakres tablicy to nalezy zwrocic informacje o bledzie
            int size = right + 1; // rozmiar tablicy
            int posToSwap = left; // zmiena wyznaczajaca poczatek tablicy, ktora bedzie pomocna przy przenoszeniu median na poczatek tablicy
            int rSizeToInsertionSort = 0; // zakres tymczasowej podtablicy potrzebenej do wyznaczenie mediany

            //ponizsza pentla pozwala na podzial tablicy na n/5 tablic o 5 elementach kazda. Jesli taki podzial jest niemozliwy
            //"bez reszty" ostatnia tablica bedzie odpowiednio pomniejszona, tak aby zapobiec wyjsciu poza jej zakres
            for(int i=left; i < size; i += 5){
                if(i + 4 >= size){
                    rSizeToInsertionSort = size - 1;

                }else {
                    rSizeToInsertionSort = i + 4;
                }
                insertionSort(array, i, rSizeToInsertionSort); // posortowanie podtablicy. Insertion sort ma stala zlozonosc bo zawsze operuje na tablicy o stalej liczie elemtnow - 5
                int median = (i + rSizeToInsertionSort + 1)/2; // index mediany z podtablicy
                swap(array,median,posToSwap); // przeniesienie mediany z potablicy na poczatek tablicy glownej. Dzieki temu nie potrzeba nowej tablicy na przechowywanie tych median i dlatego tez
                //program dziala w miejscu - zlozonosc pamieciowa O(1)
                posToSwap++; // ilosc median umieszczonych na poczatku tablicy
            }

            int medianMedian = 0;

            if(posToSwap - 1 == left){ // jesli w wyniku podzialu otrzymano tylko 1 mediane to "mediana z median" to wlasnie ta wartosc
                medianMedian = array[posToSwap-1];
            }else { // jesli nie to nalezy rekurencujnie znalesc mediane z median. Zakres tych wartosci wyznacza zmienna posToSwap oraz left
                medianMedian = howManyVotesDidElementK(array, left, posToSwap-1, (posToSwap-left)/2);
            }

            int pos = parition(array, left, right, medianMedian); // w opraciu o znaleziona mediane z median stosujemy klasyczna wersje partition

            if (pos - left == k - 1){ // jesli mediana z median po funkcji partition wyznacza k-aty element szukany przez nas to nalezy zakonczyc program
                return array[pos]; // zwaracana jest ilosc glosow ktora zdobyla k-ata piosenka w plebiscycie
            }
            if(pos - left > k - 1){ // jesli mediana z median wskazuje na element ktory znajduje sie dalej w rankingu niz przez nasz szukany nalezy szukan k-atego elemntu na lewo od wyznaczonego wczesniej pivota czyli meidany z median
                return howManyVotesDidElementK(array, left, pos - 1, k);
            }
            //jesli nasz szukany k-aty element znajduje sie dalej w rankingu od mediany z median nalezy szukac na prawo w tablicy.
            //Istotym elementem jest fakt iz trzeba oprocz zakresu poszukiwan zmienic tez odpowiednio wartosc k-atego elementu, poniwaz skoro zmienilismy zakres tablicy
            //to k-aty element na zmienionym zakresie bedzie odpowienio mniejszy. Inaczej mowiac jesli szukalismy 15 elementu w rankingu a wyznaczona mediana z meidan jest elementem 10, to jezeli
            //tablica ma 20 elementow to szukany k-aty element nie bedzie juz 15 z koleji a 4 z uwagi na zmieniony zakres tablicy na ktorym to poszukiwanie sie odbywa.
            return howManyVotesDidElementK(array, pos +1, right, k - pos + left - 1);
        }
        return -1; // zwaracmy -1 w przypadku gdy nie udalo sie znalesc k-atego elementu

    }

    private static int parition(int[] array, int left, int right, int medianMedian) {
        int i;
        for (i = left; i < right; i++)
            if (array[i] == medianMedian)
                break;
        swap(array, i, right); // swap pivota na koniec tablicy

        i = left;
        //klasyczny przebieg fukncji partiton dla pivota ktory znajduje sie na koncu tablicy
        for (int j = left; j <= right - 1; j++)
        {
            if (array[j] <= medianMedian)
            {
                swap(array, i, j);
                i++;
            }
        }
        swap(array, i, right);
        return i;
    }

    public static void insertionSort(int[] array, int left_side, int right_side){
        int size = right_side + 1;
        for(int i=left_side+1; i < size; i++ ){
            int key = array[i];
            int j = i - 1;

            while (j >= left_side && key < array[j]){
                array[j+1] = array[j];
                j = j - 1;
            }
            array[j+1] = key;
        }

    }

    public static void swap(int[] array, int i, int j){
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    //Pomocnicze Fukncje
    public static void displayArray(int[] array){
        for(int i=0; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

}

/* Uzasadnienie zlozonosci
1) Znalezienie mediany tablic o rozmiarze 5 zajmuje O(1) z uwagi na to ze insertion sort odbywa sie zawsze na 5 elementach, a tablic o tym rozmiarze jest n/5.
2) Znalezienie meidany z median zajmuje T(n/5).
3) Partycionowanie jest standardowo liniowe O(n)
4) Rozmiar odwolan rekurencyjnych w 77 i 83 lini programu w najgorszym przypadku to maksymalna liczba elementow wieksza od mediany z median
lub maksymalana liczba mniejsza od mediany z median. Wiemy iz co najmniej polowa meidan znalezionych podczas podzialu na n/5 podtablic
jest wieksza lub rowna mediany z median. Z tego wynika iz polowa z n/5 potablic dostarcza 3 elementwo ktroe sa wieksze/mniejsze niz mediana z median.
Ostatecznie liczba elentow ktore moga byc wieksze lub mniejsze wyniki : 3n/10 - 6. W najgorszym przypadku funckja wykonuje sie
n - (3n/10 - 6) co daje 7n/10 + 6 elementow.

Ostatecznie zlozonosc to: T(n/5) + T(7n/10 + 6) + O(n) => O(n)
 */


/* Testy input
1
20
2 1 6 9 7 8 12 11 15 4 3 14 5 10 18 19 20 25 24 22
1
4

1
3
1 2 3
1
2

1
15
10 11 8 9 74 1 2 3 44 5 8 6 10 56 42
3
3 4 5
 */

/* Testy output
4 4

2 2

3 3
4 5
5 6
 */