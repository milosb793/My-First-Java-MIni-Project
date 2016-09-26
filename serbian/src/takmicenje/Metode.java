package takmicenje;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public abstract class Metode
{
    private static Scanner unos;


    public static void dekoracija()
    {
        System.out.println("\n------------------------------------");
    }

    public static Scanner getScanner()
    {
        if(unos==null)
        {
            unos = new Scanner(System.in);
            return unos;
        }
        else
            return unos;
    }

    public static void ispisN(Object s)
    {
        System.out.println(s);
    }

    public static void ispis(Object s)
    {
        System.out.print(s);
    }

    public static int unosCel()
    {
        while(true)
        {
            if (!getScanner().hasNextInt() || getScanner().hasNext("\n"))
            {
                ispisN("\nНисте унели целобројну вредност. Унесите поново.");
                getScanner().nextLine();
            }
            else break;
        }
        int i = getScanner().nextInt();
        unos.nextLine();

        return i;
    }
    public static String unosStr()
    {
        return getScanner().nextLine();
    }

    public static double unosReal_d()
    {
        while(true)
        {
            if (!getScanner().hasNextDouble())
            {
                ispisN("\nНисте унели број, или покушајте , уместо . у запису. Унесите поново.");
                getScanner().nextLine();
            }
            else break;
        }

        return getScanner().nextDouble();
    }

    public static void ocistiEkran()
    {
        if(!System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            System.out.println("\033[H\033[2J");
            System.out.flush();
        }
        else
        {
            for(int i=0; i< 50; i++)
                Metode.ispisN("");

        }
    }

    public static void sacekaj(long ms)
    {
         TimeUnit t = TimeUnit.MILLISECONDS;

        try {
            t.sleep(ms);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

   public static void ucitavanje()
    {
        Metode.ispisN("\t\t\tМолимо сачекајте...");
        ispis("\n\t     ");
        for(int i = 0; i < 20; i++)
        {
            ispis('₪');
            if(i==4 || i== 7 || i==9)
            { sacekaj(400); }
            sacekaj(200);
        }
        sacekaj(700);
        Metode.ocistiEkran();

    }
}
