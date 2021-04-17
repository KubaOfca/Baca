//Jakub Baran - 8
/*
    Program sluzy do wykonania symualacji stacji pociagow. Pociagi reprezentuje lista jednokieunkowa, natomiast
    lista wagonow jest lista dwukierunkowa cykliczna, gdzie referencja na pierwszy element listy wagonow znajduje
    sie w wezle pociagu do ktorego te wagony "naleza". Na liscie pociagow oraz wagonow wykonywane sa rozne komendy min:
    dodawanie pociagu, usuwanie wagonu z poczatku i konca, wyswietlanie wagonow, odwaracanie wagonow itd. Program
    dziala wa czasie O(1) z wykluczeniem funkcji pomocniczych oraz funkcji Display i Trains ktoych zlozonosc to O(n).
 */

import java.util.Scanner;

public class Source {

    public static Scanner scn = new Scanner(System.in);
    public static TrainLinkedList trainsList;

    public static void main(String[] args) {
        int numberOfSets = scn.nextInt();

        for (int i = 0; i < numberOfSets; i++) {
            int numberOfCommands = scn.nextInt();
            trainsList = new TrainLinkedList();

            for (int j = 0; j < numberOfCommands; j++) {
                String commandName = scn.next();
                callCommand(commandName);
            }
        }
    }


    public static void callCommand(String commandName) {

        switch (commandName) {
            case "New":
                trainsList.newTrain(scn.next(),scn.next());
                break;
            case "InsertFirst":
                trainsList.insertFirst(scn.next(),scn.next());
                break;
            case "InsertLast":
                trainsList.insertLast(scn.next(),scn.next());
                break;
            case "Display":
                trainsList.display(scn.next());
                break;
            case "Trains":
                trainsList.trains();
                break;
            case "Reverse":
                trainsList.reverse(scn.next());
                break;
            case "Union":
                trainsList.union(scn.next(),scn.next());
                break;
            case "DelFirst":
                trainsList.delFirst(scn.next(),scn.next());
                break;
            case "DelLast":
                trainsList.delLast(scn.next(),scn.next());
                break;

        }
    }

}

//Struktury danych

//Wezel Pociagu
class TrainNode {
    public String name;
    public CarriageNode first; //referencja do pierwszego elementu listy wagonow
    public TrainNode next; //referencja do nastepenej komorki w liscie pociagow

    public TrainNode(String name) {
        this.name = name;
    }
}

//Wezel Wagonu
class CarriageNode {
    public String name;
    public CarriageNode next; //referencja do nastepenej komorki w liscie wagonow
    public CarriageNode prev; //referencja do poprzedniej komorki w liscie wagonow

    public CarriageNode(String name) {
        this.name = name;
    }
}

//Lista wiazana pocigow
class TrainLinkedList {
    public TrainNode trains; //referencja do pierwszego elementu listy pociagow

    public TrainLinkedList() {
        trains = null;
    }

    /*
        Ponizsza funkcja pozwala na wstawienie nowego pociagu o zadanej nazwie na poczatek listy. Wstawaimy nowy pociag na pocztek listy
        w celu zapewnienia minimalnej liczby przejsc po liscie. Pociag zostaje wstawiony wraz z przypisanym mu wagonemo zadanej nazwie.
     */
    public void newTrain(String trainName, String carriageName) {
        TrainNode newTrain;
        newTrain = findTrain(trainName); //wyszukanie czy pociag o zadanej nazwie nie istnieje obecnie w liscie.

        if (newTrain != null) { // jesli funkcja findTrain nie zwroci wartosci null wiemy iz pociag o zadanej nazwie znajduje sie juz w lisice.
            System.out.println("Train " + trainName + " already exists");
            return;
        }

        newTrain = new TrainNode(trainName); //utworzenie obiektu klasy pociag
        newTrain.next = trains; // ustawienie parametru next na obiekt na ktory obecnie wskazuje trains
        trains = newTrain; // ustawienie trains jako referencji do nowo utworzeonego obiektu pociagu. Od teraz pierwszym pociagiem w liscie jest nowy pociag

        //dodanie do nowego pociagu wagonu ktorego next i prev skazuje na samego siebie, poniewaz jest jedynym elementem w lisice
        newTrain.first = new CarriageNode(carriageName);
        newTrain.first.next = newTrain.first;
        newTrain.first.prev = newTrain.first;
    }

    /*
        Ponizsza funkcja pozwala na wyszukanie pociagu o zadanej nazwie w liscie pociagow. Wartosc jaka zwraca funkcja jest referencja do szukanego pociagu
     */
    public TrainNode findTrain(String trainName) {
        TrainNode cursour = trains; // zmienna cursour pozwala na przechodzenie po obiektach listy wiazanej pociagow

        //dopuki nie dojdziemy do konca listy pociagow lub nie znajdziemy pociagu o zadanej nazwie wykonujemy petle while umozliwiajaca przejscie po liscie pociagow.
        while (cursour != null && !cursour.name.equals(trainName)) {
            cursour = cursour.next; // przejscie do nastepnego elementu listy
        }

        return cursour;
    }

    /*
        Ponisza funkcja pozwala na wyszukanie dwoch pociagow o zadanych nazwach w liscie pociagow. Funkcja wykonuje tylko jedno przejscie po liscie, stad potrzeba utworzenia
        tablicy zmiennych.
     */
    public TrainNode[] findTrain(String trainName1, String trainName2) {
        TrainNode cursour = trains;
        TrainNode[] train = new TrainNode[]{null, null}; // utworzenie tablicy o klasie wezel pociagu, ktora umozlwia zapamietanie referencji do dwoch pociagow wykorzystujac
        //pojedyncze przejscie po liscie

        //petla while bedzie sie wykonywala albo gdy cursour osiagie koniec listy lub gdy tablica train zostanie wypelniona elementami innymi niz wartosc null.
        while(!(train[0] != null && train[1] != null) && cursour != null)
        {
            if(cursour.name.equals(trainName1)) {
                train[0] = cursour;
            }
            if(cursour.name.equals(trainName2)) {
                train[1] = cursour;
            }
            cursour = cursour.next;

        }
        //funkcja zwaraca tablice train gdzie: train[0] - przechowuje referencje do pociagu o nazwie trainName1
        //                                     train[1] - przechowuje referencje do pociagu o nazwie trainName2
        return train;
    }

    /*
        Ponizsza funkcja umozliwia wypisanie wagonow ktore naleza do pocaigu o zadanej nazwie.
     */
    public void display(String trainName) {
        TrainNode cursour;
        cursour = findTrain(trainName);

        //komunikat o bladzie, gdyz uzytkownik probuje wypisac wagonu pociagu ktory nie istnieje na liscie.
        //cursour pokazuje wartosc null poniewaz nie udalo sie znalesc referencji do pociagu o zadanej nazwie
        if (cursour == null) {
            System.out.println("Train " + trainName + " does not exist");
            return;
        }

        CarriageNode cursourCarriage = cursour.first; // cursourCarriage ustawiony jest na 1 element listy wagonow i posluzy do przejscia po kazdym obiekcie tej listy
        System.out.print(trainName + ":"); // wypisannie nazwy pociagu, ktorego wagony chcemy wypisac

        //zastosowano petle do while poniewaz koniec listy cyklicznej jaka jest lista wagonow wyznacza fakt iz cursour wskazuje na element na ktory wskazuje zmienna first.
        //Petla do while ma wiec sense poniewaz wykona sie zawsze przynajmniej raz bezwzgldu na warunek petli. Cursour na poczatku musi wskazywac na first wiec gdyby zastosowano
        //petle while nie wykonala by sie ona ani razu.
        do{
            /*
                warunek ten odnosi sie do przyadku gdy przed wywolaniem funkcji display dokonano wykonania funkcji reverse. Aby poprawnie wypisac liste wagonow potrzebna jest "naprawa"
                referencji poszczegolnych wagonow. Taki wagon wykrywany jest gdy znajdujemy sie na wagonie ktory jest poprawny i sprobojemy odwowal sie do elementu w sposob next.prev.
                Takie odwolanie powino nam wskazac wagon na ktorym aktulanie sie znadujemy. Jesli tak nie jest oznacza sie ze nastepny wagon po tym ktory aktualnie wskazuje cursourCarriage
                jest niepoprawnie przedstawiony. Stosujemy wiec w ramach "naprawy" zabieg swap() zamiany referencji next z referencja prev wagonu, ktory jest nastepny po obiektie na ktory wskazuje
                cursourCarriage. Po takim zabiegu mamy pewnosc ze wagon wskaze nam poprawnie element do ktorego powinien sie odwolywac.
             */
            if(cursourCarriage.next.prev != cursourCarriage)
            {
                swap(cursourCarriage.next);
            }
            System.out.print(" " + cursourCarriage.name); // wyspianie odpowiedniej nazwy wagonu na ekran
            cursourCarriage = cursourCarriage.next; // przejscie do kolejnego wagonu
        }while(cursourCarriage != cursour.first);

        System.out.println();
    }

    /*
        Ponizsza funkcja sluzy do wykonania swap poniedzy referencja next i prev w wagonie.
     */
    public void swap(CarriageNode cursourCarriage)
    {
        CarriageNode tmp = cursourCarriage.next; // zmienna tmp utworzona w celu zapobiegniecia utraty referencji. Wykonywany jest tutaj tzn swap wiec musimy w zmienej tymczasowej
        //zachowac jedna z zmienianych wartosci.
        cursourCarriage.next = cursourCarriage.prev;
        cursourCarriage.prev = tmp;
    }

    /*
        Ponizsza funkcja sluzy do wstawienie wagonu na poczatek listy wagonow w zadanym pociagu
     */
    public void insertFirst(String trainName, String carriageName) {
        TrainNode cursour;
        cursour = findTrain(trainName);

        if (cursour == null) {
            System.out.println("Train " + trainName + " does not exist");
            return;
        }

        CarriageNode newCarriage = new CarriageNode(carriageName);
        newCarriage.next = cursour.first; //ustawienie next nowego elementu na element na ktory wskazuje aktualnie first
        newCarriage.prev = cursour.first.prev; // ustawienie preva nowego elementu na ostatni element w liscie
        cursour.first.prev.next = newCarriage; // ustawienie next ostatniego elementu na nowy dodany element
        cursour.first.prev = newCarriage; // ustawienie prev elementu na ktory akutalnie wskazuje first na nowy element
        cursour.first = newCarriage; // ustawienie first na nowy element. Nowy element jest teraz pierwszym elementem w liscie.
    }

    /*
    Ponizsza funkcja sluzy do wstawienie wagonu na koniec listy wagonow w zadanym pociagu
    */
    public void insertLast(String trainName, String carriageName) {
        TrainNode cursour;
        cursour = findTrain(trainName);

        if (cursour == null) {
            System.out.println("Train " + trainName + " does not exist");
            return;
        }

        CarriageNode newCarriage = new CarriageNode(carriageName);
        cursour.first.prev.next = newCarriage; // ustawienie nexta ostatniego elementu na nowy element
        newCarriage.prev = cursour.first.prev; // ustawieenie preva nowego elementu na aktualny ostatni element w liscie
        newCarriage.next = cursour.first; // ustawienie nexta noewgo elementu na kolejny element w liscie
        cursour.first.prev = newCarriage; // ustawienie preva pierwszego elementu na nowy element.Teraz ostatnim elementem w liscie jest nowo utworzony element.
    }
    /*
        Ponizsza funkcja sluzy do wypisania wszystich pociagow w liscie.
     */
    public void trains() {
        TrainNode cursour = trains; // cursour ustawiony na pierwszy element listy pociagow

        if (cursour == null) {
            System.out.print("Trains: "); // jesli lista pociagow jest pusta wypisz na ekran tylko napis Trains:
        } else {
            System.out.print("Trains:");
            while (cursour != null) {
                System.out.print(" " + cursour.name);
                cursour = cursour.next;
            }


        }
        System.out.println();

    }

    public void reverse(String trainName) {
        TrainNode cursour;
        cursour = findTrain(trainName);

        if(cursour == null)
        {
            System.out.println("Train " + trainName + " does not exist");
            return;
        }

        swap(cursour.first.prev); // zamiana referencji next i preva ostaniego elementu listy. Najpierw dokonujemy zamiany
        //ostatniego elementu poniewaz po swap() na pierwszym elemencie first.prev nie wskaujze juz na ostatni element.
        swap(cursour.first); // zamiana referencji next i preva pierwszego elementy listy

        cursour.first = cursour.first.next;

    }

    /*
        Ponizsza funkcja sluzy do scalenia dwoch pociagow w jeden pociag. Wagony trainName2 sa dolaczane na koniec trainName1 a nastepnie trainName2 usuwany jest z listy pociagow
     */
    public void union(String trainName1, String trainName2) {
        TrainNode[] cursor;
        cursor = findTrain(trainName1, trainName2);

        if (cursor[0] == null) {
            System.out.println("Train " + trainName1 + " does not exist");
            return;
        }

        if (cursor[1] == null) {
            System.out.println("Train " + trainName2 + " does not exist");
            return;
        }

        CarriageNode tmp = cursor[0].first.prev; // zapamietanie ostatniego elementu w liscie wagonow pociagu trainName1
        cursor[0].first.prev.next = cursor[1].first; // ustawienie nexta ostatniego elementu na liscie wagonow pociagu trainName1 na poczatek listy wagonow pociagu trainName2
        cursor[1].first.prev.next = cursor[0].first; // ustawienie nexta ostatniego elementu na liscie wagonow pociagu trainName2 na poczatek listy wagonow pociagu trainName1
        cursor[0].first.prev = cursor[1].first.prev; // ustawienie prev pierwszego elementu na liscie wagonow pociagu trainName1 na ostatni element listy wagonow pociagu trainName2
        cursor[1].first.prev = tmp; // ustawienie prev pierwszego elementu na liscie wagonow pociagu trainName2 na ostatni element listy wagonow pociagu trainName1

        deleteTrain(trainName2); // usuniecie pociagu trainName2 z listy pociagow
    }

    /*
        Ponizsza funkcja sluzy do usuniecia pociagu z listy pociagow
     */
    public void deleteTrain(String trainToDelete) {
        if (trains.name.equals(trainToDelete)) {
            trains = trains.next; // jezeli pociag ten jest na poczatku listy wystarczy ustawic referencje trains na kolejny pociag z listy
        }
        else {
            TrainNode cursor = trains;
            //petla sluzaca do znalezienia pociagu ktory znajduje sie przed pociagiem ktory chcemy usunac
            while (!cursor.next.name.equals(trainToDelete)) {
                cursor = cursor.next;
            }

            cursor.next = cursor.next.next; // nexta pociagu znajdujacego sie przed pociagiem ktory chcemy usunac ustawiamy na next pociagu ktory chcemy usunac.
        }
    }

    /*
        Ponizsza funckja sluzy do usuniecia z zadanego pociagu pierwszego wagonu i stworzeniu nowego
        pociagu o zadanej nazwie z wagonem ktory zostal usuniety z pociagu.
     */
    public void delFirst(String trainName1, String trainName2) {

        TrainNode[] cursor;
        cursor = findTrain(trainName1, trainName2);

        if (cursor[0] == null) {
            System.out.println("Train " + trainName1 + " does not exist");
            return;
        }

        if (cursor[1] != null) {
            System.out.println("Train " + trainName2 + " already exists");
            return;
        }

        newTrain(trainName2, cursor[0].first.name); // utworzenie nowego pociagu z wagonem

        //jezli po usunieciu wagonu z pociagu pociag nadal ma wagony wykoujemy ponizsze instrukcje, nastomiast jesli nie
        //usuwamy pociag z listy
        if (cursor[0].first.next != cursor[0].first && cursor[0].first.prev != cursor[0].first) {
            if(cursor[0].first.next.prev != cursor[0].first) // jesli usuwamy wagon z pociagu ktory jest odwrocony musimy dokonac zamiany next i preva wagonu nastepnego po wagonie pierwszym
            {
                swap(cursor[0].first.next);
            }
            cursor[0].first.next.prev = cursor[0].first.prev; // ustawiamy preva wagonu ktory znajduje sie przed pierwszym wagonem na wagon ostani
            cursor[0].first.prev.next = cursor[0].first.next; // ustawiamy nexta ostatniego wagonu na wagon ktory znajudje sie przed pierwszym wagonem
            cursor[0].first = cursor[0].first.next; // ustawiamy first na element ktory znajdowal sie obecnie przed firstem

        }
        else {

            deleteTrain(trainName1);
        }

    }

    /*
    Ponizsza funckja sluzy do usuniecia z zadanego pociagu ostatniego wagonu i stworzeniu nowego
    pociagu o zadanej nazwie z wagonem ktory zostal usuniety z pociagu.
    */
    public void delLast(String trainName1, String trainName2) {
        TrainNode[] cursor;
        cursor = findTrain(trainName1, trainName2);

        if (cursor[0] == null) {
            System.out.println("Train " + trainName1 + " does not exist");
            return;
        }

        if (cursor[1] != null) {
            System.out.println("Train " + trainName2 + " already exists");
            return;
        }

        newTrain(trainName2, cursor[0].first.prev.name);
        //jezli po usunieciu wagonu z pociagu pociag nadal ma wagony wykoujemy ponizsze instrukcje, nastomiast jesli nie
        //usuwamy pociag z listy
        if (cursor[0].first.next != cursor[0].first && cursor[0].first.prev != cursor[0].first) {
            if(cursor[0].first.next.prev != cursor[0].first)// jesli usuwamy wagon z pociagu ktory jest odwrocony musimy dokonac zamiany next i preva wagonu przed wagonem ostatnim
            {
                swap(cursor[0].first.prev.prev);
            }
            cursor[0].first.prev = cursor[0].first.prev.prev; //ustawiamy prev pierwszego wagonu na przed ostatni wagon
            cursor[0].first.prev.next = cursor[0].first; // ustawiamy preva ostatniego wagonu (poprzednio przed ostatniego) a wagon pierwszy wagon

        }
        else {
            deleteTrain(trainName1);
        }

    }

}


/*
Testy Input:
1
14
New T1 W1
InsertLast T1 W2
InsertLast T1 W3
InsertLast T1 W4
New T2 W5
InsertLast T2 W6
InsertLast T2 W7
Reverse T2
Union T1 T2
Display T1
DelFirst T1 T2
Display T1
Display T2
Trains

Output:
T1: W1 W2 W3 W4 W7 W6 W5
T1: W2 W3 W4 W7 W6 W5
T2: W1
Trains: T2 T1

 */

