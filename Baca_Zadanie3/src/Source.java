//Jakub Baran - 8
/*Idea dzialania programu
    Program wykonuje konwersje wyrazen z notacji INF do notacji ONP oraz z notacji ONP do INF.
    Wyrazenia sa filtrowane z bledych znakow, nawiasow operatorow. Sprawdzana jest ich poprawnosc
    przez automat skonczony. Wyrazenia sa wczytywane za pomoca fukcnji Scanner z poziomu konsoli.
    Nalezy podac ilosc wyrazen do skonwertowania a nastepnie podac te wyrazenia poprzedzajac je wyrazeniem
    INF: lub ONP: (przykladowe testy umieszone sa na koncu kodu).
 */

import java.util.Scanner;

class Source {
    public static Scanner scn = new Scanner(System.in);

    /* Objasnienie znaczenia tabel signs, rankOfSings, rightSideSigns oraz Listy signsList

       tabela signs -> zawiera wszystkie mozliwe operatory, ktore moga byc uzyte w programie. Specialnym operatorem jest
                        '.' odpowiada ona wystapieniu litery alfabetu w przypadku konwersji z ONP => INF.
       tabela rankOfSigns -> zaweira inforamcje o piorytecie poszczegolnych operatorow. Indexy w tej tabeli odpowiadaja
                              indexa w tabeli signs tzn. index[1] w tabeli rankOfSigns zawiera pioryter operatora '('
                              ktory w tabeli signs rowniez ma przypisany index[1]. Taki zapis umozliwia nadanie operatora
                              piorytetu.
       tabela rightSideSigns -> zawiera wszystkie mozliwe operatory ktorych lacznosc jest prawostronna.
       lista signsList -> zawiera wszystkie elementy z tabeli signs. Struktura stosowana z powodu wiekszych mozliwosci
                           dokonowaych operacji, zwiekszenia przejzystosci kodu.

     */
    private static final String[] signs = new String[] {".", "(", ")", "!", "~", "^", "%", "/", "*", "-", "+", ">", "<", "?", "&", "|", "="};
    private static final int[] rankOfSigns = new int[]{   9,   0,   0,   8,   8,   7,   6,   6,   6,   5,   5,   4,   4,   3,   2,   1, 0};
    private static final String[] rightSideSigns = new String[]{"!", "~", "=", "^"};
    private static List signsList = new List(256);

    /* Opis funkcji isLetter

       sprwadza czy podany ciag znakow zawiera wylacznie male litery alfabetu greckiego

     */
    public static boolean isLetter(String input)
    {
        return input.matches("[a-z]");
    }

    /* Opis funkcji covertToINF

        funckja konwertujaca wyrazenie z notacji ONP do notacji INF.
        Do konwersji wykorzystano dwa rodzaje stosow:
            stackSymobls -> stos na ktorym umieszczane sa wyrazenia w postaci np. a, (a+b), (a+b)/(c*d)
            stackSings -> stos na ktorym umieszczane sa operatory w postaci np. '/', '+'. Specjalnym operatorem jest
                          '.' ktory oznacza ze na stos stackSymbols umieszczono litere alfabetu.
        Zmiena input przechowuje akutalnie sprawdzany element o dlugosci 1 z wyrazenia expression np. 'a', '+'.
        Algorytm konwersji wykonywany jest na nastepujacych krokach:
            Jezeli:
                input jest litera alfabetu greckiego -> poloz input na stos stackSymbols, poloz '.' na stackSigns
                input jest operatorem dwuargumentowym -> odluz ze stosu stackSybmols 2 elementy i przypisz je do
                                                         zmiennych x,y oraz ze stosu stackSigns odluz 2 elementy
                                                         i przypisz je do zmiennych opX, opY. Sprawdz warunek wstawiania
                                                         nawiasow(*) i wstaw na stos stackSymbols wyrazenie w postaci
                                                         x + " " + input + " " + y, a na stos stackSigns wstaw input.
                input jest operatorem jednoargumentowym -> postepuj analogicznie jak w przypadku operatoru dwuargumentowego
                                                           z ta roznica, iz ze obydwu stosow sciagaj po 1 elemencie.

            Po analizie wszystich elementow wyrazenia, wynik konwersji znajduje sie na stosie stackSymbols. Jesli na stosie
            na koniec dzialania algorytmu znajduje sie wiecej niz 1 element, lub w czasie jego trwania wystapila proba
            zdjecia wiekszej ilosci elementow ze stosu niz obecnie sie na im znajduje, wyswietl komunikat o bledzie i
            przerwij konwersje.

        (*)
            Warunek wstawiania nawiasow -> nawiasy wstawiane sa w opraciu o piorytet oraz argumentowos operatorow.
                                           Dwuargumentowe:
                                               Wyrazenie ktore jest w pierszej kolenosci sciagane ze stosu (y) jest brane
                                               w nawias gdy piorytet opY <= piorytert input pod warunkiem iz input != '='.
                                               Wyrazenie ktore jest w drugiej kolenosci sciagane ze stosu (x) jest brane
                                               w nawias gdy piorytet opX < piorytert input.
                                           Jednoargumentowe:
                                               Wyrazenie ktore jest w pierszej kolenosci sciagane ze stosu (x) jest brane
                                               w nawias gdy piorytet opX < piorytert input.

     */
    public static void convertToINF(String expression) {
        StackString stackSymbols = new StackString(256);
        StackString stackSigns = new StackString(256);

        for (int i = 0; i < expression.length(); i++) {
            String input = Character.toString(expression.charAt(i));

            if (isLetter(input)) {
                stackSymbols.push(input);
                stackSigns.push(".");
            } else if (!input.equals(rightSideSigns[0]) &&
                       !input.equals(rightSideSigns[1])) // jesli input jest dwuargumentowy
            {
                String x;
                String y;
                String opX;
                String opY;

                y = stackSymbols.pop();
                x = stackSymbols.pop();
                opY = stackSigns.pop();
                opX = stackSigns.pop();

                if (opX.equals("Error")) { // jesli pop() zwroci error
                    System.out.println("INF: error");
                    return;
                }

                if (rankOfSigns[signsList.locate(opX)] < rankOfSigns[signsList.locate(input)]) { //sprwadzenie piorytetow operatorow
                    x = "( " + x + " )";
                }

                if ((rankOfSigns[signsList.locate(opY)] <= rankOfSigns[signsList.locate(input)]) &&
                        (!input.equals("="))) {
                    y = "( " + y + " )";
                }

                stackSymbols.push(x + " " + input + " " + y);
                stackSigns.push(input);
            } else { // jesli input jest jednoargumentowy
                String x;
                String opX;

                x = stackSymbols.pop();
                opX = stackSigns.pop();

                if (opX.equals("Error")) {
                    System.out.println("INF: error");
                    return;
                }

                if (rankOfSigns[signsList.locate(opX)] < rankOfSigns[signsList.locate(input)]) {
                    x = "( " + x + " )";
                }

                stackSymbols.push(input + " " + x);
                stackSigns.push(input);

            }
        }

        String result = stackSymbols.pop(); //zdjecie wyniku ze stosu

        if (!stackSymbols.isEmpty()) { // jesli po zdjeciu ze stostu wyniku stos nie jest pusty wyswietl error
            System.out.println("INF: error");
        } else {
            System.out.println("INF: " + result);
        }

    }

    /* Opis funkcji convertToONP

        funckja konwertujaca wyrazenie z notacji INF do notacji ONP.
        Do konwersji wykorzystano stos oraz liste:
            stack -> stos na ktory umieszczane sa operatory.
            outputList -> lista ktory sluzy do przedstawienia wynikow konwersji.
        Zmiena input przechowuje akutalnie sprawdzany element o dlugosci 1 z wyrazenia expression np. 'a', '+'.
        Algorytm konwersji wykonywany jest na nastepujacych krokach:
           Jezeli:
               input jest litera alfabetu -> dodaj input do outputList
               input jest nawiasem '(' -> poloz input na stosie.
               input jest operatorem lewostronie lacznym -> odloz ze stosu wszystkie operatory
                                                                   o piorytecie >= od input i dopisz je na outputList.
                                                                   Nastepnie poloz na stos input.
               input jest operatorem prawostonie lacznym -> odloz ze stosu wszystkie operatory
                                                                   o piorytecie > od input i dopisz je na outputList.
                                                                   Nastepnie poloz na stos input.
               input jest nawiasem ')' -> odloz ze stosu wszytkie elementy az do napotkania '(' i dodaj ze do outputList.
                                          Nawias '(' nie jest nigdzie zapisywany.

        Po analizie wszystich elementow wyrazenia, odluz ze stosu wszystko co sie na nim znajduje i dodaj na outputList.
        Wynik konwersji znajduje sie w liscie outputList.

     */
    public static void convertToONP(String expression) {
        List outputList = new List(256);
        StackString stack = new StackString(256);

        for (int i = 0; i < expression.length(); i++) {
            String input = Character.toString(expression.charAt(i));

            if (isLetter(input)) {
                outputList.insert(input, outputList.end()); //wstawienie na liste outputList zgodnie z algorytmem
            } else if (!isLetter(input)) {
                if (input.equals("(")) {
                    stack.push(input);
                } else if (input.equals(")")) {
                    while (!stack.top().equals("(")) {
                        outputList.insert(stack.pop(), outputList.end()); // sciagaj ze stostu dopki (
                    }

                    stack.pop(); // zdejmij ze stosu ( i nigdzie go nie zapisuj
                } else if (!input.equals(rightSideSigns[0]) && !input.equals(rightSideSigns[1]) && !input.equals(rightSideSigns[2]) && !input.equals(rightSideSigns[3])) // jesli input jest lewostronny
                {
                    while (!stack.isEmpty() && (rankOfSigns[signsList.locate(stack.top())] >= rankOfSigns[signsList.locate(input)])) {
                        outputList.insert(stack.pop(), outputList.end()); // dodaj do konca listy sciagniety elemnet
                    }

                    stack.push(input); // dodaj na stos operator


                } else { // jesli input jest prawostronny

                    while (!stack.isEmpty() && (rankOfSigns[signsList.locate(stack.top())] > rankOfSigns[signsList.locate(input)])) {
                        outputList.insert(stack.pop(), outputList.end()); // dodaj do konca listy sciagniety elemnet
                    }

                    stack.push(input); // dodaj na stos operator

                }

            }

        }

        while (!stack.isEmpty()) { // sciagnij elementy ze stosu dopoki nie bedzie pusty
            outputList.insert(stack.pop(), outputList.end());
        }

        System.out.print("ONP: ");
        outputList.display(); // wyswietlenie wyniku konwersji
    }

    /* Opis fukncji isINFExpressionCorrcet

        funkcja ta sprawdza poprawnosc zapisu wyrazenia w notacji INF w opraciu o automat skonczony.
        Automat ten jest szczegolnym przyadkiem maszyny turinga. Zawiera 3 stany q0, q1, q2.
        Jesli po analizie wyrazanie maszyna znjduje sie w stanie q1, oznacza to ze wyrazenie jest zapisane w sposob
        poprawny. Przejscia pomiedzy stanami warunkowane sa operatoramai, zmiennymi a takze nawiasami.
        Dodatkowo podczas dzialania maszyny zlicznane sa nawiasy, w celu sprawdzenia czy kazdy nawias otwieracjacy
        ma nawias zamykajacy do pary, oraz czy nie wystepuje wyrazenie typu ().

        Opis operatorow:
        operatory dwuargumentowe (op2) -> {^, *, /, %, +, -, <, >, ?, &, |, =}
        operatory jednoargumentowe -> {!, ~}

     */
    public static boolean isINFExpressionCorrect(String expression) {
        int state = 0; // stan q
        int countParenthesis = 0; // licznik nawiasow

        for (int i = 0; i < expression.length(); i++) {
            char input = expression.charAt(i); // aktualnie sprawdzany element z experssion
            switch (state) {
                case 0:
                    if (Character.isLetter(input)) state = 1;
                    else if (input == '~' || input == '!') state = 2;
                    else if (input == '(') {
                        state = 0;
                        countParenthesis++;
                    } else return false;
                    break;
                case 1:
                    if (isOp2(input)) state = 0;
                    else if (input == ')') {
                        state = 1;
                        countParenthesis--;
                    } else return false;
                    break;
                case 2:
                    if (input == '(') {
                        state = 0;
                        countParenthesis++;
                    } else if (input == '~' || input == '!') state = 2;
                    else if (Character.isLetter(input)) state = 1;
                    else return false;
                    break;
            }

            if (countParenthesis < 0) { // sygnalizacja zle wstawionego nawiasu
                return false;
            }
        }

        return state == 1 & countParenthesis == 0; //wyrazenie jest poprawne gdy licznik nawiasow wskazuje i maszyna
                                                   //jest w stanie q1

    }

    /* Opis funkcji isOp2

        funckja ta sprawdzy czy operator jest operatorem dwuarguemntowym.
     */
    public static boolean isOp2(char input)
    {
        switch (input)
        {
            case '^':
                return true;
            case '*':
                return true;
            case '/':
                return true;
            case '%':
                return true;
            case '+':
                return true;
            case '-':
                return true;
            case '<':
                return true;
            case '>':
                return true;
            case '?':
                return true;
            case '&':
                return true;
            case '|':
                return true;
            case '=':
                return true;

        }
        return false;
    }

    //main
    public static void main(String[] args) {

        int numberOfSets;
        String expression; //wyrazenie postaci np. a+b, ab+
        String typeOfExpression; //typ wyrazenia np. INF:, ONP:

        for (String sign : signs) {
            signsList.insert(sign, signsList.end()); // uzupelnienie listy znakami zgodnie z piorytetem
        }

        //input
        numberOfSets = Integer.parseInt(scn.nextLine());

        for (int i = 0; i < numberOfSets; i++) {
            typeOfExpression = scn.next();
            expression = scn.nextLine();

            if (typeOfExpression.equals("INF:")) {
                expression = expression.replaceAll("[^a-z()!~^*/%+\\-<>?&|=]", "");//usuniecie zbednych znakow
            } else if (typeOfExpression.equals("ONP:")) {
                expression = expression.replaceAll("[^a-z!~^*/%+\\-<>?&|=]", "");//usuniecie zbednych znakow
            }

            if (typeOfExpression.equals("INF:")) {
                if (isINFExpressionCorrect(expression)) {
                    convertToONP(expression);
                } else {
                    System.out.println("ONP: error");
                }
            } else {
                convertToINF(expression);
            }

        }

    }
}

//StosString
class StackString {

    private int maxArraySize;
    private String[] stackStringElements;
    private int currentSize;

    StackString(int maxArraySize) {
        this.maxArraySize = maxArraySize;
        this.stackStringElements = new String[this.maxArraySize];
        this.currentSize = 0;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void push(String element) {
        stackStringElements[currentSize] = element;
        currentSize++;
    }

    public String pop() {
        if (!isEmpty()) {
            currentSize--;
            return stackStringElements[currentSize];
        }
        return "Error";
    }

    public String top() {
        if (!isEmpty()) return stackStringElements[currentSize - 1];
        else return "Error";
    }

}

//Lista
class List {

    private final int maxSize;
    private String[] listElements;
    private int currentSize;

    List(int maxSize) {
        this.maxSize = maxSize;
        this.listElements = new String[this.maxSize];
        this.currentSize = 0;
    }

    public int end() {
        return currentSize;
    }

    public int locate(String searchElement) {
        for (int i = 0; i < currentSize; i++) {
            if (listElements[i].equals(searchElement)) {
                return i;
            }
        }
        return -1;
    }

    public void insert(String insertElement, int posToInsert) {
        if (posToInsert > currentSize || posToInsert < 0) {
            return;
        } else if (posToInsert == currentSize) {
            listElements[posToInsert] = insertElement;
            currentSize++;
            return;
        }

        for (int i = currentSize; i >= posToInsert; i--) {
            listElements[i + 1] = listElements[i];
        }

        listElements[posToInsert] = insertElement;
        currentSize++;

    }

    public void display() {
        for (int i = 0; i < currentSize; i++) {
            System.out.print(listElements[i] + " ");
        }
        System.out.print("\n");
    }

}

/*

10
INF: ()a
INF: )(
INF: a+b*
INF: a+b~
INF: a [,;] + c
ONP: (ab*cd**)
ONP: ab*cd**
ONP: ab+cd*/
//ONP: ab+++
//ONP: ~a


/*
ONP: error
ONP: error
ONP: error
ONP: error
ONP: a c +
INF: a * b * ( c * d )
INF: a * b * ( c * d )
INF: ( a + b ) / ( c * d )
INF: error
INF: error

*/
