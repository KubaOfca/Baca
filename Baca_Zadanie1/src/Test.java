import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        int p = scn.nextInt();
        scn.nextLine();
        String l = scn.next();
        String k = scn.next();

        System.out.println(p + " " + l + " " + k);


    }
}



/////// stare testy

/* int current_sum = 0;
        int max_sum = 0;
        int[] sub_to_Kodome = new int[5];
        sub_to_Kodome[0] = 12;
        sub_to_Kodome[1] = 2;
        sub_to_Kodome[2] = -100;
        sub_to_Kodome[3] = 13;
        sub_to_Kodome[4] = 1;


        int temp_UpMax = 0;
        int temp_DownMax = 0;
        int UpMax = 0;
        int DownMax = 0;

        for(int k=0; k < 5 ; k++)
        {
            if( k == 0)
            {
                current_sum = sub_to_Kodome[k];
                max_sum = sub_to_Kodome[k];
            }
            else
            {
                if(sub_to_Kodome[k] < (sub_to_Kodome[k] + current_sum))
                {
                    current_sum = (sub_to_Kodome[k] + current_sum);
                    temp_DownMax = k;
                    if(current_sum > max_sum)
                    {
                        max_sum = current_sum;
                        DownMax = temp_DownMax;
                    }
                    else if(current_sum == max_sum)
                    {
                        if((temp_DownMax - temp_UpMax + 1) < (DownMax - UpMax +1))
                        {
                            System.out.println("==");
                            DownMax = temp_DownMax;
                            UpMax = temp_UpMax;
                        }

                    }
                }
                else
                {
                    current_sum = sub_to_Kodome[k];
                    temp_UpMax = k;
                    temp_DownMax = k;
                    if (current_sum > max_sum)
                    {
                        max_sum = current_sum;
                        UpMax = temp_UpMax;
                        DownMax = temp_DownMax;
                    }
                    else if(current_sum == max_sum)
                    {
                        if((temp_DownMax - temp_UpMax + 1) < (DownMax - UpMax +1))
                        {
                            System.out.println("2==");
                            DownMax = temp_DownMax;
                            UpMax = temp_UpMax;
                        }

                    }

                }
            }
        }
        System.out.println(max_sum);
        System.out.println(UpMax);
        System.out.println(DownMax);
    }*/
