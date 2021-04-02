//Jakub Baran - 8
/*Idea dzialania programu

Program ma za zadanie wyznaczyc liczbe mozliwych trojek indeksow w tablicy zawierajacej odcinki, z ktorych mozna
zbudowac trojkat tzn ze a + b > c && b + c > a && a + c > b. Kazdy z odcinkow moze wystepowac tylko raz w
budowanym trojkacie, lecz w tablicy moze byc wiele odcinkow o takiej samej dlugosci.
Do rozwiazania tego problemu wykorzystano metdoe sortowania InsertionSort oraz metode oparta o dwa wskazniki.
Metoda ta polega na tym, iz ustawia sie wzkaznik start na indexie poczatkowym, wskaznik end na indeksie o
wartosci (dlugosc tablicy - 1) oraz wskaznik k na pozycji (end - 1). Nastepnie sprawdza sie warunek czy
z wartosci tablicy w indexach start end i k mozna zbudowac trojkat. Jesli tak to wszystkie wartosci pomiedzy
start a k rowniez spelniaja ten warunek. Czynnosc ta powtarzamy zmniejszajac wartosc indexu k i zostawiajac
index start na tej samej pozycji az do chwili gdy z wartosci start end i k nie bedzie mozna zbudowac
trojakta lub gdy index k bedzie na pozycji o 1 wyzej od indexu start. Jesli w trakcie iteracji okaze
sie ze indexy start i k nie sa na sasiednich pozycjach a trojkatu z wartosci obecnych nie mozna zubodwac
to zamiast zmniejszac index k zwiekszamy index start i czynosci powtarzamy do moemntu az index start i k
nie beda na sasiednich pozycjach. W nastepych iteracjach ustawiamy index start na poczatek a wartosc indexu
end i k pomniejszamy o 1 i czynnosci powtarzamy az do wyczerpania mozliwosci ustawien.

 */
import java.util.Scanner;

class Source {

    public static Scanner scn = new Scanner(System.in);

    public static int[] InsertionSort(int[] array, int sizeOfArray) // funkcja sortujaca tablice w porzadku ASC
    {
        int tmp; // zmiena ktora przechowuje wartosc elemntu tablicy obecnie wstawianego do posortowanej czesci
                //tablicy

        for(int i=1; i < sizeOfArray ; i++) //"dzielimy tablice na posortowana i nieposortowana. Zakladamy ze
            //1 element nalezy do tablicy posortowanej dlatego poczatek pentli ustawiamy na nastepny element
        {
            tmp = array[i]; // zapisujemy obecnie sprawdzany element do zmienej tmp aby nie stracic jego wartosci
            int j = i - 1; // ustawiamy zmienna j na element o jeden mniejszy od obecnie sprawdzanego czyli na
            // element ostatni w posortowanej tablicy

            while (j >= 0 && tmp < array[j] ) //petla przesuwajaca elementy posortowanej tablicy az do momentu
                //dojscia do konca tablicy lub gdy aktualnie sprwadzany element jest wiekszy od elemntu j
            {
                array[j+1] = array[j];
                j--;
            }
            array[j+1] = tmp; // wstawienie akutalnie sprawdzanego elemntu do czesci tablicy ktora jest posortowana
            //w odpowienim miejscu zgodnie z porzadkiem ASC
        }

        return array;

    }

    public static void FindNumOfTriangles(int[] array, int sizeOfArray) //funckja wyznaczajaca ilosc mozliwych
            //trojkatow ktore da sie zbudowac z odcinkow znajdujacych sie w tablicy
    {
        int start; //wskaznik wskazujacy na 1 element tablicy
        int end; // wskaznik wskazujacy na ostatni element tablicy
        int numberOfTriangels = 0; // licznik mozliwych trojkatow zbudowanych z danych odcinkow

        for(end = sizeOfArray - 1; end > 1; end--) //ustawienie wskaznika w 1 iteracji na ostatni element tablicy
            //i zmniejszanie go o 1 w kolejnych iteracjach az do momentu gdy end bedzie wksazywalo na 2 index tablicy
        {
            int k = end - 1; //ustawienie indexu k na element o 1 mniejszy od aktualnie wskazywanego przez index end

            for(start = 0; start < k ; start++) //ustawienie indexu start na 1 element tablicy i zwiekszanie go w
                //kolejnych iteracjach az do momentu gdy bedzie w sasiedztwie k
            {
                if(IsTriangleInequalitySatisfied(array,start,end,k)) // funkcja sprawdzajaca czy podane wartosci
                    //spelnaiaja nierownosc trojkata
                {
                    numberOfTriangels += k - start; //jesli spelniaja to dodajemy do licznika ilosc mozliwych trojkatow
                    //zbudowanych z wartosci mniedzy start a k
                    k--; // zmniejszamy wartosc k o 1
                    start--; // zabieg pozwalajacy na pozostanie wskaznika start na aktualnym miejscu poniewaz w tej
                    //iteracji to k zmienia swojaj pozycje
                }
            }
        }

        System.out.print("Num_triangles= " + numberOfTriangels + "\n");
    }

    public static boolean IsTriangleInequalitySatisfied(int[] array, int start, int end, int k) //funkcja
            //zwracajaca wartosc true jezeli nierownosc trojkata jest spelniona
    {
        if(!(array[start] + array[end] > array[k]))
        {
            return false;
        }
        else if(!(array[start] + array[k] > array[end]))
        {
            return false;
        }
        else if(!(array[end] + array[k] > array[start]))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void main(String[] args) {

        int numberOfSets = scn.nextInt(); // liczba zestawow

        for(int i = 0 ; i < numberOfSets ; i++)
        {
            int sizeOfArray = scn.nextInt(); //wielkosc aktualnie wczytywanej tablicy
            int[] array = new int[sizeOfArray]; //utworzenie tablicy o wielkosci aktualnie wczytywanej

            for(int j=0; j < sizeOfArray; j++ ) //wczytanie wartosci aktualnej tablicy
            {
                array[j] = scn.nextInt();
            }

            array = InsertionSort(array, sizeOfArray); // sortowanie ASC tablicy
            FindNumOfTriangles(array,sizeOfArray); //wykonanie fukncji znajdujacej liczbe mozliwych trojkatow
            //uzyskanych z odcinkow zanajdujacych sie w tablicy

        }

    }
}


/*
Testy input
5
6
2 4 6 7 8 21
4
-2 -3 -4 5
6
1 2 3 1 2 3
5
4 3 5 7 6
3
0 0 0



Testy output
Num_triangles= 6
Num_triangles= 0
Num_triangles= 8
Num_triangles= 9
Num_triangles= 0

 */