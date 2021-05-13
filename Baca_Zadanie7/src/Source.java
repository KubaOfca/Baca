//Jakub Baran - 8

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

class Source {
    public static DecimalFormat format = new DecimalFormat("0.####"); // ustalenie formatowania wyswietlanego tekstu

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        scn.useLocale(Locale.GERMAN);
        int numberOfSets = Integer.parseInt(scn.next());

        for (int set = 0; set < numberOfSets; set++) {

            int columns = Integer.parseInt(scn.next());
            int rows = Integer.parseInt(scn.next());
            float[][] tsvTable = new float[rows][columns]; // tabela przechowujaca "plik tsv" czyli podane wartosci
            scn.nextLine();//zabieg stosowany aby po uzyciu next przeniesc sie do nowej lini, a nie pozostac w tej samej.
            String[] tsvColumnName = scn.nextLine().split("\t"); // tablica String przechowujaca nazwy kolumn

            for (int i = 0; i < rows; i++) { // uzuplenienie tablicy wartoscami
                for (int j = 0; j < columns; j++) {
                    tsvTable[i][j] = scn.nextFloat();
                }
            }

            int numberOfCommands = scn.nextInt();
            scn.nextLine();

            for (int i = 0; i < numberOfCommands; i++) {
                String modifier = scn.next(); // all lub single - modyfikator wyswietlania wynikow tj. albo calej tabeli albo pojedynczej kolumny
                String SortByColumn = scn.next(); // nazwa kolumny wedlug ktorej odbywa sie sortowanie
                boolean find = false; // informacja czy dana kolumna istnieje w tabeli tsv
                for (int j = 0; j < tsvColumnName.length; j++) { // wyszukanie indeksu kolumny ktora ma byc posortowana
                    if (tsvColumnName[j].equals(SortByColumn)) {
                        find = true;
                        quickSort(tsvTable, 0, rows - 1, j); // zastosowanie quick sort
                        displayTsvTable(tsvColumnName, tsvTable, columns, rows, modifier, SortByColumn, j);
                        break;
                    }
                }
                if(find == false){
                    System.out.println("$ " + modifier + " " + SortByColumn);
                    System.out.println("invalid column name: " + SortByColumn);
                }

            }

        }

    }

    //QuickSort---
    public static void quickSort(float[][] arrToSort, int l, int r, int col) {
        int size = arrToSort.length;
        int partition = 0;
        int end = 0;
        int tempR = r;

        while (true) {
            end--;
            while (l < tempR) {
                if (tempR - l + 1 <= 20) {
                    insertionSort(arrToSort, l, tempR, col);
                    l = tempR;

                } else {
                    partition = partition(arrToSort, l, tempR, col);
                    arrToSort[tempR][col] = -arrToSort[tempR][col];
                    tempR = partition - 1;
                    ++end;
                }

            }
            if (end < 0)
                break;
            l++;
            tempR = findNextR(arrToSort, l, size, col);
            arrToSort[tempR][col] = -arrToSort[tempR][col];
        }
    }

    //   public static void insertionSort(float[][] arr, )

    private static int findNextR(float[][] arr, int l, int size, int col) {
        for (int i = l; i < size; ++i) {
            if (arr[i][col] < 0)
                return i;
        }
        return size - 1;
    }

    private static int partition(float[][] arr, int l, int r, int col) {
        float pivot = arr[(l + r) / 2][col];
        while (l <= r) {
            while (arr[r][col] > pivot)
                r--;
            while (arr[l][col] < pivot)
                l++;
            if (l <= r) {
                swapRows(arr, l, r);
                l++;
                r--;
            }
        }
        return l;
    }

    public static void insertionSort(float[][] arr, int l, int r, int col) {
        int n = r;
        for (int i = l + 1; i <= n; ++i) {
            float[] key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j][col] > key[col]) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public static void swapRows(float[][] arr, int l, int r) {
        float[] tmp = arr[r];
        arr[r] = arr[l];
        arr[l] = tmp;

    }


    //Funkcje pomocnicze---
    public static void displayTsvTable(String[] tsvColumnName, float[][] tsvTable, int columns, int rows, String modifier, String SortByColumn, int singleColumn) {

        System.out.println("$ " + modifier + " " + SortByColumn);

        if (modifier.equals("all")) {
            for (int i = 0; i < tsvColumnName.length; i++) {
                System.out.print(tsvColumnName[i] + "\t");
            }
            System.out.println();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    System.out.print(format.format(tsvTable[i][j]) + "\t");
                }
                System.out.println();
            }
        } else if (modifier.equals("single")) {
            System.out.println(SortByColumn);
            for (int i = 0; i < rows; i++) {
                System.out.println(format.format(tsvTable[i][singleColumn]));
            }
        }

    }

}

/* Testy input
1
1 50
a
96
81
37
42
12
73
2
52
79
7
39
64
43
46
83
20
51
11
82
8
78
80
35
58
77
14
21
93
38
9
54
23
75
66
45
62
55
72
89
28
15
90
85
33
29
50
34
68
76
31
1
all a


 */

/* testy output
$ all a
a
2
7
8
9
11
12
14
15
20
21
23
28
29
31
33
34
35
37
38
39
42
43
45
46
50
51
52
54
55
58
62
64
66
68
72
73
75
76
77
78
79
80
81
82
83
85
89
90
93
96

Process finished with exit code 0



 */