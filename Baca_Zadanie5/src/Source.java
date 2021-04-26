//Jakub Baran - 8
/*Idea Dzialania Programu
    Program ma za zadanie z podanych wartosci wagowych poszczegolnych elementow, wyznaczyc taka ich sekwencje,
    ze wartosc sumaryczna wag wybranych elementow jest dokladnie rowna podanej wczesniej wartosci pojemnosci
    plecaka. W programie zaimplementowano dwie metody umozliwiajace rozwiazanie tego problemu. Jedna z nich jest
    metoda rekurencyjna, druga zas jest iteracyjna i wykorzystuje stos. W przypadku gdy nie uda sie znalesc
    sekewncji, ktora spelnia warunki zadania, na ekranie wyswietlany jest napis BRAK.
 */

import java.util.Scanner;

public class Source {
    public static Scanner scn = new Scanner(System.in);
    public static int[] globalSolution; // tablica przechowujaca sekwencje elementow (wynik) funkcji rekurencyjnej
    public static int sizeOfGlobalSolution; // rozmiar tablicy globalSolution. Dzieki niemu mozliwe jest poprawne
    //wypisanie na ekran wyniku dzialania funkcji rekurencyjnej.
    public static boolean find;// zmienna ktora informuje nas czy znaleziono poprawny wynik podczas trawnia symulacji
    public static int backpackCapacity = 0;// zadana pojemnosc plecaka
    public static int[] arrayOfElements; // tablica ktora zawiera podane wartosci wag poszczegolnych elementow

    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();

        for (int i = 0; i < numberOfSets; i++) {
            backpackCapacity = scn.nextInt();
            int amountOfElements = scn.nextInt();
            arrayOfElements = new int[amountOfElements];
            globalSolution = new int[amountOfElements];
            sizeOfGlobalSolution = 0;
            find = false;

            for (int j = 0; j < amountOfElements; j++) {
                arrayOfElements[j] = scn.nextInt();
            }

            rec_pakuj(arrayOfElements, 0, 0, backpackCapacity);
            if(find) { // jezeli rec_pakuj zmieni wartosc find z false na true, wiemy iz rozwiazanie zostalo znalezione
                // i jest ono w tablicy globalSolution
                rec_pakuj_display(globalSolution, sizeOfGlobalSolution);//wyswietlamy wynik dzialania funkcji rec_pakuj
                Stack stack = new Stack(amountOfElements);//tworzymy nowy stos wykorzysywany w dzialaniu funkcji iter_packuj
                find = false;//resetujemy zmienna find
                iter_pakuj(arrayOfElements, stack, backpackCapacity);
                stack.iter_pakuj_display(); // wyswietlamy wynik dzialania funkcji iteracyjnej
            } else{ // jezeli nie udalo sie znalesc wyniku wyswietlany jest napis BRAK
                System.out.println("BRAK");
            }
        }
    }

    public static void iter_pakuj(int[] array, Stack stack, int destinationSum) {
        int j = 0;
        while (j < array.length) {
            stack.push(j); //na stos wstawiamy index elementu z tablicy array
            destinationSum -= array[j];// od szukanej sumy odejmujemy wage elementu o indexie j

            if (destinationSum == 0) {// jezeli destinationSum == 0 oznacza to ze znalezlismy wynik
                find = true;
                return;
            } else if (destinationSum > 0 && j != array.length - 1) { // jezeli nie osiagnielismy jeszcze porzadanej sumamrycznej
                //wagi elementow i nie znajdujemy sie na ostatim elemenicie w tablicy array, zwiekszamy index j w celu dodania kolejnego elementu do
                //wartosci aktualnej sumarycznej wagi elementow
                j++;
                continue;
            } else if (destinationSum < 0 && j != array.length - 1) {// jezeli przekroczylismy porzadana pojemnosc plecaka
                //to musimy zresetowac sume o element ktory spowodowal to przekroczenie i jezeli to nie byl ostati element w tablicy array
                //to wystrczy tylko ze sciagniemy ze stosu index tego elementu i w nastepenej iteracji sprawdzimy element nastepny
                destinationSum += array[j];
                stack.pop();
            } else if (j == array.length - 1) { // jezeli przekroczylismy sume lub jeszcze jej nie osiagnelismy ale znajdujemy sie
                //na ostatnim elemencie w tablicy array oznacza to ze ze stosu musimy sciagnac indexy dwoch elementow i zresteowac sume
                destinationSum += array[j];
                stack.pop();
                if(stack.isEmpty()){//jezeli probojemy sciagnac element ze stosu ktory jest pusty jest to dla nas informacja ze
                    //dany zestaw danych nie da nam porzadanego rozwiazania
                    return;
                }
                destinationSum += array[stack.top()];
                j = stack.pop();// ustawiamy j na index elementu drugiego sciaganego ze stosu poniewaz nie udalo nam sie stworzyc kombinacji
                //wykorzystujacej ten element ktora spelnia warunki zadania. Ustawiajac j w taki sposob w kolejnej iteracji petli bedziemy
                //sprawdzac kombinacje dla kolejnego elementu.
            }
            j++;
        }
    }

    public static void rec_pakuj(int[] array, int currSum, int index, int destinationSum) {
        if (currSum == destinationSum) {//jezeli aktualana suma == sie destination sum oznacza to ze znalezlismy wynik
            if(find == false) {
                find = true;
            }
        } else if (index == array.length) {// jezeli jestemy na koncu tablicy array nie mamy juz wiecej elemntow do dodania
            //wiec musimy przerwac dzialanie funkcji poniwaz kombinacja nie zostala znaleziona
            return;
        }else if(currSum > destinationSum){// jezeli przekroczylismy zadeklaraowana sume to musimy przerwac wykonwyanie funckji
            //poniewaz dana kombinacja nie da nam oczekiwanego rezulatatu
            return;
        }
        else{
            currSum += array[index];// jezeli suma nie jest osiagnieta do aktualana sume zwiekszamy o dany element
            globalSolution[sizeOfGlobalSolution] = array[index];//zapisujemy ten element do tablicy wynikowej
            sizeOfGlobalSolution++;//zwiekszamy rozmiar tablicy wynikowej
            rec_pakuj(array, currSum, index + 1, destinationSum); // ponawiamy dzialanie funkcji w celu znalezenia kolejengo elementu do sekwencji
            if(find) {//jezeli dzialanie powyzszej funkcji ustali odpowiednia kombinacje konczymy wykonwyanie funkcji, wynik zostal znaleziony
                return;
            }// w przciwnym przypadku wykluczamy ten element z kombinacji i szukamy ponownie z wykluczeniem tego elemntu
            currSum -= array[index];
            sizeOfGlobalSolution--;
            rec_pakuj(array, currSum, index + 1, destinationSum);
            if(find) {
                return;
            }
        }

        return;
    }

    //Fukcja sluzaca do wypisania wyniku dzialania funkcji rec_pacuj
    public static void rec_pakuj_display(int[] array, int size) {
        System.out.print("REC:  " + backpackCapacity + " = ");
        for (int i = 0; i < size; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}

//----Struktury danych----
class Stack {
    public int[] arr;
    public int maxSize;
    public int currentSize;

    Stack(int maxSize) {
        this.maxSize = maxSize;
        this.arr = new int[maxSize];
        this.currentSize = 0;
    }

    public void push(int element) {
        arr[currentSize] = element;
        currentSize++;
    }

    public int pop() {
        if (isEmpty()) {
            return -1;
        } else {
            currentSize--;
            return arr[currentSize];
        }
    }

    public int top() {
        if (isEmpty()) {
            return -1;
        } else {
            return arr[currentSize - 1];
        }
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    //funkcja sluzaca do wyswietalania wyniku funkcji iter_pakuj
    public void iter_pakuj_display(){
        System.out.print("ITER: " + Source.backpackCapacity + " = ");
        for(int i=0; i<currentSize; i++){
            System.out.print(Source.arrayOfElements[arr[i]] + " ");
        }
        System.out.println();
    }
}

/*
Testy input
1
20
6
30 11 8 7 6 5
1
3
3
1 2 3
1
2
5
3 4 5 6 7
 */

/*
Testy output
REC:  20 = 8 7 5
ITER: 20 = 8 7 5
REC:  3 = 1 2
ITER: 3 = 1 2
BRAK
 */