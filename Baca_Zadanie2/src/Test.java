import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

    }
}


// samodzielny insertion sort
/*
 public static void InsertionSort(int[] array, int len)
    {
        int tmp;

        for(int i=0; i< len - 1; i++)
        {
            if(array[i+1] > array[i])
            {
                continue;
            }
            else if(array[i+1] < array[i])
            {
                tmp = array[i+1];
                array[i+1] = array[i];

                for(int j=i; j > 0 ; j--)
                {
                    if(array[j-1] > tmp)
                    {
                        array[j] = array[j-1];
                        if(j == 1)
                        {
                            array[j-1] = tmp;
                        }

                    }
                    else if(array[j-1] <= tmp)
                    {
                        array[j] = tmp;
                        break;
                    }
                }

            }

        }


    }
 */