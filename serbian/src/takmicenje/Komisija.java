package takmicenje;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class Komisija extends Korisnik
{

    public Komisija(int id, String kor_ime, String lozinka, char tip_kor)
    {
        super(id, kor_ime, lozinka, tip_kor);
    }


    public static void meni()
    {
        int opcija = -1;
        boolean odjavljen=false;

        do {//ponavlja ovaj meni dokle god se administrator ne odjavi
            Metode.ocistiEkran();
            Metode.ispisN("\nИзаберите неку од следећих опција: \n");
            Metode.sacekaj(500);
            Metode.ispisN("0. Одјавите се\n1. Прикажи списак неоцењених радова\n2. Оцени рад");
            Metode.dekoracija();
            do {
                Metode.sacekaj(500);
                Metode.ispisN("Опција: \t");
                opcija = Metode.unosCel();

                if (opcija < 0 || opcija > 2)
                {
                    Metode.sacekaj(500);
                    System.err.println("\nНисте унели ниједан од понуђених параметара. Унесите поново.");
                }
              } while (opcija < 0 || opcija > 2);


            switch (opcija)
            {
                case 0:
                    odjavljen=true;
                    Korisnik.odjaviSe();
                    break;

                case 1:
                    Metode.ocistiEkran();
                    Metode.sacekaj(500);
                    Metode.ispisN("\n\t\tПРИКАЖИ СПИСАК РАДОВА");
                    Metode.sacekaj(200);
                    Komisija.prikaziSpisakRadova();
                    break;

                case 2:
                    Metode.ocistiEkran();
                    Metode.sacekaj(500);
                    Metode.ispisN("\n\t\tОЦЕНИ РАД");
                    Komisija.oceniRad();
                    break;

                default:
                    break;
            }

        }while(opcija!=0 && odjavljen==false);

    }// metoda meni

    public static File[] vratiListuFajlova()
    {
        File[] lista_fajlova;
        File folder;
        String putanja = "";

        if(System.getProperty("os.name").toLowerCase().contains("windows")) //ako je neka verzija Windows-a
            putanja="\\"+ System.getProperty("user.dir")+"\\zaPregledanje";

        else
            putanja="/"+ System.getProperty("user.dir")+"/zaPregledanje";

        folder = new File(putanja);

        return folder.listFiles();
    }

    //otvara se direktorijum "zaPregledanje" i ucitava se spisak radova, kao lista objekata tipa File
    public static void prikaziSpisakRadova()
    {
       File[] lista_fajlova = Komisija.vratiListuFajlova();

        //ako ima fajlova u folderu
        if(lista_fajlova!=null)
        {   int i = 0;
            Metode.sacekaj(500);
            Metode.ispisN("\nРадови:");
            Metode.dekoracija();
            for (File f : lista_fajlova)
                Metode.ispisN(++i + ".\t" + f.getName());

            Metode.sacekaj(500);
            Metode.dekoracija();
            Metode.unosStr();
        }
        else
        {
            Metode.ispisN("Нема радова за оцењивање.");
            Metode.unosStr();
        }
    }

    public static void otvoriRad(String ime_rada)
    {
        String putanja = "";

        if(System.getProperty("os.name").toLowerCase().contains("windows")) //ako je neka verzija Windows-a
            putanja=System.getProperty("user.dir")+"\\zaPregledanje\\"+ime_rada;

        else
            putanja="/"+ System.getProperty("user.dir")+"/zaPregledanje/"+ime_rada;

        try
        {
            byte[] bajtovi_iz_datoteke = Files.readAllBytes(Paths.get(putanja));
            String s = new String(bajtovi_iz_datoteke, Charset.forName("UTF-8"));

            Metode.ispisN("\n\t\tСАДРЖАЈ РАДА - " + ime_rada );
            Metode.dekoracija();
            Metode.ispisN(s);
            Metode.dekoracija();
        } catch(IOException ex)
        {
            System.err.println(ex);
        }
    }

    public static void oceniRad()
    {
        // 1. Unosi se ime fajla sa formatom

        // 2. Otvara se fajl i prikazuje
        // 3. Pojavljuju se polja za unos
        // 4. Korisnik vrsi unos
        //     4.1 -kako je ime fajla korisnicko ime takmicara, treba pristupiti tabeli korisnik i uzeti id;
        //     4.2 -zatim treba pristupiti tabeli ocena i popuniti je podacima;
        //     4.3 -ukoliko clan komisije ostavi komentar, pokunjava se i tabela komentar, upisuje se (Komisija)getKorisnik().korisnik_id i takmicar_id koji vec imamo;
        //

        // 1. //
        String ime_fajla, komentar;
        double kvalitet_rada, tacnost_koda, opsti_utisak;

        boolean postoji = false;

        File[] lista_fajlova = Komisija.vratiListuFajlova();

        do {
            Metode.sacekaj(500);
            Metode.ispisN("Унесите име фајла са екстензијом: ");
            ime_fajla = Metode.unosStr();

            for (File f : lista_fajlova)
                if (f.getName().equals(ime_fajla))
                {
                    postoji = true;
                    break;
                }
            if (!postoji)
            {
                Metode.sacekaj(500);
                Metode.ispisN("Не постоји датотека са унетим називом. Покушајте поново. ");
                Metode.unosStr();
            }
        }while(!postoji);

        // 2. //
        Komisija.otvoriRad(ime_fajla);

        // 3. //
        do {
            Metode.sacekaj(500);
            Metode.ispisN(String.format("%-20s","Квалитет рада: (0-100.0)"));
            kvalitet_rada = Metode.unosReal_d();
            if (kvalitet_rada < 0 || kvalitet_rada > 100)
            {
                Metode.ispisN("Оцена је ван опсега. Унесите поново.");
                Metode.unosStr();
            }

        }while(kvalitet_rada < 0 || kvalitet_rada > 100);

        do {
            Metode.sacekaj(500);
            Metode.ispisN(String.format("%-20s","Тачност кода: (0-100.0)")); tacnost_koda  = Metode.unosReal_d();
            if (tacnost_koda < 0 || tacnost_koda > 100)
            {
                Metode.ispisN("Оцена је ван опсега. Унесите поново.");
                Metode.unosStr();
            }

        }while(tacnost_koda < 0 || tacnost_koda > 100);

        do {
            Metode.sacekaj(500);
            Metode.ispisN(String.format("%-20s","Општи утисак: (0-100.0)")); opsti_utisak  = Metode.unosReal_d(); Metode.unosStr();
            if (opsti_utisak < 0 || opsti_utisak > 100)
            {
                Metode.ispisN("Оцена је ван опсега. Унесите поново.");
                Metode.unosStr();
            }

        }while(opsti_utisak < 0 || opsti_utisak > 100);

        Metode.sacekaj(500);
        Metode.ispisN("Коментар: (можете оставити празно)\t"); komentar = Metode.unosStr();
        Metode.dekoracija();


        //UPIS U BAZU,
        int takmicar_id = -1;

        // STAVKA 4.1, upit za uzimanje takmicar_id-a iz tabele korisnik pomocu korisnickog imena

            //split string
        String[] ime_ekst = ime_fajla.split(Pattern.quote("."));
        String kor_ime = ime_ekst[0];
        String up1 = "SELECT korisnik_id FROM korisnik WHERE kor_ime=\'"+ kor_ime +"\'" + ";";

        try {

            ResultSet rs = BPKonekcija.getInstanca().upit(up1);
            if(rs.next())
               takmicar_id = rs.getInt("korisnik_id");

        } catch (SQLException e)
        {
            Metode.sacekaj(500);
            System.err.println("Грешка при уносу података у базу: " + e.getMessage());
            e.printStackTrace();
            Metode.unosStr();
        }

        //STAVKA 4.2, upis podataka u tabelu takmicar
        double srednja_ocena = (kvalitet_rada + tacnost_koda + opsti_utisak)/3;

        String up2 = "UPDATE takmicar SET kvalitet_rada=\'" + kvalitet_rada + "\'," +
                                        " tacnost_koda=\'" + tacnost_koda + "\',"  +
                                        " opsti_utisak=\'" + opsti_utisak +"\'," +
                                        " srednja_ocena=\'" + srednja_ocena +"\'" +
                                        " WHERE takmicar_id=\'" + takmicar_id + "\'" +
                                        " ;" ;
        try {

            if( BPKonekcija.getInstanca().upitIUD(up2) > 0 )
            {
                Metode.sacekaj(500);
                Metode.ispisN("Табела Такмичар је успешно ажурирана!");
                Metode.unosStr();
            }
            else
            {
                Metode.sacekaj(500);
                System.err.println("Нешто није у реду, додато је 0 редова.");
                Metode.unosStr();
            }

        } catch (SQLException e)
        {
            Metode.sacekaj(500);
            System.err.println("Грешка при уносу података у базу: " + e.getMessage());
            e.printStackTrace();
            Metode.unosStr();
        }

        //STAVKA 4.3, ukoliko postoji komentar
        if(!komentar.isEmpty())
        {
            int komisija_id = ((Komisija)getKorisnik()).korisnik_id;
            String upit = "INSERT INTO komentari VALUES(" + "'"+ takmicar_id + "'" + ", " +
                                                            "'"+ komisija_id + "'" + ", "+
                                                            "'"+ komentar +     "'" +
                                                            ")"+ " ;";
            try {
                if( BPKonekcija.getInstanca().upitIUD(upit) > 0 )
                    if( BPKonekcija.getInstanca().upitIUD(up2) > 0 )
                    {
                        Metode.sacekaj(500);
                        Metode.ispisN("Табела Коментар је успешно ажурирана!");
                        Metode.unosStr();
                    }
                    else
                    {
                        Metode.sacekaj(500);
                        System.err.println("Нешто није у реду, додато је 0 редова.");
                        Metode.unosStr();
                    }
            } catch (SQLException e)
            {
                Metode.sacekaj(500);
                System.err.println("Грешка при уносу података у базу: " + e.getMessage());
                Metode.unosStr();
                e.printStackTrace();
            }
        }
    }
}

