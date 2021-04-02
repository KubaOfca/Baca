//Jakub Baran - 8
/* Idea dzialania programu

Program ma za zadanie obliczyc najwieksza nieujemna sume podtablicy o mozliwie jak najmniejszej licze elementow
oraz indeksach leksykograficznie najmniejszych wyrazona wzorem:
                                    ms(up, down, l, r) = 3*D+2*U
    gdzie up, down - przedstawiaja zakres wierszy ktory obejmuje wyznaczona podtablica 0 <= up <= down < n
          l, r - przedstawiaja zakres kolumn ktory obejmuje wyznaczona podtablica przy czym  0 <= l <= r < m
          D - suma dodatnich elementow a[x][y] tej podtablicy, dla ktorych up <= x <= down oraz l <= y <= r
          U - suma ujemnych elementow a[x][y] tej podtablicy, dla ktorych up <= x <= down oraz l <= y <= r

Do rozwiazania problemu wykorzystano algorytm Kadane, lecz wprowadzono pewne modyfikacje poniewaz program dziala
zarowno na tablicach jedno wymiarowych jak i dwu wymiarowych.

Modyfikacja ta polega na stworzeniu dodatkowej tablicy jednowymiarowej o rozmiarze odpowiadajacej liczbie wierszy
w naszej tablicy dwuwymiarowej. Przykladowo dla podanej nizej tablicy:

                                                    +--+--+--+
                                                    |1 |2 |3 |
                                                    +--+--+--+
                                                    |4 |5 |6 |
                                                    +--+--+--+
                                                    |7 |8 |9 |
                                                    +--+--+--+
dodatkowa tablica jednowymiarowa bedzie wygladala w sposob nastepujacy:
                                                    l = 0, r=0
                                                    +--+
                                                    |1 |
                                                    +--+
                                                    |4 |
                                                    +--+
                                                    |7 |
                                                    +--+
Nastepnie z tej dodatkowej tablicy wyznaczamy podtablice o najwiekszej sumie za pomoca algorytmu Kadane oraz
zapamietujemy jej indexy. Czynnosc ta powtarzamy przez dodanie do tablicy dodatkowych wartosci z kolejnej kolumny:
                                                    l=0, r=1
                                                    +--+     +--+   +--+
                                                    |1 |     |2 |   |3 |
                                                    +--+     +--+   +--+
                                                    |4 |  +  |5 | = |9 |
                                                    +--+     +--+   +--+
                                                    |7 |     |8 |   |15|
                                                    +--+     +--+   +--+
Jesli suma podtablicy obecnie wyznaczonej jest wieksza od sumy podtablicy aktualnie maksymalnej to nadpisujemy zmienne
pamietajace wymiary maksymalnej podtalicy indexami podtablicy obecnie wyznaczonej oraz nadpisujemy wartosc
maksymalnej sumy podtablicy, suma uzyskana z podtablicy obecnie wyznaczonej.
W momencie, gdy dodamy wartosci z ostatniej kolumny w tej "iteracji" do dodatkowej tablicy, resetujemy nasza dodatkowa
tablice jedynowymiarowa i uzupelniamy ja wartosciami kolumny z kolejnej "iteracji" tj:
                                                    l = 1, r=1
                                                    +--+
                                                    |2 |
                                                    +--+
                                                    |5 |
                                                    +--+
                                                    |8 |
                                                    +--+
Czynnosci te powtarzamy az do wyczerpania mozliwosci ustawien tzn kiedy l = liczba kolumn - 1.
Po wykoaniu takich czynosci w odpowiednich zmiennych otrzymujemy maksymalna sume podtablicy wyznaczona z tablicy
poczatkowej oraz jej zakres kolum i wierszy wyrazony odpowienimi indeksami lMax,rMax,upMax,downMax tj:
                                upMax - 0, lMax - 0 +--+--+--+ rMax - 2
                                                    |1 |2 |3 |
                                                    +--+--+--+
                                                    |4 |5 |6 |
                                                    +--+--+--+
                                                    |7 |8 |9 |
                                                    +--+--+--+ downMax - 2


 */
import java.util.Scanner;

class Source {
    public static Scanner scn = new Scanner(System.in);

    public static void Kadame2D(int[][] array, int rows, int cols, int setNumber)
    {
        int maxSum = 0; // maksymalna suma podtablicy
        int lMax = 0; // index przestawiajacy pierwsza kolumne wchodzaca w zakres podtablicy o maksymalnej sumie
        int rMax = 0; // index przestawiajacy ostatnia kolumne wchodzaca w zakres podtablicy o maksymalnej sumie
        int downMax = 0; // index przestawiajacy ostatni wiersz wchodzacy w zakres podtablicy o maksymalnej sumie
        int upMax = 0; // index przestawiajacy pierwszy wiersz wchodzacy w zakres podtablicy o maksymalnej sumie
        int[] subArray; // dodatkowa talica 1D o wielkosci liczby wierszy poczatkowej tablicy 2D

        for(int l=0; l < cols; l++)
        {
            subArray = new int[rows]; // reset tablicy 1D lub pierwsze utworzenie tablicy

            for(int r=l; r < cols; r++)
            {

                for(int p=0;p < rows ; p++) // petla przechodzaca po wszystkich indeksach dodatkowej tablicy 1D
                {
                    subArray[p] += array[p][r]; // dodanie do dodatkowej tablicy 1D wartosci z kolejnych kolumn tablicy
                                                //poczatkowej
                }

                int resultOfKadame[] = Kadame(subArray,rows); // wyznaczenie podtablicy z tablicy dodatkowej 1D
                                                              // o najwiekszej sumie elemntow i zwrocenie tych wartosci
                                                              // do tablicy resultOfKadame w postaci:
                                                              // resultOfKadame[0] - maksymalan suma podtablicy obliczona z
                                                              // dodatkowej tablicy 1D (maxSum)
                                                              // resultOfKadame[1] - index poczatkowy zakresu podtablicy
                                                              // (upMax)
                                                              // resultOfKadame[2] - index koncowy zakresu podtablicy
                                                              // (downMax)

                if(l == 0 && r == 0) // przypisanie do maxSum pierwszej wartosci sumy maksymalnej podtablicy (gdyz na ten
                                    //moment jest ona suma maksymalna) oraz aktualizacja indeksow
                {
                    maxSum = resultOfKadame[0];
                    lMax = l;
                    rMax = r;
                    upMax = resultOfKadame[1];
                    downMax = resultOfKadame[2];
                }

                else if((resultOfKadame[0] > maxSum) && !(l == 0 && r == 0)) // warunek sprawdzajacy czy akutalnie sprawdzana
                    //suma nie jest wieszka od aktualnie maksymalnej
                {
                    maxSum = resultOfKadame[0]; // zastapienie starej maksymalnej sumy aktualna
                    lMax = l; // zastapnie starego indexu pierwszej kolumny wchodzacej w zakres podtablicy o maksymalnej sumie
                    rMax = r; // zastapnie starego indexu ostatniej kolumny wchodzacej w zakres podtablicy o maksymalnej sumie
                    upMax = resultOfKadame[1]; // zastapnie starego indexu pierwszego wiersza
                                                // wchodzacego w zakres podtablicy o maksymalnej sumie
                    downMax = resultOfKadame[2]; // zastapnie starego indexu ostatniego wiersza
                                                // wchodzacego w zakres podtablicy o maksymalnej sumie
                }
                else if(resultOfKadame[0] == maxSum) // warunek sprawdzajacy przypadek kiedy aktualna suma rowna sie
                    //akutalnej maksymalnie sumie
                {
                    if(((r - l + 1) * (resultOfKadame[2] - resultOfKadame[1] + 1)) ==
                            ((rMax - lMax + 1) * (downMax - upMax + 1))) // sprawdzenie czy ilosc elementow podtablicy
                        //aktualnej jest rowna ilosci elementow podtablicy, ktora ma aktualnie najwieksza sume elementow
                    {
                        if(resultOfKadame[1] < upMax) // sprawdzenie porzadku leksykograficznego
                        {
                            lMax = l; //jestli warunek spelniony to aktualizacja poszeczegolnych indeksow
                            rMax = r;
                            upMax = resultOfKadame[1];
                            downMax = resultOfKadame[2];
                        }
                        else if(resultOfKadame[1] == upMax)
                        {
                            if(l < lMax) // sprawdzenie porzadku leksykograficznego w przypadku gdy nie wystarczy porownanie
                                // pierwszych wartosci indexow
                            {
                                lMax = l;
                                rMax = r;
                                upMax = resultOfKadame[1];
                                downMax = resultOfKadame[2];
                            }
                        }
                    }else if(((r - l + 1) * (resultOfKadame[2] - resultOfKadame[1] + 1)) <
                            ((rMax - lMax + 1) * (downMax - upMax + 1))) // jesli sumy max i aktualne sa rowne ale
                        //ilosc elementow w aktualnie sprawdzanej podtablici jest mniejsza od aktualnie maksymalnej
                        // to maksymalana podtablica staje sie aktualnie sprawdzana podtablica
                    {
                        lMax = l;
                        rMax = r;
                        upMax = resultOfKadame[1];
                        downMax = resultOfKadame[2];
                    }
                }

            }
        }
        //wypisanie wyniku programu na ekran konsoli
        System.out.println(setNumber + ": " + "n=" + rows + " m=" + cols + ", ms= " + maxSum + ", mstab= a[" + upMax
                + ".." + downMax + "]" +"[" + lMax + ".." + rMax + "]");
    }

    public static int[] Kadame(int[] subArray, int rows) // obliczanie maksymalnej sumy podtablicy z tablicy 1D za pomoca
            //algorytmu Kadame
    {
        int currentSum = subArray[0]; // przypisanie do aktualniej sumy 1 elementu talicy 1D
        int maxSum = subArray[0]; // przypisanie maksymalnej wartosci sumy podtablicy 1 elemntu tablicy 1D
        int tempUpMax = 0; // tymczasowa zmienna pamietajaca aktutalny index poczatkowy podtablicy
        int upMax=0; //  index poczatkowy podtablicy o maksymalnej sumie
        int downMax=0; // //  index koncowy podtablicy o maksymalnej sumie

        for(int k=1; k < rows ; k++)
        {
            if(subArray[k] < (subArray[k] + currentSum)) // jesli akutalnie sprawdzany element podtablicy nie jest wiekszy
                // od aktualenej sumy podtablicy powiekszonej o ten element to wtedy aktualna sume nalezy powiekszyc
                // o wartosc tego elementu
            {
                currentSum += subArray[k];


                if(currentSum > maxSum) // sprawdzenie czy aktualana suma nie jest wieksza od maksyalnej
                {
                    maxSum = currentSum;
                    downMax = k;

                }

            }
            else //jesli akutalnie sprawdzany element podtablicy jest wiekszy
            // od aktualenej sumy podtablicy powiekszonej o ten element to wtedy aktualna sume nalezy nadpisac
            // wartoscia tego elementu a takze zrestowac tymaczasowe zakresy i ustawic je na index tego elementu
            {
                currentSum = subArray[k];
                tempUpMax = k;

                if (currentSum > maxSum)
                {
                    maxSum = currentSum;
                    upMax = k;
                    downMax = k;

                }

            }

            if(currentSum == maxSum)
            {
                if((k - tempUpMax + 1) < (downMax - upMax +1)) //jesli aktualnie sprawdzana podtablica sklada sie z
                    //mniejszej liczby elementow niz aktualna podtablica o maksymalnej sumie, to podtablica o maksymalnej
                    //sumie staje sie aktualnie sprawdzana podtablica
                {
                    downMax = k;
                    upMax = tempUpMax;
                }

            }

        }

        //gdy znamy juz najwieszka sume podtablicy obliczenej z tablicy 1D
        //zwracamy ta sume oraz indexy podtablicy ktore wyznaczaja ta sume do tablicy
        //result. Tablica ta posluzy nam do sprawdzenia warunku w funkcji Kadame2D, ktore sprawdzaja
        //czy uzyskana przez nas suma podtablicy jest wieksza od obecnie najwiekszej

        int[] result = new int[3];
        result[0] = maxSum;
        result[1] = upMax;
        result[2] = downMax;

        return result;

    }

    public static boolean isAllValuesNegative(int[][] array, int rows, int columns) // sprawdzenie czy wszystkie elementy
            //podtablcy sa ujemne
    {
        for(int i=0; i< rows; i++)
        {
            for(int j=0; j<columns; j++)
            {
                if(array[i][j] >= 0)
                {
                    return false; // jesli choc jeden element jest wiekszy badz rowny 0 fukcja zwaraca false
                }
            }
        }
        return true;
    }

    public static void main(String args[]) {

        int numberOfSets = Integer.parseInt(scn.next()); // liczba zestawow do rozpatrzenia

            for(int i=0; i<numberOfSets ; i++)
            {

                int setNumber = Integer.parseInt(scn.next()); // numer aktualnego zestawu
                scn.next(); // pomieniecie ":"
                int rows = Integer.parseInt(scn.next()); // liczba wierszy tablicy
                int columns = Integer.parseInt(scn.next()); // liczba kolumn tablicy
                int[][] arrayToCalc = new int[rows][columns]; // tablica sluzaca do przeksztalcenia wartosci
                // podanej tablicy tak aby byly one zgodne ze wzorem podanym na wstepie


                for(int j = 0; j < rows ; j++)
                {
                    for(int k = 0 ; k < columns ; k++)
                    {
                        arrayToCalc[j][k] = Integer.parseInt(scn.next()); // wypelnienie tablicy wartosciami
                    }
                }

                int[][] array = new int[rows][columns]; // wlasciwa tablica sluzaca do dalszych obliczen w ktorej
                //wartosci zostaly poddane przemnozeniem wedlug wzoru podanego we wstepie

                for(int n=0; n< rows ; n++) // petle sluzace do przeksztalcenia wartosci w tablicy wedlug wzoru
                                            //podanego we wstepie
                {
                    for (int m=0; m<columns;m++)
                    {
                        if(arrayToCalc[n][m] > 0)
                        {
                            array[n][m] = 3*arrayToCalc[n][m];
                        }
                        else
                        {
                            array[n][m] = 2*arrayToCalc[n][m];
                        }
                    }
                }



                //Sprwadzenie warunkow czy dane w tablicu nie sa wylaczenie wartoscami ujemnymi
                if(isAllValuesNegative(array, rows, columns))
                {
                    System.out.println(setNumber + ": " + "n=" + rows + " m=" + columns + ", ms= 0" + ", mstab is empty");
                }
                else
                {
                    //wywolanie fukncji obliczajaca maksymalana podtablice z zadanej tablicy 2D badz 1D
                    Kadame2D(array, rows, columns, setNumber);
                }



            }
        }


    }


/* Testy input
5
1 : 1 5
1 5 8 -100 14
2 : 1 2
0 1
3 : 1 5
12 2 -100 13 1
4 : 2 3
2 5 -100
-100 -100 7
5 : 3 2
-1 -2
-3 -4
-5 1

Testy output
1: n=1 m=5, ms= 42, mstab= a[0..0][4..4]
2: n=1 m=2, ms= 3, mstab= a[0..0][1..1]
3: n=1 m=5, ms= 42, mstab= a[0..0][0..1]
4: n=2 m=3, ms= 21, mstab= a[1..1][2..2]
5: n=3 m=2, ms= 3, mstab= a[2..2][1..1]  */

