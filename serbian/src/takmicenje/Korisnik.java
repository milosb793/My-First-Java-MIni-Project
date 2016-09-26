package takmicenje;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class Korisnik
{
    protected String kor_ime;
    protected String lozinka;
    protected char tip_kor;
    protected int  korisnik_id;

    private static Object korisnik;


    public static Object getKorisnik()
    {
        return korisnik;
    }

    public static void setKorisnik(Object k)
    {
        korisnik=k;
    }

    public Korisnik(int id,String kor_ime, String lozinka, char tip_kor)
    {
        this.kor_ime = kor_ime;
        this.lozinka = lozinka;
        this.tip_kor = tip_kor;
        this.korisnik_id = id;
    }

    public static void meni()
    {
        int opcija=-1;
        Metode.dekoracija();
        System.out.println("\n\t\tДОБРО ДОШЛИ У АПЛИКАЦИЈУ!");

     do { //ponavlja ovaj meni dokle god se administrator ne odjavi

        Metode.ispisN("\nИзаберите неку од следећих опција:\n"); Metode.sacekaj(500);
        Metode.ispisN("0. Изађите из апликације\n1. Пријавите се\n2. Ранг листа такмичара");
        Metode.ispisN("------------------------------------");

        do {
            Metode.sacekaj(500);
            Metode.ispisN("Опција:\t");

            opcija = Metode.unosCel();

            if(opcija < 0 || opcija > 2)
                System.err.println("\nНисте унели ниједну од понуђених опција. Унесите поново.");

        }while(opcija < 0 || opcija > 2 );

            switch (opcija)
            {
                case 0:
                    Metode.sacekaj(400);
                    Metode.ispisN("\nХвала на коришћењу!");
                    Metode.sacekaj(400);
                    System.exit(0);

                case 1:
                {
                        int pokusaji = 3;
                        do {
                            Metode.ocistiEkran();
                            try {
                                Korisnik.setKorisnik(Korisnik.prijaviSe());
                            }catch(Exception e)
                            {
                                Metode.sacekaj(400);
                                Metode.ispisN("Дошло је до грешке. Проверите да ли сте унели корисничко име веће од 3 карактера.\n" );
                                Metode.unosStr();
                            }

                            if (Korisnik.getKorisnik() == null)
                            {
                                pokusaji--;
                                    try
                                    {
                                        Metode.sacekaj(400);
                                        if (pokusaji != 0)
                                            throw new Exception("\nУ бази не постоји особа са унетим подацима. Покушајте поново.");
                                    } catch (Exception e)
                                    {
                                        Metode.ispisN("Грешка:  " + e.getMessage());
                                        if(pokusaji==2)
                                            Metode.ispisN("\t(Остало је још " + pokusaji + " покушаја.)");
                                        else
                                            Metode.ispisN("\t(Остао је још " + pokusaji + " покушај.)");
                                        Metode.unosStr();
                                    }
                                if (pokusaji == 0)
                                {
                                    Metode.sacekaj(400);
                                    System.err.println("\nУнели сте три пута непостојеће податке. Покрените поново апликацију и покушајте поново.\n");
                                        System.exit(0);
                                }
                            } else
                            {
                                Metode.sacekaj(400);
                                Metode.ispisN("\nУспешно сте се пријавили!");
                                Metode.sacekaj(1000);
                            }

                        } while (Korisnik.getKorisnik() == null);

                    if (Korisnik.getKorisnik() instanceof Takmicar)
                    {
                        Metode.ispisN("\t\t\tДобро дошли " + ((Takmicar) getKorisnik()).kor_ime + "!\n");
                        Metode.sacekaj(1000);
                        Takmicar.meni();
                    }

                    if (Korisnik.getKorisnik() instanceof Komisija)
                    {

                        Metode.ispisN("\t\t\tДобро дошли " + ((Komisija) getKorisnik()).kor_ime + "!\n");
                        Metode.sacekaj(1000);
                        Komisija.meni();
                    }

                    if (Korisnik.getKorisnik() instanceof Administrator)
                    {

                        Metode.ispisN("\n\t\t\tДобро дошли " + ((Administrator) getKorisnik()).kor_ime + "!\n");
                        Metode.sacekaj(1000);
                        Administrator.meni();
                    }
                    if (Korisnik.getKorisnik() instanceof Mentor)
                    {
                        Metode.ispisN("\t\t\tДобро дошли " + ((Mentor) getKorisnik()).kor_ime + "!\n");
                        Metode.sacekaj(1000);
                        ((Mentor) Korisnik.getKorisnik()).prikaziMojeTakmicare();
                        Metode.unosStr();
                        Metode.ocistiEkran();
                    }
                    break;
                }

                case 2:
                    Metode.ocistiEkran();
                    Metode.sacekaj(500);
                    Korisnik.prikaziRangListu();
                    break;

                default:
                    break;

            }//switch
        }while(opcija!=0);
    }

    public static Object prijaviSe()
    {
        String kIme, lozinka;

        Metode.ispisN("Унесите своје корисничко име са префиксом у формату тип-корисничко име и лозинку:" +
                            "\n(пример: a-petar (администратор петар))\n\n");

        Metode.ispis(String.format("%-20s","тип-корисничко_име:"));   kIme = Metode.unosStr().trim();
        Metode.ispis("\n"+String.format("%-20s","Лозинка:           "));   lozinka = Metode.unosStr().trim();

        return Korisnik.vratiKorisnika(kIme,lozinka);
    }

    //metoda prosledjuje unete podatke i vraca potrebni tip Korisnika
    public static Object vratiKorisnika(String tipKorIme, String loz)
    {
        String kIme = tipKorIme.substring(2);
        char tip;
        int id;

        tip = tipKorIme.charAt(0);

        String up = "SELECT * FROM korisnik WHERE kor_ime=\'"+ kIme + "\' AND lozinka=\'"+ loz +"\' ; ";
        ResultSet rez = null;
        try
        {
            rez = BPKonekcija.getInstanca().upit(up);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if(rez.next())
            {
                if (rez.getString("tip").charAt(0) == 't')
                    return new Takmicar(rez.getInt("korisnik_id"), kIme, loz, tip);

                if (rez.getString("tip").charAt(0) == 'k')
                    return new Komisija(rez.getInt("korisnik_id"), kIme, loz, tip);

                if (rez.getString("tip").charAt(0) == 'a')
                    return new Administrator(rez.getInt("korisnik_id"), kIme, loz, tip);

                if (rez.getString("tip").trim().charAt(0) == 'm')
                    return new Mentor(rez.getInt("korisnik_id"), kIme, loz, tip);

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean postoji(String kIme)
    {
            String up = "SELECT * FROM korisnik WHERE kor_ime='"+ kIme + "' ; ";

            ResultSet rez = null;
            try
            {
                rez = BPKonekcija.getInstanca().upit(up);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            try
            {
                if(rez.next())
                {
                    if(  rez.getString("kor_ime").contains(kIme) )
                        return true;
                    else
                        return false;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        return false;
        }

    public static void prikaziRangListu()
    {
        String upit1 = "SELECT * FROM takmicar WHERE lokacija_rada IS NOT NULL ORDER BY srednja_ocena DESC ;";
        String upit2 = "SELECT * FROM korisnik WHERE tip=" + "'"+"t"+"'" + " ;";
        int rbr = 0,i=0, id;
        ArrayList<Takmicar> takmicari = new ArrayList<>();
        DecimalFormat dc = new DecimalFormat("000.00");

        Metode.ispisN("\t\t\t\t\t\t\t\t\t\t\tРАНГ ЛИСТА\n");
        Metode.ispisN("\t\t ______________________________________________________________________________");
        Metode.ispisN("\t\t| Р.Број| ID  |        Кор.име      |                  Бодови                  |");
        Metode.ispisN("\t\t|------------------------------------------------------------------------------|");
        Metode.ispisN("\t\t|                                   | Квалитет | Тачност | Општи ут. |  ОЦЕНА  |");
        Metode.ispisN("\t\t|------------------------------------------------------------------------------|");


        try {

            ResultSet tak = BPKonekcija.getInstanca().upit(upit1);
            ResultSet kor = BPKonekcija.getInstanca().upit(upit2);

            //upisujemo sve takmicare iz tabele korisnik kako bismo imali korisnicko ime
            while(kor.next())
            {
                takmicari.add(new Takmicar(kor.getInt("korisnik_id"),kor.getString("kor_ime").trim(),"",'t'));
            }


            while(tak.next())
            {
                id = tak.getInt("takmicar_id");

                //trazimo id od takmicara u tabeli korisnik
                for(int j=0; j<takmicari.size(); j++)
                {
                    if(id == takmicari.get(j).korisnik_id )
                    {
                        i=j;
                        break;
                    }
                }

                Metode.ispis("\t\t|------------------------------------------------------------------------------|\n");
                Metode.ispisN("\t\t|  "  +
                                        String.format("%02d",++rbr) +  "  |  " +
                                        String.format("%02d",id) + " | " +
                                        String.format("%-20s",takmicari.get(i).kor_ime) + " |  " +
                                        dc.format(tak.getDouble("kvalitet_rada")) + "  |  " +
                                        dc.format(tak.getDouble("tacnost_koda")) + "  |  " +
                                        dc.format(tak.getDouble("opsti_utisak")) + "  |  " +
                                        dc.format(tak.getDouble("srednja_ocena")) + " |");
            }
            Metode.ispisN("\t\t|------------------------------------------------------------------------------|");


        } catch (SQLException e)
        {
            System.err.println("Неуспело учитавање ранг листе. Покушајте поново да покренете програм. \nГрешка: " + e.getMessage());
        }



    }

    public static void odjaviSe()
    {
        Metode.sacekaj(500);
        Korisnik.setKorisnik(null);
        Metode.ispisN("\nУспешно сте се одјавили!");
        Metode.sacekaj(1000);
        Metode.ocistiEkran();
    }

}
