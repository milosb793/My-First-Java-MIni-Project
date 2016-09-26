package takmicenje;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Takmicar extends Korisnik
{
        protected int mentor_id;
        protected Takmicar(int id, String ime, String lozinka, char tip) {  super(id, ime, lozinka, tip);   }

    public static void meni()
    {
        int takmicar_id = ((Takmicar) getKorisnik()).korisnik_id;

        //provera da li je rad ocenjen
        boolean ocenjen = false;
        double kvalitet_rada = 0, tacnost_koda = 0, opsti_utisak = 0, srednja_ocena = 0;
        String lokacija_rada = "";

        //putanja koju korisnik unosi
        String putanja = "";

        //ovde cemo ujedno uzeti sve podatke koji ce nam kasnije trebati
        String upit = "SELECT * FROM takmicar WHERE takmicar_id=" + "'" + takmicar_id + "'" + ";";

        try {
            ResultSet rs = BPKonekcija.getInstanca().upit(upit);

            if (rs.next())
            {
                lokacija_rada = rs.getString("lokacija_rada");
                kvalitet_rada = rs.getDouble("kvalitet_rada");
                tacnost_koda = rs.getDouble("tacnost_koda");
                opsti_utisak = rs.getDouble("opsti_utisak");
                srednja_ocena = rs.getDouble("srednja_ocena");

                if (kvalitet_rada <= 0)
                    ocenjen = false;
                else
                    ocenjen = true;
            }


        } catch (SQLException e)
        {
            Metode.sacekaj(500);
            System.err.println("Грешка при ишчитавању података из базе: " + e.getMessage());
            e.printStackTrace();
            Metode.unosStr();
        }

        if ((lokacija_rada == null) || lokacija_rada.equals("")) //ako nije postavljen rad
        {
            Metode.ocistiEkran();
            Metode.ispisN("\t\t\tДобро дошли " + ((Takmicar) getKorisnik()).kor_ime);
            while(true) //for petlja koja se ponavlja dokle god nesto nije u redu, kako bismo izbegli
            //odjavu u slucaju greske
            {

                do {
                    Metode.sacekaj(500);
                    Metode.ispisN("\nУнесите апсолутну путању до Вашег рада: \nПример: C:\\Users\\...\\kor_ime.txt");
                    putanja = Metode.unosStr();
                    if (!putanja.toLowerCase().contains(".txt")) {
                        Metode.sacekaj(500);
                        Metode.ispisN("Фајл мора бити .txt формата. Унесите поново.");
                        Metode.sacekaj(500);
                    }

                } while (putanja.isEmpty() || putanja.length() < 10 || !putanja.toLowerCase().contains(".txt"));

                //ubacivanje u bazu
                String up1 = "INSERT INTO takmicar(takmicar_id, lokacija_rada) VALUES(" + "'" + takmicar_id + "'" + "," +
                        "'" + putanja + "'" +
                        ")" + ";";
                //kreiranje fajla i ubacivanje u folder zaPregledanje
                File fajl = new File(putanja);
                File folder = new File(System.getProperty("file.separator")  +  System.getProperty("user.dir") + System.getProperty("file.separator") +  "zaPregledanje" + System.getProperty("file.separator") + ((Takmicar) (getKorisnik())).kor_ime + ".txt");

                try {
                    Files.copy(fajl.toPath(), folder.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    if (BPKonekcija.getInstanca().upitIUD(up1) > 0) {
                        Metode.sacekaj(500);
                        Metode.ispisN("Ваш рад је предат на оцењивање. Проверите касније статус.");
                        Metode.unosStr();
                        Metode.ocistiEkran();
                        return;
                    } else {
                        Metode.sacekaj(500);
                        Metode.ispisN("Нешто није у реду, додато је 0 редова.");
                        Metode.unosStr();
                        return;
                    }

                } catch (IOException | SQLException e) {
                    Metode.sacekaj(500);
                    Metode.ispisN("Дошло је до грешке, табела Такмичар није ажурирана: " + e.getMessage());
                    e.printStackTrace();
                    Metode.unosStr();
                }
            }
        }
        //ako je ocenjen rad, prikazuje se ocena
       else if (ocenjen == true)
        {
            String komentar = "";

            Metode.sacekaj(500);
            Metode.ispisN("Ваш рад је оцењен!");
            Metode.sacekaj(800);
            Metode.dekoracija();
            Metode.ispisN("Квалитет рада:  " + kvalitet_rada);
            Metode.sacekaj(500);
            Metode.ispisN("Тачност кода:   " + tacnost_koda);
            Metode.sacekaj(500);
            Metode.ispisN("Општи утисак:   " + opsti_utisak);
            Metode.sacekaj(500);
            Metode.ispisN("\t\t- - - -");
            Metode.sacekaj(500);
            Metode.ispisN("Средња оцена:   " + srednja_ocena);

            //komentar
            try {
                ResultSet rs = BPKonekcija.getInstanca().upit("SELECT komentar FROM komentari WHERE takmicar_id=" + "'" + takmicar_id + "'" + ";");
                if (rs.next())
                    komentar = rs.getString("komentar");

            } catch (SQLException e) {
                Metode.sacekaj(500);
                System.err.println("Грешка при читању података из базе: " + e.getMessage());
                e.printStackTrace();
                Metode.unosStr();
            }
            Metode.sacekaj(500);
            Metode.ispisN("Коментар комисије: " + komentar);
            Metode.dekoracija();

            Metode.unosStr();

            return;

        } else
        {
            Metode.dekoracija();
            Metode.sacekaj(1500);
            Metode.ispisN("\n\tВаш рад још увек није оцењен.\n\tПроверите касније поново.");
            Metode.sacekaj(200);
            Metode.dekoracija();
            Metode.unosStr();
            Metode.ocistiEkran();

            return;
        }
    }

    public static ArrayList<Takmicar> sviTakmicari()
    {
        ArrayList<Takmicar> sviTakm = new ArrayList<Takmicar>();

        try
        {
            ResultSet rs = BPKonekcija.getInstanca().upit("SELECT * FROM korisnik WHERE tip='t' ;");

            while(rs.next())
            {
                sviTakm.add( new Takmicar(rs.getInt("korisnik_id"),rs.getString("kor_ime").trim(), "***", 't') );
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            return sviTakm;
        }
    }
}//klasa
