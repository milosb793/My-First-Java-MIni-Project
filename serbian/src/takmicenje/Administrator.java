package takmicenje;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Milos on 9.5.2016..
 */

public class Administrator extends Korisnik
{

    public Administrator(int id, String kor_ime, String lozinka, char tip_kor)
    {
        super(id, kor_ime, lozinka, tip_kor);

    }

    public static void meni()
    {
        int opcija = -1;
        boolean administrator_odjavljen=false;

    do { //ponavlja ovaj meni dokle god se administrator ne odjavi
        Metode.ocistiEkran();
        Metode.sacekaj(500);
        Metode.ispisN("\nИзаберите неку од следећих опција:\n");
        Metode.sacekaj(500);
        Metode.ispisN("0. Одјавите се\n1. Додајте такмичара\n2. Додајте члана комисије\n3. Обришите корисника");
        Metode.sacekaj(500);
        Metode.dekoracija();
        do {
            Metode.sacekaj(500);
            Metode.ispisN("Опција:\t");

            opcija = Metode.unosCel();

            if (opcija < 0 || opcija > 3)
            {
                Metode.sacekaj(500);
                System.err.println("\nНисте унели ниједан од понуђених параметара. Унесите поново.");
            }
        } while (opcija < 0 || opcija > 3);

            switch (opcija)
            {
                case 0:
                    Korisnik.odjaviSe();
                    break;

                case 1:
                    Metode.sacekaj(500);
                    Metode.ocistiEkran();
                    Metode.ispisN("\n\t\tДОДАЈ ТАКМИЧАРА");

                    try
                    {
                        Administrator.dodajTakmicara();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        break;
                    }


                case 2:
                    Metode.sacekaj(500);
                    Metode.ocistiEkran();
                    Metode.ispisN("\n\t\tДОДАЈ ЧЛАНА КОМИСИЈЕ");
                    try
                    {
                        Administrator.dodajClanaKomisije();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        break;
                    }

                case 3:
                    Metode.sacekaj(500);
                    Metode.ocistiEkran();
                    Metode.ispisN("\n\t\tОБРИШИ КОРИСНИКА");
                    try
                    {
                       administrator_odjavljen = Administrator.obrisiKorisnika();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                        break;
                    }

                default:
                    break;
            }
        }while(opcija!=0 && administrator_odjavljen==false);
    }

//    1. Dodajte takmičara (није реализовано)
    public static void dodajTakmicara() throws SQLException
    {
        //id je autoinkrement
        String kor_ime, lozinka;
        char tip;

    do {
        Metode.ispis("\nУнесите корисничко име:   ");kor_ime = Metode.unosStr();
        Metode.ispis("\nУнесите лозинку:          ");lozinka = Metode.unosStr();
        if(kor_ime.length()<3 || lozinka.length()<3)
        {
            Metode.ispisN("\nКорисничко име и лозинка морају бити дужи од 3 карактера.\nУнесите поново.\n");
            Metode.sacekaj(400);
        }
        }while(kor_ime.length()<3 || lozinka.length()<3);

        tip = 't';

        //ukoliko korisnik vec ne postoji u bazi, dozvoljava unos
        if( Korisnik.postoji(kor_ime) == false )
        {
            //prvo upisujemo u tabelu Korisnik
            String up1 = "INSERT INTO korisnik( kor_ime, lozinka, tip) VALUES( " + "'"+ kor_ime + "'" + ", " +
                                                                                  "'"+ lozinka + "'" + ", "+
                                                                                  "'"+ tip +     "'" +
                                                                                  " )"+ ";";
            try
            {
                if( BPKonekcija.getInstanca().upitIUD(up1) > 0 )
                {
                    Metode.sacekaj(500);
                    Metode.ispisN("Тачмичар успешно додат!");
                    Metode.unosStr();
                }
                else
                {
                    Metode.sacekaj(500);
                    Metode.ispisN("Нешто није у реду, додато је 0 редова.");
                    Metode.unosStr();
                }
            }
            catch (SQLException e)
            {
                Metode.sacekaj(500);
                Metode.ispisN("Дошло је до грешке, такмичар није додат: " + e.getMessage() );
                e.printStackTrace();
                Metode.unosStr();
            }
        }
        else
        {
            Metode.sacekaj(500);
            Metode.ispisN("Корисник са тим корисничким именом већ постоји.");
            Metode.unosStr();
        }
    }

//    2. Dodajte člana komisije
    public static void dodajClanaKomisije() throws SQLException
    {
        String kor_ime, lozinka;
        char tip;

        do {
            Metode.ispis("\nУнесите корисничко име:   ");kor_ime = Metode.unosStr();
            Metode.ispis("\nУнесите лозинку:          ");lozinka = Metode.unosStr();
            if(kor_ime.length()<3 || lozinka.length()<3)
            {
                Metode.ispisN("\nКорисничко име и лозинка морају бити дужи од 3 карактера.\nУнесите поново.\n");
                Metode.sacekaj(400);
            }
        }while(kor_ime.length()<3 || lozinka.length()<3);
        tip = 'k';


        //ukoliko korisnik vec ne postoji u bazi, dozvoljava unos
        if(  Korisnik.postoji(kor_ime) == false  )
        {
            //upisujemo u tabelu Korisnik
            String up = "INSERT INTO korisnik( kor_ime, lozinka, tip) VALUES( " +  "'"+ kor_ime + "'" + ", " +
                                                                                   "'"+ lozinka + "'" + ", "+
                                                                                   "'"+ tip +     "'" +
                                                                                   " )"+ ";";
            try
            {
                if( BPKonekcija.getInstanca().upitIUD(up) > 0 )
                {
                    Metode.sacekaj(500);
                    Metode.ispisN("Члан комисије успешно додат!");
                    Metode.unosStr();
                }
                else
                {
                    Metode.sacekaj(500);
                    Metode.ispisN("Нешто није у реду, додато је 0 редова.");
                    Metode.unosStr();
                }
            }
            catch (SQLException e)
            {
                Metode.sacekaj(500);
                Metode.ispisN("Дошло је до грешке, члан комисије није додат: " + e.getMessage() );
                e.printStackTrace();
                Metode.unosStr();
            }
        }
        else
        {
            Metode.sacekaj(500);
            Metode.ispisN("Корисник са тим корисничким именом већ постоји.");
            Metode.unosStr();
        }
    }

//    3. Obrišite korisnika (није реализовано)
    public static boolean obrisiKorisnika() throws SQLException  //vraca true ako je admin obrisao sam sebe
    {
        String tipKorIme, kor_ime;
        char tip;
        String up,upit_takmicar="", upit_komentar="";

        Metode.ispisN("\nУнесите корисничко име у формату: (t-ime)\t"); tipKorIme = Metode.unosStr();
        kor_ime = tipKorIme.substring(2);
        tip = tipKorIme.charAt(0);

        //ako je administrator uneo svoje korisnicko ime odjavljuje se
        if( ((Administrator)getKorisnik()).kor_ime.equals(kor_ime) )
        {
            Metode.sacekaj(500);
            Metode.ispisN("\nУспешно сте се одјавили!");
            Metode.unosStr();
            setKorisnik(null);
            return true;
        }

        //ako je unet administrator za brisanje
        else if (tip == 'a')
        {
            Metode.sacekaj(500);
            Metode.ispisN("Корисници који су администратори се не могу обрисати.");
            Metode.unosStr();
            return false;
        }

        //ako je takmicar, brise se iz tabele korisnik i tabele takmicar
        else if( tipKorIme.charAt(0) == 't' )
        {
            int takmicar_id=-1;

            ResultSet rs = BPKonekcija.getInstanca().upit("SELECT korisnik_id FROM korisnik WHERE kor_ime=\'" +kor_ime + "\' AND tip='" +tip + "' ;" );
            if(rs.next())
             takmicar_id  = rs.getInt("korisnik_id");

            upit_takmicar = "DELETE FROM takmicar WHERE takmicar_id=\'" + takmicar_id + "\'" + ";" ;
            upit_komentar = "DELETE FROM komentari WHERE takmicar_id=\'" + takmicar_id + "\'" + ";" ;
        }

        up = "DELETE FROM korisnik WHERE kor_ime=\'"+ kor_ime + "\' AND tip=\'"+ tipKorIme.charAt(0) +"\' ; ";

        try
        {
            if( BPKonekcija.getInstanca().upitIUD(up) > 0 )
            {
                Metode.sacekaj(500);
                Metode.ispisN("Корисник " + tipKorIme + " је успешно обрисан!");
                Metode.unosStr();

                if (tipKorIme.charAt(0) == 't')
                    if (BPKonekcija.getInstanca().upitIUD(upit_takmicar) > 0)
                    {
                        Metode.sacekaj(500);
                        Metode.ispisN("Корисник " + tipKorIme + " је успешно обрисан и у табели Такмичар!");
                        Metode.unosStr();

                        if (BPKonekcija.getInstanca().upitIUD(upit_komentar) > 0)
                        {
                            Metode.sacekaj(500);
                            Metode.ispisN("Корисник " + tipKorIme + " је успешно обрисан и у табели Коментар!");
                            Metode.unosStr();
                        }
                    }
            }
            else
            {
                Metode.sacekaj(500);
                Metode.ispisN("Нешто није у реду, ништа није обрисано...");
                Metode.unosStr();
            }
        }
        catch (SQLException e)
        {
            Metode.sacekaj(500);
            Metode.ispisN("Дошло је до грешке, корисник није обрисан: " + e.getMessage() );
            e.printStackTrace();
            Metode.unosStr();
        }
        return false;
    }

}//klasa
