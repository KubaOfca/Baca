//Jakub Baran - 8
/*Idea dzialania programu

 */

import java.util.Scanner;

class Source {
    public static Scanner scn = new Scanner(System.in);

    private static final String[] signs = new String[] {".", "(", ")", "!", "~", "^", "%", "/", "*", "-", "+", ">", "<", "?", "&", "|", "="}; //index 0 najmnieszy piorytet !
    private static final int[] rankOfSign = new int[]{9,   0,   0,   8,   8,   7,   6,   6,   6,   5,   5,   4,   4,   3,   2,   1, 0};
    private static final String[] rightSide = new String[]{"!", "~", "=", "^"};
    private static List signsList = new List(256);

    public static boolean isLetter(String input)
    {
        return input.matches("[a-z]");
    }

    public static void convertToINF(String expression) {
        StackString stackSymbols = new StackString(256);
        StackString stackSigns = new StackString(256);

        for (int i = 0; i < expression.length(); i++) {
            String input = Character.toString(expression.charAt(i));

            if (isLetter(input)) {
                stackSymbols.push(input);
                stackSigns.push(".");
            } else if (!input.equals(rightSide[0]) &&
                       !input.equals(rightSide[1])) // kiedy input lewo storny
            {
                String x;
                String y;
                String opX;
                String opY;

                y = stackSymbols.pop();
                x = stackSymbols.pop();
                opY = stackSigns.pop();
                opX = stackSigns.pop();

                if (opX.equals("1")) {
                    System.out.println("INF: error");
                    return;
                }

                if (rankOfSign[signsList.locate(opX)] < rankOfSign[signsList.locate(input)]) {
                    x = "( " + x + " )";
                }

                if ((rankOfSign[signsList.locate(opY)] <= rankOfSign[signsList.locate(input)]) &&
                        (!input.equals("="))) {
                    y = "( " + y + " )";
                }

                stackSymbols.push(x + " " + input + " " + y);
                stackSigns.push(input);
            } else if (input.equals(rightSide[0]) ||
                    input.equals(rightSide[1])) {
                String x;
                String opX;

                x = stackSymbols.pop();
                opX = stackSigns.pop();

                if (opX.equals("1")) {
                    System.out.println("INF: error");
                    return;
                }

                if (rankOfSign[signsList.locate(opX)] < rankOfSign[signsList.locate(input)]) {
                    x = "( " + x + " )";
                }

                stackSymbols.push(input + " " + x);
                stackSigns.push(input);

            }
        }

        String result = stackSymbols.pop();

        if (!stackSymbols.isEmpty()) {
            System.out.println("INF: error");
        } else {
            System.out.println("INF: " + result);
        }

    }

    public static void convertToONP(String expression) {
        List outputList = new List(256); // rozmiar moze byc nie taki
        StackString stack = new StackString(256);

        for (int i = 0; i < expression.length(); i++) {
            String input = Character.toString(expression.charAt(i)); // poprawa czytelnosci ??

            if (isLetter(input)) {
                outputList.insert(input, outputList.end()); // 1 arg to np 'a' a end to pozycja na ktora mozna wstawic
            } else if (!isLetter(input)) {
                if (input.equals("(")) {
                    stack.push(input);
                } else if (input.equals(")")) {
                    while (!stack.top().equals("(")) {
                        outputList.insert(stack.pop(), outputList.end()); // sciagaj ze stostu dopki (
                    }

                    stack.pop(); // zdejmij ze stosu ( i nigdzie go nie zapisuj
                } else if (!input.equals(rightSide[0]) && !input.equals(rightSide[1]) && !input.equals(rightSide[2]) && !input.equals(rightSide[3])) // jesli jest lewo
                {
                    //1.sprawdz jaki element jest na szczycie
                    //2. sprawdz jaki ma index w tabeli signs
                    //3. sprawdz jaka wartosc kryje sie pod tym indexem w tabeli rank


                    while (!stack.isEmpty() && (rankOfSign[signsList.locate(stack.top())] >= rankOfSign[signsList.locate(input)])) {
                        outputList.insert(stack.pop(), outputList.end()); // dodaj do konca listy sciagniety elemnet
                    }

                    stack.push(input); // dodaj na stos element op1


                } else if (input.equals(rightSide[0]) || input.equals(rightSide[1]) || input.equals(rightSide[2]) || input.equals(rightSide[3])) {

                    while (!stack.isEmpty() && (rankOfSign[signsList.locate(stack.top())] > rankOfSign[signsList.locate(input)])) {
                        outputList.insert(stack.pop(), outputList.end()); // dodaj do konca listy sciagniety elemnet
                    }

                    stack.push(input); // dodaj na stos element op1

                }

            }

        }

        while (!stack.isEmpty()) {
            outputList.insert(stack.pop(), outputList.end());
        }

        System.out.print("ONP: ");
        outputList.display();
    }

    public static boolean isINFExpressionCorrect(String expression) {
        int state = 0;
        int countParenthesis = 0;

        for (int i = 0; i < expression.length(); i++) {
            char input = expression.charAt(i); // aktualnie sprawdzany element z expersion
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

            if (countParenthesis < 0) {
                return false;
            }
        }

        return state == 1 & countParenthesis == 0;

    }

    public static boolean isOp2(char input) {
        return switch (input) {
            case '^' -> true;
            case '*' -> true;
            case '/' -> true;
            case '%' -> true;
            case '+' -> true;
            case '-' -> true;
            case '<' -> true;
            case '>' -> true;
            case '?' -> true;
            case '&' -> true;
            case '|' -> true;
            case '=' -> true;
            default -> false;
        };
    }

    //main--
    public static void main(String[] args) {

        int numberOfSets;
        String expression;
        String typeOfExpression;

        for (String sign : signs) {
            signsList.insert(sign, signsList.end()); // uzupelnienie listy znakami zgodnie z rankingiem
        }

        //input---
        numberOfSets = Integer.parseInt(scn.nextLine());

        for (int i = 0; i < numberOfSets; i++) {
            typeOfExpression = scn.next();
            expression = scn.nextLine();

            if (typeOfExpression.equals("INF:")) {
                expression = expression.replaceAll("[^a-z()!~^*/%+\\-<>?&|=]", "");//usuniecie znakow
            } else if (typeOfExpression.equals("ONP:")) {
                expression = expression.replaceAll("[^a-z!~^*/%+\\-<>?&|=]", "");//usuniecie znakow
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

        }//koniec petli dzialania programu


    }
}

//StosString--
class StackString {

    private int maxArraySize;
    private String[] stackStringElements;
    private int currentSize;

    StackString(int maxArraySize) {
        this.maxArraySize = maxArraySize;
        this.stackStringElements = new String[this.maxArraySize];
        this.currentSize = 0;
    }

    public void display() {
        for (int i = 0; i < currentSize; i++) {
            System.out.print(stackStringElements[i] + " ");
        }
        System.out.print("\n");
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
        return "1"; // cos nie tak
    }

    public String top() {
        if (!isEmpty()) return stackStringElements[currentSize - 1];
        else return "0";// cos nie tak
    }

}

//Lista---
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

    public int first() {
        return 0;
    }

    public int locate(String searchElement) {
        for (int i = 0; i < currentSize; i++) {
            if (listElements[i].equals(searchElement)) {
                return i;
            }
        }
        return -1; // czyli ze nie ma
    }

    public void insert(String insertElement, int posToInsert) {
        if (posToInsert > currentSize || posToInsert < 0) {
            return; // ?? moze cos innego niz puste return ?
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

    public void delete(int posToDelete) {
        if (posToDelete < 0 || posToDelete >= currentSize) {
            return; // bo nie ma takiej pozycji
        }

        for (int i = posToDelete; i < currentSize - 1; i++) {
            listElements[i] = listElements[i + 1];
        }
        currentSize--;
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
