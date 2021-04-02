//Jakub Baran - 8
import java.util.Scanner;

class Source {
    public static Scanner scn = new Scanner(System.in);
    
    private static char[] signs = new char[]   {'.','(',')','!','~','^','%','/','*','-','+','>','<','?','&','|','='}; //index 0 najmnieszy piorytet !
    private static int[] rankOfSign = new int[]{  9,  0,  0,  8,  8,  7,  6,  6,  6,  5,  5,  4,  4,  3,  2,  1, 0};
    private static char[] rightSide = new char[] {'!','~','=','^'};
    static List signsList = new List(256);

    public static void ConvertToINF(String expression)
    {
       List outputList = new List(256);
       StackString stackSymbols = new StackString(256);
       Stack stackSigns = new Stack(256);

       for(int i=0; i<expression.length(); i++)
       {
           String input = Character.toString(expression.charAt(i)) ;

           if(Character.isLetter(expression.charAt(i)))
           {
               stackSymbols.push(input);
               stackSigns.push('.');
           }
           else if(expression.charAt(i) != rightSide[0] &&
                   expression.charAt(i) != rightSide[1]) // kiedy input lewo storny
           {
               String x;
               String y;
               char opX;
               char opY;

               y = stackSymbols.pop();
               x = stackSymbols.pop();
               opY = stackSigns.pop();
               opX = stackSigns.pop();

               if(opX == '1')
               {
                   System.out.println("INF: error");
                   return;
               }

               if(rankOfSign[signsList.locate(opX)] < rankOfSign[signsList.locate(expression.charAt(i))])
               {
                   x = "( " + x + " )";
               }

               if((rankOfSign[signsList.locate(opY)] <= rankOfSign[signsList.locate(expression.charAt(i))]) &&
               (expression.charAt(i) != '='))
               {
                   y = "( " + y + " )";
               }

               stackSymbols.push(x + " " + input + " " + y);
               stackSigns.push(expression.charAt(i));
           }
           else if(expression.charAt(i) == rightSide[0] ||
                   expression.charAt(i) == rightSide[1])
           {
               String x;
               char opX;

               x = stackSymbols.pop();
               opX = stackSigns.pop();

               if(opX == '1')
               {
                   System.out.println("INF: error");
                   return;
               }

               if(rankOfSign[signsList.locate(opX)] < rankOfSign[signsList.locate(expression.charAt(i))])
               {
                   x = "( " + x + " )";
               }

               stackSymbols.push(input + " " + x);
               stackSigns.push(expression.charAt(i));

           }
       }

       String result = stackSymbols.pop();

       if(!stackSymbols.isEmpty())
       {
           System.out.println("INF: error");
       }
       else
       {
           System.out.println("INF: " + result);
       }

    }

    public static void ConvertToONP(String expression)
    {
        List outputList = new List(256); // rozmiar moze byc nie taki
        Stack stack = new Stack(256);

        for(int i=0; i<expression.length(); i++)
        {
            char input = expression.charAt(i); // poprawa czytelnosci ??

            if(Character.isLetter(input))
            {
                outputList.insert(input, outputList.end()); // 1 arg to np 'a' a end to pozycja na ktora mozna wstawic
            }
            else if(!Character.isLetter(input))
            {
                if(input == '(')
                {
                    stack.push(input);
                }
                else if(input == ')')
                {
                    while (stack.top() != '(')
                    {
                        outputList.insert(stack.pop(), outputList.end()); // sciagaj ze stostu dopki (
                    }

                    stack.pop(); // zdejmij ze stosu ( i nigdzie go nie zapisuj
                }
                else if(input != rightSide[0] && input != rightSide[1] && input != rightSide[2] && input != rightSide[3]) // jesli jest lewo
                {
                    //1.sprawdz jaki element jest na szczycie
                    //2. sprawdz jaki ma index w tabeli signs
                    //3. sprawdz jaka wartosc kryje sie pod tym indexem w tabeli rank


                    while(!stack.isEmpty() && (rankOfSign[signsList.locate(stack.top())] >= rankOfSign[signsList.locate(input)]))
                    {
                        outputList.insert(stack.pop(), outputList.end()); // dodaj do konca listy sciagniety elemnet
                    }

                    stack.push(input); // dodaj na stos element op1


                }
                else if(input == rightSide[0] || input == rightSide[1] || input == rightSide[2] || input == rightSide[3])
                {

                    while(!stack.isEmpty() && (rankOfSign[signsList.locate(stack.top())] > rankOfSign[signsList.locate(input)]))
                    {
                        outputList.insert(stack.pop(), outputList.end()); // dodaj do konca listy sciagniety elemnet
                    }

                    stack.push(input); // dodaj na stos element op1

                }

            }

        }

        while (!stack.isEmpty())
        {
            outputList.insert(stack.pop(), outputList.end());
        }

        System.out.print("ONP: ");
        outputList.display();
    }

    public static boolean IsINFExpressionCorrect(String expression)
    {
        int state = 0;
        int countParenthesis = 0;

        for(int i=0; i < expression.length(); i++)
        {
            char input = expression.charAt(i); // aktualnie sprawdzany element z expersion
            switch (state)
            {
                case 0:
                    if(Character.isLetter(input)) state = 1;
                    else if(input == '~' || input == '!') state = 2;
                    else if(input == '(')
                    {
                        state = 0;
                        countParenthesis++;
                    }
                    else return false;
                    break;
                case 1:
                    if(isOp2(input)) state = 0;
                    else if(input == ')')
                    {
                        state = 1;
                        countParenthesis--;
                    }
                    else return false;
                    break;
                case 2:
                    if(input == '(')
                    {
                        state = 0;
                        countParenthesis++;
                    }
                    else if(input == '~' || input == '!') state = 2;
                    else if(Character.isLetter(input)) state = 1;
                    else return false;
                    break;
            }

            if(countParenthesis < 0)
            {
                return false;
            }
        }

        if(state == 1 && countParenthesis == 0) return true;
        else return false;

    }

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

    //main--
    public static void main(String[] args) {

        int numberOfSets;
        String expression;
        String typeOfExpression;

        for(int l=0; l<signs.length; l++)
        {
            signsList.insert(signs[l], signsList.end()); // uzupelnienie listy znakami zgodnie z rankingiem
        }

        //input---
        numberOfSets = Integer.parseInt(scn.nextLine());

        for(int i=0; i<numberOfSets; i++)
        {
            typeOfExpression = scn.next();
            expression = scn.nextLine();

            if(typeOfExpression.equals("INF:"))
            {
                expression = expression.replaceAll("[^a-z()!~^*/%+\\-<>?&|=]","");//usuniecie znakow
            }
            else if(typeOfExpression.equals("ONP:"))
            {
                expression = expression.replaceAll("[^a-z!~^*/%+\\-<>?&|=]","");//usuniecie znakow
            }

            if(typeOfExpression.equals("INF:"))
            {
                if(IsINFExpressionCorrect(expression))
                {
                    ConvertToONP(expression);
                }
                else
                {
                    System.out.println("ONP: error");
                }
            }
            else
            {
                ConvertToINF(expression);
            }

        }//koniec petli dzialania programu



    }
}

//Stos char---
class Stack{

    private int maxArraySize;
    private char[] stackElements;
    private int currentSize;

    Stack(int maxArraySize)
    {
        this.maxArraySize = maxArraySize;
        this.stackElements = new char[this.maxArraySize];
        this.currentSize = 0;
    }

    public void display()
    {
        for(int i=0; i<currentSize; i++)
        {
            System.out.print(stackElements[i] + " ");
        }
        System.out.print("\n");
    }

    public boolean isEmpty()
    {
        if(currentSize == 0) return true;
        else return false;
    }

    public void push(char element)
    {
        stackElements[currentSize] = element;
        currentSize++;
    }

    public char pop()
    {
        if(!isEmpty())
        {
            currentSize--;
            return stackElements[currentSize];
        }
        return '1'; // cos nie tak
    }

    public char top()
    {
        if(!isEmpty()) return stackElements[currentSize-1];
        else return '0';// cos nie tak
    }

}

//StosString--
class StackString{

    private int maxArraySize;
    private String[] stackStringElements;
    private int currentSize;

    StackString(int maxArraySize)
    {
        this.maxArraySize = maxArraySize;
        this.stackStringElements = new String[this.maxArraySize];
        this.currentSize = 0;
    }

    public void display()
    {
        for(int i=0; i<currentSize; i++)
        {
            System.out.print(stackStringElements[i] + " ");
        }
        System.out.print("\n");
    }

    public boolean isEmpty()
    {
        if(currentSize == 0) return true;
        else return false;
    }

    public void push(String  element)
    {
        stackStringElements[currentSize] = element;
        currentSize++;
    }

    public String  pop()
    {
        if(!isEmpty())
        {
            currentSize--;
            return stackStringElements[currentSize];
        }
        return "1"; // cos nie tak
    }

    public String top()
    {
        if(!isEmpty()) return stackStringElements[currentSize-1];
        else return "0";// cos nie tak
    }

}

//Lista---
class List{

    private int maxSize;
    private char[] listElements;
    private int currentSize;

    List(int maxSize)
    {
        this.maxSize = maxSize;
        this.listElements = new char[this.maxSize];
        this.currentSize = 0;
    }

    public int end()
    {
        return currentSize;
    }

    public int first()
    {
        return 0;
    }

    public int locate(char searchElement)
    {
        for(int i=0; i<currentSize; i++)
        {
            if(listElements[i] == searchElement)
            {
                return i;
            }
        }
        return -1; // czyli ze nie ma
    }

    public void insert(char insertElement, int posToInsert)
    {
        if(posToInsert > currentSize || posToInsert < 0)
        {
            return; // ?? moze cos innego niz puste return ?
        }
        else if(posToInsert == currentSize)
        {
            listElements[posToInsert] = insertElement;
            currentSize++;
            return;
        }

        for(int i=currentSize; i >= posToInsert; i--)
        {
            listElements[i+1] = listElements[i];
        }

        listElements[posToInsert] = insertElement;
        currentSize++;

    }

    public void delete(int posToDelete)
    {
        if(posToDelete < 0 || posToDelete >= currentSize)
        {
            return; // bo nie ma takiej pozycji
        }

        for(int i=posToDelete; i<currentSize-1; i++)
        {
            listElements[i] = listElements[i+1];
        }
        currentSize--;
    }

    public void display()
    {
        for(int i=0; i<currentSize; i++)
        {
            System.out.print(listElements[i] + " ");
        }
        System.out.print("\n");
    }

    public char retrieve(int pos)
    {
        if(pos < 0 || pos >= currentSize)
        {
            return '1'; // ????
        }
        return listElements[pos];
    }

    public int next(int pos)
    {
        if(pos == end())
        {
            return '1'; // ????
        }

        return listElements[pos+1];
    }

    public int previous(int pos)
    {
        if(pos <= 1)
        {
            return '1'; // moze byc nie tak
        }

        return listElements[pos-1];

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
